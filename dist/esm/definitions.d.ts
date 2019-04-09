declare global {
    interface PluginRegistry {
        Jitsi?: JitsiPlugin;
    }
}
export interface JitsiPlugin {
    joinConference(options: {
        roomName: string;
        url: string;
        channelLastN: string;
        startWithAudioMuted: boolean;
        startWithVideoMuted: boolean;
    }): Promise<{
        roomName: string;
        url: string;
        channelLastN: string;
        startWithAudioMuted: boolean;
        startWithVideoMuted: boolean;
    }>;
}
