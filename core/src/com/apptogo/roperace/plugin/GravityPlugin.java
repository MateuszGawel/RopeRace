package com.apptogo.roperace.plugin;

import com.badlogic.gdx.math.Vector2;

public class GravityPlugin extends AbstractPlugin {

	private final Vector2 gravity = new Vector2(0, -15);
	private boolean started = false;
	
	@Override
	public void run() {
//		if(started)
//			actor.getBody().applyForceToCenter(gravity, true);
	}

	@Override
	public void setUpDependencies() {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
}
