package com.apptogo.roperace.game;

import com.apptogo.roperace.actors.Hoop;
import com.apptogo.roperace.custom.MyShapeRenderer;
import com.apptogo.roperace.custom.MyShapeRenderer.ShapeType;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.scene2d.ColorSet;
import com.apptogo.roperace.scene2d.Image;
import com.apptogo.roperace.scene2d.Label;
import com.apptogo.roperace.scene2d.ShadowedButton;
import com.apptogo.roperace.scene2d.ShadowedButton.ButtonSize;
import com.apptogo.roperace.screen.BasicScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class EndScreenGroup extends Group {

	private float margin = 100;
	private MyShapeRenderer shapeRenderer;
	private BasicScreen currentScreen;
	private HudLabel hudLabel;
	private Hoop hoop;
	private boolean showed;
	private boolean success;
	
	public EndScreenGroup(HudLabel hudLabel, Hoop hoop) {
		setSize(Main.SCREEN_WIDTH - 2*margin, Main.SCREEN_HEIGHT - 2*margin);
		setPosition(-Main.SCREEN_WIDTH / 2 + margin, -10000);
		shapeRenderer = new MyShapeRenderer();
		currentScreen = Main.getInstance().getCurrentScreen();
		this.hudLabel = hudLabel;
		this.hoop = hoop;
	}

	public void init() {
		MoveToAction action = new MoveToAction();
		action.setPosition(-Main.SCREEN_WIDTH / 2 + margin, -Main.SCREEN_HEIGHT / 2 + margin);
		action.setDuration(1f);
		action.setInterpolation(Interpolation.elasticOut);
		this.addAction(action);
		
		if(!hudLabel.isGameOver() && hudLabel.getCurrentColorSet() != ColorSet.GRAY){
			success = true;
		}
		
		if(success)
			createMedal(hudLabel.getMedalColor());
		createTitle();
		createButtons();
		createPointLabels();
	}

	private void createPointLabels() {
		Image diamondImage = Image.getFromTexture("diamond");
		diamondImage.setSize(diamondImage.getWidth()*0.7f, diamondImage.getHeight()*0.7f);
		this.addActor(diamondImage);
		Label totalLabel = Label.get("2000", "big");
		totalLabel.setColor(ColorSet.PURPLE.getMainColor());
		this.addActor(totalLabel);		
		totalLabel.position(getWidth()/2 - totalLabel.getWidth()/2 + diamondImage.getWidth()/2, getHeight()/2 - totalLabel.getHeight() + 100);
		diamondImage.setPosition(getWidth()/2 - totalLabel.getWidth()/2 - diamondImage.getWidth()/2, getHeight() - 220 - diamondImage.getHeight() / 2);
		
		if(success){
			Label scoreLabel = Label.get("200", "big");
			scoreLabel.position(margin + 150 - scoreLabel.getWidth()/2, getHeight()/2 - scoreLabel.getHeight() + 100);
			scoreLabel.setColor(hudLabel.getMedalColor().getMainColor());
			this.addActor(scoreLabel);
			
			//move total to the right
			totalLabel.position(totalLabel.getX() + getWidth()/2 - margin - 150, totalLabel.getY());
			diamondImage.position(diamondImage.getX() + getWidth()/2 - margin - 150, diamondImage.getY());
		}
	}

	private void createMedal(ColorSet colorSet) {
		ShadowedButton medal = new ShadowedButton("medal", colorSet);
		medal.setPosition(getWidth()/2 - medal.getWidth() / 2, getHeight() - 220 - medal.getHeight() / 2);
		medal.applyColorToContent(colorSet);
		this.addActor(medal);
	}

	private void createTitle() {
		String labelText = "";
		if(success)
			labelText = "Congratulations";
		else
			labelText = "Game Over";
		
		Label scoreLabel = Label.get(labelText, "big");
		
		scoreLabel.position(getWidth()/2 - scoreLabel.getWidth() / 2, getHeight() - scoreLabel.getHeight() - 20);
		scoreLabel.setColor(currentScreen.getCurrentColorSet().getMainColor());
		this.addActor(scoreLabel);
	}
	
	private void createButtons(){
		ColorSet currentColorset = Main.getInstance().getCurrentScreen().getCurrentColorSet();
		
		ShadowedButton restart = new ShadowedButton("restart", currentColorset, ButtonSize.SMALL);
		restart.setPosition(getWidth() - restart.getWidth()/2 - margin - 150, margin/2);
		this.addActor(restart);
		
		if(success){
			ShadowedButton ok = new ShadowedButton("ok", currentColorset, ButtonSize.SMALL);
			ok.setPosition(getWidth()/2 - ok.getWidth() / 2, margin/2);
			this.addActor(ok);
		}
		else{
			ShadowedButton ok = new ShadowedButton("back-button", currentColorset, ButtonSize.SMALL);
			ok.setPosition(getWidth()/2 - ok.getWidth() / 2, margin/2);
			this.addActor(ok);
		}
		
		if(success /* and determine when it should ba available*/){
			ShadowedButton bonus = new ShadowedButton("bonus", currentColorset, ButtonSize.SMALL);
			bonus.setPosition(margin + 150 - bonus.getWidth()/2, margin/2);
			this.addActor(bonus);
		}
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
		shapeRenderer.rect(getX() + margin + getWidth() / 2, getY() + margin + getHeight() / 2, getWidth(), getHeight());
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
