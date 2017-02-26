package com.apptogo.roperace.save;

import com.apptogo.roperace.scene2d.ColorSet;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.googlecode.gwt.crypto.client.TripleDesKeyGenerator;

public class SaveManager {

	private static final boolean ALWAYS_CLEAR = true;
	
	private Preferences save;
	private TripleDesCipher encryptor;

	public SaveManager() {
		save = Gdx.app.getPreferences("saveData");
		initializeEncryptor();
		gameData = load();
	}

	private GameData load() {
		Json json = new Json();

		if(ALWAYS_CLEAR){
			gameData = new GameData();
			save();
		}

		if (save.getString(GameData.NAME, null) == null) {
			gameData = new GameData();
			save();
		}

		String encryptedGameData = save.getString(GameData.NAME);
		String gameDataJson = decryptString(encryptedGameData);

		return json.fromJson(GameData.class, gameDataJson);
	}

	private void save() {
		Json json = new Json();

		String gameDataJson = json.prettyPrint(gameData);
		String encryptedGameData = encryptString(gameDataJson);

		save.putString(GameData.NAME, encryptedGameData);
		save.flush();
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------- PUBLIC METHODS ---------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	public void addPoints(int points){
		gameData.setPoints(gameData.getPoints() + points);
		save();
	}
	
	public void usePoints(int points){
		gameData.setPoints(gameData.getPoints() - points);
		save();
	}
	
	public int getPoints() {
		return gameData.getPoints();
	}
	
	public void unlockLevel(int levelNo, ColorSet medal) {
		LevelNode levelNode = getByNumber(levelNo);

		if (levelNode != null) {
			if (levelNode.getMedal().getMedalNumber() < medal.getMedalNumber()) {
				levelNode.setMedal(medal);
				save();
			}
		} else {
			levelNode = new LevelNode(levelNo, medal);
			gameData.getUnlockedLevels().add(levelNode);
			save();
		}
	}
	
	public ColorSet getMedalForLevel(int levelNo){
		LevelNode levelNode = getByNumber(levelNo);
		if(levelNode != null)
			return levelNode.getMedal();
		else
			return ColorSet.GRAY;
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------ HELPERS ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	private LevelNode getByNumber(int levelNo) {
		for (LevelNode levelNode : gameData.getUnlockedLevels()) {
			if (levelNode.getLevelNo() == levelNo)
				return levelNode;
		}
		return null;
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ---------------------------------------------- ENCRYPTION ------------------------------------------------ **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	private void initializeEncryptor() {
		TripleDesKeyGenerator generator = new TripleDesKeyGenerator();
		byte[] key = generator.decodeKey("04578a8f0be3a7109d9e5e86839e3bc41654927034df92ec");

		encryptor = new TripleDesCipher();
		encryptor.setKey(key);
	}

	private String encryptString(String string) {
		try {
			string = encryptor.encrypt(string);
		} catch (DataLengthException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (InvalidCipherTextException e1) {
			e1.printStackTrace();
		}

		return string;
	}

	private String decryptString(String string) {
		try {
			string = encryptor.decrypt(string);
		} catch (DataLengthException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (InvalidCipherTextException e) {
			e.printStackTrace();
		}

		return string;
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ----------------------------------------------- MANAGER -------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	private static SaveManager INSTANCE;
	public static GameData gameData;

	public static void create() {
		INSTANCE = new SaveManager();
	}

	public static void destroy() {
		INSTANCE = null;
	}

	public static SaveManager getInstance() {
		return INSTANCE;
	}


}
