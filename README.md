# Jitsi Meet Capacitor Plugin for Ionic Apps
<img src="https://img.shields.io/npm/v/capacitor-jitsi-meet?style=flat-square" />

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
        v5
      </td>
      <td>
        >= 4.0.0
      </td>
      <td>
        current
      </td>
    </tr>
    <tr>
      <td>
        v4
      </td>
      <td>
        <= 3.1.0
      </td>
      <td>
        until May 31, 2023
      </td>
    </tr>
    <tr>
      <td>
        v3
      </td>
      <td>
        <= 2.3.0
      </td>
      <td>
        until Dec 31, 2022
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

Follow the [official Capacitor doc to upgrade to Capacitor 5](https://capacitorjs.com/docs/updating/5-0).

## iOS Compatible Versions

See the plugin [changelog](https://github.com/calvinckho/capacitor-jitsi-meet/blob/master/CHANGELOG.md) for plugin versions that are compatible to your local Xcode version.

## Embedding in web applications

This plugin does not currently support web implementation. We recommend using the Jitsi iFrame API (https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-iframe) for full web implementation.

## Usage

1. Install the plugin

Install from NPM (release build):
```
npm install capacitor-jitsi-meet
```
Install from Github. Make sure you insert the branch name at the end:
```
npm i git+ssh://git@github.com:calvinckho/capacitor-jitsi-meet#[branch name]
```
2. use it as a [Capacitor Plugin](https://capacitorjs.com/docs/getting-started#adding-capacitor-to-your-app). See below for Capacitor 3+ usage. For older versions of Capacitor, see [here](https://github.com/calvinckho/capacitor-jitsi-meet/blob/7321356fcae47228d250aec1e9acba3796835769/README.md).
```javascript

import { Jitsi } from 'capacitor-jitsi-meet';

```javascript
const result = await Jitsi.joinConference({
    // required parameters
    roomName: 'room1', // room identifier for the conference
    url: 'https://meet.jit.si', // endpoint of the Jitsi Meet video bridge

    // recommended settings for production build. see full list of featureFlags in the official Jitsi Meet SDK documentation
    featureFlags: {
        'prejoinpage.enabled': false, // go straight to the meeting and do not show the pre-join page
        'recording.enabled': false, // disable as it requires Dropbox integration
        'live-streaming.enabled': false, // 'sign in on Google' button not yet functional
        'android.screensharing.enabled': false, // experimental feature, not fully production ready
    },

    // optional parameters
    subject: string, // name of the video room
    displayName: string, // user's display name
    email: string, // user's email
    avatarURL: string, // user's avatar url
    startWithAudioMuted: true, // start with audio muted, default: false
    startWithVideoMuted: false, // start with video muted, default: false
    chatEnabled: false, // enable Chat feature, default: true
    inviteEnabled: false, // enable Invitation feature, default: true

    // advanced parameters (optional)
    token: string, // jwt authentication token
    configOverrides: { 'p2p.enabled': false }, // see list of config overrides in the official Jitsi Meet SDK documentation
});
console.log(result) // { success: true }

window.addEventListener('onConferenceJoined', () => {
    // do things here
});
window.addEventListener('onConferenceTerminated', () => {
    // do things here
});
window.addEventListener('onConferenceLeft', () => {
    // do things here
});

const result = await Jitsi.leaveConference()
console.log(result) // { success: true }
```

3. Build the project

```
$ npm run build
```

4. Follow the deployment instructions for [Android](android/README.md) and [iOS](ios/README.md).

## Official Jitsi-Meet SDK Documentation

This plugin uses the Jitsi Meet SDK. See the [Jitsi Meet SDK documentation](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-ios-sdk), and the lists of [feature flags](https://jitsi.github.io/handbook/docs/dev-guide/mobile-feature-flags) and [config overrides](https://github.com/jitsi/jitsi-meet/blob/master/config.js).

## Sample React App for Android

You can see a [React Demo App](https://github.com/calvinckho/react-capacitor-jitsi-meet-sample) which runs the Jitsi meeting on the Android device

## Feature Requests, Jitsi SDK UI Customizations, Picture-In-Picture Mode Implementation

For feature requests, create an issue with a label 'feature request'. I also offer paid consultation services, such as SDK UI customization, and helping you implement the [Picture-in-Picture mode of the video view](https://ds.ivr.solutions/media/pip_demo.mp4). To submit a request, create an issue and add the label 'sdk customization' or 'pip implementation'.

## Using the Latest SDK Version

Jitsi releases new SDK versions fairly frequently. Help is appreciated to update the plugin to use the latest SDKs, and to test it on actual projects. Follow the instructions below to update the plugin, then create a PR. I will release it on NPM once it is thoroughly tested.

### iOS folder:

in /CapacitorJitsiMeet.podspec, change s.dependency 'JitsiMeetSDK' with the latest version number

in /ios/Plugin/Podfile, update line 10 and line 25 with the latest version number


### Android folder:

in /capacitor-jitsi-meet/android/build.gradle, update line 55 with the latest version number
```
implementation ('org.jitsi.react:jitsi-meet-sdk:[version number]') { transitive = true }
```

## Production Jitsi Server Configuration
https://meet.jitsi.si is designed for testing purposes when used with the Jitsi API. According to Jitsi, it is not intended for production use. Meetings created with this server will be limited to 5 minutes. In addition, also note that fully anonymous meetings are no longer possible under the Jitsi Meet API when using this server (see authentication post below).

The examples here use `url: 'https://meet.jit.si'` for the Jitsi server, but this server is only intended for test usage.

For production usage, one should either use a self-hosted server, or use [JaaS](https://jaas.8x8.vc/), Jitsi-as-a-Service. This latter service can be found at https://jaas.8x8.vc/.

To use JaaS with this plugin use: `url: 'https://8x8.vc/'` when initializing via the start() function. For embedding in web applications (a browser), use the iFrame API, not this plugin. You will need to load the iFrame javascript from `https://8x8.vc/libs/external_api.min.js'`. 

The usage of JaaS requires an account and a payment method. At the time of this writing, 25 users are permitted per month for free, and after that one must pay.

Further information:

https://jitsi.org/api/

https://jitsi.org/blog/authentication-on-meet-jit-si/

https://community.jitsi.org/t/authentication-on-meet-jit-si/125922/53


## Acknowledgements

This plugin enables web and mobile apps to implement video conferencing feature for free. The Jitsi Meet mobile SDKs are actively maintained by Jitsi. Video bridges for multi-user video conferencing is powered by [Jitsi Meet](https://meet.jit.si), or you can run [your own video bridge](https://jitsi.github.io/handbook/docs/devops-guide/devops-guide-start).
