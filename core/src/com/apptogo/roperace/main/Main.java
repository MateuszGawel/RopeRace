package com.apptogo.roperace.main;


import com.apptogo.roperace.callback.GameCallback;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.manager.ParticlesManager;
import com.apptogo.roperace.manager.ResourcesManager;
import com.apptogo.roperace.save.SaveManager;
import com.apptogo.roperace.screen.BasicScreen;
import com.apptogo.roperace.screen.SplashScreen;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

/**
 * Main class of the game. All constants can be defined here.	
 */
public class Main extends Game {
	public static final float SCREEN_WIDTH = 1280f, SCREEN_HEIGHT = 768f;
	
	public static GameCallback gameCallback;
	private Screen screenToSet = null;
	
	public static Main getInstance() {
		return (Main) Gdx.app.getApplicationListener();
	}

	public Main(GameCallback gameCallback) {
		super();
		Main.gameCallback = gameCallback;
	}

	public BasicScreen getCurrentScreen() {
		return (BasicScreen) getScreen();
	}

	public static boolean isAndroid(){
		return gameCallback.isAndroid();
	}
	
	@Override
	public void setScreen(Screen screen) {
		screenToSet = screen;
	}

	public void doSetScreen(Screen screen) {
		if (this.screen != null) {
			this.screen.dispose();
		}
		super.setScreen(screen);
	}

	@Override
	public void create() {
		//use this to define log level. It overrides local settings
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		//set handle back button
		Gdx.input.setCatchKey(Input.Keys.BACK, true);

		ResourcesManager.create();
		CustomActionManager.create();
		ResourcesManager.getInstance().loadResources();
		SaveManager.create();
		ParticlesManager.create();
		ParticlesManager.getInstance().initParticles(ResourcesManager.getInstance().getAtlas());
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render() {
		super.render();
		
		if(screenToSet != null){
			doSetScreen(screenToSet);
			screenToSet = null;
		}
	}

	@Override
	public void dispose() {
		ResourcesManager.destroy();
		CustomActionManager.destroy();
		SaveManager.destroy();
	}
}
