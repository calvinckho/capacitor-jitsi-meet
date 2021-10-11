import { registerPlugin } from '@capacitor/core';

import type { JitsiPlugin } from './definitions';

const Jitsi = registerPlugin<JitsiPlugin>('Jitsi', {
    web: () => import('./web').then(m => new m.JitsiWeb()),
});

export * from './definitions';
export { Jitsi };
