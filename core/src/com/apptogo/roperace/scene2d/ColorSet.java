package com.apptogo.roperace.scene2d;

import com.badlogic.gdx.graphics.Color;

public enum ColorSet {
	BLUE(new Color(0, 0.7f, 1, 1), new Color(0, 0.54f, 0.75f, 1)),
	GREEN(new Color(0.65f, 0.86f, 0.25f, 1), new Color(0.47f, 0.63f, 0.16f, 1)),
	GOLD(new Color(0.9f, 0.78f, 0.11f, 1), new Color(0.79f, 0.67f, 0f, 1)),
	SILVER(new Color(0.73f, 0.73f, 0.73f, 1), new Color(0.59f, 0.59f, 0.59f, 1)),
	BRONZE(new Color(0.69f, 0.48f, 0.2f, 1), new Color(0.54f, 0.37f, 0.15f, 1));

	private final Color mainColor;
	private final Color shadowColor;

	ColorSet(Color mainColor, Color shadowColor) {
		this.mainColor = mainColor;
		this.shadowColor = shadowColor;
	}

	public Color getMainColor() {
		return mainColor;
	}

	public Color getShadowColor() {
		return shadowColor;
	}

}
