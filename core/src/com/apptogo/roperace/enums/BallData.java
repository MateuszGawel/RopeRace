package com.apptogo.roperace.enums;

import java.util.HashMap;
import java.util.Map;


public enum BallData {
	
	NORMAL(1, 0.5f, 1.5f, 0.5f, 0.5f, -0.02f),
	TENNIS(2, 0.25f, 7f, 0.6f, 0.6f, -0.02f),
	BASKET(3, 0.5f, 5f, 0.5f, 0.5f, -0.02f),
	BOWLING(4, 0.5f, 5f, 0.5f, 0.5f, -0.02f),
	RUBBER(5, 0.2f, 5f, 0.5f, 0.8f, -0.02f),
	BEACH(6, 0.55f, 5f, 0.5f, 0.5f, -0.02f);
	
    private static Map<Integer, BallData> map = new HashMap<Integer, BallData>();

    static {
        for (BallData ball : BallData.values()) {
            map.put(ball.number, ball);
        }
    }
	
	public int number;
	public float size;
	public float density;
	public float friction;
	public float restitution;
	public float damping;
	
	private BallData(int number, float size, float density, float friction, float restitution, float damping) {
		this.number = number;
		this.size = size;
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
		this.damping = damping;
	}

	public static BallData valueOf(Integer number){
		return map.get(number);
	}

}
