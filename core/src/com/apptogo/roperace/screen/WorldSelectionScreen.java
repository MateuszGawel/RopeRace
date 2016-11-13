package com.apptogo.roperace.screen;

import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.scene2d.Image;
import com.apptogo.roperace.scene2d.Listener;
import com.apptogo.roperace.scene2d.ShadowedButton;
import com.apptogo.roperace.scene2d.ShadowedButton.ButtonSize;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class WorldSelectionScreen extends BasicScreen {

	public WorldSelectionScreen(Main game) {
		super(game);
	}
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ---------------------------------------------- PREPARATION ----------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	protected void prepare() {
		prepareBackStage();
		prepareFrontStage();
	}

	protected void prepareBackStage() {
		//		Image background = Image.get("background").width(Main.SCREEN_WIDTH).position(0, -Main.SCREEN_HEIGHT / 2f).centerX();
		//		backStage.addActor(background);
	}

	protected void prepareFrontStage() {		
		float small_padding = 20;
		float big_padding = 100;
		
		ShadowedButton backButton = new ShadowedButton("back-button", currentColorSet, ButtonSize.SMALL);
		backButton.addListener(Listener.click(game, new MenuScreen(game)));
		backButton.setPosition(Main.SCREEN_WIDTH / 2 - backButton.getWidth() - small_padding, -Main.SCREEN_HEIGHT/2 + small_padding);
		frontStage.addActor(backButton);

		Image world1 = Image.getFromTexture("world1");
		world1.size(world1.getRegion().getRegionWidth(), world1.getRegion().getRegionHeight());
		world1.position(-Main.SCREEN_WIDTH/2 + big_padding, Main.SCREEN_HEIGHT/2 - world1.getHeight() - big_padding);
		world1.addListener(Listener.click(game, new LevelSelectionScreen(game)));
		frontStage.addActor(world1);
		
		Image world2 = Image.getFromTexture("world2");
		world2.size(world2.getRegion().getRegionWidth(), world2.getRegion().getRegionHeight());
		world2.position(-Main.SCREEN_WIDTH/2 + world2.getWidth() + 2*big_padding , Main.SCREEN_HEIGHT/2 - world2.getHeight() - big_padding);
		world2.addListener(Listener.click(game, new LevelSelectionScreen(game)));
		frontStage.addActor(world2);
	
	}
	
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------------- STEP -------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	protected void step(float delta) {
		// --- backstage render first --- //

		//simulate physics and handle body contacts

		// --- frontstage render last --- //
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.backViewport.apply();
		this.backStage.act(delta);
		this.backStage.draw();

		step(delta);

		this.frontViewport.apply();
		this.frontStage.act(delta);
		this.frontStage.draw();

		handleInput();
	}
	
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------ DISPOSE ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	protected void handleInput() {
		if (Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.setScreen(new MenuScreen(game));
		}
	}
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------- GETTERS / SETTERS --------------------------------------------**/
	/** ---------------------------------------------------------------------------------------------------------- **/

}
