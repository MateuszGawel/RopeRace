package com.apptogo.roperace.save;

import java.util.ArrayList;
import java.util.List;

public class GameData {

	protected static final String NAME = "GAME_DATA";

	private int points;
	private List<LevelNode> unlockedLevels = new ArrayList<LevelNode>();

	protected int getPoints() {
		return points;
	}

	protected void setPoints(int points) {
		this.points = points;
	}

	protected List<LevelNode> getUnlockedLevels() {
		return unlockedLevels;
	}

}
