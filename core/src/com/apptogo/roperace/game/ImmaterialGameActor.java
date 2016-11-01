package com.apptogo.roperace.game;

import com.badlogic.gdx.graphics.g2d.Batch;

public class ImmaterialGameActor extends AbstractActor {
	
	public ImmaterialGameActor(String name) {
		super(name);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);

		setCurrentAnimation();

		//we add customOffset to adjust animation position (and actor) with body to make game enjoyable
		//we add animation deltaOffset and few lines below we subtracting it. Thanks that actor and graphic is always in the same position.
		//more information about deltaOffset in AnimationActor

		if(getWidth() == 0 && getHeight() == 0)
			setSize(currentAnimation.getWidth(), currentAnimation.getHeight());

		currentAnimation.position(getX() - currentAnimation.getDeltaOffset().x, getY() - currentAnimation.getDeltaOffset().y);
		currentAnimation.act(delta);

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		currentAnimation.draw(batch, parentAlpha);
	}
}
