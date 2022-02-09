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
        Boolean recordingEnabled = getIntent().getBooleanExtra("recordingEnabled", false);
        Boolean liveStreamingEnabled = getIntent().getBooleanExtra("liveStreamingEnabled", false);
        Boolean screenSharingEnabled = getIntent().getBooleanExtra("screenSharingEnabled", false);

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
                .setFeatureFlag("recording.enabled", recordingEnabled)
                .setFeatureFlag("live-streaming.enabled", liveStreamingEnabled)
                .setFeatureFlag("android.screensharing.enabled", screenSharingEnabled)
                //.setAudioOnly(false)
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
                    on("onConferenceJoined");
                    break;
                case CONFERENCE_WILL_JOIN:
                    on("onConferenceWillJoin");
                    break;
                case READY_TO_CLOSE:
                    view.dispose();
                    view = null;
                    finish();
                    on("onConferenceLeft"); // intentionally uses the obsolete onConferenceLeft in order to be consistent with iOS deployment and broadcast to JS listeners
                    break;
                case PARTICIPANT_JOINED:
                    on("onParticipantJoined");
                    break;
                case PARTICIPANT_LEFT:
                    on("onParticipantLeft");
                    break;
            }
        }
    }

    private void on(String name) {
        UiThreadUtil.assertOnUiThread();
        Timber.d(JitsiMeetView.class.getSimpleName() + ": " + name);

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

    // The following handler is triggered when the app transitions from the background to the foreground.
    // For PIP users, uncomment the code to try PIP mode. Also change android:supportsPictureInPicture="true" in AndroidManifest.xml
    // When PIP is enabled, it can detect when the PIP window is closed by caller so it can terminate the call correctly.
    // TODO: Some users who enabled PIP report that it doesn't always terminate the call correctly.
    //  Therefore the PIP mode is disabled in the published plugin until this issue is resolved
    /*@Override
    public void onStop() {
        Timber.d("Picture-in-picture is stopped. Disposing view and finishing activity.");
        if (view != null) {
            view.dispose();
            view = null;
            finish();
        }
        on("onConferenceLeft"); // intentionally uses the obsolete onConferenceLeft in order to be consistent with iOS deployment and broadcast to JS listeners
        super.onStop();
    }*/

    // for logging entering and leaving PIP only
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);

        Timber.d("Is in picture-in-picture mode: " + isInPictureInPictureMode);
    }

    private static final String ADD_PEOPLE_CONTROLLER_QUERY = null;
}
