package com.apptogo.roperace.scene2d;

import com.apptogo.roperace.enums.ColorSet;
import com.apptogo.roperace.enums.Powerup;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.save.SaveManager;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;

public class PowerupButton extends ShadowedButton {

    private final Powerup powerup;
    private List<Image> charges = new ArrayList<>();
    private ShadowedButton ok;

    public PowerupButton(String regionName, ColorSet currentColorSet, ButtonSize big, Powerup powerup) {
        super(regionName, currentColorSet, big);
        this.powerup = powerup;
    }

    public void initChargesGraphic(){
        for(int i=0; i<3; i++) {
            Image powerupCharge = Image.get("powerup-charge");
            TextureAtlas.AtlasRegion chargeRegion = ((TextureAtlas.AtlasRegion) powerupCharge.getRegion());
            float diff = (chargeRegion.originalWidth - circle.getWidth())/2;
            powerupCharge.position(chargeRegion.offsetX - diff, chargeRegion.offsetY - diff);
            powerupCharge.setColor(ColorSet.LIGHT_GRAY.getSecondaryColor());

            rotateBy(i*120, powerupCharge);

            addActor(powerupCharge);
            charges.add(powerupCharge);
        }
    }

    public void initOkButton(){
        ok = new ShadowedButton("ok", ColorSet.LIGHT_GRAY, ButtonSize.SMALL);
        ok.setScale(0.5f);
        ok.setPosition(getX() + getWidth()/2 - ok.getWidth()/2*ok.getScaleX(), getY() - ok.getHeight()/2 - 20);
        Main.getInstance().getCurrentScreen().getFrontStage().addActor(ok);

        if(SaveManager.getInstance().getActivePowerup() == powerup){
            toggleOk(true);
        }
    }

    public void toggleOk(boolean active){
        if(active){
            SaveManager.getInstance().setActivePowerup(powerup);
            ok.changeColor(colorSet);
        }
        else{
            ok.changeColor(ColorSet.LIGHT_GRAY);
        }

    }

    public void setActiveCharges(int count){
        if(count > 3){
            return;
        }
        for(int i = 0; i<count; i++){
            Image charge = charges.get(i);
            charge.setColor(colorSet.getMainColor());
        }
    }

    private void rotateBy(int i, Image powerupCharge) {
        float center = this.getWidth()/2;
        float x = powerupCharge.getX();
        float y = powerupCharge.getY();
        powerupCharge.setOrigin(center - x, center - y);
        powerupCharge.setRotation(i*5);
    }

    public void addOkListener(ClickListener listener){
        ok.addListener(listener);
    }

    public Powerup getPowerup() {
        return powerup;
    }
}
