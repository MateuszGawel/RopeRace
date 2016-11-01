package com.apptogo.roperace.plugin;

import com.apptogo.roperace.game.GameActor;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class AbstractPlugin {
	protected GameActor actor;
	protected Body body;
	
	abstract public void setUpDependencies();
	abstract public void run();
	
	public void postSetActor() {	
	}
	
	public GameActor getActor() {
		return actor;
	}

	public void setActor(GameActor actor) {
		this.actor = actor;
		this.body = actor.getBody();
		
		setUpDependencies();
		postSetActor();
	}
}
