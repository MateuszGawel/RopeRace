package com.apptogo.roperace.plugin;

import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraFollowingPlugin extends AbstractPlugin {

	private OrthographicCamera camera;

	private Vector2 mapSize;
	private float posX, posY;
	float bottomGutter, topGutter, rightGutter, leftGutter;
	
	public CameraFollowingPlugin(Vector2 mapSize) {
		super();
		this.mapSize = mapSize;
	}

	@Override
	public void postSetActor() {

		float screenWidth = UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH);
		float screenHeight = UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT);

		camera = (OrthographicCamera) actor.getStage().getCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);
		camera.position.set(actor.getBody().getPosition().x, actor.getBody().getPosition().y, 0);

		
		bottomGutter = UnitConverter.toBox2dUnits(actor.getStage().getViewport().getBottomGutterHeight());
		topGutter = UnitConverter.toBox2dUnits(actor.getStage().getViewport().getTopGutterHeight());
		leftGutter = UnitConverter.toBox2dUnits(actor.getStage().getViewport().getLeftGutterWidth());
		rightGutter = UnitConverter.toBox2dUnits(actor.getStage().getViewport().getRightGutterWidth());

	}

	@Override
	public void run() {
		posX = actor.getBody().getPosition().x;
		posY = actor.getBody().getPosition().y;
		
		float speed = actor.getBody().getLinearVelocity().len();
		camera.zoom = MathUtils.lerp(camera.zoom, 1 + speed/20, 0.1f);
	}

	public void updateCamera() {
		camera.position.set(posX, posY, 0);
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
