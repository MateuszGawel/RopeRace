package com.apptogo.roperace.plugin;

import java.util.HashMap;
import java.util.Map;

import com.apptogo.roperace.exception.SoundException;
import com.apptogo.roperace.manager.ResourcesManager;
import com.badlogic.gdx.audio.Sound;

public class SoundPlugin extends AbstractPlugin {

	private Map<String, Sound> sounds = new HashMap<String, Sound>();

	//TODO add sound volume handling (player position)

	public SoundPlugin(String... soundNames) {
		for (String soundName : soundNames)
			addSound(soundName);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public Map<String, Sound> getSounds() {
		return sounds;
	}

	public Sound getSound(String soundName) {
		Sound sound = sounds.get(soundName);
		if (sound == null)
			throw new SoundException("Sound: '" + soundName + "' not registered in SoundHandler of: '" + actor.getName() + "'");
		return sound;
	}

	public void addSound(String soundName, Sound sound) {
		if (sounds.containsKey(soundName))
			throw new SoundException("Sound: '" + soundName + "' is already defined for: '" + actor.getName() + "'");
		this.sounds.put(soundName, sound);
	}

	public void addSound(String soundName) {
		Sound sound = ResourcesManager.getInstance().getSound(soundName);
		if (sounds.containsKey(soundName))
			throw new SoundException("Sound: '" + soundName + "' is already defined for: '" + actor.getName() + "'");
		this.sounds.put(soundName, sound);
	}

	public long playSound(String soundName) {
		return get(soundName).play();
	}

	public long loopSound(String soundName) {
		return get(soundName).loop();
	}

	public void stopSound(String soundName) {
		get(soundName).stop();
	}

	public void pauseSound(String soundName) {
		get(soundName).pause();
	}

	public void resumeSound(String soundName) {
		get(soundName).resume();
	}
	
	/**
	 * Stops all sounds (not played staticly)
	 */
	public void stopAllSounds(){
		for (Sound sound : sounds.values()){
			sound.stop();
		}
	}
	
	private Sound get(String soundName) {
		Sound sound = sounds.get(soundName);
		if (sound == null)
			throw new SoundException("Sound: '" + soundName + "' not registered in SoundHandler of: '" + actor.getName() + "'");
		return sound;
	}

	@Override
	public void setUpDependencies() {
		// TODO Auto-generated method stub

	}

	/** ---------- STATIC METHODS ---------- **/

	/**
	 * Play sound if loaded in resources manager
	 * 
	 * @param soundName to play
	 * @return id of played sound
	 */
	public static long playSingleSound(String soundName) {
		Sound sound = ResourcesManager.getInstance().getSound(soundName);
		return sound.play();
	}

	/**
	 * Loop sound if loaded in resources manager
	 * 
	 * @param soundName to play
	 * @return id of played sound
	 */
	public static long loopSingleSound(String soundName) {
		Sound sound = ResourcesManager.getInstance().getSound(soundName);
		return sound.loop();
	}

	/**
	 * Pause sound if loaded in resources manager
	 * 
	 * @param soundName to play
	 * @return id of played sound
	 */
	public static void pauseSingleSound(String soundName) {
		Sound sound = ResourcesManager.getInstance().getSound(soundName);
		sound.pause();
	}

	/**
	 * Stop sound if loaded in resources manager
	 * 
	 * @param soundName to play
	 * @return id of played sound
	 */
	public static void stopSingleSound(String soundName) {
		Sound sound = ResourcesManager.getInstance().getSound(soundName);
		sound.stop();
	}
	
	/**
	 * Sets volume of specified played sound instance of specified sound type.
	 * 
	 * @param soundName of sound for changing sound
	 * @param soundId instance of played sound
	 * @param volume between 0 to 1
	 */
	public static void setVolume(String soundName, long soundId, float volume) {
		Sound sound = ResourcesManager.getInstance().getSound(soundName);
		sound.setVolume(soundId, volume);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}