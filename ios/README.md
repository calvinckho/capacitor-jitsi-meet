# Jitsi Meet Plugin for iOS

This plugin uses the Jitsi SDK 2.10.2 for iOS.

## Xcode and iOS SDK Compatibility

Please note that different Xcode versions are required to work with different iOS SDK versions.

capacitor-jitsi-meet 1.3.13 uses iOS SDK 2.7.0, hence the last version that works with Xcode 11.3.1. See Jitsi-meet SDK [changelog](https://github.com/jitsi/jitsi-meet-release-notes/blob/master/CHANGELOG-MOBILE-SDKS.md#280-2020-04-21)

capacitor-jitsi-meet 1.4.3+ provides support for Xcode 11.4.1+. 

capacitor-jitsi-meet 1.5.3 provides support for Xcode 12.0. 

See Jitsi-meet SDK [changelog](https://github.com/jitsi/jitsi-meet-release-notes/blob/master/CHANGELOG-MOBILE-SDKS.md#280-2020-04-21)

### Usage

1. npm install capacitor-jitsi-meet, then use it as a Capacitor Plugin

```
import { Plugins } from '@capacitor/core';
import 'capacitor-jitsi-meet';

const { Jitsi } = Plugins;
const result = await Jitsi.joinConference({
   roomName: 'room1', // room identifier for the conference
   url: 'https://meet.jit.si' // endpoint of the Jitsi Meet video bridge
   token: string; // jwt authentication token
   channelLastN: string; // last N participants allowed to join
   startWithAudioMuted: true, // start with audio muted
   startWithVideoMuted: false // start with video muted
   chatEnabled: false, // enable Chat feature
   inviteEnabled: false // enable Invitation feature
});

window.addEventListener('onConferenceJoined', () => {
    // do things here
});
window.addEventListener('onConferenceLeft', () => {
    // do things here
});

```

2. npx cap sync

3. Turn off Bitcode in the app target as well as the pod targets. In xcode Project Navigator,


   i. Pods -> Project -> Pods -> Build Settings -> Enable Bitcode -> No
   
   ii Pods -> Targets -> CapacitorJitsiMeet -> Build Settings -> Enable Bitcode -> No
   

4. Use Swift 5 for the Capacitor target. In Xcode Project Navigator,

    i. Pods -> Targets -> Capacitor -> Build Settings -> Swift Language Version -> Swift 5

5. Deploy it to your device

### iOS SDK Developer Guide

[Documentation](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-ios-sdk)

