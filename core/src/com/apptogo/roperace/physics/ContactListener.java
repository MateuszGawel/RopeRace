package com.apptogo.roperace.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener
{
	
	public static final ContactSnapshot SNAPSHOT_BEGIN = new ContactSnapshot();
	public static final ContactSnapshot SNAPSHOT_END = new ContactSnapshot();
	
	@Override
	public void beginContact(Contact contact)
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		UserData dataA = UserData.get(fa);
		UserData dataB = UserData.get(fb);

		if(!dataA.ignoreCollision && !dataB.ignoreCollision) {
			SNAPSHOT_BEGIN.addContact(dataA, dataB);
		}

		if(dataA.key.equals("ropeBullet") && dataB.key.equals("level")){
			SNAPSHOT_BEGIN.setRopeBulletCollisionData(fb.getBody(), contact.getWorldManifold().getPoints()[0]);
		}
		else if(dataA.key.equals("level") && dataB.key.equals("ropeBullet")){
			SNAPSHOT_BEGIN.setRopeBulletCollisionData(fa.getBody(), contact.getWorldManifold().getPoints()[0]);
		}
	}

	@Override
	public void endContact(Contact contact) 
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		UserData dataA = UserData.get(fa);
		UserData dataB = UserData.get(fb);
		
		if(!dataA.ignoreCollision && !dataB.ignoreCollision) {
			SNAPSHOT_END.addContact(dataA, dataB);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{	
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		//workaround due to box2d bug causing bounce even 0 restitution
		if(checkFixturesTypes(fa, fb, "level", "ropeBullet")){
			Fixture fixture = getFixtureByType(fa, fb, "ropeBullet");
			fixture.getBody().setLinearVelocity(0,0);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if(checkFixturesTypes(fa, fb, "level", "player")){
			SNAPSHOT_BEGIN.setPlayerContactImpulse(impulse.getNormalImpulses()[0]);
		}
	}
	
	private boolean checkFixturesTypes(Fixture fixtureA, Fixture fixtureB, String typeA, String typeB)
	{
		if(fixtureA.getUserData() != null && fixtureB.getUserData() != null){
			if
			(
				( 
						UserData.get(fixtureA).key.equals( typeA ) 
						&& 
						UserData.get(fixtureB).key.equals( typeB ) 
				) 
				|| 
				( 
						UserData.get(fixtureA).key.equals( typeB ) 
						&& 
						UserData.get(fixtureB).key.equals( typeA ) 
				)
			)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean checkIsOneOfType(Fixture fixtureA, Fixture fixtureB, String type)
	{
		if(fixtureA.getUserData() != null && fixtureB.getUserData() != null){
			if
			(
					UserData.get(fixtureA).key.equals( type ) 
					||
					UserData.get(fixtureB).key.equals( type ) 
			)
			{
				return true;
			}
		}
		
		return false;
	}
		
	private Fixture getFixtureByType(Fixture fixtureA, Fixture fixtureB, String type)
	{
		return UserData.get(fixtureA).key.equals(type) ? fixtureA : fixtureB;
	}
}
