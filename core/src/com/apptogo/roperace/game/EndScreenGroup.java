package com.apptogo.roperace.game;

import com.apptogo.roperace.actors.Hoop;
import com.apptogo.roperace.custom.MyShapeRenderer;
import com.apptogo.roperace.custom.MyShapeRenderer.ShapeType;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.scene2d.ColorSet;
import com.apptogo.roperace.scene2d.Label;
import com.apptogo.roperace.scene2d.ShadowedButton;
import com.apptogo.roperace.screen.BasicScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class EndScreenGroup extends Group {

	private float margin = 200;
	private MyShapeRenderer shapeRenderer;
	private BasicScreen currentScreen;
	private HudLabel hudLabel;
	private Hoop hoop;
	private boolean showed;
	
	public EndScreenGroup(HudLabel hudLabel, Hoop hoop) {
		debug();
		setSize(Main.SCREEN_WIDTH - margin, Main.SCREEN_HEIGHT - margin);
		setPosition(-Main.SCREEN_WIDTH / 2 + margin / 2, -10000);
		shapeRenderer = new MyShapeRenderer();
		currentScreen = Main.getInstance().getCurrentScreen();
		this.hudLabel = hudLabel;
		this.hoop = hoop;
	}

	public void init() {
		MoveToAction action = new MoveToAction();
		action.setPosition(-Main.SCREEN_WIDTH / 2 + margin / 2, -Main.SCREEN_HEIGHT / 2 + margin / 2);
		action.setDuration(1f);
		action.setInterpolation(Interpolation.elasticOut);
		this.addAction(action);

		if(!hudLabel.isGameOver()){
			createMedal(hudLabel.getMedalColor());
		}
		createLabel();
	}

	private void createMedal(ColorSet colorSet) {
		ShadowedButton medal = new ShadowedButton("medal", colorSet);
		medal.setPosition(Main.SCREEN_WIDTH / 2 - margin / 2 - medal.getWidth() / 2, Main.SCREEN_HEIGHT - margin / 2 - 400 - medal.getHeight() / 2);
		medal.applyColorToContent(colorSet);
		this.addActor(medal);
	}

	private void createLabel() {
		String labelText = "";
		if(hudLabel.isGameOver())
			labelText = "Game Over";
		else
			labelText = "Congratulations";
		Label scoreLabel = Label.get(labelText, "big");
		
		scoreLabel.position(Main.SCREEN_WIDTH / 2 - margin / 2 - scoreLabel.getWidth() / 2, Main.SCREEN_HEIGHT - margin / 2 - 200 - scoreLabel.getHeight() / 2);
		scoreLabel.setColor(currentScreen.getCurrentColorSet().getMainColor());
		this.addActor(scoreLabel);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if((hudLabel.isGameOver() || hoop.isFinished()) && !showed){
			init();
			showed = true;
			hudLabel.setCounting(false);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(getX() + margin / 2 + getWidth() / 2, getY() + margin / 2 + getHeight() / 2, getWidth(), getHeight());
		shapeRenderer.end();
		batch.begin();
		super.draw(batch, parentAlpha);
	}

	public Hoop getHoop() {
		return hoop;
	}

	public void setHoop(Hoop hoop) {
		this.hoop = hoop;
	}

}
