// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorJitsiMeet",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "CapacitorJitsiMeet",
            targets: ["CapacitorJitsiMeet"]
        )
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "8.0.0"),
        .package(url: "https://github.com/jitsi/jitsi-meet-ios-sdk-releases", from: "12.0.0")
    ],
    targets: [
        .target(
            name: "CapacitorJitsiMeet",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm"),
                .product(name: "JitsiMeetSDK", package: "jitsi-meet-ios-sdk-releases")
            ],
            path: "ios/Sources/CapacitorJitsiMeet",
            exclude: ["Info.plist"],
            resources: [
                .process("JitsiMeet.storyboard")
            ]
        ),
        .testTarget(
            name: "CapacitorJitsiMeetTests",
            dependencies: ["CapacitorJitsiMeet"],
            path: "ios/Tests/CapacitorJitsiMeetTests"
        )
    ]
)
