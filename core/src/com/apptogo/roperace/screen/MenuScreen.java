package com.apptogo.roperace.screen;

import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.save.LevelNode;
import com.apptogo.roperace.save.SaveManager;
import com.apptogo.roperace.scene2d.BallButton;
import com.apptogo.roperace.scene2d.ColorSet;
import com.apptogo.roperace.scene2d.Image;
import com.apptogo.roperace.scene2d.Listener;
import com.apptogo.roperace.scene2d.ShadowedButton;
import com.apptogo.roperace.scene2d.ShadowedButton.ButtonSize;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class MenuScreen extends BasicScreen {
	
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
		//frontStage.setViewport(new FillViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT)));
		
		float padding = 20;
		
		Image logo = Image.get("roperace-logo");
		logo.size(logo.getWidth(), logo.getHeight());
		logo.position(0, Main.SCREEN_HEIGHT/2 - logo.getHeight() - padding).centerX();
		logo.setColor(currentColorSet.getMainColor());
		frontStage.addActor(logo);
		
		ShadowedButton worldsButton = new ShadowedButton("worlds-button", currentColorSet, ButtonSize.SMALL);
		worldsButton.addListener(Listener.click(game, new WorldSelectionScreen()));
		worldsButton.setPosition(Main.SCREEN_WIDTH/2-worldsButton.getWidth()-padding, -Main.SCREEN_HEIGHT/2+padding);
		frontStage.addActor(worldsButton);
		
		ShadowedButton ballsButton = new ShadowedButton("balls", currentColorSet, ButtonSize.SMALL);
		ballsButton.addListener(Listener.click(game, new BallSelectionScreen()));
		ballsButton.setPosition(Main.SCREEN_WIDTH/2-2*ballsButton.getWidth()-padding - 20, -Main.SCREEN_HEIGHT/2+padding);
		frontStage.addActor(ballsButton);

		LevelNode levelNode = SaveManager.getInstance().getLatestAvailableLevel();
		ShadowedButton playButton = new ShadowedButton("play-button", currentColorSet, ButtonSize.BIG);
		playButton.addListener(Listener.click(game, new GameScreen(levelNode.getLevelNo(), levelNode.getWorldNo())));
		playButton.setPosition(-playButton.getWidth()/2, -playButton.getHeight()/2 - 100);
		frontStage.addActor(playButton);	
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

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------- GETTERS / SETTERS --------------------------------------------**/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
}
