package com.apptogo.roperace.save;

import com.apptogo.roperace.enums.ColorSet;
import com.apptogo.roperace.enums.Powerup;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.googlecode.gwt.crypto.client.TripleDesKeyGenerator;

public class SaveManager {

	private static final boolean DEBUG_ALWAYS_CLEAR = true;
	
	private Preferences save;
	private TripleDesCipher encryptor;

	public SaveManager() {
		save = Gdx.app.getPreferences("saveData");
		initializeEncryptor();
		gameData = load();
	}

	private GameData load() {
		Json json = new Json();

		if(DEBUG_ALWAYS_CLEAR){
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
	
	public void completeLevel(int levelNo, int worldNo, ColorSet medal) {
		LevelNode levelNode = getByNumber(levelNo, worldNo);

		if (levelNode != null) {
			if (levelNode.getMedal().getMedalNumber() < medal.getMedalNumber()) {
				levelNode.setMedal(medal);
				save();
				
				//unlock next
				LevelNode newLevelNode;
				if(worldNo == 9){
					newLevelNode = new LevelNode(1, worldNo+1, ColorSet.GRAY);
				}
				else{
					newLevelNode = new LevelNode(levelNo+1, worldNo, ColorSet.GRAY);
				}
				gameData.getUnlockedLevels().add(newLevelNode);
				save();
			}
		} 
		else {
			levelNode = new LevelNode(levelNo, worldNo, medal);
			gameData.getUnlockedLevels().add(levelNode);
			save();
		}
	}
	
	public void unlockWorld(Integer worldNumber){
		gameData.getUnlockedWorlds().add(worldNumber);
		save();
	}
	
	public ColorSet getMedalForLevel(int levelNo, int worldNo){
		LevelNode levelNode = getByNumber(levelNo, worldNo);
		if(levelNode != null)
			return levelNode.getMedal();
		else
			return ColorSet.GRAY;
	}
	
	public boolean isLevelUnlocked(int levelNumber, int worldNumber){
		//check if previous one exists. It means that the current one is available
		LevelNode level = getByNumber(levelNumber, worldNumber);
		if(level == null)
			return false;
		else
			return true;
	}
	
	public LevelNode getLatestAvailableLevel() {
		return gameData.getUnlockedLevels().get(gameData.getUnlockedLevels().size()-1);
	}
	
	public boolean isWorldUnlocked(Integer worldNumber) {
		return gameData.getUnlockedWorlds().contains(worldNumber);
	}

	public boolean isBallUnlocked(Integer number) {
		return gameData.getUnlockedBalls().contains(number);
	}
	
	public void unlockBall(Integer number) {
		if(!isBallUnlocked(number)){
			gameData.getUnlockedBalls().add(number);
			save();
		}
	}
	
	public void setActiveBall(int number) {
		gameData.setActiveBall(number);
		save();
	}
	
	public int getActiveBall() {
		return gameData.getActiveBall();
	}
	
	public boolean isBallActive(int number) {
		return gameData.getActiveBall() == number;
	}

	public void buyPowerup(Powerup powerup) {
		gameData.buyPowerup(powerup);
	}

	public int getPowerupCount(Powerup powerup){
		Integer count = gameData.getBoughtPowerups().get(powerup);
		return count != null ? count : 0;
	}

	public int getActivePowerupCount(){
		Integer count = gameData.getBoughtPowerups().get(gameData.getActivePowerup());
		return count != null ? count : 0;
	}

	public Powerup getActivePowerup() {
		return gameData.getActivePowerup();
	}

	public void setActivePowerup(Powerup activePowerup) {
		gameData.setActivePowerup(activePowerup);
	}
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------ HELPERS ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	private LevelNode getByNumber(int levelNo, int worldNo) {
		for (LevelNode levelNode : gameData.getUnlockedLevels()) {
			if (levelNode.getLevelNo() == levelNo && levelNode.getWorldNo() == worldNo)
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
