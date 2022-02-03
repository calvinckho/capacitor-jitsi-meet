# Jitsi Meet Capacitor Plugin for Ionic Apps

This Ionic Capacitor plugin is created to make video calls through the free, open-sourced Jitsi video platform (https://meet.jit.si) on iOS and Android.

## Compatibility to Capacitor Versions

<table>
  <thead>
    <tr>
      <th>Capacitor</th>
      <th>capacitor-jitsi-meet</th>
      <th>supported</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        v3
      </td>
      <td>
        >= 2.0.0
      </td>
      <td>
        current
      </td>
    </tr>
    <tr>
      <td>
        v2
      </td>
      <td>
        <= 1.5.14
      </td>
      <td>
        until Sept 30, 2021
      </td>
    </tr>
    <tr>
      <td>
        v1
      </td>
      <td>
        <= 1.3.6
      </td>
      <td>
        until June 30, 2020
      </td>
    </tr>
  </tbody>
</table>

Follow the [official Capacitor doc to upgrade to Capacitor 3](https://capacitorjs.com/docs/updating/3-0).

## iOS Compatible Versions

See the plugin [changelog](https://github.com/calvinckho/capacitor-jitsi-meet/blob/master/CHANGELOG.md) for plugin versions that are compatible to your local Xcode version.

## Embedding in web applications
   
This plugin does not currently support web implementation. We recommend using the Jitsi iFrame API (https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-iframe) for full web implementation.

## Usage

1. npm install capacitor-jitsi-meet

2. use it as a Capacitor Plugin

```javascript
// On Capacitor 3
import { Jitsi } from 'capacitor-jitsi-meet';

// On Capacitor 1 and 2
import { Plugins } from '@capacitor/core';
import 'capacitor-jitsi-meet';

const { Jitsi } = Plugins;
```

```javascript
const result = await Jitsi.joinConference({
   roomName: 'room1', // room identifier for the conference
   url: 'https://meet.jit.si' // endpoint of the Jitsi Meet video bridge,
   token: string; // jwt authentication token
   displayName: string; // user's display name
   email: string; // user's email
   avatarURL: string; // user's avatar url
   channelLastN: string; // last N participants allowed to join
   startWithAudioMuted: true, // start with audio muted
   startWithVideoMuted: false // start with video muted
   chatEnabled: false, // enable Chat feature
   inviteEnabled: false // enable Invitation feature
   callIntegrationEnabled: true // enable call integration (CallKit on iOS, ConnectionService on Android)
});

window.addEventListener('onConferenceJoined', () => {
    // do things here
});
window.addEventListener('onConferenceLeft', () => {
    // do things here
});

await Jitsi.leaveConference();

```


3. Build the project

4. Follow the deployment instructions for [Android](android/README.md) and [iOS](ios/README.md).

## Feature Requests and Jitsi SDK Customizations

I appreciate those who use this plugin in their production apps and are funding this project as sponsors. Your sponsorship enables me to continue to maintain and roll out releases in a timely manner. Consider joining as a sponsor to get first-class support and consultation. For feature requests, create an issue with a label 'feature request'. I also offer paid consultation service to those needing to modify the Jitsi UI interface. To submit a request, create an issue and add the label 'sdk customization'.

## Acknowledgements

This plugin uses the [Jitsi Meet SDK](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-mobile). It was originally developed by then ESTOS' developer Philipp Hancke who then contributed it to the community where development continues with joint forces!
