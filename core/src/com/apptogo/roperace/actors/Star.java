package com.apptogo.roperace.actors;

import com.apptogo.roperace.game.GameActor;
import com.apptogo.roperace.game.ParticleEffectActor;
import com.apptogo.roperace.manager.ParticlesManager;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.plugin.SoundPlugin;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Star extends GameActor {

	private GameScreen screen;

	public Star(GameScreen screen, Body body, String name) {
		super("star");
		this.screen = screen;
		setBody(body);
		setName(name);

		screen.getFrontStage().addActor(this);
		setStaticImage("star");

	}

	@Override
	public void act(float delta) {
		super.act(delta);
		setPosition(getX(), getY());
		getCurrentAnimation().setPosition(getX(), getY());

		boolean collide = ContactListener.SNAPSHOT_BEGIN.collide(getName(), "player");

		if (collide) {
			ParticleEffectActor explosionParticle = ParticlesManager.getInstance().getStarParticle();
			screen.getFrontStage().addActor(explosionParticle);
			explosionParticle.obtainAndStart(getX() + getWidth()/2, getY() + getHeight()/2, 0);
			
			remove();
			getBody().setTransform(new Vector2(0, 100), 0);
			screen.getHudLabel().onStarCollected();
			SoundPlugin.playSingleSound("star");
		}
	}

}
