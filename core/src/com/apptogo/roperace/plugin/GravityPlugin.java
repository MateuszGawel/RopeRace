package com.apptogo.roperace.plugin;

import com.badlogic.gdx.math.Vector2;

public class GravityPlugin extends AbstractPlugin {

	private final Vector2 gravity = new Vector2(0, -5);

	@Override
	public void run() {
		actor.getBody().applyForceToCenter(gravity, false);
	}

	@Override
	public void setUpDependencies() {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
