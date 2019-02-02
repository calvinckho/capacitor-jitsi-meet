declare global {
    interface PluginRegistry {
        Jitsi?: JitsiPlugin;
    }
}

export interface JitsiPlugin {
    joinConference(options: { roomName: string, url: string }): Promise<{roomName: string, url: string}>;
}
