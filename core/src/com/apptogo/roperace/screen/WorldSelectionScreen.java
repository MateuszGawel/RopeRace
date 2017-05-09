package com.apptogo.roperace.screen;

import java.util.HashMap;
import java.util.Map;

import com.apptogo.roperace.game.UnlockScreenGroup;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.CustomAction;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.save.SaveManager;
import com.apptogo.roperace.scene2d.ColorSet;
import com.apptogo.roperace.scene2d.Listener;
import com.apptogo.roperace.scene2d.ShadowedButton;
import com.apptogo.roperace.scene2d.ShadowedButton.ButtonSize;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WorldSelectionScreen extends BasicScreen {
	private UnlockScreenGroup unlockWorldScreenGroup;
	private static final float SMALL_PADDING = 20;
	private static final float BIG_PADDING = 100;
	private Map<Integer, ShadowedButton> worldButtons = new HashMap<Integer, ShadowedButton>();
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ---------------------------------------------- PREPARATION ----------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/
	
	@Override
	protected void prepare() {
		prepareBackStage();
		prepareFrontStage();
		prepareUnlockWorldScreen();
	}

	private void prepareUnlockWorldScreen() {
		unlockWorldScreenGroup = new UnlockScreenGroup("Unlock world");
		frontStage.addActor(unlockWorldScreenGroup);
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

		prepareWorldButton(1, ColorSet.BLUE, 100);
		prepareWorldButton(2, ColorSet.GREEN, 100);
	
	}
	
	private void refreshWorldButton(int worldNumber, int cost){
		ShadowedButton currentButton = worldButtons.get(worldNumber);
		prepareWorldButton(worldNumber, currentButton.getColorSet(), cost);
		currentButton.remove();
	}
	
	private void prepareWorldButton(final int worldNumber, ColorSet colorSet, final int cost){
		ShadowedButton worldButton = new ShadowedButton(getWorldRegion(worldNumber), colorSet, ButtonSize.BIG);
		worldButton.setPosition(-Main.SCREEN_WIDTH/2 - worldButton.getWidth() + worldNumber*BIG_PADDING + worldNumber * worldButton.getWidth(), Main.SCREEN_HEIGHT/2 - worldButton.getHeight() - BIG_PADDING);
		if(SaveManager.getInstance().isWorldUnlocked(worldNumber)){
			worldButton.addListener(Listener.click(game, new LevelSelectionScreen(worldNumber, colorSet)));
		}
		else{
			worldButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					unlockWorldScreenGroup.init(worldNumber, cost);
				}
			});
		}
		frontStage.addActor(worldButton);
		worldButtons.put(worldNumber, worldButton);
	}
	
	private String getWorldRegion(int worldNumber){
		if(SaveManager.getInstance().isWorldUnlocked(worldNumber))
			return "world" + worldNumber;
		else
			return "locker";
	}
	
	@Override
	public void unlockAction(int worldNumber, final int cost){
		SaveManager.getInstance().unlockWorld(worldNumber);
		transferPoints(cost);
		refreshWorldButton(worldNumber, cost);
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
