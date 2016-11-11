package com.apptogo.roperace.plugin;

import com.apptogo.roperace.custom.MyTouchpad;
import com.apptogo.roperace.custom.MyTouchpad.TouchpadStyle;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.CustomAction;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.physics.UserData;
import com.apptogo.roperace.scene2d.Image;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public abstract class SteeringPlugin extends AbstractPlugin {

	private static final float ROPE_SHORTENING_SPEED = 0.03f;
	private static final float ROPE_SHOOT_SPEED = 40f;
	private static final float ROPE_LENGTH = 0.25f;
	private static final float VIEWFINDER_RADIUS = 1;
	
	private GameScreen screen;
	private Body ropeBullet;
	private RopeJoint joint;
	private boolean ropeAttached;
	private CustomAction shorteningAction;
	
	private MyTouchpad touchpad;
	private Body viewfinder;
	protected Vector2 viewfinderOffset = new Vector2(VIEWFINDER_RADIUS, 0);
	
	public SteeringPlugin(GameScreen screen) {
		this.screen = screen;
		
		ropeBullet = BodyBuilder.get()
				.type(BodyType.DynamicBody)
				.position(-100, -100)
				.addFixture("rope", "ropeBullet").circle(0.05f).density(1000000000f).friction(1).restitution(0)
				.create();

		createTouchpad();
	}

	public void prepare(){
		
	}
	
	@Override
	public void postSetActor() {
		viewfinder = BodyBuilder.get()
				.type(BodyType.KinematicBody)
				.position(actor.getBody().getPosition().x, actor.getBody().getPosition().y)
				.addFixture("viewfinder").circle(0.1f)
				.sensor(true)
				.create();
	}

	@Override
	public void run() {
	}

	/**
	 * Sets proper position for viewfinder. Must be called just after world.step() to avoid offset.
	 */
	public void handleViewfinder() {
		if(touchpad.getKnobX() != touchpad.getWidth()/2 && touchpad.getKnobY() != touchpad.getHeight()/2){
			viewfinderOffset.setAngle(getTouchpadAngle());
		}
		
		viewfinder.setTransform(actor.getBody().getPosition().add(viewfinderOffset), 0);
	}

	protected float getTouchpadAngle() {
		return new Vector2(touchpad.getKnobX() - touchpad.getWidth()/2, touchpad.getKnobY() - touchpad.getHeight()/2).angle();
	}

	public void handleBulletCollision(){
		if(joint != null && ContactListener.SNAPSHOT.collide(UserData.get(ropeBullet), "level")){
			ropeAttached = true;
			ropeBullet.setLinearVelocity(new Vector2(0, 0));
			joint.setMaxLength(actor.getBody().getPosition().dst(ropeBullet.getPosition()));
			triggerAutoRopeShortening(joint);
		}
	}

	private void createTouchpad() {
		Drawable knob = Image.getFromTexture("touchpad-knob").getDrawable();
		Drawable background = Image.getFromTexture("touchpad-background").getDrawable();
		float margin = 20;
		
		touchpad = new MyTouchpad(2, new TouchpadStyle(background, knob));
		touchpad.setBounds(0, 0, background.getMinWidth(), background.getMinHeight());
		touchpad.setPosition(Main.SCREEN_WIDTH/2 - touchpad.getWidth() - margin, -Main.SCREEN_HEIGHT/2 + margin);
		touchpad.debug();
		touchpad.setForcedRadius(1.5f);
		screen.getHudStage().addActor(touchpad);
	}
	
	protected void shootRopeBullet(float angle) {
		ropeBullet.setTransform(actor.getBody().getPosition(), 0);	
		ropeBullet.setLinearVelocity(new Vector2(ROPE_SHOOT_SPEED, 0).setAngle(angle));
	}

	protected void shootRope() {
		actor.getBody().setAwake(true);
		
		RopeJointDef jointDef = new RopeJointDef();
		jointDef.bodyA = actor.getBody();
		jointDef.bodyB = ropeBullet;
		jointDef.localAnchorA.set(0,0);
		jointDef.localAnchorB.set(0,0);
		joint = (RopeJoint)screen.getWorld().createJoint(jointDef);

		joint.setMaxLength(10000);
	}
	
	protected void destroyCurrentJoint(){
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
	
	protected void triggerAutoRopeCut() {
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

	@Override
	public void dispose() {
		destroyCurrentJoint();
	}
	
}
