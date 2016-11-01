package com.apptogo.roperace.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Logger;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener
{
	
	public static final ContactSnapshot SNAPSHOT = new ContactSnapshot();
	
	@Override
	public void beginContact(Contact contact)
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		UserData dataA = UserData.get(fa);
		UserData dataB = UserData.get(fb);
		
		if(!dataA.ignoreCollision && !dataB.ignoreCollision) {
			SNAPSHOT.addContact(dataA, dataB);
		}
	}

	@Override
	public void endContact(Contact contact) 
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{	
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
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
