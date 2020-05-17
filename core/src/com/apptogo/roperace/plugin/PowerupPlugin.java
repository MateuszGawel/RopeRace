package com.apptogo.roperace.plugin;

import com.apptogo.roperace.actors.Rope;
import com.apptogo.roperace.enums.Powerup;
import com.apptogo.roperace.exception.PluginException;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.save.SaveManager;
import com.apptogo.roperace.scene2d.PowerupButton;
import com.apptogo.roperace.scene2d.ShadowedButton;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PowerupPlugin extends AbstractPlugin {

    private PowerupButton powerupButtonClicked;
    private Rope rope;
    private GameEventsPlugin gameEventsPlugin;

	public PowerupPlugin() {
        createPowerupButton();
    }

    private void createPowerupButton() {
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
                    powerupButtonClicked = powerupButton;
                }
            });
            currentScreen.getHudStage().addActor(powerupButton);
        }
    }

    private void usePowerup(PowerupButton powerupButton) {
        Powerup powerup = powerupButton.getPowerup();
        if(!powerupButton.hasActiveCharges()){
            return;
        }

        boolean executed = false;
        switch(powerup){
            case JUMP:
                executed = jump();
                break;
            case SLIPPY:
                break;
        }
        if(executed) {
            powerupButton.reduceCharge();
        }
        powerupButtonClicked = null;
    }

    private boolean jump() {
        if(rope == null || rope.isRopeAttached()){
	        return false;
        }
	    if(gameEventsPlugin.isPlayerCollidedWithLevel()){
            return false;
        }
        Body body = actor.getBody();
        body.setLinearVelocity(body.getLinearVelocity().x, 0);
        body.applyLinearImpulse(new Vector2(0, body.getMassData().mass * 12), body.getWorldCenter(), true);
        return true;
    }

    @Override
	public void setUpDependencies() {
        try{
            rope = actor.getPlugin(TouchSteeringPlugin.class).getRope();
        }
        catch(PluginException e){
            rope =  actor.getPlugin(KeyboardSteeringPlugin.class).getRope();
        }
        gameEventsPlugin = actor.getPlugin(GameEventsPlugin.class);
	}

	@Override
	public void run() {
        if(powerupButtonClicked != null){
            usePowerup(powerupButtonClicked);
        }
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}