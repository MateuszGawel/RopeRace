package com.apptogo.roperace.actors;

import com.apptogo.roperace.custom.MyTouchpad;
import com.apptogo.roperace.game.GameActor;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Viewfinder extends GameActor{

	private static final float VIEWFINDER_RADIUS = 1;
	
	private GameActor player;
	private MyTouchpad touchpad;
	
	private Vector2 viewfinderOffset = new Vector2(VIEWFINDER_RADIUS, 0);
	
	public Viewfinder(GameScreen screen, GameActor actor, MyTouchpad touchpad) {
		super("viewfinder");
		this.player = actor;
		this.touchpad = touchpad;
		
		setBody(BodyBuilder.get()
				.type(BodyType.KinematicBody)
				.position(actor.getBody().getPosition().x, actor.getBody().getPosition().y)
				.addFixture("viewfinder").circle(0.1f).sensor(true)
				.create());
		setStaticImage("viewfinder");
		setFixedRotation(true);
		screen.getFrontStage().addActor(this);
	}

	public Vector2 getViewfinderOffset() {
		return viewfinderOffset;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (touchpad.getKnobX() != touchpad.getWidth() / 2 && touchpad.getKnobY() != touchpad.getHeight() / 2) {
			viewfinderOffset.setAngle(touchpad.getAngle());
		}

		getBody().setTransform(player.getBody().getPosition().add(viewfinderOffset), 0);
		setRotation(touchpad.getAngle());
	}

}
