package com.apptogo.roperace.save;

import com.apptogo.roperace.scene2d.ColorSet;

public class LevelNode {
	private int levelNo;
	private int worldNo;
	private ColorSet medal;

	public LevelNode(){	}

	public LevelNode(int levelNo, int worldNo, ColorSet medal) {
		this.levelNo = levelNo;
		this.worldNo = worldNo;
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

	public int getWorldNo() {
		return worldNo;
	}
	
	public void setWorldNo(int worldNo){
		this.worldNo = worldNo;
	}
}
