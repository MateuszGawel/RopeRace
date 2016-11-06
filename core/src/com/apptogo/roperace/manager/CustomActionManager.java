package com.apptogo.roperace.manager;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class CustomActionManager extends Group {

	private static CustomActionManager INSTANCE;

	public static void create() {
		INSTANCE = new CustomActionManager();
	}

	public static void destroy() {
		INSTANCE = null;
	}

	public static CustomActionManager getInstance() {
		return INSTANCE;
	}

	/**
	 * @param action to be registered
	 */
	public void registerAction(CustomAction action) {
		this.addActor(action);
	}

	/**
	 * Actor will be automatically removed from manager after it finishes, but you can do it earlier.
	 * After removing actor is reseted so it can be registered again.
	 * @param action to be unregistered
	 */
	public void unregisterAction(CustomAction action) {
		this.removeActor(action);
		action.resetAction();
	}

	public Actor[] getAllRegisteredActions(){
		return this.getChildren().items;
	}
	
	public int getRegisteredActionCount(){
		return this.getChildren().size;
	}
	
	public void clearAllActions(){
		for(Actor actor : this.getChildren()){
			if(actor instanceof CustomAction){
				unregisterAction((CustomAction)actor);
			}
		}
	}
}
