package com.apptogo.roperace.scene2d;

import com.badlogic.gdx.graphics.Color;

public enum ColorSet {
	BLUE(new Color(0, 0.7f, 1, 1), new Color(0, 0.54f, 0.75f, 0)),
	GREEN(new Color(0.65f, 0.86f, 0.25f, 1), new Color(0.47f, 0.63f, 0.16f, 0)),
	GRAY(new Color(0.13f, 0.13f, 0.13f, 1), new Color(0.23f, 0.23f, 0.23f, 0)),
	PURPLE(new Color(0.73f, 0.22f, 0.71f, 1), new Color(0.85f, 0.09f, 0.83f, 0)),
	
	//MEDALS
	GOLD(new Color(0.9f, 0.78f, 0.11f, 1), new Color(0.79f, 0.67f, 0f, 1), 3),
	SILVER(new Color(0.73f, 0.73f, 0.73f, 1), new Color(0.59f, 0.59f, 0.59f, 1), 2),
	BRONZE(new Color(0.69f, 0.48f, 0.2f, 1), new Color(0.54f, 0.37f, 0.15f, 1), 1);

	private final Color mainColor;
	private final Color shadowColor;
	private int medalNumber;
	
	ColorSet(Color mainColor, Color shadowColor) {
		this.mainColor = mainColor;
		this.shadowColor = shadowColor;
		this.medalNumber = 0;
	}
	
	ColorSet(Color mainColor, Color shadowColor, int medalNumber) {
		this.mainColor = mainColor;
		this.shadowColor = shadowColor;
		this.medalNumber = medalNumber;
	}

	public Color getMainColor() {
		return mainColor;
	}

	public Color getShadowColor() {
		return shadowColor;
	}

	public int getMedalNumber() {
		return medalNumber;
	}

}
