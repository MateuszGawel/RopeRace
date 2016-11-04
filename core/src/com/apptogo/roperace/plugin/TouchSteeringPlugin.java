package com.apptogo.roperace.plugin;

import com.apptogo.roperace.manager.CustomAction;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.physics.UserData;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

public class TouchSteeringPlugin extends SteeringPlugin {
	private static final float ROPE_SHORTENING_SPEED = 0.03f;
	private static final float ROPE_SHOOT_SPEED = 40f;
	private static final float CUT_ROPE_AFTER = 0.18f;
	
	protected enum TouchState{
		NOT_TOUCHED, JUST_TOUCHED, KEEP_TOUCHED, JUST_UNTOUCHED
	}
	
	private GameScreen screen;
	private Body ropeBullet;
	private RopeJoint joint;
	private TouchState touchState = TouchState.NOT_TOUCHED;;
	private boolean ropeAttached;
	
	public TouchSteeringPlugin(GameScreen screen) {
		super();
		this.screen = screen;
		
		ropeBullet = BodyBuilder.get()
				.type(BodyType.DynamicBody)
				.position(0, 0)
				.addFixture("rope", "ropeBullet").circle(0.1f).density(1000000000f)
				.position(-100, -100)
				.create();
	}

	@Override
	public void run() {
		super.run();
		handleBulletCollision();
		handleTouchState();
	}

	private void handleBulletCollision(){
		if(joint != null && ContactListener.SNAPSHOT.collide(UserData.get(ropeBullet), "level")){
			ropeAttached = true;
			ropeBullet.setLinearVelocity(new Vector2(0, 0));
			joint.setMaxLength(actor.getBody().getPosition().dst(ropeBullet.getPosition()));
			triggerAutoRopeShortening(joint);
		}
	}
	
	private void handleTouchState(){
		if (Gdx.input.getInputProcessor() != null) {
			switch(touchState){
				case JUST_TOUCHED:
					touchState = TouchState.KEEP_TOUCHED;
					float posX = Gdx.input.getX();
					float posY = Gdx.input.getY();
					destroyCurrentJoint();
					shootRopeBullet(posX, posY);
					shootRope();
					triggerAutoRopeCut();
					break;
				case KEEP_TOUCHED:
					if(!Gdx.input.isTouched()){
						touchState = TouchState.JUST_UNTOUCHED;
					}
					break;
				case JUST_UNTOUCHED:
					touchState = TouchState.NOT_TOUCHED;
					destroyCurrentJoint();
					break;
				case NOT_TOUCHED:
					if (Gdx.input.justTouched()) {
						touchState = TouchState.JUST_TOUCHED;
					}
					break;
				default:
					break;
			}
		}
	}


	private void shootRopeBullet(float posX, float posY) {
		Vector3 screenVector = new Vector3(posX, posY, 0);
		
		Vector3 worldVector = screen.getFrontStage().getCamera().unproject(screenVector);
		Vector2 worldVector2 = new Vector2(worldVector.x, worldVector.y);

		double theta = 180.0 / Math.PI * Math.atan2(actor.getBody().getPosition().x - worldVector2.x, worldVector2.y - actor.getBody().getPosition().y);
		theta += 90;

		ropeBullet.setTransform(actor.getBody().getPosition(), 0);
		
		ropeBullet.setActive(true);
		ropeBullet.setLinearVelocity(new Vector2(ROPE_SHOOT_SPEED, 0).setAngle((float)theta));
	}

	private void shootRope() {
		actor.getBody().setAwake(true);
		
		RopeJointDef jointDef = new RopeJointDef();
		jointDef.bodyA = actor.getBody();
		jointDef.bodyB = ropeBullet;
		jointDef.localAnchorA.set(0,0);
		jointDef.localAnchorB.set(0,0);
		joint = (RopeJoint)screen.getWorld().createJoint(jointDef);

		joint.setMaxLength(10000);
	}
	
	private void destroyCurrentJoint(){
		if(joint != null){
			screen.getWorld().destroyJoint(joint);
			joint = null;
			ropeAttached = false;
			ropeBullet.setTransform(new Vector2(-100, -100), 0);
		}
	}
	
	private void triggerAutoRopeCut() {
		CustomActionManager.getInstance().registerAction(new CustomAction(CUT_ROPE_AFTER, 1) {
			
			@Override
			public void perform() {
				if(joint != null && !ropeAttached){
					destroyCurrentJoint();
				}
			}
		});
	}
	
	private void triggerAutoRopeShortening(final RopeJoint joint){
		CustomActionManager.getInstance().registerAction(new CustomAction(0.01f, -1) {
			
			@Override
			public void perform() {
				if(joint.getMaxLength() > 1){
					joint.setMaxLength(joint.getMaxLength() - ROPE_SHORTENING_SPEED);
				}
				else{
					unregister();
				}
			}
		});
	}
}
