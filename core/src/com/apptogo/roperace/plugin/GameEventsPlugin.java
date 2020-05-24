package com.apptogo.roperace.plugin;

import com.apptogo.roperace.actors.Rope;
import com.apptogo.roperace.enums.BallData;
import com.apptogo.roperace.exception.PluginException;
import com.apptogo.roperace.game.ParticleEffectActor;
import com.apptogo.roperace.manager.ParticlesManager;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.physics.UserData;
import com.apptogo.roperace.save.SaveManager;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;

import static com.apptogo.roperace.enums.BallData.BUBBLE;

public class GameEventsPlugin extends AbstractPlugin {

	private BallData ball;
	private Rope rope;
	private GameScreen screen;
	private SoundPlugin soundPlugin;
	private boolean playerCollidedWithLevel;
	private float playerContactImpulse;

	public GameEventsPlugin(GameScreen screen) {
		this.screen = screen;
	}

	@Override
	public void run() {
		if(bubbleHit()) {
			bubblePop();
			return;
		}

		float currentPlayerContactImpulse = handleCollision();

		if(playerHitLevel(currentPlayerContactImpulse)){

			//play only for harder hits
			if(currentPlayerContactImpulse > 2.0) {
				soundPlugin.playSound(actor.getName());
			}

			//workaround for easy hits when rope attached to remove glittering
			if(rope.isRopeAttached() && currentPlayerContactImpulse < 2.0) {
//				System.out.println(" friction 0 bo " + currentPlayerContactImpulse);
				setBallNotBouncing();
			}
			else{
//				System.out.println(" friction " + ball.friction + " bo " + currentPlayerContactImpulse);
				setBallBouncing();
			}
		}

		resetBouncingstate();
		playerContactImpulse = currentPlayerContactImpulse;
	}

	private boolean bubbleHit() {
		return BUBBLE.name().toLowerCase().equals(actor.getName()) && ContactListener.SNAPSHOT_BEGIN.collide(UserData.get(actor.getBody()), "level");
	}

	private void resetBouncingstate() {
		if(!rope.isRopeAttached()){
			setBallBouncing();
		}
	}

	private float handleCollision() {
		float currentPlayerContactImpulse = 0;
		if(ContactListener.SNAPSHOT_BEGIN.collide(UserData.get(actor.getBody()), "level")) {
			currentPlayerContactImpulse = ContactListener.SNAPSHOT_BEGIN.getPlayerContactImpulse();
			playerCollidedWithLevel = true;
		}
		if(ContactListener.SNAPSHOT_END.collide(UserData.get(actor.getBody()), "level")){
			playerCollidedWithLevel = false;
		}
		return currentPlayerContactImpulse;
	}

	private void setBallBouncing() {
		actor.getBody().getFixtureList().get(0).setFriction(ball.friction);
		actor.getBody().getFixtureList().get(0).setRestitution(ball.restitution);
	}

	private void setBallNotBouncing() {
		actor.getBody().getFixtureList().get(0).setFriction(0);
		actor.getBody().getFixtureList().get(0).setRestitution(0);
	}

	private boolean playerHitLevel(float currentPlayerContactImpulse) {
		return currentPlayerContactImpulse != 0 && currentPlayerContactImpulse != playerContactImpulse;
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

		try{
			rope = actor.getPlugin(TouchSteeringPlugin.class).getRope();
		}
		catch(PluginException e){
			rope = actor.getPlugin(KeyboardSteeringPlugin.class).getRope();
		}
		int activeBallNumber = SaveManager.getInstance().getActiveBall();
		ball = BallData.valueOf(activeBallNumber);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public boolean isPlayerCollidedWithLevel() {
		return playerCollidedWithLevel;
	}
}
