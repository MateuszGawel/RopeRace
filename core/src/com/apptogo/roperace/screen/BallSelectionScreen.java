package com.apptogo.roperace.screen;

import com.apptogo.roperace.enums.ColorSet;
import com.apptogo.roperace.game.UnlockScreenGroup;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.save.SaveManager;
import com.apptogo.roperace.scene2d.BallButton;
import com.apptogo.roperace.scene2d.Image;
import com.apptogo.roperace.scene2d.Listener;
import com.apptogo.roperace.scene2d.ShadowedButton;
import com.apptogo.roperace.scene2d.ShadowedButton.ButtonSize;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class BallSelectionScreen extends BasicScreen {

	public static final int BALL_COST = 50;
	
	private static final int BALL_COUNT = 6;
	
	private Table table;
	private ScrollPane scrollPane;
	private UnlockScreenGroup unlockBallScreenGroup;

	public BallSelectionScreen() {
		super();
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ---------------------------------------------- PREPARATION ----------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	@Override
	protected void prepare() {
		prepareFrontStage();
		prepareScrollPane();
		prepareUnlockBallScreen();
	}
	
	private void prepareUnlockBallScreen() {
		unlockBallScreenGroup = new UnlockScreenGroup("Unlock ball");
		frontStage.addActor(unlockBallScreenGroup);
	}

	protected void prepareFrontStage(){
		float small_padding = 20;
		
		ShadowedButton backButton = new ShadowedButton("back-button", currentColorSet, ButtonSize.SMALL);
		backButton.addListener(Listener.click(game, new MenuScreen()));
		backButton.setPosition(Main.SCREEN_WIDTH / 2 - backButton.getWidth() - small_padding, -Main.SCREEN_HEIGHT/2 + small_padding);
		frontStage.addActor(backButton);
		
		inputMultiplexer.addProcessor(backStage);
	}

	private void prepareScrollPane() {
		float padding = 50;

		TextureRegion dummyImage = Image.get("circle").getRegion();

		table = new Table();
		table.row().pad(0, padding, 0, padding);

		for (int i = 1; i <= BALL_COUNT; i++) {
			final BallButton ball = new BallButton(i, currentColorSet);
			ball.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (ball.isUnlocked()) {
						getBallButton(SaveManager.getInstance().getActiveBall()).setActive(false);
						ball.setActive(true);
					} else {
						((BallSelectionScreen) Main.getInstance().getCurrentScreen()).getBallScreenGroup().init(ball.getNumber(), BallSelectionScreen.BALL_COST);
					}
				}
			});
			Cell<BallButton> cell = table.add(ball);
			if (i == 1) {
				cell.pad(0, Main.SCREEN_WIDTH / 2 - ball.getWidth() / 2, 0, padding);
			}
		}

		scrollPane = new ScrollPane(table);
		scrollPane.setScrollingDisabled(false, true);
		scrollPane.setFadeScrollBars(false);
		scrollPane.setSize(Main.SCREEN_WIDTH, dummyImage.getRegionHeight());
		scrollPane.setPosition(-Main.SCREEN_WIDTH / 2, -dummyImage.getRegionWidth() / 2);
		backStage.addActor(scrollPane);
	}

	public UnlockScreenGroup getBallScreenGroup() {
		return unlockBallScreenGroup;
	}
	
	@Override
	public void unlockAction(int ballNumber, final int cost){
		getBallButton(ballNumber).unlock();
		transferPoints(cost);
	}
	private BallButton getBallButton(int number){
		return (BallButton)table.getCells().get(number-1).getActor();
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
			game.setScreen(new MenuScreen());
		}
	}


}
