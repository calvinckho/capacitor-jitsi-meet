# Jitsi Meet Plugin for iOS

This plugin uses the Jitsi SDK.

## Xcode and iOS SDK Compatibility

Each version of the jitsi capacitor plugin uses the latest Jitsi SDK version published by Jitsi. In iOS deployment, the SDK version is important because later SDKs compiled using the latest Xcode would not work if you local Xcode is older. So for iOS deployment, make sure you check your Xcode version and install the plugin version that uses the compatible SDK version.  

See the plugin [changelog](https://github.com/calvinckho/capacitor-jitsi-meet/blob/master/CHANGELOG.md) for plugin versions that are compatible to your local Xcode

### Usage

1. npm install capacitor-jitsi-meet, then use it as a Capacitor Plugin

```javascript
// On Capacitor 3
import { Jitsi } from 'capacitor-jitsi-meet';

// On Capacitor 2
import { Plugins } from '@capacitor/core';
import 'capacitor-jitsi-meet';

const { Jitsi } = Plugins;
```

```javascript
const result: any = await Jitsi.joinConference({
   roomName: 'room1', // room identifier for the conference
   url: 'https://meet.jit.si', // endpoint of the Jitsi Meet video bridge
   token: string, // jwt authentication token
   displayName: string, // user's display name
   email: string, // user's email
   avatarURL: string, // user's avatar url
   channelLastN: string, // last N participants allowed to join
   startWithAudioMuted: true, // start with audio muted
   startWithVideoMuted: false, // start with video muted
   chatEnabled: false, // enable Chat feature
   inviteEnabled: false, // enable Invitation feature
   callIntegrationEnabled: true, // enable call integration (CallKit on iOS, ConnectionService on Android)
   recordingEnabled: false, // enable recording feature
   liveStreamingEnabled: false, // enable LiveStreaming feature
   screenSharingEnabled: this.platform.is('android'), // enable ScreenSharing feature
});
console.log("join status", result.success);

window.addEventListener('onConferenceJoined', () => {
    // do things here
});
window.addEventListener('onConferenceLeft', () => {
    // do things here
});

const result: any = await Jitsi.leaveConference();
console.log("leave status: " +  result.success);

```

2. npx cap sync

3. Turn off Bitcode in the app target as well as the pod targets. In xcode Project Navigator,


   i. Pods -> Project -> Pods -> Build Settings -> Enable Bitcode -> No
   
   ii Pods -> Targets -> CapacitorJitsiMeet -> Build Settings -> Enable Bitcode -> No
   

4. Use Swift 5 for the Capacitor target. In Xcode Project Navigator,

    i. Pods -> Targets -> Capacitor -> Build Settings -> Swift Language Version -> Swift 5
5. In order for app to properly work in the background, select the "audio" and "voip" background modes.
```
<key>UIBackgroundModes</key>
	<array>
		<string>audio</string>
		<string>voip</string>
	</array>
```
6. Deploy it to your device

### iOS SDK Developer Guide

Consult the official Jitsi iOS SDK [documentation](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-ios-sdk).

