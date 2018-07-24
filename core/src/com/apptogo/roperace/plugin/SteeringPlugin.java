package com.apptogo.roperace.plugin;

import com.apptogo.roperace.actors.Rope;
import com.apptogo.roperace.actors.Viewfinder;
import com.apptogo.roperace.custom.InvisibleSteeringActor;
import com.apptogo.roperace.screen.GameScreen;

public abstract class SteeringPlugin extends AbstractPlugin {

	private GameScreen screen;

	protected Rope rope;
	protected Viewfinder viewfinder;
	protected InvisibleSteeringActor invisibleSteeringActor;
	
	public SteeringPlugin(GameScreen screen) {
		this.screen = screen;
	}

	@Override
	public void postSetActor() {
		viewfinder = new Viewfinder(screen, actor);
		rope = new Rope(screen, actor);
		invisibleSteeringActor = new InvisibleSteeringActor(screen, viewfinder);
	}

	@Override
	public void dispose() {
		rope.destroyCurrentJoint();
	}

	public Rope getRope() {
		return rope;
	}
	
}
