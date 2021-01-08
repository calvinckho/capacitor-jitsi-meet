package com.capacitor.jitsi.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.UiThreadUtil;

import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetViewListener;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

public class JitsiActivity extends JitsiMeetActivity {
    private JitsiMeetView view;
    private JitsiMeetUserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new JitsiMeetView(this);
        Log.d("Listener", "entering");
        view.setListener(new JitsiMeetViewListener() {
            private void on(String name, Map<String, Object> data) {
                UiThreadUtil.assertOnUiThread();

                // Log with the tag "ReactNative" in order to have the log
                // visible in react-native log-android as well.
                Log.d(
                        "Listener",
                        JitsiMeetViewListener.class.getSimpleName() + " "
                                + name + " "
                                + data);
                Intent intent = new Intent(name);
                intent.putExtra("eventName", name);
                sendBroadcast(intent);
            }


            @Override
            public void onConferenceJoined(Map<String, Object> data) {
                on("onConferenceJoined", data);
            }

            @Override
            public void onConferenceWillJoin(Map<String, Object> data) {
                on("onConferenceWillJoin", data);
            }

            @Override
            public void onConferenceTerminated(Map<String, Object> data) {
                view.dispose();
                view = null;
                finish();
                on("onConferenceLeft", data); // intentionally uses the obsolete onConferenceLeft in order to be consistent with iOS deployment and broadcast to JS listeners
            }
        });

        // Initialize default options for Jitsi Meet conferences.
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

        Log.d("DEBUG", roomName);

        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setRoom(roomName)
                .setToken(token)
                .setSubject(" ")
                .setAudioMuted(startWithAudioMuted)
                .setVideoMuted(startWithVideoMuted)
                .setFeatureFlag("chat.enabled", chatEnabled)
                .setFeatureFlag("invite.enabled", inviteEnabled)
                //.setAudioOnly(false)
                .setWelcomePageEnabled(false)
                .setUserInfo(userInfo)
                .build();
        view.join(options);
        setContentView(view);
    }

    private static final String ADD_PEOPLE_CONTROLLER_QUERY = null;
}

