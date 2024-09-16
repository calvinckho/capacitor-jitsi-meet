package com.capacitor.jitsi.plugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import timber.log.Timber;

import com.facebook.react.bridge.UiThreadUtil;
import org.jitsi.meet.sdk.*;
import org.json.JSONObject;

public class JitsiActivity extends JitsiMeetActivity {
    private BroadcastReceiver broadcastReceiver;
    private static final String TAG = "CapacitorJitsiMeet";
    private static final String ACTION_JITSI_MEET_CONFERENCE = "org.jitsi.meet.CONFERENCE";
    private static final String JITSI_MEET_CONFERENCE_OPTIONS = "JitsiMeetConferenceOptions";
    private static JitsiMeetConferenceOptions session_options;

    @Override
    protected void initialize() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onBroadcastReceived(intent);
            }
        };
        registerForBroadcastMessages();
        join(getConferenceOptions(getIntent()));
    }
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

    private void registerForBroadcastMessages() {
        IntentFilter intentFilter = new IntentFilter();

        for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.getAction());
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void onBroadcastReceived(Intent intent) {
        JitsiMeetView view = getJitsiView();
        if (intent != null) {
            BroadcastEvent event = new BroadcastEvent(intent);
            switch (event.getType()) {
                case CONFERENCE_JOINED:
                    on("onConferenceJoined", null);
                    break;
                case CONFERENCE_WILL_JOIN:
                    on("onConferenceWillJoin", null);
                    break;
                case CONFERENCE_TERMINATED, READY_TO_CLOSE:
                    finish();
                    on("onConferenceLeft", null); // intentionally uses the obsolete onConferenceLeft in order to be consistent with iOS deployment and broadcast to JS listeners
                    break;
                case PARTICIPANT_JOINED:
                    on("onParticipantJoined", null);
                    break;
                case PARTICIPANT_LEFT:
                    on("onParticipantLeft", null);
                    break;
                case CHAT_MESSAGE_RECEIVED:
                    on("onChatMessageReceived", event);
                    break;
                case PARTICIPANTS_INFO_RETRIEVED:
                    on("onParticipantsInfoRetrieved", event);
                    break;
            }
        }
    }

    private void on(String eventName, BroadcastEvent event) {
        UiThreadUtil.assertOnUiThread();
        Intent intent = new Intent(eventName);
        intent.putExtra("eventName", eventName);
        if (event != null) {
            JSONObject json = new JSONObject(event.getData());
            intent.putExtra("data", json.toString());
            Timber.tag(TAG).d(JitsiMeetView.class.getSimpleName() + ": " + eventName + ", " + json);
        }
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
            on("onConferenceLeft", null); // intentionally uses the obsolete onConferenceLeft in order to be consistent with iOS deployment and broadcast to JS listeners
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // Here we are trying to handle the following corner case: an application using the SDK
        // is using this Activity for displaying meetings, but there is another "main" Activity
        // with other content. If this Activity is "swiped out" from the recent list we will get
        // Activity#onDestroy() called without warning. At this point we can try to leave the
        // current meeting, but when our view is detached from React the JS <-> Native bridge won't
        // be operational so the external API won't be able to notify the native side that the
        // conference terminated. Thus, try our best to clean up.
        leave();
        finish();
        JitsiMeetOngoingConferenceService.abort(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        JitsiMeetActivityDelegate.onHostDestroy(this);
        super.onDestroy();
    }

    // for logging entering and leaving PIP only
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);

        Timber.tag(TAG).d("Is in picture-in-picture mode: " + isInPictureInPictureMode);
    }

    private @Nullable
    JitsiMeetConferenceOptions getConferenceOptions(Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            if (uri != null) {
                return new JitsiMeetConferenceOptions.Builder().setRoom(uri.toString()).build();
            }
        } else if (ACTION_JITSI_MEET_CONFERENCE.equals(action)) {
            return intent.getParcelableExtra(JITSI_MEET_CONFERENCE_OPTIONS);
        }

        return null;
    }

    private static final String ADD_PEOPLE_CONTROLLER_QUERY = null;
}
