# Jitsi Meet Capacitor Plugin for Ionic Apps

This plugin is used to make video calls using Jitsi video platform (https://meet.jit.si) on iOS and Android using Capacitor.

## Compatible Versions

1.4.2: requires Capacitor 2.0+, Xcode 1.4.1+

1.3.12: requires Capacitor 2.0+, Xcode 1.3.1. See below section Upgrade to Capacitor 2.0 for instructions.

1.3.6: requires Capacitor 1.0+, Xcode 1.3.1

## Upgrade to Capacitor 2.0

Follow the [official Capacitor doc to upgrade to 2.0+](https://ionicframework.com/blog/announcing-capacitor-2-0/?utm_campaign=capacitor&utm_source=hs_email&utm_medium=email&utm_content=86094990&_hsenc=p2ANqtz-894lhie-saMpN3lq1GaI2aQiC9cBv-bvtZK-a9UyN5obOCgqkOxIb7yk1IRuJeK-LEsyKKqkZ3uxmmfV8nxqWzZZXInQ&_hsmi=86094990). 

For android deployment, ensure you follow step 6 in [README](android/README.md).

## Embedding in web applications
   
The web plugin defaults to open a video call in the jitsi meet web interface. For full web implementation, we recommend using the Jitsi Meet API (https://github.com/jitsi/jitsi-meet/blob/master/doc/api.md)

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


3. Build the project

4. Follow the deployment instructions for [Android](android/README.md) and [iOS](ios/README.md).

## Acknowledgements

This plugin uses the Jitsi Meet SDK. It was originally developed by then ESTOS' developer Philipp Hancke who then contributed it to the community where development continues with joint forces!
