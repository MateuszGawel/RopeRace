package com.apptogo.roperace.scene2d;

import com.apptogo.roperace.enums.BallData;
import com.apptogo.roperace.enums.ColorSet;
import com.apptogo.roperace.save.SaveManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class BallButton extends ShadowedButton {

	private Image ok;
	
	private int number = 0;
	private boolean unlocked;
	private boolean active;
	
	public BallButton(int number, ColorSet colorSet) {
		super();
		this.number = number;
		setName("ball" + number);
		this.colorSet = colorSet;
		
		this.shadow = Image.get("locker_shadow");
		AtlasRegion shadowRegion = ((AtlasRegion) this.shadow.getRegion());
		this.shadow.setColor(colorSet.getSecondaryColor());
		this.shadow.position(shadowRegion.offsetX, shadowRegion.offsetY);
		this.addActor(shadow);

		this.content = Image.get("locker");
		AtlasRegion contentRegion = ((AtlasRegion) this.content.getRegion());
		this.content.position(contentRegion.offsetX, contentRegion.offsetY);
		this.addActor(this.content);
		
		setSize(contentRegion.originalWidth, contentRegion.originalHeight);

		addCircleBackground(colorSet, ButtonSize.BIG);
		setSize(this.circle.getWidth(), this.circle.getHeight());
		
		
		this.ok = Image.get("ok");
		this.ok.position(getWidth()/2 - ok.getWidth()/2, getHeight()/2 - ok.getHeight()/2);
		this.ok.setVisible(false);
		this.ok.setColor(colorSet.getMainColor());
		this.addActor(this.ok);
		
		if(SaveManager.getInstance().isBallUnlocked(number)){
			unlock();
		}
		if(SaveManager.getInstance().isBallActive(number)){
			setActive(true);
		}
		
	}

	public void unlock() {
		this.circle.remove();
		
		this.circle = Image.get(BallData.valueOf(number).name().toLowerCase());

		this.addActor(circle);
		this.setWidth(circle.getWidth());
		this.setHeight(circle.getHeight());

		this.content.setVisible(false);
		this.shadow.setVisible(false);

		unlocked = true;
		SaveManager.getInstance().unlockBall(number);
	}


	public void setActive(boolean active) {
		if(this.active == active) return;
		
		this.active = active;

		
		if(active){
			SaveManager.getInstance().setActiveBall(number);
			this.ok.setVisible(true);
			this.ok.toFront();
		}
		else{
			this.ok.setVisible(false);
		}
	}

	public boolean isUnlocked() {
		return unlocked;
	}

	public int getNumber() {
		return number;
	}
}
