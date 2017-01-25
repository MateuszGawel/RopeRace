package com.apptogo.roperace.level;

public class LevelData {

	public enum LevelType {
		TIME, DIAMONDS, ROPES
	}

	private LevelType type;
	private float goldReq;
	private float silverReq;
	private float bronzeReq;

	public LevelData(LevelType type, float goldReq, float silverReq, float bronzeReq) {
		super();
		this.type = type;
		this.goldReq = goldReq;
		this.silverReq = silverReq;
		this.bronzeReq = bronzeReq;
	}

	public double getGoldReq() {
		return goldReq;
	}

	public void setGoldReq(float goldReq) {
		this.goldReq = goldReq;
	}

	public double getSilverReq() {
		return silverReq;
	}

	public void setSilverReq(float silverReq) {
		this.silverReq = silverReq;
	}

	public double getBronzeReq() {
		return bronzeReq;
	}

	public void setBronzeReq(float bronzeReq) {
		this.bronzeReq = bronzeReq;
	}

	public LevelType getType() {
		return type;
	}

	public void setType(LevelType type) {
		this.type = type;
	}

}
