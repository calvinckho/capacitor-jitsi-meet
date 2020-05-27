# Jitsi Meet Capacitor Plugin for Ionic Apps

This plugin is used to make video calls using Jitsi video platform (https://meet.jit.si) on iOS and Android using Capacitor. Since the current implementation of Ionic/PWA apps on iOS run on top of WKWebView, and as of today (24/01/2019), Apple does not support WebRTC on WKWebView, the only way to work with Jitsi Video on this platform is to build it natively.

## Compatible Versions

Capacitor 1.x:
At this time this plugin only supports Capacitor 1.x. Capacitor 2 utilizes Android X which require updating our Android template files. If anyone wants to contribute to this update, please contact me.

Xcode 1.3.1:
Some users report issue with Xcode 1.4. For now, this works great with Xcode 1.3.1

## Embedding in external applications
   
For web implementation, see the Jitsi Meet API doc (https://github.com/jitsi/jitsi-meet/blob/master/doc/api.md)

## Usage

1. npm install capacitor-jitsi-meet

2. use it as a Capacitor Plugin

```
import { Plugins } from '@capacitor/core';
import 'capacitor-jitsi-meet';

const { Jitsi } = Plugins;
const result = await Jitsi.joinConference({
   roomName: 'room1', // room identifier for the conference
   url: 'https://meet.jit.si' // endpoint of the Jitsi Meet video bridge,
   startWithAudioMuted: true, // start with audio muted
   startWithVideoMuted: false // start with video muted
});

window.addEventListener('onConferenceJoined', () => {
    // do things here
});
window.addEventListener('onConferenceLeft', () => {
    // do things here
});

```


3. Build the project

4. Follow the deployment instructions for [Android](android/README.md) and [iOS](ios/README.md).

## Acknowledgements

This plugin uses the Jitsi Meet SDK. It was originally developed by then ESTOS' developer Philipp Hancke who then contributed it to the community where development continues with joint forces!
