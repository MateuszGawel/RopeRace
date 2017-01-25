package com.apptogo.roperace.actors;

import com.apptogo.roperace.game.GameActor;
import com.apptogo.roperace.manager.CustomAction;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.manager.ResourcesManager;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.physics.UserData;
import com.apptogo.roperace.screen.GameScreen;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

public class Rope extends GameActor{

	private static final float ROPE_SHORTENING_SPEED = 0.04f;
	private static final float ROPE_SHOOT_SPEED = 40f;
	private static final float ROPE_LENGTH = 0.3f;
	
	private GameScreen screen;
	private GameActor player;
	
	private Body ropeBullet;
	private RopeJoint joint;
	private boolean ropeAttached;
	private CustomAction shorteningAction;
	private float ropeLength, startU2Length;
	private TextureRegion ropeTextureRegion;
	private Vector2 shootVector = new Vector2(ROPE_SHOOT_SPEED, 0);
	private float penetration = 0.02f;
	
	public Rope(GameScreen screen, GameActor actor) {
		super("rope");
		this.screen = screen;
		this.player = actor;
		
		ropeTextureRegion = new TextureRegion(ResourcesManager.getInstance().getTexture("chain"));
		startU2Length = ropeTextureRegion.getU2() - ropeTextureRegion.getU();
		
		//rope bullet (rope end)
		ropeBullet = BodyBuilder.get()
				.type(BodyType.DynamicBody)
				.position(-100, -100)
				.addFixture("ropeBullet").circle(0.01f).density(1000000000f).friction(1).restitution(0)
				.create();
		
		screen.getFrontStage().addActor(this);
		toBack();
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------ PUBLIC -------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	public void shoot(float angle) {
		destroyCurrentJoint();
		
		shootVector.setAngle(angle);
		
		ropeBullet.setTransform(player.getBody().getPosition(), 0);	
		ropeBullet.setLinearVelocity(shootVector);
		
		createJoint();
		triggerAutoRopeCut();
	}

	public void destroyCurrentJoint(){
		if(joint != null){
			screen.getWorld().destroyJoint(joint);
			joint = null;
			ropeAttached = false;
			ropeBullet.setTransform(new Vector2(-100, -100), 0);
			if(shorteningAction != null){
				shorteningAction.unregister();
			}
		}
	}
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------ HELPERS ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	private void handleBulletCollision(){
		if(joint != null && ContactListener.SNAPSHOT_BEGIN.collide(UserData.get(ropeBullet), "level")){
       			ropeAttached = true;
			
			joint.setMaxLength(player.getBody().getPosition().dst(ropeBullet.getPosition()));
			triggerAutoRopeShortening(joint);
		}
	}
	
	private void createJoint() {
		
		RopeJointDef jointDef = new RopeJointDef();
		jointDef.bodyA = player.getBody();
		jointDef.bodyB = ropeBullet;
		jointDef.localAnchorA.set(0,0);
		jointDef.localAnchorB.set(0,0);
		joint = (RopeJoint)screen.getWorld().createJoint(jointDef);

		joint.setMaxLength(10000);
	}
	
	private void triggerAutoRopeCut() {
		CustomActionManager.getInstance().registerAction(new CustomAction(ROPE_LENGTH, 1) {
			
			@Override
			public void perform() {
				if(joint != null && !ropeAttached){
					destroyCurrentJoint();
				}
			}
		});
	}
	
	private void triggerAutoRopeShortening(final RopeJoint joint){
		shorteningAction = new CustomAction(0.01f, -1) {
			boolean first = true;
			
			@Override
			public void perform() {
				if(joint.getMaxLength() > 1){				
					float currentLength = joint.getAnchorA().dst(joint.getAnchorB());
					
					//if ball stucks, rope shouldn't short to avoid jitter jump.
					if(first || currentLength <= joint.getMaxLength() + ROPE_SHORTENING_SPEED/2){
						joint.setMaxLength(joint.getMaxLength() - ROPE_SHORTENING_SPEED);
						first = false;
					}
				}
				else{
					unregister();
				}
			}
		};
		
		CustomActionManager.getInstance().registerAction(shorteningAction);
	}
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ----------------------------------------------- ACT/DRAW ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	public void act(float delta) {
		super.act(delta);
		handleBulletCollision();
		
		setPosition(player.getBody().getPosition().x, player.getBody().getPosition().y);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(joint != null){
			ropeLength = new Vector2(getX(), getY()).dst(ropeBullet.getPosition()) + penetration;
			float u2Backup = ropeTextureRegion.getU2();
			ropeTextureRegion.setU2(ropeTextureRegion.getU() + startU2Length * ((UnitConverter.PPM * ropeLength) / ropeTextureRegion.getRegionWidth()));
			
			batch.draw(ropeTextureRegion, 
					getX(), 
					getY() - UnitConverter.toBox2dUnits(ropeTextureRegion.getRegionHeight())/2, 
					getOriginX(), UnitConverter.toBox2dUnits(ropeTextureRegion.getRegionHeight())/2, 
					ropeLength, UnitConverter.toBox2dUnits(ropeTextureRegion.getRegionHeight()), 
					getScaleX(), getScaleY(),
					ropeBullet.getPosition().cpy().sub(new Vector2(getX(), getY())).angle());
			
			ropeTextureRegion.setU2(u2Backup);
		}
	}
}
