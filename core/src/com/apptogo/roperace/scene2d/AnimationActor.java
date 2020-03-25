package com.apptogo.roperace.scene2d;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class AnimationActor extends Actor {

	protected com.badlogic.gdx.graphics.g2d.Animation animation;

	private float frameWidth, frameHeight;
	public TextureRegion currentFrame;
	protected float scaleBy = 1, stateTime = 0;
	protected boolean doAnimate = true;
	private boolean finished;
	
	protected Array<AtlasRegion> animationRegions;
	
	// values to calculate delta offset to prevent animation jittering caused by trimming
	private Vector2 previousOffset = new Vector2();
	private Vector2 currentOffset = new Vector2();
	private Vector2 deltaOffset = new Vector2();

	public AnimationActor(float frameDuration, Array<? extends TextureRegion> keyFrames) {
		this(frameDuration, keyFrames, PlayMode.NORMAL);
	}

	public AnimationActor(float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode) {
		animation = new com.badlogic.gdx.graphics.g2d.Animation(frameDuration, keyFrames, playMode);
	}

	public void scaleFramesBy(float scale) {
		scaleBy = scale;
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (doAnimate){
			stateTime += delta;
	
			//set status to finished. In looping it's true only in one frame
			float frameIndex = animation.getKeyFrameIndex(stateTime);
			animation.getKeyFrames();
			int length = animation.getKeyFrames().length;
			if(length == frameIndex+1)
				finished = true;
			else
				finished = false;
		}
			
		currentFrame = (TextureRegion) animation.getKeyFrame(stateTime);
		frameWidth = currentFrame.getRegionWidth();
		frameHeight = currentFrame.getRegionHeight();

		setSize(frameWidth * scaleBy, frameHeight * scaleBy);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		calculateOffsets(true);

		batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a * parentAlpha);
		batch.draw(currentFrame, getX() + deltaOffset.x, getY() + deltaOffset.y, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

		calculateOffsets(false);
	}

	public void setFrameDuration(float frameDuration){
		animation.setFrameDuration(frameDuration);	
	}
	
	public float getFrameDuration(){
		return animation.getFrameDuration();
	}
	
	/**
	 * true when animation is finished.
	 * It's also true at every loops end
	 */
	public boolean isFinished(){
		return finished;
	}
	
	/**
	 * calculates deltaOffset to prevent jittering caused by trimming. It takes
	 * current and previous offset and then position is set based on calculated
	 * value.
	 * 
	 * @param beforeDraw updates current or previous offset based on flag
	 */
	private void calculateOffsets(boolean beforeDraw) {
		//check if we have current frame already
		if (currentFrame != null) {
			
			//we call that method twice. Before and after draw. This flag is identifier where we are.
			if (beforeDraw) {
				currentOffset.x = UnitConverter.toBox2dUnits(((AtlasRegion) currentFrame).offsetX);
				currentOffset.y = UnitConverter.toBox2dUnits(((AtlasRegion) currentFrame).offsetY);
			} else {
				previousOffset.x = UnitConverter.toBox2dUnits(((AtlasRegion) currentFrame).offsetX);
				previousOffset.y = UnitConverter.toBox2dUnits(((AtlasRegion) currentFrame).offsetY);
			}
			
			//if offset are equal we don't have to modify deltaOffset
			if (!currentOffset.equals(previousOffset)) {
				if (previousOffset.x == 0) {
					//we enter here in first iteration when previousOffset is 0. 
					//deltaOffset is set by gameActor (average of offsets of 1st frame from every animation).
					//thanks that operation we don't start from deltaOffset 0. It's different for every animation
					//thanks that every animation will start from the same place.
					deltaOffset.x = -(deltaOffset.x - currentOffset.x);
				} else {
					//in every other case we just calculate delta by subtracting.
					deltaOffset.x += currentOffset.x - previousOffset.x;
				}
				
				//the same as in x axis
				if (previousOffset.y == 0) {
					deltaOffset.y = -(deltaOffset.y - currentOffset.y);
				} else {
					deltaOffset.y += currentOffset.y - previousOffset.y;
				}				
			}
		}
	}

	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(TextureRegion currentFrame) {
		this.currentFrame = currentFrame;
	}

	public Vector2 getDeltaOffset() {
		return deltaOffset;
	}

	public void setDeltaOffset(Vector2 deltaOffset) {
		this.deltaOffset = deltaOffset;
	}

	public com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> getGdxAnimation() {
		return animation;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
		stateTime = 0;
	}

	public Array<AtlasRegion> getAnimationRegions() {
		return animationRegions;
	}

	public void setAnimationRegions(Array<AtlasRegion> animationRegions) {
		this.animationRegions = animationRegions;
	}
}
