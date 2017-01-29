package com.apptogo.roperace.actors;

import com.apptogo.roperace.game.GameActor;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Diamond extends GameActor {

	private GameScreen screen;

	public Diamond(GameScreen screen, Body body, String name) {
		super("diamond");
		debug();
		this.screen = screen;
		setBody(body);
		setName(name);

		screen.getFrontStage().addActor(this);
		setStaticImage("diamond");

	}

	@Override
	public void act(float delta) {
		super.act(delta);
		setPosition(getX(), getY() - getHeight());
		getCurrentAnimation().setPosition(getX(), getY());

		boolean collide = ContactListener.SNAPSHOT_BEGIN.collide(getName(), "player");

		if (collide) {
			remove();
			getBody().setTransform(new Vector2(0, 100), 0);
			screen.getHudLabel().onDiamondCollected();
		}
	}

}
