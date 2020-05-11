package com.apptogo.roperace.save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apptogo.roperace.enums.BallData;
import com.apptogo.roperace.enums.ColorSet;
import com.apptogo.roperace.enums.Powerup;

public class GameData {

	public GameData() {
		//first always unlocked
		unlockedLevels.add(new LevelNode(1, 1, ColorSet.GRAY));
		unlockedLevels.add(new LevelNode(2, 1, ColorSet.GRAY));
		unlockedLevels.add(new LevelNode(3, 1, ColorSet.GRAY));
//        unlockedLevels.add(new LevelNode(4, 1, ColorSet.GRAY));
		unlockedWorlds.add(1);
		unlockedBalls.add(BallData.NORMAL.number);
		activeBall = BallData.NORMAL.number;
	}

	protected static final String NAME = "GAME_DATA";

	private int points = 1000;
	private int activeBall;
	private List<LevelNode> unlockedLevels = new ArrayList<LevelNode>();
	private List<Integer> unlockedWorlds = new ArrayList<Integer>();
	private List<Integer> unlockedBalls = new ArrayList<Integer>();
	private Map<Powerup, Integer> boughtPowerups = new HashMap<>();

	protected int getPoints() {
		return points;
	}

	protected void setPoints(int points) {
		this.points = points;
	}

	protected List<LevelNode> getUnlockedLevels() {
		return unlockedLevels;
	}

	public List<Integer> getUnlockedWorlds() {
		return unlockedWorlds;
	}

	public List<Integer> getUnlockedBalls() {
		return unlockedBalls;
	}

	public int getActiveBall() {
		return activeBall;
	}

	public void setActiveBall(int activeBall) {
		this.activeBall = activeBall;
	}

	public Map<Powerup, Integer> getBoughtPowerups() {
		return boughtPowerups;
	}

	public void buyPowerup(Powerup powerup){
		Integer count = boughtPowerups.get(powerup);
		boughtPowerups.put(powerup, count != null ? ++count : 1);
	}
}
