package com.capacitor.jitsi.plugin;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.*;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;

import android.content.IntentFilter;
import android.content.Intent;
import android.Manifest;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import timber.log.Timber;

import org.jitsi.meet.sdk.*;
import org.json.JSONException;

@CapacitorPlugin(
    name= "Jitsi",
    permissions={
        @Permission(strings = {Manifest.permission.RECORD_AUDIO}),
        @Permission(strings = {Manifest.permission.CAMERA}),
      }
  )
public class Jitsi extends Plugin {
    private static final String TAG = "CapacitorJitsiMeet";
    private JitsiBroadcastReceiver receiver;
    private JitsiMeetUserInfo userInfo;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @PluginMethod()
    public void joinConference(PluginCall call) throws JSONException {
        URL url = null;
        try {
            url = new URL(call.getString("url"));
        } catch (MalformedURLException e) {
            call.reject("Must provide an url");
            e.printStackTrace();
        }
        String roomName = call.getString("roomName");
        String token = call.getString("token");
        String displayName = call.getString("displayName");
        String subject = call.getString("subject", " ");
        String email = call.getString("email");
        String avatarURL = call.getString("avatarURL");
        Boolean startWithAudioMuted = call.getBoolean("startWithAudioMuted");
        Boolean startWithVideoMuted = call.getBoolean("startWithVideoMuted");
        JSObject featureFlags = call.getObject("featureFlags", new JSObject());
        JSObject configOverrides = call.getObject("configOverrides", new JSObject());
        receiver = new JitsiBroadcastReceiver();
        receiver.setModule(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("onConferenceWillJoin");
        filter.addAction("onConferenceJoined");
        filter.addAction("onConferenceLeft"); // intentionally uses the obsolete onConferenceLeft in order to be consistent with iOS deployment and broadcast to JS listeners
        getContext().registerReceiver(receiver, filter);

        if(roomName == null) {
            call.reject("Must provide an conference room name");
            return;
        }

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
                userInfo.setAvatar(new URL(avatarURL));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        //Timber.tag(TAG).d("display url: " + call.getString("url"));

        JitsiMeetConferenceOptions.Builder builder = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(url)
                .setRoom(roomName)
                .setToken(token)
                .setSubject(subject)
                .setUserInfo(userInfo);
        if(startWithAudioMuted != null){
            builder.setAudioMuted(startWithAudioMuted);
        }
        if(startWithVideoMuted != null){
            builder.setVideoMuted(startWithVideoMuted);
        }

        // default PiP is off, but allowing user to overrides it by providing the featureFlag below
        builder.setFeatureFlag("pip.enabled", false);

        // general settings can be overridden by subsequent featureFlags settings
        if(call.getBoolean("chatEnabled") != null){
            builder.setFeatureFlag("chat.enabled", call.getBoolean("chatEnabled"));
        }
        if(call.getBoolean("inviteEnabled") != null){
            builder.setFeatureFlag("invite.enabled", call.getBoolean("inviteEnabled"));
        }

        // setfeatureFlag() provides finer control, and will override some of the setFeatureFlag methods above
        Iterator<String> keys = featureFlags.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            // Can only be bool, int or string according to
            // the overloads of setFeatureFlag.

            if (featureFlags.get(key) != null) {
                if (featureFlags.get(key) instanceof Boolean) {
                    builder.setFeatureFlag(key, (Boolean) featureFlags.get(key));
                } else if (featureFlags.get(key) instanceof Integer) {
                    builder.setFeatureFlag(key, (Integer) featureFlags.get(key));
                } else if (featureFlags.get(key) instanceof String) {
                    builder.setFeatureFlag(key, (String) featureFlags.get(key));
                } else {
                    builder.setFeatureFlag(key, featureFlags.get(key).toString());
                }
            }
        }

        keys = configOverrides.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            // Can only be bool, int or string according to
            // the overloads of setFeatureFlag.
            if (configOverrides.get(key) != null) {
                if (configOverrides.get(key) instanceof Boolean) {
                    builder.setConfigOverride(key, (Boolean) configOverrides.get(key));
                } else if (configOverrides.get(key) instanceof Integer) {
                    builder.setConfigOverride(key, (Integer) configOverrides.get(key));
                } else if (configOverrides.get(key) instanceof String[]) {
                    builder.setConfigOverride(key, (String[]) configOverrides.get(key));
                } else {
                    builder.setConfigOverride(key, configOverrides.get(key).toString());
                }
            }
        }
        JitsiMeetConferenceOptions options = builder.build();
        JitsiActivity.launch(getActivity(), options);

        JSObject ret = new JSObject();
        ret.put("success", true);
        call.resolve(ret);
    }

    @PluginMethod()
    public void leaveConference(PluginCall call) {
        Intent leaveBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent();
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(leaveBroadcastIntent);

        JSObject ret = new JSObject();
        ret.put("success", true);
        call.resolve(ret);
    }

    public void onEventReceived(String eventName) {
        bridge.triggerWindowJSEvent(eventName);
        Timber.tag(TAG).d(eventName);
        if(eventName.equals("onConferenceLeft")) {
            if (receiver != null) {
                getContext().unregisterReceiver(receiver);
                receiver = null;
            }
        }
    }
}
