package com.apptogo.roperace.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ContactSnapshot {	
	private Map<UserData, UserData> snapshot = new HashMap<UserData, UserData>();
	private float playerContactImpulse;
	private Body ropeBulletCollidedBody;
	private Vector2 ropeBulletCollisionPoint;

	/**
	 * Add collision to snapshot. Order doesn't matter.
	 * 
	 * @param dataA
	 * @param dataB
	 */
	public void addContact(UserData dataA, UserData dataB){
		if(!dataB.equals(snapshot.get(dataA)))
			snapshot.put(dataA, dataB);
		if(!dataA.equals(snapshot.get(dataB)))
			snapshot.put(dataB, dataA);
	}
	
	/**
	 * @return full snapshot of current collisions
	 */
	public Map<UserData, UserData> getSnapshot(){
		return snapshot;
	}
	
	/**
	 * It doesn't matter what's the order of parameters.
	 * 
	 * @param keyA
	 * @param keyB
	 * @return boolean if fixtures with provided UserData.key collides with each other
	 */
	public boolean collide(String keyA, String keyB){	
		for(UserData ud : snapshot.keySet()){
			if(ud.key == keyA &&  snapshot.get(ud).key.contains(keyB))
				return true;
		}
		return false;
	}
	
	/**
	 * It doesn't matter what's the order of parameters.
	 * 
	 * @param dataA
	 * @param dataB
	 * @return boolean if fixtures with provided UserData collides with each other.
	 * UserD
	 */
	public boolean collide(UserData dataA, UserData dataB){
		return dataA.equals(snapshot.get(dataB));
	}
	
	/**
	 * It doesn't matter what's the order of parameters.
	 * 
	 * @param dataA
	 * @param keyB
	 * @return boolean if fixture with UserData dataA collides with fixture with UserData.key keyB
	 */
	public boolean collide(UserData dataA, String keyB){
		boolean collision = false;
		if(snapshot.get(dataA) != null){
			collision = keyB.contains(snapshot.get(dataA).key);
			if(!collision)
				collision = snapshot.get(dataA).key.contains(keyB);
		}
		return collision;
	}
	
	
	/**
	 * @param keyA
	 * @param keyB
	 * @return Entry with UserData of keyA as K and UserData of keyB as V
	 */
	public Entry<UserData, UserData> getCollision(String keyA, String keyB){
		for(Entry<UserData, UserData> entry : snapshot.entrySet()){
			if(entry.getKey().key.contains(keyA) && entry.getValue().key.contains(keyB))
				return entry;
		}
		return null;
	}
	
	/**
	 * @param dataA
	 * @param dataB
	 * @return Entry with UserData dataA as K and UserData dataB as V
	 */
	public Entry<UserData, UserData> getCollision(UserData dataA, UserData dataB){
		for(Entry<UserData, UserData> entry : snapshot.entrySet()){
			if(entry.getKey().equals(dataA) && entry.getValue().equals(dataB))
				return entry;
		}
		return null;
	}
	
	/**
	 * @param dataA
	 * @param keyB
	 * @return Entry with UserData dataA as K and UserData of keyB as V
	 */
	public Entry<UserData, UserData> getCollision(UserData dataA, String keyB){
		for(Entry<UserData, UserData> entry : snapshot.entrySet()){
			if(entry.getKey().equals(dataA) && entry.getValue().key.contains(keyB))
				return entry;
		}
		return null;
	}
	
	/**
	 * @param keyA
	 * @param dataB
	 * @return Entry with UserData of keyA as K and UserData dataB as V
	 */
	public Entry<UserData, UserData> getCollision(String keyA,UserData dataB){
		for(Entry<UserData, UserData> entry : snapshot.entrySet()){
			if(entry.getKey().key.contains(keyA) && entry.getValue().equals(dataB))
				return entry;
		}
		return null;
	}

	public void setPlayerContactImpulse(float playerContactImpulse) {
		this.playerContactImpulse = playerContactImpulse;
	}

	public float getPlayerContactImpulse() {
		return playerContactImpulse;
	}

	public Body getRopeBulletCollidedBody() {
		return ropeBulletCollidedBody;
	}

	public Vector2 getRopeBulletCollisionPoint() {
		return ropeBulletCollisionPoint;
	}

	public void setRopeBulletCollisionData(Body ropeBulletCollidedBody, Vector2 ropeBulletCollisionPoint) {
		this.ropeBulletCollidedBody = ropeBulletCollidedBody;
		this.ropeBulletCollisionPoint = ropeBulletCollisionPoint;
	}

	/**
	 * clears all collisions. Should be run in every loop.
	 */
	public void clear(){
		snapshot.clear();
		playerContactImpulse = 0;
	}

	@Override
	public String toString() {
		return "ContactSnapshot [snapshot=" + snapshot + "]";
	}



}
