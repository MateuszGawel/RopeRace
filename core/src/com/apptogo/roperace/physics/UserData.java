package com.apptogo.roperace.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

public class UserData {
	//new fields may be added
	public String key; //general unique key of fixture
	public String type; //general type of fixture (can be anything)
	
	//used for body builder and for texture offsets
	public float width;
	public float height;
	
	//flag for ignoring collision check
	public boolean ignoreCollision = false;
	
	//level specific params
	public SegmentType segmentType = SegmentType.NONE;
	public Vector2 position;
	
	//one of them is used
	public Vector2 size;
	public float[] vertices;
	public Float radius;
	public boolean polyLineBottom;
	
	public UserData() {
		type = "default";
	}

	public UserData(String key) {
		this();
		this.key = key;
	}

	/**
	 * @param fixture with userData
	 * @return userData of fixture
	 */
	public static UserData get(Fixture fixture) {
		return ((UserData) fixture.getUserData());
	}

	/**
	 * @param body with userData
	 * @return userData of body
	 */
	public static UserData get(Body body) {
		return ((UserData) body.getUserData());
	}
	
	public enum SegmentType{
		NONE, CATCHABLE
	}

	@Override
	public String toString() {
		return "UserData [key=" + key + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserData other = (UserData) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
	
}
