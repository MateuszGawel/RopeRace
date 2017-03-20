package com.apptogo.roperace.screen;

import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.save.SaveManager;
import com.apptogo.roperace.scene2d.ColorSet;
import com.apptogo.roperace.scene2d.Image;
import com.apptogo.roperace.scene2d.Listener;
import com.apptogo.roperace.scene2d.ShadowedButton;
import com.apptogo.roperace.scene2d.ShadowedButton.ButtonSize;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class BallSelectionScreen extends BasicScreen {

	private Table table;
	private ScrollPane scrollPane;

	public BallSelectionScreen() {
		super();
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ---------------------------------------------- PREPARATION ----------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	@Override
	protected void prepare() {
		prepareBackStage();
		prepareFrontStage();
		prepareScrollPane();
	}

	protected void prepareFrontStage() {
		frontViewport = new FillViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		frontStage.setViewport(frontViewport);
	}
	
	protected void prepareBackStage(){
		float small_padding = 20;
		
		ShadowedButton backButton = new ShadowedButton("back-button", currentColorSet, ButtonSize.SMALL);
		backButton.addListener(Listener.click(game, new MenuScreen()));
		backButton.setPosition(Main.SCREEN_WIDTH / 2 - backButton.getWidth() - small_padding, -Main.SCREEN_HEIGHT/2 + small_padding);
		backStage.addActor(backButton);
		
		backViewport = new FitViewport(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		backStage.setViewport(backViewport);
		inputMultiplexer.addProcessor(backStage);
	}

	private void prepareScrollPane() {
		float padding = 50;

		TextureRegion dummyImage = Image.getFromTexture("circle").getRegion();

		table = new Table();
		table.row().pad(0, padding, 0, padding);

		for (int i = 1; i <= 4; i++) {
			Image ball = Image.getFromTexture("ball"+i);

			
			Cell<Image> cell = table.add(ball);
			if (i == 1) {
				cell.pad(0, Main.SCREEN_WIDTH / 2 - ball.getWidth() / 2, 0, padding);
			}
		}

		scrollPane = new ScrollPane(table);
		scrollPane.setScrollingDisabled(false, true);
		scrollPane.setFadeScrollBars(false);
		scrollPane.setSize(Main.SCREEN_WIDTH, dummyImage.getRegionHeight());
		scrollPane.setPosition(-Main.SCREEN_WIDTH / 2, -dummyImage.getRegionWidth() / 2);
		frontStage.addActor(scrollPane);
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
			game.setScreen(new WorldSelectionScreen());
		}
	}
}
