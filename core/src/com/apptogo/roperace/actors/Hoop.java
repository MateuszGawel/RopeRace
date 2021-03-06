package com.apptogo.roperace.actors;

import com.apptogo.roperace.game.ImmaterialGameActor;
import com.apptogo.roperace.game.ParticleEffectActor;
import com.apptogo.roperace.manager.ParticlesManager;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.screen.GameScreen;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Hoop extends Group{

	private ImmaterialGameActor hoopBottom;
	private ImmaterialGameActor hoopTop;
	private Body body;
	private boolean finished;
	private GameScreen screen;
	
	public Hoop(GameScreen screen, Vector2 position, Float rotation) {
		this.screen = screen;
		hoopBottom = new ImmaterialGameActor("hoopBottom");
		hoopBottom.setStaticImage("hoop-bottom");
		screen.getFrontStage().addActor(hoopBottom);
		
		hoopTop = new ImmaterialGameActor("hoopTop");
		hoopTop.setStaticImage("hoop-top");
		screen.getFrontStage().addActor(hoopTop);
		
		body = BodyBuilder.get()
				.type(BodyType.StaticBody)
				.addFixture("hoop1").box(2f, 0.25f, 0, 0.125f).sensor(true)
				.addFixture("hoop2").box(2f, 0.25f, 0, -0.125f).sensor(true)
				.addFixture("hoop").box(0.2f, 0.5f, -1.1f, 0).sensor(false)
				.addFixture("hoop").box(0.2f, 0.5f, 1.1f, 0).sensor(false)
				.create();
		
		body.setTransform(position, (float)Math.toRadians(360-rotation));
		hoopTop.setPosition(position.x, position.y);
		hoopBottom.setPosition(position.x, position.y);

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
		
		hoopTop.setPosition(getX() - hoopTop.getWidth()/2, getY() - UnitConverter.toBox2dUnits(hoopTop.getCurrentAnimation().getAnimationRegions().first().originalHeight/2));
		hoopBottom.setPosition(getX() - hoopBottom.getWidth()/2, getY() - hoopBottom.getHeight()/2);
		
		hoopTop.setOrigin(getOriginX(), getOriginY());
		hoopBottom.setOrigin(getOriginX(), getOriginY());
		
		hoopTop.setRotation(getRotation());
		hoopBottom.setRotation(getRotation());
		
		handleCollision();
	}
	
	boolean hoop1_collision = false;
	boolean hoop2_collision = false;
	boolean hoop1_touchedFirst = false;
	boolean hoop2_touchedFirst = false;
	
	private void handleCollision(){
		boolean hoop1_justTouched = ContactListener.SNAPSHOT_BEGIN.collide("hoop1", "player");
		boolean hoop1_justLeft = ContactListener.SNAPSHOT_END.collide("hoop1", "player");
		
		boolean hoop2_justTouched = ContactListener.SNAPSHOT_BEGIN.collide("hoop2", "player");
		boolean hoop2_justLeft = ContactListener.SNAPSHOT_END.collide("hoop2", "player");
		
		//set state of collision
		if(hoop1_justTouched){
			hoop1_collision = true;
		}
		else if(hoop1_justLeft){
			hoop1_collision = false;
		}
		
		if(hoop2_justTouched){
			hoop2_collision = true;
		}
		else if(hoop2_justLeft){
			hoop2_collision = false;
		}
		
		//check directory of ball flying through
		if(hoop1_justTouched && hoop2_collision){
			hoop2_touchedFirst = true;
		}
		else if(hoop2_justTouched && hoop1_collision){
			hoop1_touchedFirst = true;
		}
		
		//if ball fly through the hoop
		if(hoop1_touchedFirst && hoop1_justLeft && hoop2_collision){
			finish();
			hoop1_collision = false;
			hoop2_collision = false;
		}
		else if(hoop2_touchedFirst && hoop2_justLeft && hoop1_collision){
			finish();
			hoop1_collision = false;
			hoop2_collision = false;
		}
		
		//player not touching anything. Clear state
		if(!hoop1_collision && !hoop2_collision){
			hoop1_touchedFirst = false;
			hoop2_touchedFirst = false;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
	
	public void finish(){
		ParticleEffectActor hoopParticle = ParticlesManager.getInstance().getHoopParticle();
		screen.getFrontStage().addActor(hoopParticle);
		PooledEffect effect = hoopParticle.obtainAndStart(getX() + getWidth()/2, getY() + getHeight()/2, 0);
		ParticlesManager.changeColor(screen.getCurrentColorSet().getMainColor(), effect);
		
		finished = true;
	}

	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	

}
