//
//  JitsiMeetViewController.swift
//  Plugin
//
//  Created by Calvin Ho on 1/25/19.
//  Copyright © 2019 Max Lynch. All rights reserved.
//

import Foundation
import UIKit
import JitsiMeetSDK

public class JitsiMeetViewController: UIViewController {

    var jitsiMeetView: JitsiMeetView!
    var url: String = ""
    var roomName: String = ""
    var token: String? = nil
    var startWithAudioMuted: Bool = false
    var startWithVideoMuted: Bool = false
    var chatEnabled: Bool = true
    var inviteEnabled: Bool = true
    var callIntegrationEnabled: Bool = true
    var email: String? = nil
    var displayName: String? = nil
    var avatarUrl: String? = nil
    let userLocale = NSLocale.current as NSLocale
    weak var delegate: JitsiMeetViewControllerDelegate?

    public override func viewDidLoad() {
        super.viewDidLoad()
        print("[Jitsi Plugin Native iOS]: JitsiMeetViewController::viewDidLoad");
    }

    public override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated);

        print("[Jitsi Plugin Native iOS]: JitsiMeetViewController::viewDidAppear");

        jitsiMeetView = view as? JitsiMeetView;
        jitsiMeetView?.delegate = self

        if  userLocale.countryCode?.contains("CN") ?? false ||
            userLocale.countryCode?.contains("CHN") ?? false ||
            userLocale.countryCode?.contains("MO") ?? false ||
            userLocale.countryCode?.contains("HK") ?? false {
            print("currentLocale is China so we cannot use CallKit.")
            callIntegrationEnabled = false
        }

        let userInfo = JitsiMeetUserInfo()
        userInfo.displayName = self.displayName
        userInfo.email = self.email
        userInfo.avatar = URL(string: self.avatarUrl ?? "")

        let options = JitsiMeetConferenceOptions.fromBuilder({ builder in
            builder.serverURL = URL(string: self.url)
            builder.room = self.roomName
            builder.token = self.token
            builder.setFeatureFlag("meeting-name.enabled", withBoolean: false)
            builder.setFeatureFlag("chat.enabled", withBoolean: self.chatEnabled)
            builder.setFeatureFlag("invite.enabled", withBoolean: self.inviteEnabled)
            builder.setFeatureFlag("call-integration.enabled", withBoolean: self.callIntegrationEnabled)

            builder.userInfo = userInfo
        })
        jitsiMeetView.join(options)
        jitsiMeetView.setAudioMuted(self.startWithAudioMuted)
        jitsiMeetView.setVideoMuted(self.startWithVideoMuted)
    }

    public override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)

        print("[Jitsi Plugin Native iOS]: JitsiMeetViewController::leaveConference");

        jitsiMeetView = view as? JitsiMeetView;
        jitsiMeetView?.delegate = self
        jitsiMeetView.leave()

    }
}

protocol JitsiMeetViewControllerDelegate: AnyObject {
    func onConferenceJoined()

    func onConferenceLeft()
}

// MARK: JitsiMeetViewDelegate
extension JitsiMeetViewController: JitsiMeetViewDelegate {
    @objc public func conferenceJoined(_ data: [AnyHashable : Any]!) {
        delegate?.onConferenceJoined()
        print("[Jitsi Plugin Native iOS]: JitsiMeetViewController::conference joined");
    }

    @objc public func conferenceTerminated(_ data: [AnyHashable : Any]!) {
        print("[Jitsi Plugin Native iOS]: JitsiMeetViewController::conference left");
        delegate?.onConferenceLeft()
        self.dismiss(animated: true, completion: nil); // e.g. user ends the call. This is preferred over conferenceLeft to shorten the white screen while exiting the room
    }
}
