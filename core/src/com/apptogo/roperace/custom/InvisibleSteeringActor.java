package com.apptogo.roperace.custom;

import com.apptogo.roperace.actors.Viewfinder;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.screen.GameScreen;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class InvisibleSteeringActor extends Actor{
	
	private Vector2 center = new Vector2(0, 0);
	private Vector2 dragged = new Vector2(0, 0);
	
	public InvisibleSteeringActor(GameScreen screen, final Viewfinder viewfinder) {
		setName("invisibleSteeringActor");
		
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setPosition(-Main.SCREEN_WIDTH /2, -Main.SCREEN_HEIGHT / 2);
		screen.getSteeringHudStage().addActor(this);
		toBack();
		
		addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				center.set(x,y);
				center.sub(UnitConverter.toGraphicsUnits(viewfinder.getViewfinderOffset().cpy().scl(0.3f)));
				return true;
			}
			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				dragged.set(x,y);
				viewfinder.setAngle(dragged.sub(center).angle());
			}
		});
	}
}
