package com.capacitor.jitsi.plugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import timber.log.Timber;

import com.facebook.react.bridge.UiThreadUtil;

import org.jitsi.meet.sdk.*;

public class JitsiActivity extends JitsiMeetActivity {
    private BroadcastReceiver broadcastReceiver;
    private static final String TAG = "CapacitorJitsiMeet";
    private static final String ACTION_JITSI_MEET_CONFERENCE = "org.jitsi.meet.CONFERENCE";
    private static JitsiMeetConferenceOptions session_options;

    // this overrides the launch class and runs the extended JitsiActivity class instead
    public static void launch(Context context, JitsiMeetConferenceOptions options) {
        session_options = options;
        Intent intent = new Intent(context, JitsiActivity.class);
        intent.setAction(ACTION_JITSI_MEET_CONFERENCE);
        intent.putExtra("JitsiMeetConferenceOptions", options);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private void onBroadcastReceived(Intent intent) {
        JitsiMeetView view = getJitsiView();
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
        Timber.tag(TAG).d(JitsiMeetView.class.getSimpleName() + ": " + name);

        Intent intent = new Intent(name);
        intent.putExtra("eventName", name);
        sendBroadcast(intent);
    }

    // The following handler is triggered when the app transitions from the background to the foreground.
    // When PIP is enabled, it can detect when the PIP window is closed by caller so it can terminate the call correctly.
    @Override
    public void onStop() {
        JitsiMeetView view = getJitsiView();
        Timber.tag(TAG).d("onStop %s", session_options.getFeatureFlags().getBoolean("pip.enabled"));
        if (session_options.getFeatureFlags().getBoolean("pip.enabled")) { //TODO: also check the CapacitorJitsiMeet's AndroidManifest.xml file and ensure android:supportsPictureInPicture="true"
            finish();
            on("onConferenceLeft"); // intentionally uses the obsolete onConferenceLeft in order to be consistent with iOS deployment and broadcast to JS listeners
        }
        super.onStop();
    }

    // for logging entering and leaving PIP only
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);

        Timber.tag(TAG).d("Is in picture-in-picture mode: " + isInPictureInPictureMode);
    }

    private static final String ADD_PEOPLE_CONTROLLER_QUERY = null;
}
