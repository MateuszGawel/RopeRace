package com.apptogo.roperace.custom;

import com.apptogo.roperace.actors.Viewfinder;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.screen.GameScreen;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class InvisibleSteeringActor extends Actor{

	private static final float SPEED_UP_FACTOR = 2.5f;

	private Vector2 initiallyTouchedPosition = new Vector2(0, 0);
	private Vector2 initialViewfinderPosition = new Vector2(0, 0);
	private Vector2 draggedPosition = new Vector2(0, 0);
	private Vector2 diffPosition = new Vector2(0, 0);
	private Vector2 finalPosition = new Vector2(0, 0);

	private GameScreen screen;

	public InvisibleSteeringActor(GameScreen screen, final Viewfinder viewfinder) {
		this.screen = screen;
		setName("invisibleSteeringActor");
		
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setPosition(-Main.SCREEN_WIDTH /2, -Main.SCREEN_HEIGHT / 2);
		screen.getSteeringHudStage().addActor(this);
		toBack();
		
		addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Vector3 unprojected = unproject(x, y);
				initiallyTouchedPosition.set(unprojected.x, unprojected.y);
				initialViewfinderPosition.set(viewfinder.getViewfinderOffset());
				System.out.println("XXXXXXXXXXXXXXXX initial: " + initialViewfinderPosition);
				return true;
			}
			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				Vector3 unprojected = unproject(x, y);
				draggedPosition.set(unprojected.x, unprojected.y);
				diffPosition.set(draggedPosition.x - initiallyTouchedPosition.x, draggedPosition.y - initiallyTouchedPosition.y);
				finalPosition.set(diffPosition.x*SPEED_UP_FACTOR + initialViewfinderPosition.x, diffPosition.y*SPEED_UP_FACTOR + initialViewfinderPosition.y);
				viewfinder.setViewfinderPosition(finalPosition);
				System.out.println("dragged:" + draggedPosition + " diff: " + diffPosition + " final: " + finalPosition);
			}
		});
	}

	private Vector3 unproject(float x, float y) {
		Vector3 worldCameraPosition = screen.getFrontStage().getCamera().position;
		Vector2 playerPosition = screen.getPlayer().getBody().getPosition();

		float xDiff = playerPosition.x - worldCameraPosition.x;
		float yDiff = playerPosition.y - worldCameraPosition.y;

		Vector3 unprojected = getStage().getCamera().unproject(new Vector3(x, -y, 0));
		unprojected.set(UnitConverter.toBox2dUnits(unprojected.x), UnitConverter.toBox2dUnits(unprojected.y), 0);
//		unprojected.add(worldCameraPosition.x, worldCameraPosition.y, 0);

		return unprojected;
	}
}
