package com.apptogo.roperace.custom;

import com.apptogo.roperace.actors.Viewfinder;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.screen.GameScreen;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class InvisibleSteeringActor extends Actor{

	private static final float SPEED_UP_FACTOR = 1f;

	private Vector2 initiallyTouchedPosition = new Vector2(0, 0);
	private Vector2 initialViewfinderPosition = new Vector2(0, 0);
	private Vector2 draggedPosition = new Vector2(0, 0);
	private Vector2 diffPosition = new Vector2(0, 0);
	private Vector2 finalPosition = new Vector2(0, 0);

	private GameScreen screen;
	private final Viewfinder viewfinder;

	public InvisibleSteeringActor(GameScreen screen, final Viewfinder viewfinder) {
		this.screen = screen;
		this.viewfinder = viewfinder;
		setName("invisibleSteeringActor");
		
		setSize(Main.SCREEN_WIDTH/2 + 100, Main.SCREEN_HEIGHT);
		setPosition(-100, -Main.SCREEN_HEIGHT/2);
		screen.getSteeringHudStage().addActor(this);
		toBack();

		setDebug(true);
		addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				initiallyTouchedPosition.set(x, y);
				initialViewfinderPosition.set(viewfinder.getX(), viewfinder.getY());
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				setPosition(-100, -Main.SCREEN_HEIGHT/2);
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {

				Vector2 outsidePosition = calculateOutsidePosition(new Vector2(x, y));
				if(isBorderReached()){
					setPosition(getX() + outsidePosition.x, getY() + outsidePosition.y);
				}

				draggedPosition.set(x, y);
				diffPosition.set(draggedPosition.x - initiallyTouchedPosition.x, draggedPosition.y - initiallyTouchedPosition.y);
				finalPosition.set(diffPosition.x*SPEED_UP_FACTOR + initialViewfinderPosition.x, diffPosition.y*SPEED_UP_FACTOR + initialViewfinderPosition.y);
				viewfinder.setPosition(MathUtils.clamp(finalPosition.x, -Main.SCREEN_WIDTH/2, Main.SCREEN_WIDTH/2 - viewfinder.getWidth()), MathUtils.clamp(finalPosition.y, -Main.SCREEN_HEIGHT/2, Main.SCREEN_HEIGHT/2 - viewfinder.getHeight()));
			}
		});
	}
	private boolean isBorderReached(){
		if(viewfinder.getX() <= -Main.SCREEN_WIDTH/2 ||
				viewfinder.getX() >= Main.SCREEN_WIDTH/2 - viewfinder.getWidth() ||
				viewfinder.getY() <= -Main.SCREEN_HEIGHT/2 ||
				viewfinder.getY() >= Main.SCREEN_HEIGHT/2 - viewfinder.getHeight()){
			return true;
		}
		return false;
	}

	private Vector2 calculateOutsidePosition(Vector2 touchPosition){

		float left = initialViewfinderPosition.x + Main.SCREEN_WIDTH/2;
		float right = Main.SCREEN_WIDTH - initialViewfinderPosition.x - Main.SCREEN_WIDTH/2;
		float bottom = initialViewfinderPosition.y + Main.SCREEN_HEIGHT/2;
		float top = Main.SCREEN_HEIGHT - initialViewfinderPosition.y - Main.SCREEN_HEIGHT/2;

		float minX = initiallyTouchedPosition.x - left;
		float maxX = initiallyTouchedPosition.x + right;
		float minY = initiallyTouchedPosition.y - bottom;
		float maxY = initiallyTouchedPosition.y + top;

		Vector2 clampedPosition = new Vector2(touchPosition);
		Vector2 outsidePosition = new Vector2(0 ,0);

		if(touchPosition.x < minX) clampedPosition.x = minX/SPEED_UP_FACTOR;
		if(touchPosition.x > maxX) clampedPosition.x = maxX/SPEED_UP_FACTOR;
		if(touchPosition.y < minY) clampedPosition.y = minY/SPEED_UP_FACTOR;
		if(touchPosition.y > maxY) clampedPosition.y = maxY/SPEED_UP_FACTOR;

		outsidePosition.x = touchPosition.x != clampedPosition.x ? touchPosition.x - clampedPosition.x : 0;
		outsidePosition.y = touchPosition.y != clampedPosition.y ? touchPosition.y - clampedPosition.y : 0;

		return outsidePosition;
	}

}
