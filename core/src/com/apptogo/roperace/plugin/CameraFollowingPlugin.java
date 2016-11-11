 package com.apptogo.roperace.plugin;

import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class CameraFollowingPlugin extends AbstractPlugin {

	private OrthographicCamera camera;
	
	private Vector2 mapSize;
	private float minCameraX;
	private float maxCameraX;
	private float minCameraY;
	private float maxCameraY;

	public CameraFollowingPlugin(Vector2 mapSize) {
		super();
		this.mapSize = mapSize;
	}

	@Override
	public void postSetActor() {
		camera = (OrthographicCamera) actor.getStage().getCamera();
		camera.setToOrtho(false, UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT));
		camera.position.set(0, actor.getBody().getPosition().y - 0, 0);
		
		minCameraX = camera.zoom * (camera.viewportWidth / 2);
		minCameraY = camera.zoom * (camera.viewportHeight / 2);
		maxCameraX = UnitConverter.toBox2dUnits(mapSize.x) - (camera.zoom * (camera.viewportWidth / 2));
		maxCameraY = UnitConverter.toBox2dUnits(mapSize.y) - (camera.zoom * (camera.viewportHeight / 2));
	}

	@Override
	public void run() {
		float posX = actor.getBody().getPosition().x;
		float posY = actor.getBody().getPosition().y;
		camera.position.set(Math.min(maxCameraX, Math.max(posX, minCameraX)), Math.min(maxCameraY, Math.max(posY, minCameraY)), 0);

	}

	@Override
	public void setUpDependencies() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
