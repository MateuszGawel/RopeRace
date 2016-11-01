package com.apptogo.roperace.game;

import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.CustomAction;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.manager.ResourcesManager;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParallaxActor extends Actor {
	
	private TextureRegion textureRegion;
	private OrthographicCamera camera;
		
	/**
	 * Previous camera X coordinate's value - used for calculating how to change current U
	 */
	private float previousCameraX;
	
	/**
	 * Camera's speed modifier (if you want to scroll parallax a little bit slower or faster than camera)
	 */
	private float speedModifier = 1;
		
	/**
	 * Full width of texture in Box2D units (should be equal to device screen's width)
	 * Texture's width also should be equal to device screen's width (in other case it will be stretched)
	 */
	private float FULL_WIDTH = Main.SCREEN_WIDTH / UnitConverter.PPM;
	
	/**
	 * Width and height of textureRegion (width is being ste up using FULL_WIDTH) - used for drawing
	 */
	private float width, height;
	
	/**
	 * Factors to cast xy to uv coordinates
	 */
	private float x2u, u2x;
	
	/**
	 * Beginning U coordinate's value (used for reset) 
	 */
	private float startU;
	
	/**
	 * Current U coordinate's value (used for "splitting" the region)
	 */
	private float currentU;
	
	/**
	 * Fixed speed set up by setfixedSpeed() (when non-zero region's moving does not depend on camera speed)
	 */
	private float fixedSpeed = 0;	
	
	
	/**
	 * 
	 * @param camera - camera to follow
	 * @param textureRegionName - name of region (in texture atlas)
	 */
	public ParallaxActor(Camera camera, String textureRegionName) {
		this.textureRegion = new TextureRegion(ResourcesManager.getInstance().getAtlasRegion(textureRegionName));
		this.setName(textureRegionName);
		
		this.camera = (OrthographicCamera)camera;
		previousCameraX = camera.position.x;
				
		//Calculating width and height
		width = FULL_WIDTH;
		height = textureRegion.getRegionHeight()/64f;
		setSize(width, height);
		
		//Calculating factors
		x2u = width / (textureRegion.getU2() - textureRegion.getU());
		u2x = (textureRegion.getU2() - textureRegion.getU()) / width;
		
		//Saving start U coordinate's value
		startU = textureRegion.getU();
		
		//Setting currentU (should be 0 at the beginning by default)
		currentU = textureRegion.getU();
		
		//Setting up moving action 
		CustomActionManager.getInstance().registerAction(parallaxAction);
	}
	
	/**
	 * This action updates texture position due to camera's position (if fixedSpeed == 0)
	 * or moving it by fixed offset (id fixedSpeed != 0)
	 */
	CustomAction parallaxAction = new CustomAction(0, 0) {
		@Override
		public void perform() {
			//Calculating left part width and currentU value
			if(fixedSpeed == 0) {
				float cameraSpeed = speedModifier * (camera.position.x - previousCameraX);
				width -= cameraSpeed;			
				currentU += cameraSpeed * u2x;
			}
			else {
				width -= fixedSpeed;
				currentU += fixedSpeed * u2x;
			}
			
			//Backuping camera.position.x value
			previousCameraX = camera.position.x;
			
			//Resetting region when left part is 0 or lesser
			if( currentU >= textureRegion.getU2())
			{
				currentU = currentU - (textureRegion.getU2() - startU);
				width = FULL_WIDTH - (currentU * u2x);
			}
		}
	};
	
	/**
	 * Updates actor's position due to camera's position (so it will be always at the center of screen)
	 */
	@Override
	public void act(float delta) {
	}
	
	/**
	 * Draws left and right part of regions (due to calculated left region's width and region's offset)
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		//Setting up current U (equal to currentU's value)
		textureRegion.setU(currentU);
		
		//Drawing left part of region
		batch.draw(textureRegion, camera.position.x - FULL_WIDTH/2f, camera.position.y + getY(), width, height);
		
		//Backuping current U and U2 coordinates
		float backupU = textureRegion.getU();
		float backupU2 = textureRegion.getU2();
		
		//Setting up U and U2 values for right region temporarily
		textureRegion.setU(startU);
		textureRegion.setU2(currentU);

		//Drawing right part of region
		batch.draw(textureRegion, camera.position.x - FULL_WIDTH/2f + width, camera.position.y + getY(), FULL_WIDTH - width, height);
		
		//Restoring backed up U and U2 coordinates
		textureRegion.setU(backupU);
		textureRegion.setU2(backupU2);
	}
	
	/**
	 * Wrapper get method for chaining purposes
	 * @param camera - camera to follow
	 * @param textureRegionName - name of region (in texture atlas)
	 * @return ParallaxActor instance
	 */
	public static ParallaxActor get(Camera camera, String textureRegionName){
		return new ParallaxActor(camera, textureRegionName);
	}
	
	/**
	 * Wrapper of setY() Actor's method for chaining purposes
	 * @param y - y coordinate (in Box2D coordinates)
	 * @return ParallaxActor instance
	 */
	public ParallaxActor moveToY(float y) {
		setY(y);
		return this;
	}
	
	/**
	 * Setting fixed speed of scrolling texture (region will not follow camera anymore)
	 * @param speed - fixed speed (gt 0 will texture move forward)
	 * @return ParallaxActor instance
	 */
	public ParallaxActor setFixedSpeed(float speed) {
		this.fixedSpeed = speed;
		return this;
	}
	
	/**
	 * Setting camera's speed modifier (if you want to scroll parallax slower/faster than camera)
	 * @param modifier - modifier's value (it will be multiplying camera's speed)
	 * @return ParallaxActor instance
	 */
	public ParallaxActor setSpeedModifier(float modifier) {
		this.speedModifier = modifier;
		return this;
	}
}
