package com.apptogo.roperace.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.apptogo.roperace.exception.AnimationException;
import com.apptogo.roperace.scene2d.Animation;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AbstractActor extends Actor {

	protected Map<String, Animation> availableAnimations = new HashMap<String, Animation>();
	protected Queue<Animation> animationQueue = new LinkedList<Animation>();
	protected Animation currentAnimation;
	
	public AbstractActor(String name) {
		setName(name);
		//setDebug(true);
	}
	
	/**-------- ANIMATIONS/STATIC IMAGE -------- **/

	/**
	 * sets static image. It uses animation mechanism to achieve that
	 */
	public void setStaticImage(String imageName){
		//it's in fact 1 frame animation
		currentAnimation = Animation.get(imageName).scaleFrames(1/UnitConverter.PPM);
		
		calculateAverageOffset();
		
		currentAnimation.start();
	}
	
	public Animation getCurrentAnimation() {
		return currentAnimation;
	}
	
	public Map<String, Animation> getAvailableAnimations() {
		return availableAnimations;
	}

	/**
	 * queue animation to be displayed after current one finishes
	 * @throws AnimationException if animation is not in availableAnimation map
	 */
	public void queueAnimation(String animationName) {
		Animation animation = availableAnimations.get(animationName);
		if (animation == null) {
			throw new AnimationException("Animation: '" + animationName + "' is not available for GameActor: '" + getName() + "' Possible choices are: " + availableAnimations.keySet());
		}

		animationQueue.add(animation);
	}

	/**
	 * queue animation to be displayed after current one finishes
	 * @param count queue multiple times
	 * @throws AnimationException if animation is not in availableAnimation map
	 */
	protected void queueAnimation(String animationName, int count) {
		for (int i = 0; i < count; i++) {
			queueAnimation(animationName);
		}
	}

	/**
	 * defines all possible animations for this actor
	 */
	public void setAvailableAnimations(String... animationNames) {
		this.availableAnimations = Animation.getAnimations(animationNames);
		for(Animation animation : availableAnimations.values()){
			animation.scaleFrames(1 / UnitConverter.PPM);
		}
		calculateAverageOffset();
	}

	/**
	 * adds another animation which is defined before. 
	 * Mostly used for custom cases.
	 * @throws AnimationException if name is not provided.
	 */
	public void addAvailableAnimation(Animation animation) {
		if (animation.getName() == null || animation.getName().isEmpty())
			throw new AnimationException("Animation name is not provided");
		if (availableAnimations.containsKey(animation.getName()))
			throw new AnimationException("Animation with name: '" + animation.getName() + "' already exists");

		this.availableAnimations.put(animation.getName(), animation);
		animation.scaleFrames(1 / UnitConverter.PPM);
		calculateAverageOffset();
	}

	/**
	 * immediately changes animation. Be careful, any previously queued animation will be removed 
	 */
	public Animation changeAnimation(String animationName) {
		Animation animation = availableAnimations.get(animationName);
		if (animation == null) {
			throw new AnimationException("Animation: '" + animationName + "' is not available for GameActor: '" + getName() + "' Possible choices are: " + availableAnimations.keySet());
		}
		animationQueue.clear();
		animationQueue.add(animation);
		currentAnimation.setFinished(true);
		
		return animation;
	}
	
	/**
	 * handles animationQueue and sets currentAnimation
	 */
	protected void setCurrentAnimation() {
		//we should never allow situation when there's no current animation and queue is empty
		//invisible actors can be handled by immaterialActor
		if (currentAnimation == null && animationQueue.isEmpty())
			throw new AnimationException("Animation queue is empty. Add an animation");

		//when there's no current animation or something is queued
		if (currentAnimation == null || (animationQueue.size() > 0 && currentAnimation.isFinished())) {
			currentAnimation = animationQueue.poll();
			currentAnimation.setFinished(true);
			currentAnimation.start();
		}
	}

	/**
	 * we calculate average offset of all 1st frames from all animations.
	 * thanks that every animation will be positioned in the same place
	 */
	protected void calculateAverageOffset() {
		Vector2 averageOffset = new Vector2();
		Vector2 offsetSum = new Vector2();
		for (Animation animation : availableAnimations.values()) {
			AtlasRegion atlasRegion = ((AtlasRegion) animation.getGdxAnimation().getKeyFrames()[0]);
			offsetSum.x += UnitConverter.toBox2dUnits(atlasRegion.offsetX);
			offsetSum.y += UnitConverter.toBox2dUnits(atlasRegion.offsetY);
		}
		averageOffset.x = offsetSum.x / availableAnimations.size();
		averageOffset.y = offsetSum.y / availableAnimations.size();

		for(Animation animation : availableAnimations.values()){
			animation.setDeltaOffset(new Vector2(averageOffset));
		}
	}
}
