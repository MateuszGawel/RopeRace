package com.apptogo.roperace.screen;

import com.apptogo.roperace.main.Main;
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

public class LevelSelectionScreen extends BasicScreen {

	private static final int NUMBER_OF_LEVELS = 10;

	public LevelSelectionScreen(Main game) {
		super(game);
	}

	private Table table;
	private ScrollPane scrollPane;

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ---------------------------------------------- PREPARATION ----------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	@Override
	protected void prepare() {
		prepareFrontStage();
		prepareScrollPane();
	}

	protected void prepareFrontStage() {
		float small_padding = 20;
		
		ShadowedButton backButton = new ShadowedButton("back-button", currentColorSet, ButtonSize.SMALL);
		backButton.addListener(Listener.click(game, new WorldSelectionScreen(game)));
		backButton.setPosition(Main.SCREEN_WIDTH / 2 - backButton.getWidth() - small_padding, -Main.SCREEN_HEIGHT/2 + small_padding);
		frontStage.addActor(backButton);

	}

	private void prepareScrollPane() {
		float padding = 50;

		TextureRegion dummyImage = Image.getFromTexture("circle").getRegion();

		table = new Table();
		table.row().pad(0, padding, 0, padding);

		for (int i = 1; i < NUMBER_OF_LEVELS; i++) {
			
			ShadowedButton button = new ShadowedButton(String.valueOf(i), ColorSet.BLUE, ButtonSize.BIG);
			button.addListener(Listener.click(game, new GameScreen(game, i)));
			Cell<ShadowedButton> cell = table.add(button);
			if (i == 1) {
				cell.pad(0, Main.SCREEN_WIDTH / 2 - button.getWidth() / 2, 0, padding);
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
			game.setScreen(new WorldSelectionScreen(game));
		}
	}
}
