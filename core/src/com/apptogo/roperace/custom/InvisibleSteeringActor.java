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
	private final Viewfinder viewfinder;


	public InvisibleSteeringActor(GameScreen screen, final Viewfinder viewfinder) {
		this.screen = screen;
		this.viewfinder = viewfinder;
		setName("invisibleSteeringActor");
		
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setPosition(-Main.SCREEN_WIDTH /2, -Main.SCREEN_HEIGHT / 2);
		screen.getSteeringHudStage().addActor(this);
		toBack();

		addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				initiallyTouchedPosition.set(x, y);
				initialViewfinderPosition.set(viewfinder.getX(), viewfinder.getY());
				return true;
			}
			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				draggedPosition.set(x, y);
				diffPosition.set(draggedPosition.x - initiallyTouchedPosition.x, draggedPosition.y - initiallyTouchedPosition.y);
				finalPosition.set(diffPosition.x*SPEED_UP_FACTOR + initialViewfinderPosition.x, diffPosition.y*SPEED_UP_FACTOR + initialViewfinderPosition.y);
				viewfinder.setPosition(clampX(finalPosition.x), clampY(finalPosition.y));
			}
		});
	}

	private boolean isBorderReached(float x, float y){
		if(x <= -Main.SCREEN_WIDTH/2 ||
		x >= Main.SCREEN_WIDTH/2 - viewfinder.getWidth() ||
		y <= -Main.SCREEN_HEIGHT/2 ||
		y >= Main.SCREEN_HEIGHT/2 - viewfinder.getHeight()){
			return true;
		}
		return false;
	}

	private float clampX(float x){
		if(x <= -Main.SCREEN_WIDTH/2) return -Main.SCREEN_WIDTH/2;
		if(x >= Main.SCREEN_WIDTH/2 - viewfinder.getWidth()) return Main.SCREEN_WIDTH/2 - viewfinder.getWidth();
		return x;
	}

	private float clampY(float y){
		if(y <= -Main.SCREEN_HEIGHT/2) return -Main.SCREEN_HEIGHT/2;
		if(y >= Main.SCREEN_HEIGHT/2 - viewfinder.getHeight()) return Main.SCREEN_HEIGHT/2- viewfinder.getHeight();
		return y;
	}
}
