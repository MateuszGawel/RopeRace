package com.apptogo.roperace.scene2d;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.apptogo.roperace.manager.ResourcesManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;

public class Animation extends AnimationActor {
	
	
	
    /**
     * @param pattern will be used as animation name if not overriden by name() method
     * @return created animation
     */
    public static Animation get(float frameDuration, String pattern)
    {
    	Animation animation = new Animation(frameDuration, ResourcesManager.getInstance().getRegions(pattern), PlayMode.NORMAL);
    	animation.setName(pattern);
        return animation;
    }

    /**
     * @param pattern will be used as animation name if not overriden by name() method
     * @return created animation
     */
    public static Animation get(float frameDuration, String pattern, PlayMode playMode)
    {
		Animation animation = new Animation(frameDuration, ResourcesManager.getInstance().getRegions(pattern), playMode);
		animation.setName(pattern);
        return animation;
    }
    
    /**
     * @param pattern will be used as animation name if not overriden by name() method
     * @return created animation
     */
    public static Animation get(String pattern)
    {
    	Animation animation = new Animation(0.02f, ResourcesManager.getInstance().getRegions(pattern), PlayMode.NORMAL);
    	animation.setName(pattern);
        return animation;
    }

    /** @param animationNames
     * @return Map of animations with name as key based on animationNames 
     */
    public static Map<String, Animation> getAnimations(String... animationNames) {
        Map<String, Animation> animations = new HashMap<String, Animation>();
        for (String animationName : animationNames) {
            animations.put(animationName, get(animationName).stop());
        }
        
        return animations;
    }

    /**-------- CONSTRUCTORS -------- **/
    
    public Animation(float frameDuration, Array<? extends TextureRegion> keyFrames)
    {
        super(frameDuration, keyFrames);
    }

    @SuppressWarnings("unchecked")
	public Animation(float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode)
    {
        super(frameDuration, keyFrames, playMode);
        animationRegions = (Array<AtlasRegion>) keyFrames;
    }

    
    
    /**-------- CHAIN METHODS -------- **/
    public Animation name(String name){
    	setName(name);
    	return this;
    }
    
    public Animation scaleFrames(float scale)
    {
        super.scaleFramesBy(scale);
        return this;
    }

    public Animation position(float x, float y)
    {
        setPosition(x, y);
        return this;
    }

    public Animation centerX()
    {
        setPosition(scaleBy * -animation.getKeyFrame(0).getRegionWidth() / 2f, getY());
        return this;
    }

    public Animation centerX(float offset)
    {
        setPosition(scaleBy * -animation.getKeyFrame(0).getRegionWidth() / 2f + offset, getY());
        return this;
    }

    public Animation centerY()
    {
        setPosition(getX(), scaleBy * -animation.getKeyFrame(0).getRegionHeight() / 2f);
        return this;
    }

    public Animation centerY(float offset)
    {
        setPosition(getX(), scaleBy * -animation.getKeyFrame(0).getRegionHeight() / 2f + offset);
        return this;
    }

    public Animation action(Action action)
    {
        addAction(action);
        return this;
    }

    public Animation stop()
    {
        doAnimate = false;
        return this;
    }

    public Animation start()
    {
        doAnimate = true;
        return this;
    }
}
