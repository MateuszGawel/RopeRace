package com.apptogo.roperace.custom;

import com.apptogo.roperace.actors.Viewfinder;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.screen.GameScreen;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class InvisibleSteeringActor extends Actor{

	private static final float SPEED_UP_FACTOR = 2f;

	private Vector2 initiallyTouchedPosition = new Vector2(0, 0);
	private Vector2 initialViewfinderPosition = new Vector2(0, 0);
	private Vector2 diffPosition = new Vector2(0, 0);
	private Vector2 finalPosition = new Vector2(0, 0);

	private final Viewfinder viewfinder;

	public InvisibleSteeringActor(GameScreen screen, final Viewfinder viewfinder) {
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
				adjustTouchAreaPosition(x, y);

				diffPosition.set(x - initiallyTouchedPosition.x, y - initiallyTouchedPosition.y);
				finalPosition.set(diffPosition.x*SPEED_UP_FACTOR + initialViewfinderPosition.x, diffPosition.y*SPEED_UP_FACTOR + initialViewfinderPosition.y);
				viewfinder.setPosition(MathUtils.clamp(finalPosition.x, -Main.SCREEN_WIDTH/2, Main.SCREEN_WIDTH/2 - viewfinder.getWidth()), MathUtils.clamp(finalPosition.y, -Main.SCREEN_HEIGHT/2, Main.SCREEN_HEIGHT/2 - viewfinder.getHeight()));
			}
		});
	}

	/**
	 * Moves touch rectangle based on calculated offset. It prevents feeling of viewfinder sticking to the edges.
	 */
	private void adjustTouchAreaPosition(float x, float y) {
		Vector2 outsidePosition = calculateOutsidePosition(new Vector2(x, y));
		if(isBorderReached()){
			setPosition(getX() + outsidePosition.x, getY() + outsidePosition.y);
		}
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

	/**
	 * Calculates how many pixels touch position would be outside of the boundaries since last frame.
	 * Boundaries are calculated based on viewfinder position and movement speed.
	 */
	private Vector2 calculateOutsidePosition(Vector2 touchPosition){
		//how much pixels viewfinder needs to reach screen boundaries. 0,0 is middle of the screen
		float distanceToleft = initialViewfinderPosition.x + Main.SCREEN_WIDTH/2;
		float distanceToRight = Main.SCREEN_WIDTH - initialViewfinderPosition.x - Main.SCREEN_WIDTH/2;
		float distanceToBottom = initialViewfinderPosition.y + Main.SCREEN_HEIGHT/2;
		float distanceToTop = Main.SCREEN_HEIGHT - initialViewfinderPosition.y - Main.SCREEN_HEIGHT/2;

		//if viewfinder is faster, distances are reduced.
		//We need that, because we will map viewfinder position to touch position. Touch has always same speed.
		distanceToleft /= SPEED_UP_FACTOR;
		distanceToRight /= SPEED_UP_FACTOR;
		distanceToBottom /= SPEED_UP_FACTOR;
		distanceToTop /= SPEED_UP_FACTOR;

		//boundaries for touch area
		float minX = initiallyTouchedPosition.x - distanceToleft;
		float maxX = initiallyTouchedPosition.x + distanceToRight;
		float minY = initiallyTouchedPosition.y - distanceToBottom;
		float maxY = initiallyTouchedPosition.y + distanceToTop;

		Vector2 outsidePosition = new Vector2(0 ,0);

		//if finger/mouse is outside of calculated boundaries calculate how much
		if(touchPosition.x < minX) outsidePosition.x = touchPosition.x - minX;
		if(touchPosition.x > maxX) outsidePosition.x = touchPosition.x - maxX;
		if(touchPosition.y < minY) outsidePosition.y = touchPosition.y - minY;
		if(touchPosition.y > maxY) outsidePosition.y = touchPosition.y - maxY;

		return outsidePosition;
	}

}
