package com.apptogo.roperace.screen;

import com.apptogo.roperace.actors.Hoop;
import com.apptogo.roperace.enums.BallData;
import com.apptogo.roperace.game.EndScreenGroup;
import com.apptogo.roperace.game.GameActor;
import com.apptogo.roperace.game.HudLabel;
import com.apptogo.roperace.game.StartGameGroup;
import com.apptogo.roperace.level.LevelData;
import com.apptogo.roperace.level.LevelGenerator;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.physics.BodyBuilder;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.plugin.CameraFollowingPlugin;
import com.apptogo.roperace.plugin.GameEventsPlugin;
import com.apptogo.roperace.plugin.GravityPlugin;
import com.apptogo.roperace.plugin.SoundPlugin;
import com.apptogo.roperace.plugin.SteeringPlugin;
import com.apptogo.roperace.plugin.TouchSteeringPlugin;
import com.apptogo.roperace.save.SaveManager;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends BasicScreen {

	protected Box2DDebugRenderer debugRenderer;
	protected FPSLogger logger = new FPSLogger();

	protected World world;
	protected ContactListener contactListener = new ContactListener();


	protected GameActor player;
	protected GameActor ground, ceiling;
	protected BallData ball = BallData.NORMAL;

	protected int levelNo;
	protected int worldNo;
	protected LevelGenerator levelGenerator;
	protected LevelData levelData;

	protected Stage hudStage;
	protected Viewport hudViewport;
	protected Stage steeringHudStage;
	protected Viewport steeringHudViewport;
	protected Stage labelStage;
	protected Viewport labelViewport;
	protected SteeringPlugin steeringPlugin;
	protected CameraFollowingPlugin cameraFollowingPlugin;
	protected HudLabel hudLabel;
	protected Hoop hoop;
	private StartGameGroup startGameGroup;
	private GravityPlugin gravityPlugin;
	private SoundPlugin soundPlugin;
	private GameEventsPlugin gameEventsPlugin;

	private float ballDefaultGraphicRadius = 107;

	public GameScreen(int levelNo, int worldNo) {
		super();
		this.levelNo = levelNo;
		this.worldNo = worldNo;
	}

	public GameScreen() {
		super();
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ---------------------------------------------- PREPARATION ----------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	@Override
	protected void prepare() {

		debugRenderer = new Box2DDebugRenderer();

		world = new World(new Vector2(0, -15), true);
		world.setContactListener(contactListener);

		prepareFrontStage();
		prepareSteeringHudStage();
		prepareHudStage();
		prepareLabelStage();
		createLevel();
		createPlayer();
		createLabel();
		createStartGameGroup();
		createEndScreenGroup();
	}

	protected void createStartGameGroup() {
		startGameGroup = new StartGameGroup(levelData);
		hudStage.addActor(startGameGroup);
		startGameGroup.init();
	}

	protected void createEndScreenGroup(){
		EndScreenGroup endScreenGroup = new EndScreenGroup(hudLabel, hoop, levelNo, worldNo);
		hudStage.addActor(endScreenGroup);
	}

	protected void createLabel() {
		hudLabel = new HudLabel(levelData, player);
		hudStage.addActor(hudLabel);
	}

	protected void prepareFrontStage() {
		this.frontViewport = new FillViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT));
		frontStage.setViewport(frontViewport);
	}

	protected void prepareLabelStage() {
		this.labelViewport = new FillViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		this.labelStage = new Stage(this.labelViewport);
	}

	protected void prepareHudStage() {
		this.hudViewport = new FitViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		this.hudStage = new Stage(this.hudViewport);
		((OrthographicCamera) hudStage.getCamera()).position.set(0f, 0f, 0f);

		inputMultiplexer.addProcessor(hudStage);
	}

	protected void prepareSteeringHudStage() {
		this.steeringHudViewport = new FillViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		this.steeringHudStage = new Stage(this.steeringHudViewport);
		((OrthographicCamera) steeringHudStage.getCamera()).position.set(0f, 0f, 0f);

		inputMultiplexer.addProcessor(steeringHudStage);
	}

	protected void createPlayer(){

		int activeBallNumber = SaveManager.getInstance().getActiveBall();
		this.ball = BallData.valueOf(activeBallNumber);

		String ballName = BallData.valueOf(activeBallNumber).name().toLowerCase();
		player = new GameActor(ballName);
		player.setBody(BodyBuilder.get()
				.type(BodyType.DynamicBody)
				.position(levelGenerator.getStartingPoint())
				.addFixture("player").circle(ball.size).density(ball.density).friction(ball.friction).restitution(ball.restitution)
				.create());
		player.getBody().setLinearDamping(ball.damping);
		player.getBody().setAngularDamping(0.5f);
		player.setStaticImage(ballName);
		player.getCurrentAnimation().scaleFrames(ball.size/ballDefaultGraphicRadius);
		player.setOrigin(ball.size/ballDefaultGraphicRadius/2, ball.size/ballDefaultGraphicRadius/2);
		player.modifyCustomOffsets(0f, 0f);
		frontStage.addActor(player);

		cameraFollowingPlugin = new CameraFollowingPlugin(levelGenerator.getMapSize());
		steeringPlugin = new TouchSteeringPlugin(this);
		gravityPlugin = new GravityPlugin();
		soundPlugin = new SoundPlugin("basket", "rubber", "bubble", "beach", "bowling", "normal");
		gameEventsPlugin = new GameEventsPlugin(this);

		player.addPlugin(steeringPlugin);
		player.addPlugin(cameraFollowingPlugin);
		player.addPlugin(gravityPlugin);
		player.addPlugin(soundPlugin);
		player.addPlugin(gameEventsPlugin);
	}

	protected void createLevel(){
		levelGenerator = new LevelGenerator(this);
		levelGenerator.loadLevel(levelNo, worldNo);
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------------- STEP -------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	@Override
	protected void step(float delta) {
		// --- backstage render first --- //
		//debug renderer
		if(!Main.isAndroid()){
//			debugRenderer.render(world, frontStage.getCamera().combined);
		}
		//simulate physics and handle body contacts
		ContactListener.SNAPSHOT_BEGIN.clear();
		ContactListener.SNAPSHOT_END.clear();
		world.step(delta, 3, 3);

		// --- frontstage render last --- //
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(currentColorSet.getMainColor().r, currentColorSet.getMainColor().g, currentColorSet.getMainColor().b, currentColorSet.getMainColor().a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.backStage.act(delta);

		this.hudStage.act(delta);
		this.steeringHudStage.act(delta);
		this.labelStage.act(delta);


		this.frontStage.act(delta);
		this.cameraFollowingPlugin.updateCamera();
		this.backViewport.apply();
		this.backStage.draw();



		this.frontViewport.apply();
		this.levelGenerator.render();

		this.labelViewport.apply();
		this.labelStage.draw();
		this.frontStage.draw();

		step(delta);
		handleInput();

		this.hudViewport.apply();
		this.hudStage.draw();
		this.steeringHudViewport.apply();
		this.steeringHudStage.draw();


		if(!gravityPlugin.isStarted() && startGameGroup.isFinished()){
			gravityPlugin.setStarted(true);
			hudLabel.setCounting(true);
		}

		if(hoop.isFinished()){
			inputMultiplexer.removeProcessor(steeringHudStage);
		}
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------ DISPOSE ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		this.hudStage.getViewport().update(width, height);
		this.steeringHudStage.getViewport().update(width, height);
		this.labelStage.getViewport().update(width, height);
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

	public Stage getSteeringHudStage() {
		return steeringHudStage;
	}

	public LevelData getLevelData() {
		return levelData;
	}

	public void setLevelData(LevelData levelData) {
		this.levelData = levelData;
	}

	public Hoop getHoop() {
		return hoop;
	}

	public void setHoop(Hoop hoop) {
		this.hoop = hoop;
	}

	public HudLabel getHudLabel() {
		return hudLabel;
	}

	public GameActor getPlayer() {
		return player;
	}

	public Stage getLabelStage() {
		return labelStage;
	}

	public BallData getBall() {  return ball;  }
}
