package com.capacitor.jitsi.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.modules.core.PermissionListener;

import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetViewListener;
import org.jitsi.meet.sdk.invite.AddPeopleController;
import org.jitsi.meet.sdk.invite.AddPeopleControllerListener;
import org.jitsi.meet.sdk.invite.InviteController;
import org.jitsi.meet.sdk.invite.InviteControllerListener;

public class JitsiActivity extends JitsiMeetActivity {
    private JitsiMeetView view;

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
            }

            @Override
            public void onConferenceFailed(Map<String, Object> data) {
                // e.g. user cancels at the password prompt
                view.dispose();
                view = null;
                finish();
                on("CONFERENCE_FAILED", data);
            }

            @Override
            public void onConferenceJoined(Map<String, Object> data) {
                on("CONFERENCE_JOINED", data);
            }

            @Override
            public void onConferenceLeft(Map<String, Object> data) {
                // e.g. the user ends the call
                view.dispose();
                view = null;
                finish();
                on("CONFERENCE_LEFT", data);
            }

            @Override
            public void onConferenceWillJoin(Map<String, Object> data) {
                on("CONFERENCE_WILL_JOIN", data);
            }

            @Override
            public void onConferenceWillLeave(Map<String, Object> data) {
                on("CONFERENCE_WILL_LEAVE", data);
            }

            @Override
            public void onLoadConfigError(Map<String, Object> data) {
                on("LOAD_CONFIG_ERROR", data);
            }
        });
        // inviteController
        final InviteController inviteController
                = view.getInviteController();

        inviteController.setListener(new InviteControllerListener() {
            public void beginAddPeople(
                    AddPeopleController addPeopleController) {
                onInviteControllerBeginAddPeople(
                        inviteController,
                        addPeopleController);
            }
        });
        inviteController.setAddPeopleEnabled(
                ADD_PEOPLE_CONTROLLER_QUERY != null);
        inviteController.setDialOutEnabled(
                inviteController.isAddPeopleEnabled());

        view.setWelcomePageEnabled(false);
        String url = getIntent().getStringExtra("url");
        String roomName = getIntent().getStringExtra("roomName");
        String fullurl = url + '/' + roomName;
        Log.d("DEBUG", fullurl);

        //view.loadURLString(fullurl);
        Bundle config = new Bundle();
        config.putBoolean("startWithAudioMuted", false);
        config.putBoolean("startWithVideoMuted", false);
        config.putString("googleApiApplicationClientID", "896030655830-pveqh7f6cj8af3p10qh2rhokoqnsapcj.apps.googleusercontent.com");
        Bundle urlObject = new Bundle();
        urlObject.putBundle("config", config);
        urlObject.putString("url", fullurl);
        view.loadURLObject(urlObject);
        setContentView(view);
    }

    /**
     * The query to perform through {@link AddPeopleController} when the
     * {@code InviteButton} is tapped in order to exercise the public API of the
     * feature invite. If {@code null}, the {@code InviteButton} will not be
     * rendered.
     */
    private static final String ADD_PEOPLE_CONTROLLER_QUERY = null;

    private void onAddPeopleControllerInviteSettled(
            AddPeopleController addPeopleController,
            List<Map<String, Object>> failedInvitees) {
        UiThreadUtil.assertOnUiThread();

        // XXX Explicitly invoke endAddPeople on addPeopleController; otherwise,
        // it is going to be memory-leaked in the associated InviteController
        // and no subsequent InviteButton clicks/taps will be delivered.
        // Technically, endAddPeople will automatically be invoked if there are
        // no failedInviteees i.e. the invite succeeeded for all specified
        // invitees.
        addPeopleController.endAddPeople();
    }

    private void onAddPeopleControllerReceivedResults(
            AddPeopleController addPeopleController,
            List<Map<String, Object>> results,
            String query) {
        UiThreadUtil.assertOnUiThread();

        int size = results.size();

        if (size > 0) {
            // Exercise AddPeopleController's inviteById implementation.
            List<String> ids = new ArrayList<>(size);

            for (Map<String, Object> result : results) {
                Object id = result.get("id");

                if (id != null) {
                    ids.add(id.toString());
                }
            }

            addPeopleController.inviteById(ids);

            return;
        }

        // XXX Explicitly invoke endAddPeople on addPeopleController; otherwise,
        // it is going to be memory-leaked in the associated InviteController
        // and no subsequent InviteButton clicks/taps will be delivered.
        addPeopleController.endAddPeople();
    }

    private void onInviteControllerBeginAddPeople(
            InviteController inviteController,
            AddPeopleController addPeopleController) {
        UiThreadUtil.assertOnUiThread();

        // Log with the tag "ReactNative" in order to have the log visible in
        // react-native log-android as well.
        Log.d(
                "ReactNative",
                InviteControllerListener.class.getSimpleName() + ".beginAddPeople");

        String query = ADD_PEOPLE_CONTROLLER_QUERY;

        if (query != null
                && (inviteController.isAddPeopleEnabled()
                || inviteController.isDialOutEnabled())) {
            addPeopleController.setListener(new AddPeopleControllerListener() {
                public void onInviteSettled(
                        AddPeopleController addPeopleController,
                        List<Map<String, Object>> failedInvitees) {
                    onAddPeopleControllerInviteSettled(
                            addPeopleController,
                            failedInvitees);
                }

                public void onReceivedResults(
                        AddPeopleController addPeopleController,
                        List<Map<String, Object>> results,
                        String query) {
                    onAddPeopleControllerReceivedResults(
                            addPeopleController,
                            results, query);
                }
            });
            addPeopleController.performQuery(query);
        } else {
            // XXX Explicitly invoke endAddPeople on addPeopleController;
            // otherwise, it is going to be memory-leaked in the associated
            // InviteController and no subsequent InviteButton clicks/taps will
            // be delivered.
            addPeopleController.endAddPeople();
        }
    }
}

