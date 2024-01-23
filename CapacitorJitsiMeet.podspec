
  Pod::Spec.new do |s|
    s.name = 'CapacitorJitsiMeet'
    s.version = '4.1.0'
    s.summary = 'This plugin is used to make video calls using Jitsi video platform (https://meet.jit.si) on iOS and Android using Capacitor. Since the current implementation of Ionic/PWA apps on iOS run on top of WKWebView, and as of today (24/01/2019), Apple does not support WebRTC on WKWebView, the only way to work with Jitsi Video on this platform is to build it natively.'
    s.license = 'MIT'
    s.homepage = 'https://github.com/calvinckho/capacitor-jitsi-meet'
    s.author = 'Calvin Ho'
    s.source = { :git => 'https://github.com/calvinckho/capacitor-jitsi-meet', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.resource_bundles = {
        'Plugin' => [
        'ios/Plugin/Plugin/*.storyboard'
        ]
    }
    s.ios.deployment_target  = '13.0'
    s.dependency 'Capacitor'
    s.dependency 'JitsiMeetSDK', '8.6.3'
  end
