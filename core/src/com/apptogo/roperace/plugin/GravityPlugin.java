package com.apptogo.roperace.plugin;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;

public class GravityPlugin extends AbstractPlugin {

	private final Logger logger = new Logger(getClass().getName(), Logger.DEBUG);

	private final Vector2 gravity = new Vector2(0, -5);

	@Override
	public void run() {
		actor.getBody().applyForceToCenter(gravity, false);
	}

	@Override
	public void setUpDependencies() {

	}
}
