package com.apptogo.roperace.plugin;

import com.apptogo.roperace.actors.Rope;
import com.apptogo.roperace.actors.Viewfinder;
import com.apptogo.roperace.custom.MyTouchpad;
import com.apptogo.roperace.custom.MyTouchpad.TouchpadStyle;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.scene2d.Image;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public abstract class SteeringPlugin extends AbstractPlugin {

	private GameScreen screen;

	protected Rope rope;
	protected Viewfinder viewfinder;
	protected MyTouchpad touchpad;

	public SteeringPlugin(GameScreen screen) {
		this.screen = screen;
		createTouchpad();
	}

	@Override
	public void postSetActor() {
		viewfinder = new Viewfinder(screen, actor, touchpad);
		rope = new Rope(screen, actor);
	}

	private void createTouchpad() {
		Drawable knob = Image.getFromTexture("touchpad-knob").getDrawable();
		Drawable background = Image.getFromTexture("touchpad-background").getDrawable();
		float margin = 20;

		touchpad = new MyTouchpad(2, new TouchpadStyle(background, knob));
		touchpad.setBounds(0, 0, background.getMinWidth(), background.getMinHeight());
		touchpad.setPosition(Main.SCREEN_WIDTH / 2 - touchpad.getWidth() - margin, -Main.SCREEN_HEIGHT / 2 + margin);
		touchpad.setForcedRadius(1.5f);
		screen.getHudStage().addActor(touchpad);
	}

	@Override
	public void dispose() {
		rope.destroyCurrentJoint();
	}

	public Rope getRope() {
		return rope;
	}
	
}
