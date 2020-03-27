package com.apptogo.roperace.actors;

import com.apptogo.roperace.game.GameActor;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Viewfinder extends GameActor{

	private static final float VIEWFINDER_INITIAL_RADIUS = 2;

	private GameScreen screen;
	private GameActor player;
	private float angle;
	private Vector2 viewfinderOffset = new Vector2(VIEWFINDER_INITIAL_RADIUS, 0);
	private float length;
	private float x, y;

	public Viewfinder(GameScreen screen, GameActor actor) {
		super("viewfinder");
		this.screen = screen;
		this.player = actor;

		setBody(BodyBuilder.get()
				.type(BodyType.KinematicBody)
				.addFixture("viewfinder").circle(0.1f).sensor(true)
				.create());
		setStaticImage("viewfinder");
		setFixedRotation(true);
		screen.getFrontStage().addActor(this);

		viewfinderOffset.set(player.getBody().getPosition().x + viewfinderOffset.x, 0);
	}

	public Vector2 getViewfinderOffset() {
		return viewfinderOffset;
	}

	@Override
	public void act(float delta) {
		Vector2 positionToSet = new Vector2(screen.getFrontStage().getCamera().position.x + viewfinderOffset.x, screen.getFrontStage().getCamera().position.y + viewfinderOffset.y);
		getBody().setTransform(positionToSet, 0);
		super.act(delta);
	}


	public float getAngle() {
		return getBody().getPosition().cpy().sub(player.getBody().getPosition()).angle();
	}

	public void setViewfinderPosition(Vector2 calculatedPosition) {
		viewfinderOffset.set(calculatedPosition);
	}
}
