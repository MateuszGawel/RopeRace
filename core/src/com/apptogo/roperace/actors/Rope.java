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
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Rope extends GameActor{

	private float changeLenghtSpeed = 0.04f;
	private static final float ROPE_SHOOT_SPEED = 40f;
	private static final float ROPE_LENGTH = 0.38f;
	private static final float ROPE_MAX_LENGTH = 25000;
	
	private GameScreen screen;
	private GameActor player;
	
	private Body ropeBullet;
	private final Body rotor;
	private PrismaticJoint ropePrismaticJoint;
	private RevoluteJoint bulletRevoluteJoint;
	private boolean ropeAttached;
	private CustomAction shorteningAction;
	private CustomAction extendingAction;
	private float ropeLength, startU2Length;
	private TextureRegion ropeTextureRegion;
	private Vector2 shootVector = new Vector2(ROPE_SHOOT_SPEED, 0);
	private float penetration = 0.02f;
	private int shootCounter;
	
	public Rope(GameScreen screen, GameActor actor) {
		super("rope");
		this.screen = screen;
		this.player = actor;
		this.shootCounter = 0;
		
		ropeTextureRegion = new TextureRegion(ResourcesManager.getInstance().getAtlasRegion("chain"));
		startU2Length = ropeTextureRegion.getU2() - ropeTextureRegion.getU();
		createRopeLenghtActions();

		//rope bullet (rope end)
		ropeBullet = BodyBuilder.get()
				.type(BodyType.DynamicBody)
				.position(-100, -100)
				.addFixture("ropeBullet").circle(0.11f).density(1)
				.create();
		rotor = BodyBuilder.get()
				.type(BodyType.DynamicBody)
				.position(player.getBody().getPosition())
				.addFixture("rotor").circle(0.11f).density(1).sensor(true)
				.create();
		RevoluteJointDef rotorJointDef = new RevoluteJointDef();
		rotorJointDef.bodyA = player.getBody();
		rotorJointDef.bodyB = rotor;
		rotorJointDef.localAnchorB.set(rotor.getLocalCenter());
		rotorJointDef.localAnchorA.set(player.getBody().getLocalCenter());
		rotorJointDef.collideConnected = false;
		screen.getWorld().createJoint(rotorJointDef);

		screen.getFrontStage().addActor(this);
		toBack();
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------ PUBLIC -------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	public void shoot(float angle) {
		destroyCurrentJoint();
		shootVector.setAngle(angle);
		
		ropeBullet.setTransform(player.getBody().getPosition(), (float) Math.toRadians(angle));
		ropeBullet.setLinearVelocity(shootVector);

//		createJoint();
		triggerAutoRopeCut();
		
		shootCounter++;
	}
	public void destroyCurrentJoint(){
		if(ropePrismaticJoint != null) {
			screen.getWorld().destroyJoint(ropePrismaticJoint);
			ropePrismaticJoint = null;
		}
		if(bulletRevoluteJoint != null) {
			screen.getWorld().destroyJoint(bulletRevoluteJoint);
			bulletRevoluteJoint = null;
		}
		ropeAttached = false;
		ropeBullet.setTransform(new Vector2(-100, -100), 0);
		if(shorteningAction != null){
			shorteningAction.unregister();
		}
		screen.getWorld().setGravity(new Vector2(0,-15));
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------ HELPERS ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	private void handleBulletCollision(){
		if( ContactListener.SNAPSHOT_BEGIN.collide(UserData.get(ropeBullet), "level")){
			ropeAttached = true;
			Body ropeBulletCollidedBody = ContactListener.SNAPSHOT_BEGIN.getRopeBulletCollidedBody();
			RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
			revoluteJointDef.bodyA = ropeBulletCollidedBody;
			revoluteJointDef.bodyB = ropeBullet;
			revoluteJointDef.localAnchorB.set(ropeBullet.getLocalCenter());
			revoluteJointDef.localAnchorA.set(ContactListener.SNAPSHOT_BEGIN.getRopeBulletCollisionPoint().sub(ropeBulletCollidedBody.getPosition()));
			revoluteJointDef.collideConnected = false;
			bulletRevoluteJoint = (RevoluteJoint) screen.getWorld().createJoint(revoluteJointDef);
//			shortenRope(changeLenghtSpeed);

			rotor.setTransform(rotor.getPosition().x, rotor.getPosition().y, 0);
			PrismaticJointDef jointDef = new PrismaticJointDef();
			jointDef.collideConnected = false;
			jointDef.bodyB = ropeBullet;
			jointDef.bodyA = rotor;
			Vector2 normal = ropeBullet.getPosition().cpy().sub(rotor.getPosition()).nor();
			jointDef.localAxisA.set(normal);
			jointDef.motorSpeed = -40f;
			jointDef.maxMotorForce = 30;
            jointDef.enableMotor = true;
			jointDef.upperTranslation = 0;
			jointDef.lowerTranslation = 1;
			jointDef.enableLimit = true;
			ropePrismaticJoint = (PrismaticJoint)screen.getWorld().createJoint(jointDef);
		}
	}
	
	private void createJoint() {

//		RopeJointDef jointDef = new RopeJointDef();
//		jointDef.bodyA = player.getBody();
//		jointDef.bodyB = ropeBullet;
//		jointDef.localAnchorA.set(0,0);
//		jointDef.localAnchorB.set(0,0);
//		joint = (RopeJoint)screen.getWorld().createJoint(jointDef);
//
//		joint.setMaxLength(ROPE_MAX_LENGTH);
	}
	
	private void triggerAutoRopeCut() {
		CustomActionManager.getInstance().registerAction(new CustomAction(ROPE_LENGTH, 1) {
			
			@Override
			public void perform() {
				if(!ropeAttached){
					destroyCurrentJoint();
				}
			}
		});
	}
	
	public void createRopeLenghtActions(){
		shorteningAction = new CustomAction(0.01f, -1) {
			boolean first = true;
			
			@Override
			public void perform() {
//				System.out.println("SHORTEN1");
//				if (ropeAttached && joint.getMaxLength() > 1) {
//					float currentLength = joint.getAnchorA().dst(joint.getAnchorB());
//					float playerBulletDst = player.getBody().getPosition().dst(ropeBullet.getPosition());
////					System.out.println("SHORTEN2");
//					//if ball stucks, rope shouldn't short to avoid jitter jump.
//					if (first || currentLength <= joint.getMaxLength() + changeLenghtSpeed / 2) {
////						System.out.println("SHORTEN3");
//						if(currentLength < joint.getMaxLength()){
//							joint.setMaxLength(currentLength - changeLenghtSpeed);
////							System.out.println("SHORTEN4");
//						}
//						else {
//							joint.setMaxLength(joint.getMaxLength() - changeLenghtSpeed);
////							System.out.println("SHORTEN5");
//						}
//						first = false;
//					}
//				}
//				else{
//					unregister();
//				}
			}
		};

		extendingAction = new CustomAction(0.01f, -1) {
			@Override
			public void perform() {
				if(ropeAttached) {
					float currentLength = ropePrismaticJoint.getAnchorA().dst(ropePrismaticJoint.getAnchorB());

//					if (currentLength <= ROPE_MAX_LENGTH) {
//						joint.setMaxLength(joint.getMaxLength() + changeLenghtSpeed);
//					}
				}
				else {
					unregister();
				}
			}
		};
		
	}

	public void extendRope(float speed){
	    changeLenghtSpeed = speed;
	    if(shorteningAction.isRegistered()) {
            shorteningAction.unregister();
            shorteningAction.resetAction();
        }
        if(!extendingAction.isRegistered()) {
            CustomActionManager.getInstance().registerAction(extendingAction);
        }
	}

	public void shortenRope(float speed){
        changeLenghtSpeed = speed;
	    if(extendingAction.isRegistered()) {
            extendingAction.unregister();
            extendingAction.resetAction();
        }
        if(!shorteningAction.isRegistered()) {
            CustomActionManager.getInstance().registerAction(shorteningAction);
        }
	}

	public void stopRope(){
	    if(extendingAction.isRegistered()) {
            extendingAction.unregister();
            extendingAction.resetAction();
        }
        if(shorteningAction.isRegistered()) {
            shorteningAction.unregister();
            shorteningAction.resetAction();
        }
	}

	/** don't allow too fast joint. It can happen when gravity helps, so if gravity forks in favor, slow down motor by redcuing max force */
	private void tunePrismaticJoint() {
		if(ropePrismaticJoint != null){
			if(ropePrismaticJoint.getJointSpeed() < -3 && ropePrismaticJoint.getMaxMotorForce() > 0){
				ropePrismaticJoint.setMaxMotorForce(ropePrismaticJoint.getMaxMotorForce()-10);
			}
			else if(ropePrismaticJoint.getMaxMotorForce() < 30){
				ropePrismaticJoint.setMaxMotorForce(ropePrismaticJoint.getMaxMotorForce()+10);
			}
		}
	}
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ----------------------------------------------- ACT/DRAW ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	public void act(float delta) {
		super.act(delta);
		handleBulletCollision();
		
		setPosition(player.getBody().getPosition().x, player.getBody().getPosition().y);
		tunePrismaticJoint();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(ropePrismaticJoint != null){
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

	public int getShootCounter() {
		return shootCounter;
	}

	public boolean isRopeAttached() {
		return ropeAttached;
	}

	public Body getRopeBullet() {
		return ropeBullet;
	}
}
