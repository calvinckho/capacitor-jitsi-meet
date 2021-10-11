package com.capacitor.jitsi.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.react.bridge.UiThreadUtil;

import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetViewListener;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;
import org.jitsi.meet.sdk.BroadcastEvent;

import timber.log.Timber;

public class JitsiActivity extends JitsiMeetActivity {
    private JitsiMeetView view;
    private JitsiMeetUserInfo userInfo;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void initialize() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onBroadcastReceived(intent);
            }
        };
        registerForBroadcastMessages();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new JitsiMeetView(this);
        Timber.d("entering");

        URL serverURL;
        try {
            serverURL = new URL(getIntent().getStringExtra("url"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }

        String roomName = getIntent().getStringExtra("roomName");
        String token = getIntent().getStringExtra("token");
        Boolean startWithAudioMuted = getIntent().getBooleanExtra("startWithAudioMuted", false);
        Boolean startWithVideoMuted = getIntent().getBooleanExtra("startWithVideoMuted", false);
        Boolean chatEnabled = getIntent().getBooleanExtra("chatEnabled", false);
        Boolean inviteEnabled = getIntent().getBooleanExtra("inviteEnabled", false);
        Boolean callIntegrationEnabled = getIntent().getBooleanExtra("callIntegrationEnabled", false);

        String displayName = getIntent().getStringExtra("displayName");
        String email = getIntent().getStringExtra("email");
        String avatarURL = getIntent().getStringExtra("avatarURL");

        // assign user info
        userInfo = new JitsiMeetUserInfo();
        if (displayName != null) {
            userInfo.setDisplayName(displayName);
        }
        if (email != null) {
            userInfo.setEmail(email);
        }
        if (avatarURL != null) {
            // try to assign avatar URL
            try {
                userInfo.setAvatar(new URL(getIntent().getStringExtra("avatarURL")));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setRoom(roomName)
                .setToken(token)
                .setSubject(" ")
                .setAudioMuted(startWithAudioMuted)
                .setVideoMuted(startWithVideoMuted)
                .setFeatureFlag("chat.enabled", chatEnabled)
                .setFeatureFlag("invite.enabled", inviteEnabled)
                .setFeatureFlag("call-integration.enabled", callIntegrationEnabled)
                //.setAudioOnly(false)
                .setWelcomePageEnabled(false)
                .setUserInfo(userInfo)
                .build();
        view.join(options);
        setContentView(view);
    }

    private void registerForBroadcastMessages() {
        IntentFilter intentFilter = new IntentFilter();

        for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.getAction());
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void onBroadcastReceived(Intent intent) {
        if (intent != null) {
            BroadcastEvent event = new BroadcastEvent(intent);

            switch (event.getType()) {
                case CONFERENCE_JOINED:
                    on("onConferenceJoined", event.getData());
                    break;
                case CONFERENCE_WILL_JOIN:
                    on("onConferenceWillJoin", event.getData());
                    break;
                case CONFERENCE_TERMINATED:
                    view.dispose();
                    view = null;
                    finish();
                    on("onConferenceLeft", event.getData()); // intentionally uses the obsolete onConferenceLeft in order to be consistent with iOS deployment and broadcast to JS listeners
                    break;
                case PARTICIPANT_JOINED:
                    on("onParticipantJoined", event.getData());
                    break;
                case PARTICIPANT_LEFT:
                    on("onParticipantLeft", event.getData());
                    break;
            }
        }
    }

    private void on(String name, Map<String, Object> data) {
        UiThreadUtil.assertOnUiThread();

        // Log with the tag "ReactNative" in order to have the log
        // visible in react-native log-android as well.
        Timber.d(JitsiMeetViewListener.class.getSimpleName() + " "
                + name + " "
                + data);
        Intent intent = new Intent(name);
        intent.putExtra("eventName", name);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
        super.onDestroy();
    }

    private static final String ADD_PEOPLE_CONTROLLER_QUERY = null;
}

