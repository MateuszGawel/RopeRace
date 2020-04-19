package com.apptogo.roperace.plugin;

import com.apptogo.roperace.game.ParticleEffectActor;
import com.apptogo.roperace.manager.ParticlesManager;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.physics.UserData;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.math.Vector2;

import static com.apptogo.roperace.enums.BallData.BUBBLE;

public class GameEventsPlugin extends AbstractPlugin {

	private GameScreen screen;
	private SoundPlugin soundPlugin;

	public GameEventsPlugin(GameScreen screen) {
		this.screen = screen;
	}

	@Override
	public void run() {
		if(BUBBLE.name().toLowerCase().equals(actor.getName()) && ContactListener.SNAPSHOT_BEGIN.collide(UserData.get(actor.getBody()), "level")) {
			bubblePop();
			return;
		}

		if(ContactListener.SNAPSHOT_BEGIN.collide(UserData.get(actor.getBody()), "level")) {
			float playerContactImpulse = ContactListener.SNAPSHOT_BEGIN.getPlayerContactImpulse();
			if(playerContactImpulse > 2) {
				soundPlugin.playSound(actor.getName());
			}
		}


	}

	private void bubblePop(){
		ParticleEffectActor hoopParticle = ParticlesManager.getInstance().getBubbleParticle();
		screen.getFrontStage().addActor(hoopParticle);
		ParticleEffectPool.PooledEffect effect = hoopParticle.obtainAndStart(actor.getX() + actor.getWidth()/2, actor.getY() + actor.getHeight()/2, 0);
		soundPlugin.playSound("bubble");
		ParticlesManager.changeColor(screen.getCurrentColorSet().getMainColor(), effect);
		actor.setVisible(false);
		actor.getBody().setActive(false);
		screen.getHoop().setFinished(true);
		screen.getHudLabel().setGameOver(true);
	}

	@Override
	public void setUpDependencies() {
		soundPlugin = actor.getPlugin(SoundPlugin.class);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
