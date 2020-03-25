package com.apptogo.roperace.screen;

import com.apptogo.roperace.enums.ColorSet;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.CustomAction;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.save.SaveManager;
import com.apptogo.roperace.scene2d.Label;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class BasicScreen implements Screen {
	//main instance of game
	protected Main game;

	protected Stage backStage;
	protected Viewport backViewport;

	protected Viewport frontViewport;
	protected Stage frontStage;

	protected InputMultiplexer inputMultiplexer;
	
	protected ColorSet currentColorSet = ColorSet.BLUE;

	protected CustomAction transferPointsAction;

	private Label scoreLabel;
	
	public BasicScreen() {
		this.game = Main.getInstance();
	}

	@Override
	public void show() {
		this.backViewport = new FillViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		this.backStage = new Stage(this.backViewport);
		((OrthographicCamera) backStage.getCamera()).position.set(0f, 0f, 0f);

		this.frontViewport = new FitViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		this.frontStage = new Stage(this.frontViewport);
		((OrthographicCamera) frontStage.getCamera()).position.set(0f, 0f, 0f);

		inputMultiplexer = new InputMultiplexer(); 
		inputMultiplexer.addProcessor(frontStage);
		Gdx.input.setInputProcessor(inputMultiplexer);

		prepare();

		//prepare CustomActionManager
		backStage.addActor(CustomActionManager.getInstance());
		
		createLabel();
	}

	private void createLabel() {
		scoreLabel = Label.get(String.valueOf(SaveManager.getInstance().getPoints()), "big");
		scoreLabel.position(Main.SCREEN_WIDTH/2-scoreLabel.getWidth()-10, Main.SCREEN_HEIGHT/2 - 70);
		scoreLabel.setColor(ColorSet.PURPLE.getMainColor());
		frontStage.addActor(scoreLabel);

	}
	
	protected void setScoreValue(int value) {
		if (!scoreLabel.isVisible())
			scoreLabel.setVisible(true);
		scoreLabel.setText(String.valueOf(value));
	}
	
	public int getScoreValue() {
		if (scoreLabel == null)
			return 0;
		return Integer.valueOf(scoreLabel.getText().toString());
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
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

	protected void handleInput() {
		if (Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.setScreen(new MenuScreen());
		}
	}

	@Override
	public void resize(int width, int height) {
		this.backStage.getViewport().update(width, height);
		this.frontStage.getViewport().update(width, height);
	}

	abstract protected void prepare();

	abstract protected void step(float delta);

	
	
	protected void transferPoints(int cost){
		SaveManager.getInstance().usePoints(cost);
		
		if (cost > 0) {
			transferPointsAction = new CustomAction(0.01f, cost) {

				@Override
				public void perform() {
					setScoreValue(getScoreValue() - 1);
				}

			};
			CustomActionManager.getInstance().registerAction(transferPointsAction);
		}
	}
	public void unlockAction(int worldNumber, int cost) {
		//to implement in child class
	}
	
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		backStage.dispose();
		frontStage.dispose();
		CustomActionManager.getInstance().clearAllActions();
	}

	public Stage getFrontStage() {
		return frontStage;
	}

	public Stage getBackStage() {
		return backStage;
	}

	public InputMultiplexer getInputMultiplexer() {
		return inputMultiplexer;
	}

	public ColorSet getCurrentColorSet() {
		return currentColorSet;
	}


}
