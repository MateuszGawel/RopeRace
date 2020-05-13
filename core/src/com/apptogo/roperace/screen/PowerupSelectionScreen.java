package com.apptogo.roperace.screen;

import com.apptogo.roperace.enums.Powerup;
import com.apptogo.roperace.game.UnlockScreenGroup;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.save.SaveManager;
import com.apptogo.roperace.scene2d.Listener;
import com.apptogo.roperace.scene2d.PowerupButton;
import com.apptogo.roperace.scene2d.ShadowedButton;
import com.apptogo.roperace.scene2d.ShadowedButton.ButtonSize;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.HashMap;
import java.util.Map;

public class PowerupSelectionScreen extends BasicScreen {
	private static final float SMALL_PADDING = 20;
	private static final float BIG_PADDING = 100;
	private Map<Powerup, Integer> boughtPowerups;
	private Map<Powerup, PowerupButton> powerupButtons = new HashMap<>();
	private UnlockScreenGroup unlockPowerupScreenGroup;

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ---------------------------------------------- PREPARATION ----------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	protected void prepare() {
		boughtPowerups = SaveManager.getInstance().getBoughtPowerups();
		prepareBackStage();
		prepareFrontStage();
		prepareUnlockPowerupScreen();
	}

	private void prepareUnlockPowerupScreen() {
		unlockPowerupScreenGroup = new UnlockScreenGroup("Unlock powerup");
		frontStage.addActor(unlockPowerupScreenGroup);
	}

	protected void prepareBackStage() {
		//		Image background = Image.get("background").width(Main.SCREEN_WIDTH).position(0, -Main.SCREEN_HEIGHT / 2f).centerX();
		//		backStage.addActor(background);
	}

	protected void prepareFrontStage() {		
		ShadowedButton backButton = new ShadowedButton("back-button", currentColorSet, ButtonSize.SMALL);
		backButton.addListener(Listener.click(game, new MenuScreen()));
		backButton.setPosition(Main.SCREEN_WIDTH / 2 - backButton.getWidth() - SMALL_PADDING, -Main.SCREEN_HEIGHT/2 + SMALL_PADDING);
		frontStage.addActor(backButton);

		preparePowerupButton(Powerup.JUMP);
		preparePowerupButton(Powerup.SLIPPY);
	
	}
	
	private void refreshPowerupButton(Powerup powerup){
		PowerupButton currentButton = powerupButtons.get(powerup);
		PowerupButton newButton = preparePowerupButton(powerup);
		newButton.setPosition(currentButton.getX(), currentButton.getY());
		currentButton.remove();
	}

	private PowerupButton preparePowerupButton(final Powerup powerup){
		Map<Powerup, Integer> boughtPowerups = SaveManager.getInstance().getBoughtPowerups();
		final Integer powerupCount = boughtPowerups.get(powerup);

		final PowerupButton powerupButton = new PowerupButton(powerup.regionName, currentColorSet, ButtonSize.BIG);
		powerupButton.setPosition(-Main.SCREEN_WIDTH/2 - powerupButton.getWidth() + powerup.number * BIG_PADDING + powerup.number * powerupButton.getWidth(), Main.SCREEN_HEIGHT/2 - powerupButton.getHeight() - BIG_PADDING);

		powerupButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(powerupCount != null && powerupCount >= 3){
					unlockPowerupScreenGroup.setBuyImpossible("You already have 3 charges");
				}
				else{
					unlockPowerupScreenGroup.setBuyPossible();
				}
				unlockPowerupScreenGroup.toFront();
				unlockPowerupScreenGroup.init(powerup.number, powerup.cost, powerup.displayName, powerup.description);
			}
		});

		powerupButton.initChargesGraphic();
		if(powerupCount != null){
			powerupButton.setActiveCharges(powerupCount);
		}

		frontStage.addActor(powerupButton);
		powerupButtons.put(powerup, powerupButton);

		return powerupButton;
	}

	@Override
	public void unlockAction(int number, final int cost){
		Powerup powerup = Powerup.valueOf(number);
		transferPoints(cost);
		boughtPowerups = SaveManager.getInstance().buyPowerup(powerup);
		refreshPowerupButton(powerup);
		System.out.println("mam " + powerup + " sztuk " + boughtPowerups.get(powerup));
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
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------- GETTERS / SETTERS --------------------------------------------**/
	/** ---------------------------------------------------------------------------------------------------------- **/

}
