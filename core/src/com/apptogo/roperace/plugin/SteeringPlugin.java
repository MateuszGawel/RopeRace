package com.apptogo.roperace.plugin;

import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.physics.UserData;
import com.badlogic.gdx.utils.Logger;

public abstract class SteeringPlugin extends AbstractPlugin {

	private final Logger logger = new Logger(getClass().getName(), Logger.DEBUG);



	@Override
	public void run() {
		if (ContactListener.SNAPSHOT.collide(UserData.get(actor.getBody()), "ground")) {

		}
	}

	@Override
	public void setUpDependencies() {

	}
}
