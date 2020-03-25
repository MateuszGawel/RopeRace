package com.apptogo.roperace.save;

import java.util.ArrayList;
import java.util.List;

import com.apptogo.roperace.enums.BallData;
import com.apptogo.roperace.enums.ColorSet;

public class GameData {

	public GameData() {
		//first always unlocked
		unlockedLevels.add(new LevelNode(1, 1, ColorSet.GRAY));
		unlockedWorlds.add(1);
		unlockedBalls.add(BallData.NORMAL.number);
		activeBall = BallData.NORMAL.number;
	}

	protected static final String NAME = "GAME_DATA";

	private int points = 100;
	private int activeBall;
	private List<LevelNode> unlockedLevels = new ArrayList<LevelNode>();
	private List<Integer> unlockedWorlds = new ArrayList<Integer>();
	private List<Integer> unlockedBalls = new ArrayList<Integer>();

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

}
