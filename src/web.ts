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
      console.log('this is a sample feature. It uses the default jitsi.meet interface for web implementation using this roomName:', options.roomName);
      window.open('https://meet.jit.si' + options.roomName + ';channelLastN=' + options.channelLastN + ';startWithAudioMuted=' + options.startWithAudioMuted + ';startWithVideoMuted=' + options.startWithVideoMuted, '_blank');
      return options;
  }
}

const Jitsi = new JitsiWeb();

export { Jitsi };
