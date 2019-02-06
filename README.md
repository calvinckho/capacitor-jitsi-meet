# Jitsi Meet Capacitor Plugin for Ionic Apps

This plugin is used to make video calls using Jitsi video platform (https://meet.jit.si) on iOS and Android using Capacitor. Since the current implementation of Ionic/PWA apps on iOS run on top of WKWebView, and as of today (24/01/2019), Apple does not support WebRTC on WKWebView, the only way to work with Jitsi Video on this platform is to build it natively.

## Usage

1. npm install capacitor-jitsi-meet

2. use it as a Capacitor Plugin

```
import { Plugins } from '@capacitor/core';

const { Jitsi } = Plugins;
const result = await Jitsi.joinConference({
    roomName: 'room1',
    url: 'https://meet.jit.si'
});

window.addEventListener('onConferenceJoined', () => {
    // do things here
});
window.addEventListener('onConferenceLeft', () => {
    // do things here
});

```
roomName (String): Room name for the conference

url: the endpoint of the Jitsi Meet video bridge


3. Build the project

4. Follow the deployment instructions for [Android](android/README.md) and [iOS](ios/README.md).

## Acknowledgements

This plugin uses the Jitsi Meet SDK. It was originally developed by then ESTOS' developer Philipp Hancke who then contributed it to the community where development continues with joint forces!