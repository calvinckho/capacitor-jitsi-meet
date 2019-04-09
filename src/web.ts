import { WebPlugin } from '@capacitor/core';
import { JitsiPlugin } from './definitions';

export class JitsiWeb extends WebPlugin implements JitsiPlugin {
  constructor() {
    super({
      name: 'Jitsi',
      platforms: ['web']
    });
  }

  async joinConference(options: {
      roomName: string, url: string,
      channelLastN: string;
      startWithAudioMuted: boolean;
      startWithVideoMuted: boolean; }): Promise<{
      roomName: string,
      url: string,
      channelLastN: string;
      startWithAudioMuted: boolean;
      startWithVideoMuted: boolean;
  }> {
      console.log('join conference', options);
      return options;
  }
}

const Jitsi = new JitsiWeb();

export { Jitsi };
