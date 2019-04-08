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
    }): Promise<{
        roomName: string;
        url: string;
        channelLastN: string;
    }>;
}
