package com.apptogo.roperace.screen;

import com.apptogo.roperace.game.EndScreenGroup;
import com.apptogo.roperace.game.GameActor;
import com.apptogo.roperace.game.HudLabel;
import com.apptogo.roperace.level.LevelData;
import com.apptogo.roperace.level.LevelGenerator;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.plugin.CameraFollowingPlugin;
import com.apptogo.roperace.plugin.GravityPlugin;
import com.apptogo.roperace.plugin.SteeringPlugin;
import com.apptogo.roperace.plugin.TouchSteeringPlugin;
import com.apptogo.roperace.scene2d.Label;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends BasicScreen {

	protected Box2DDebugRenderer debugRenderer;
	protected FPSLogger logger = new FPSLogger();

	protected World world;
	protected ContactListener contactListener = new ContactListener();

	protected GameActor player;
	protected GameActor ground, ceiling;
	
	protected int level;
	protected LevelGenerator levelGenerator;
	protected LevelData levelData;
	
	protected Stage hudStage;
	protected Viewport hudViewport;
	protected SteeringPlugin steeringPlugin;
	protected CameraFollowingPlugin cameraFollowingPlugin;
	protected HudLabel hudLabel;
	
	public GameScreen(Main game, int level) {
		super(game);
		this.level = level;
	}
	
	public GameScreen(Main game) {
		super(game);
		this.level = 1; //TODO get last level from save
	}
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ---------------------------------------------- PREPARATION ----------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	protected void prepare() {

		debugRenderer = new Box2DDebugRenderer();

		world = new World(new Vector2(0, 0), true);
		world.setContactListener(contactListener);

		prepareFrontStage();
		prepareHudStage();
		createLevel();
		createPlayer();
		createLabel();
		createEndScreenGroup();
	}

	protected void createEndScreenGroup(){
		EndScreenGroup endScreenGroup = new EndScreenGroup(hudLabel);
		hudStage.addActor(endScreenGroup);
	}

	protected void createLabel() {
		hudLabel = new HudLabel(levelData);
		hudStage.addActor(hudLabel);
	}
	
	protected void prepareFrontStage() {
		this.frontViewport = new FillViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT));
		frontStage.setViewport(frontViewport);
//		((OrthographicCamera) frontStage.getCamera()).zoom = 0.7f; //0.7 is ok
	}

	protected void prepareHudStage() {
		this.hudViewport = new ExtendViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		this.hudStage = new Stage(this.hudViewport);
		((OrthographicCamera) hudStage.getCamera()).position.set(0f, 0f, 0f);
		
		inputMultiplexer.addProcessor(hudStage);
	}
	
	protected void createPlayer(){
		player = new GameActor("player");
		player.setBody(BodyBuilder.get()
				.type(BodyType.DynamicBody)
				.position(levelGenerator.getStartingPoint())
				.addFixture("player").circle(0.5f).density(2.5f).friction(0.5f).restitution(0.5f)
				.create());
		player.getBody().setLinearDamping(-0.02f);
		player.setStaticImage("ball");

		player.modifyCustomOffsets(0f, 0f);
		frontStage.addActor(player);
		
		cameraFollowingPlugin = new CameraFollowingPlugin(levelGenerator.getMapSize());
		steeringPlugin = new TouchSteeringPlugin(this);
		player.addPlugin(cameraFollowingPlugin);
		player.addPlugin(steeringPlugin);
		player.addPlugin(new GravityPlugin());
	}
	
	protected void createLevel(){	
		levelGenerator = new LevelGenerator(this);
		levelGenerator.loadLevel(level);
	}
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------------- STEP -------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	protected void step(float delta) {
		// --- backstage render first --- //
		//debug renderer
		if(!Main.isAndroid()){
			debugRenderer.render(world, frontStage.getCamera().combined);
		}
		//simulate physics and handle body contacts
		ContactListener.SNAPSHOT.clear();
		world.step(delta, 3, 3);
		
		// --- frontstage render last --- //
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(currentColorSet.getMainColor().r, currentColorSet.getMainColor().g, currentColorSet.getMainColor().b, currentColorSet.getMainColor().a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.backViewport.apply();
		this.frontViewport.apply();
		
		this.backStage.act(delta);
		this.frontStage.act(delta);
		this.hudStage.act(delta);
		
		this.backStage.draw();
		this.levelGenerator.render();
		this.frontStage.draw();
		this.hudStage.draw();
		
		step(delta);
		handleInput();
		
		this.cameraFollowingPlugin.updateCamera();

	}
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------ DISPOSE ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		cameraFollowingPlugin.postSetActor();
	}

	@Override
	public void dispose() {
		super.dispose();
		debugRenderer.dispose();
		world.dispose();
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------- GETTERS / SETTERS --------------------------------------------**/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	public World getWorld() {
		return world;
	}

	public GameActor getGround() {
		return ground;
	}

	public GameActor getCeiling() {
		return ceiling;
	}

	public Stage getHudStage() {
		return hudStage;
	}

	public LevelData getLevelData() {
		return levelData;
	}

	public void setLevelData(LevelData levelData) {
		this.levelData = levelData;
	}
}
