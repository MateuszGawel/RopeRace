package com.apptogo.roperace.screen;

import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.scene2d.Image;
import com.apptogo.roperace.scene2d.Listener;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class MenuScreen extends BasicScreen {

	protected Box2DDebugRenderer debugRenderer;
	protected FPSLogger logger = new FPSLogger();

	protected World world;
	protected ContactListener contactListener = new ContactListener();
	
	public MenuScreen(Main game) {
		super(game);
	}
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ---------------------------------------------- PREPARATION ----------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	protected void prepare() {

		debugRenderer = new Box2DDebugRenderer();

		world = new World(new Vector2(0, 0), true);
		world.setContactListener(contactListener);

		prepareBackStage();
		prepareFrontStage();
	}

	protected void prepareBackStage() {
		//		Image background = Image.get("background").width(Main.SCREEN_WIDTH).position(0, -Main.SCREEN_HEIGHT / 2f).centerX();
		//		backStage.addActor(background);
	}

	protected void prepareFrontStage() {
		frontStage.setViewport(new FillViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT)));
		
		float padding = UnitConverter.toBox2dUnits(20);
		
		Image background = Image.getFromTexture("roperace-logo");
		background.size(UnitConverter.toBox2dUnits(background.getRegion().getRegionWidth()), 
				UnitConverter.toBox2dUnits(background.getRegion().getRegionHeight())).position(0, 2).centerX();
		frontStage.addActor(background);
		
		Image chooseLevel = Image.getFromTexture("choose-level-button");
		chooseLevel.size(UnitConverter.toBox2dUnits(chooseLevel.getRegion().getRegionWidth()), UnitConverter.toBox2dUnits(chooseLevel.getRegion().getRegionHeight()));
		chooseLevel.position(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH/2)-chooseLevel.getWidth()-padding, -UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT/2)+padding);
		chooseLevel.addListener(Listener.click(game, new LevelSelectionScreen(game)));
		frontStage.addActor(chooseLevel);
		
		Image playButton = Image.getFromTexture("play-button");
		playButton.size(UnitConverter.toBox2dUnits(playButton.getRegion().getRegionWidth()), 
				UnitConverter.toBox2dUnits(playButton.getRegion().getRegionHeight())).position(0, -5).centerX();
		playButton.addListener(Listener.click(game, new GameScreen(game)));
		frontStage.addActor(playButton);
	}
	
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------------- STEP -------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	protected void step(float delta) {
		// --- backstage render first --- //

		//simulate physics and handle body contacts
		ContactListener.SNAPSHOT.clear();
		world.step(delta, 3, 3);

		//debug renderer
		debugRenderer.render(world, frontStage.getCamera().combined);

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
		debugRenderer.dispose();
		world.dispose();
		frontStage.dispose();
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------- GETTERS / SETTERS --------------------------------------------**/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	public World getWorld() {
		return world;
	}
}
