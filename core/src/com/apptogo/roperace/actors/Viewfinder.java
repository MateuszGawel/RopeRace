package com.apptogo.roperace.actors;

import com.apptogo.roperace.game.GameActor;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Viewfinder extends GameActor{

	private static final float VIEWFINDER_RADIUS = 2;
	
	private GameActor player;
	private float angle;
	private Vector2 viewfinderOffset = new Vector2(VIEWFINDER_RADIUS, 0);
	
	public Viewfinder(GameScreen screen, GameActor actor) {
		super("viewfinder");
		this.player = actor;
		
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
		viewfinderOffset.setAngle(angle);
		setRotation(angle);
		getBody().setTransform(player.getBody().getPosition().add(viewfinderOffset), 0);
		
		super.act(delta);
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getAngle() {
		return angle;
	}

}
