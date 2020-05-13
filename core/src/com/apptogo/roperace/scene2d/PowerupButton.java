package com.apptogo.roperace.scene2d;

import com.apptogo.roperace.enums.ColorSet;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;
import java.util.List;

public class PowerupButton extends ShadowedButton {

    private List<Image> charges = new ArrayList<>();

    public PowerupButton(String regionName, ColorSet currentColorSet, ButtonSize big) {
        super(regionName, currentColorSet, big);
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

}
