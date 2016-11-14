package com.apptogo.roperace.screen;

import com.apptogo.roperace.game.GameActor;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.LevelGenerator;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.plugin.CameraFollowingPlugin;
import com.apptogo.roperace.plugin.GravityPlugin;
import com.apptogo.roperace.plugin.KeyboardSteeringPlugin;
import com.apptogo.roperace.plugin.SteeringPlugin;
import com.apptogo.roperace.plugin.TouchSteeringPlugin;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
	
	protected Stage hudStage;
	protected Viewport hudViewport;
	protected SteeringPlugin steeringPlugin;
	
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

		prepareBackStage();
		prepareFrontStage();
		prepareHudStage();
		createLevel();
		createPlayer();


	}

	protected void prepareBackStage() {
		//		Image background = Image.get("background").width(Main.SCREEN_WIDTH).position(0, -Main.SCREEN_HEIGHT / 2f).centerX();
		//		backStage.addActor(background);
	}

	protected void prepareFrontStage() {
		frontStage.setViewport(new FillViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT)));
//		((OrthographicCamera) frontStage.getCamera()).zoom = 0.75f;
	}

	protected void prepareHudStage() {
		this.hudViewport = new FillViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		this.hudStage = new Stage(this.hudViewport);
		((OrthographicCamera) hudStage.getCamera()).position.set(0f, 0f, 0f);
		
		inputMultiplexer.addProcessor(hudStage);
	}
	
	protected void createPlayer(){
		player = new GameActor("player");
		player.setBody(BodyBuilder.get()
				.type(BodyType.DynamicBody)
				.position(levelGenerator.getStartingPoint())
				.addFixture("player").circle(0.25f).density(5f).friction(1f).restitution(0.5f)
				.create());
		player.getBody().setLinearDamping(-0.02f);
		player.setStaticImage("ball");

		player.modifyCustomOffsets(0f, 0f);
		frontStage.addActor(player);
		
		
		player.addPlugin(new CameraFollowingPlugin(levelGenerator.getMapSize()));
		steeringPlugin = new TouchSteeringPlugin(this);
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

		//simulate physics and handle body contacts
		ContactListener.SNAPSHOT.clear();
		world.step(delta, 3, 3);
		
		//must be done as soon as possible so it's extracted out of the standard flow
		steeringPlugin.handleViewfinder();
		steeringPlugin.handleBulletCollision();
		
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
		levelGenerator.render();
		
		this.frontViewport.apply();
		this.frontStage.act(delta);
		this.frontStage.draw();

		
		this.frontViewport.apply();
		this.hudStage.act(delta);
		this.hudStage.draw();
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
}
