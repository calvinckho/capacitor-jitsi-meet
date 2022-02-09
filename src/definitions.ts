export interface JitsiPlugin {
    joinConference(options: {
        roomName: string;
        url: string;
        token: string;
        channelLastN: string;
        displayName: string;
        email: string;
        avatarURL: string;
        startWithAudioMuted: boolean;
        startWithVideoMuted: boolean;
        chatEnabled: boolean;
        inviteEnabled: boolean;
        callIntegrationEnabled: boolean;
        recordingEnabled: boolean;
        liveStreamingEnabled: boolean;
        screenSharingEnabled: boolean;
    }): Promise<{
        roomName: string;
        url: string;
        token: string;
        channelLastN: string;
        displayName: string;
        email: string;
        avatarURL: string;
        startWithAudioMuted: boolean;
        startWithVideoMuted: boolean;
        chatEnabled: boolean;
        inviteEnabled: boolean;
        callIntegrationEnabled: boolean;
        recordingEnabled: boolean;
        liveStreamingEnabled: boolean;
        screenSharingEnabled: boolean;
    }>;
    leaveConference(options: {}): Promise<{}>;
}
