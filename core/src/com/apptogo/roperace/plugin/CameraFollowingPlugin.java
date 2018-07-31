package com.apptogo.roperace.plugin;

import com.apptogo.roperace.actors.Rope;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.screen.GameScreen;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

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
	}

//	public void updateCamera() {
//		camera.position.set(posX, posY, 0);
//	}
	public void updateCamera() {
		float screenWidth = UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH);
		float screenHeight = UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT);

		camera.position.set(posX, posY, 0);
		float minX = screenWidth/2 + leftGutter;
		float maxX = UnitConverter.toBox2dUnits(mapSize.x) - screenWidth/2 + rightGutter;
		float minY = screenHeight/2 + bottomGutter;
		float maxY = UnitConverter.toBox2dUnits(mapSize.y) - screenHeight/2 - topGutter;
		camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
		camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);

		float speed = actor.getBody().getLinearVelocity().len();
		if(camera.position.x <= minX + 1 || camera.position.x >= maxX - 1 || camera.position.y <= minY + 1 || camera.position.y >= maxY - 1){
			camera.zoom = MathUtils.lerp(camera.zoom, 1, 0.03f);
		}
		else{
			camera.zoom = MathUtils.lerp(camera.zoom, 1 + speed/20, 0.03f);
		}

		updateLabelCamera();
	}

	private void updateLabelCamera() {
		OrthographicCamera labelCamera = (OrthographicCamera)((GameScreen) Main.getInstance().getCurrentScreen()).getLabelStage().getCamera();
		labelCamera.setToOrtho(false, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		labelCamera.position.x = UnitConverter.toGraphicsUnits(camera.position.x);
		labelCamera.position.y = -Main.SCREEN_HEIGHT/2 + UnitConverter.toGraphicsUnits(camera.position.y);
		labelCamera.zoom = camera.zoom;
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
