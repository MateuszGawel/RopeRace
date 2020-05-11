package com.apptogo.roperace.enums;

import java.util.HashMap;
import java.util.Map;

public enum Powerup {

	JUMP(1,"jump-big-button", 1000, "JUMP", "Jump in air? No problem!\nYou can jump in direction of flight.\nNo rope attached, no surface touched."),
	SLIPPY(2,"jump-big-button", 1, "SLIPPY", "Oops, everything is so slippery.\nFriction disappears for 10s,\nyou can slip through tight corridors faster.");

	public int number;
	public String regionName;
	public int cost;
	public String displayName;
	public String description;

	private static Map<Integer, Powerup> map = new HashMap<>();

	static {
		for (Powerup ball : Powerup.values()) {
			map.put(ball.number, ball);
		}
	}

	Powerup(int number, String regionName, int cost, String displayName, String description) {
		this.number = number;
		this.regionName = regionName;
		this.cost = cost;
		this.displayName = displayName;
		this.description = description;
	}

	public static Powerup valueOf(Integer number){
		return map.get(number);
	}
}
