package org.omnirom.games.eggs;

import android.media.SoundPool;
import android.util.Log;

public class SoundHandler {
	
	private SoundPool soundPool;
	private int[] soundIds = new int[7];
	private boolean soundsOn;
	private MainActivity main;
	private int mStreamId = -1;

    public static final int SOUND_COUNTDOWN = 0;
    public static final int SOUND_GO = 1;

    public SoundHandler(MainActivity main, boolean soundsOn) {
		this.main = main;
		this.soundsOn = soundsOn;
	}

	public void initializeSoundFx() {
		soundPool = new SoundPool.Builder().setMaxStreams(7).build();
		soundIds[SOUND_COUNTDOWN] = soundPool.load(main, R.raw.countdown_sec, 1);
		soundIds[SOUND_GO] = soundPool.load(main, R.raw.countdown_sec_go, 1);
		
	}
	
	void playSoundEffect(int id, int vol) {
		
		if(soundsOn){
			if (mStreamId != -1) {
				soundPool.stop(mStreamId);
			}
			mStreamId = soundPool.play(soundIds[id], vol, vol, 1, 0, 1);
			Log.d("maxwen", "play " + id);
		}
	}
}
