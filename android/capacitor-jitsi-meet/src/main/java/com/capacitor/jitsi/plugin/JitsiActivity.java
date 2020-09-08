package com.capacitor.jitsi.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    private JitsiMeetUserInfo info;

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
        Boolean startWithAudioMuted = getIntent().getBooleanExtra("startWithAudioMuted", false);
        Boolean startWithVideoMuted = getIntent().getBooleanExtra("startWithVideoMuted", false);
        Boolean chatEnabled = getIntent().getBooleanExtra("chatEnabled", false);
        Boolean inviteEnabled = getIntent().getBooleanExtra("inviteEnabled", false);
        String jwtToken = getIntent().getStringExtra("jwt");

        Log.d("DEBUG", roomName);

        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setRoom(roomName)
                .setToken(jwtToken)
                .setSubject(" ")
                .setAudioMuted(startWithAudioMuted)
                .setVideoMuted(startWithVideoMuted)
                .setFeatureFlag("chat.enabled", chatEnabled)
                .setFeatureFlag("invite.enabled", inviteEnabled)
                //.setAudioOnly(false)
                .setWelcomePageEnabled(false)
                .build();
        view.join(options);
        setContentView(view);
    }

    /**
     * The query to perform through {@link AddPeopleController} when the
     * {@code InviteButton} is tapped in order to exercise the public API of the
     * feature invite. If {@code null}, the {@code InviteButton} will not be
     * rendered.
     */
    private static final String ADD_PEOPLE_CONTROLLER_QUERY = null;
}

