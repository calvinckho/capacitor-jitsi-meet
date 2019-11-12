//
//  JitsiMeetViewController.swift
//  Plugin
//
//  Created by Calvin Ho on 1/25/19.
//  Copyright © 2019 Max Lynch. All rights reserved.
//

import Foundation
import UIKit
import JitsiMeet

public class JitsiMeetViewController: UIViewController {
    
    var jitsiMeetView: JitsiMeetView!
    var url: String = ""
    var roomName: String = ""
    var startWithAudioMuted: Bool = false
    var startWithVideoMuted: Bool = false
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

        let options = JitsiMeetConferenceOptions.fromBuilder({ builder in
            builder.serverURL = URL(string: self.url)
            builder.room = self.roomName
            builder.audioMuted = self.startWithAudioMuted
            builder.videoMuted = self.startWithVideoMuted
        }
        jitsiMeetView.join(options)
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
