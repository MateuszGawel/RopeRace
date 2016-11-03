package com.apptogo.roperace.screen;

import com.apptogo.roperace.game.GameActor;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.plugin.CameraFollowingPlugin;
import com.apptogo.roperace.plugin.GravityPlugin;
import com.apptogo.roperace.plugin.KeyboardSteeringPlugin;
import com.apptogo.roperace.plugin.TouchSteeringPlugin;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class GameScreen extends BasicScreen {

	protected Box2DDebugRenderer debugRenderer;
	protected FPSLogger logger = new FPSLogger();

	protected World world;
	protected ContactListener contactListener = new ContactListener();

	protected GameActor player;
	protected GameActor ground, ceiling;
	
	public GameScreen(Main game) {
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
		createPlayer();
		createLevel();
		
		//prepare CustomActionManager
		frontStage.addActor(CustomActionManager.getInstance());
	}

	protected void prepareBackStage() {
		//		Image background = Image.get("background").width(Main.SCREEN_WIDTH).position(0, -Main.SCREEN_HEIGHT / 2f).centerX();
		//		backStage.addActor(background);
	}

	protected void prepareFrontStage() {
		frontStage.setViewport(new FillViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT)));
		//((OrthographicCamera) gameworldStage.getCamera()).zoom = 1f;
	}
	
	protected void createPlayer(){
		player = new GameActor("player");
		player.setBody(BodyBuilder.get()
				.type(BodyType.DynamicBody)
				.position(0, 0)
				.addFixture("player").circle(0.2f).density(2f)
				.create());

		player.modifyCustomOffsets(0f, 0f);
		frontStage.addActor(player);
		
		player.addPlugin(new CameraFollowingPlugin());
		player.addPlugin(new KeyboardSteeringPlugin());
		player.addPlugin(new TouchSteeringPlugin(this));
		player.addPlugin(new GravityPlugin());
	}
	
	protected void createLevel(){
		final float levelWidth = 1000;
		
		ground = new GameActor("ground");
		ground.setBody(BodyBuilder.get()
				.type(BodyType.StaticBody)
				.position(levelWidth/2, -UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT/2) + 1)
				.addFixture("level", "ground").box(levelWidth, 0.2f)
				.create());
		
		ceiling = new GameActor("ceiling");
		ceiling.setBody(BodyBuilder.get()
				.type(BodyType.StaticBody)
				.position(levelWidth/2, UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT/2) - 1)
				.addFixture("level", "ceiling").box(levelWidth, 0.2f)
				.create());
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

	public GameActor getGround() {
		return ground;
	}

	public GameActor getCeiling() {
		return ceiling;
	}
}
