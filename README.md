# Jitsi Meet Capacitor Plugin for Ionic Apps

This plugin is used to make video calls using Jitsi video platform (https://meet.jit.si) on iOS and Android using Capacitor. Since the current implementation of Ionic/PWA apps on iOS run on top of WKWebView, and as of today (24/01/2019), Apple does not support WebRTC on WKWebView, the only way to work with Jitsi Video on this platform is to build it natively.

## Embedding in external applications

Jitsi Meet provides a very flexible way of embedding it in external applications by using the [Jitsi Meet API](doc/api.md).

## Security
WebRTC today does not provide a way of conducting multiparty conversations with
end-to-end encryption. As a matter of fact, unless you consistently vocally
compare DTLS fingerprints with your peers, the same goes for one-to-one calls.
As a result when using a Jitsi Meet instance, your stream is encrypted on the
network but decrypted on the machine that hosts the bridge.

The Jitsi Meet architecture allows you to deploy your own version, including
all server components, and in that case your security guarantees will be roughly
equivalent to these of a direct one-to-one WebRTC call. This is what's unique to
Jitsi Meet in terms of security.

The [meet.jit.si](https://meet.jit.si) service is maintained by the Jitsi team
at [8x8](https://8x8.com).

## Mobile app
Jitsi Meet is also available as a React Native app for Android and iOS.
Instructions on how to build it can be found in the ios and android directories.

## Acknowledgements

Jitsi Meet started out as a sample conferencing application using Jitsi Videobridge. It was originally developed by then ESTOS' developer Philipp Hancke who then contributed it to the community where development continues with joint forces!