package com.apptogo.roperace.actors;

import com.apptogo.roperace.game.GameActor;
import com.apptogo.roperace.game.ImmaterialGameActor;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.screen.GameScreen;
import com.apptogo.roperace.tools.UnitConverter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Viewfinder extends ImmaterialGameActor {

	private static final float VIEWFINDER_INITIAL_RADIUS = 2;

	private GameScreen screen;
	private GameActor player;
	private float angle;

	public Viewfinder(GameScreen screen, GameActor player) {
		super("viewfinder");
		this.screen = screen;
		this.player = player;

		setStaticImage("viewfinder");
		getCurrentAnimation().scaleFrames(1);
		screen.getSteeringHudStage().addActor(this);
		setSize(getCurrentAnimation().getWidth(),getCurrentAnimation().getHeight());
		setPosition(0,0);
	}

	public float getAngle() {
		Vector3 unprojected = screen.getSteeringHudStage().getCamera().unproject(new Vector3(getX() + Main.SCREEN_WIDTH/2 + getWidth()/2, -getY() + Main.SCREEN_HEIGHT/2 - getHeight()/2, 0));
		float camX = screen.getFrontStage().getCamera().position.x;
		float camY = screen.getFrontStage().getCamera().position.y;
		float unprojectedX = UnitConverter.toBox2dUnits(unprojected.x);
		float unprojectedY = UnitConverter.toBox2dUnits(unprojected.y);
		Vector2 viewfinderWorldPosition = new Vector2(camX + unprojectedX, camY + unprojectedY);
		float angle = viewfinderWorldPosition.sub(player.getBody().getPosition()).angle();
		return angle;
	}
}
