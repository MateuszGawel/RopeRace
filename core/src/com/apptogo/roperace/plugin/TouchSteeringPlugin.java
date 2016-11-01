package com.apptogo.roperace.plugin;

import com.badlogic.gdx.Gdx;

public class TouchSteeringPlugin extends SteeringPlugin {
	private float CENTER = Gdx.app.getGraphics().getWidth() / 2f;

	@Override
	public void run() {
		super.run();

		if (Gdx.input.getInputProcessor() != null) {
			if (Gdx.input.justTouched() && Gdx.input.getX() > CENTER) {

			}
		}
	}
}
