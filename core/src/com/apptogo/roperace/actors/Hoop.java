package com.apptogo.roperace.actors;

import com.apptogo.roperace.custom.MyTouchpad;
import com.apptogo.roperace.game.GameActor;
import com.apptogo.roperace.game.ImmaterialGameActor;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.scene2d.Image;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Hoop extends Group{

	private ImmaterialGameActor hoopBottom;
	private ImmaterialGameActor hoopTop;
	private Body body;
	
	public Hoop(GameScreen screen, Vector2 position, Float rotation) {
		
		hoopBottom = new ImmaterialGameActor("hoopBottom");
		hoopBottom.setStaticImage("hoop-bottom");
		screen.getFrontStage().addActor(hoopBottom);
		
		hoopTop = new ImmaterialGameActor("hoopTop");
		hoopTop.setStaticImage("hoop-top");
		screen.getFrontStage().addActor(hoopTop);
		
		body = BodyBuilder.get()
				.type(BodyType.StaticBody)
				.addFixture("hoop").box(1.75f, 0.5f).sensor(true)
				.addFixture("hoop").box(0.25f, 0.5f, -1f, 0).sensor(false)
				.addFixture("hoop").box(0.25f, 0.5f, 1f, 0).sensor(false)
				.create();
		
		body.setTransform(position, (float)Math.toRadians(rotation));
		
		screen.getFrontStage().addActor(this);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		setPosition(body.getPosition().x, body.getPosition().y);
		setOrigin(hoopBottom.getWidth()/2, hoopBottom.getHeight()/2);
		setRotation((float)Math.toDegrees(body.getAngle()));
		
		hoopTop.toFront();
		hoopBottom.toBack();
		
		hoopTop.setPosition(getX() - hoopTop.getWidth()/2, getY() - hoopTop.getHeight()/2);
		hoopBottom.setPosition(getX() - hoopBottom.getWidth()/2, getY() - hoopBottom.getHeight()/2);
		
		hoopTop.setOrigin(getOriginX(), getOriginY());
		hoopBottom.setOrigin(getOriginX(), getOriginY());
		
		hoopTop.setRotation(getRotation());
		hoopBottom.setRotation(getRotation());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
	
	

}
