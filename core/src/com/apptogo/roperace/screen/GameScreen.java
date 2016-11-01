package com.apptogo.roperace.screen;

import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.physics.ContactListener;
import com.apptogo.roperace.scene2d.Image;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class GameScreen extends BasicScreen {
	protected static final float GRAVITY = -145;

	protected Box2DDebugRenderer debugRenderer;
	protected FPSLogger logger = new FPSLogger();

	protected World world;
	protected ContactListener contactListener = new ContactListener();

	public GameScreen(Main game) {
		super(game);
	}

	@Override
	protected void prepare() {

		debugRenderer = new Box2DDebugRenderer();

		world = new World(new Vector2(0, GRAVITY), true);
		world.setContactListener(contactListener);

		createBackStage();
		createFrontStage();

		//prepare CustomActionManager
		frontStage.addActor(CustomActionManager.getInstance());
	}

	@Override
	protected void step(float delta) {
		// --- backstage render first --- //
		
		//simulate physics and handle body contacts
		ContactListener.SNAPSHOT.clear();
		world.step(delta, 3, 3);

		//debug renderer
		//debugRenderer.render(world, gameworldStage.getCamera().combined);
		
		// --- frontstage render last --- //
	}

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


	protected void createBackStage() {
		Image background = Image.get("background").width(Main.SCREEN_WIDTH).position(0, -Main.SCREEN_HEIGHT / 2f).centerX();
		backStage.addActor(background);
	}

	protected void createFrontStage() {
		frontStage = new Stage();
		frontStage.setViewport(new FillViewport(UnitConverter.toBox2dUnits(Main.SCREEN_WIDTH), UnitConverter.toBox2dUnits(Main.SCREEN_HEIGHT)));
		//((OrthographicCamera) gameworldStage.getCamera()).zoom = 1f;
	}

	/**------ GETTERS / SETTERS ------**/

	public World getWorld() {
		return world;
	}
}
