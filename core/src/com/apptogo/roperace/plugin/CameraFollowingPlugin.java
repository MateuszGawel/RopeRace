package com.apptogo.roperace.plugin;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraFollowingPlugin extends AbstractPlugin {

	private OrthographicCamera camera;
	
	@Override
	public void postSetActor() {
		camera = (OrthographicCamera)actor.getStage().getCamera();
		camera.position.set(0, actor.getBody().getPosition().y - 0, 0);
	}
	
	@Override
	public void run() { 
		camera.position.set(actor.getBody().getPosition().x + 1f, camera.position.y, camera.position.z);
	}

	@Override
	public void setUpDependencies() {
		// TODO Auto-generated method stub
		
	}

}
