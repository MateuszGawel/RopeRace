package com.apptogo.roperace.plugin;

import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class KeyboardSteeringPlugin extends SteeringPlugin {
	
	public KeyboardSteeringPlugin(GameScreen screen) {
		super(screen);
	}

	private final float X_STRENGTH = 1f;
	private final float Y_STRENGTH = 2f;
	
	@Override
	public void run() {

		if(Gdx.input.isKeyPressed(Keys.UP)){
			actor.getBody().applyForceToCenter(new Vector2(0, Y_STRENGTH), true);
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			actor.getBody().applyForceToCenter(new Vector2(0, -Y_STRENGTH), true);
		}
		
		if(Gdx.input.isKeyPressed(Keys.LEFT) && actor.getBody().getLinearVelocity().x > -5f){
			actor.getBody().applyForceToCenter(new Vector2(-X_STRENGTH, 0), true);
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT) && actor.getBody().getLinearVelocity().x < 5f){
			actor.getBody().applyForceToCenter(new Vector2(X_STRENGTH, 0), true);
		}

	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void setUpDependencies() {
		// TODO Auto-generated method stub
		
	}
}
