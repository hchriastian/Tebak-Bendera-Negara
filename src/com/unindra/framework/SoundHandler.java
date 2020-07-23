package com.unindra.framework;

import java.util.HashMap;

import com.futuristicbear.americanquiz.R;
import com.unindra.flq.Config;
import com.unindra.flq.X;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
/**
 * This class handles sounds in game
 * 
 *
 */
public class SoundHandler {

	private static SoundHandler instance = null;
	private HashMap<Integer, Integer> soundMap;
	private SoundPool sp;

	/**
	 * Sound constants
	 */
	public static final int SOUND_CLICK = 1;
	public static final int SOUND_SUCCESS = 2;
	public static final int SOUND_WRONG = 3;

	private SoundHandler() {
		// EMPTY CONSTRUCTOR
	}

	/**
	 * Singleton pattern
	 * @return instance of class
	 */
	public static SoundHandler getInstance() {
		if (instance == null) {
			instance = new SoundHandler();
		}
		return instance;
	}

	/**
	 * Initialize sounds, add sound references to sound constants
	 * @param c context
	 */
	public void initSounds(Context c) {
		sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		soundMap = new HashMap<Integer, Integer>();
		soundMap.put(SOUND_CLICK, sp.load(c, R.raw.click, 1));
		soundMap.put(SOUND_SUCCESS, sp.load(c, R.raw.ok, 1));
		soundMap.put(SOUND_WRONG, sp.load(c, R.raw.wrong, 1));
	}

	/**
	 * Play the sound
	 * @param c context
	 * @param sound constant which sound should be played
	 */
	public void playSound(Context c, int sound) {
		if (X.getBooleanPreferences(Config.PREFS_SOUND, true, c)) {
			AudioManager mgr = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = streamVolumeCurrent / streamVolumeMax;
			sp.play(sound, volume, volume, 1, 0, 1.0f);
		}
	}

}
