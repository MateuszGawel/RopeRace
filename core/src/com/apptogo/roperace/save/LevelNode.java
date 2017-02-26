package com.apptogo.roperace.save;

import com.apptogo.roperace.scene2d.ColorSet;

public class LevelNode {
	private int levelNo;
	private ColorSet medal;

	public LevelNode(){	}

	public LevelNode(int levelNo, ColorSet medal) {
		this.levelNo = levelNo;
		this.medal = medal;
	}

	public int getLevelNo() {
		return levelNo;
	}

	public void setLevelNo(int levelNo) {
		this.levelNo = levelNo;
	}

	public ColorSet getMedal() {
		return medal;
	}

	public void setMedal(ColorSet medal) {
		this.medal = medal;
	}
}
