package com.apptogo.roperace.plugin;

import com.apptogo.roperace.enums.Powerup;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.save.SaveManager;
import com.apptogo.roperace.scene2d.PowerupButton;
import com.apptogo.roperace.scene2d.ShadowedButton;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PowerupPlugin extends AbstractPlugin {

	public PowerupPlugin() {
		Powerup activePowerup = SaveManager.getInstance().getActivePowerup();
		if(activePowerup != null) {
			GameScreen currentScreen = (GameScreen) Main.getInstance().getCurrentScreen();
			final PowerupButton powerupButton = new PowerupButton(currentScreen.getCurrentColorSet(), ShadowedButton.ButtonSize.BIG, activePowerup);
			powerupButton.initChargesGraphic();
			int count = SaveManager.getInstance().getActivePowerupCount();
			powerupButton.setActiveCharges(count);
			powerupButton.setScale(0.5f);
			powerupButton.setPosition(Main.SCREEN_WIDTH/2 - powerupButton.getWidth()*powerupButton.getScaleX() - 50, -Main.SCREEN_HEIGHT/2 + 50);
			powerupButton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					powerupButton.reduceCharge();
				}
			});
			currentScreen.getHudStage().addActor(powerupButton);
		}
	}

	@Override
	public void setUpDependencies() {

	}

	@Override
	public void run() {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}