package com.apptogo.roperace.game;

import com.apptogo.roperace.actors.Hoop;
import com.apptogo.roperace.custom.MyShapeRenderer;
import com.apptogo.roperace.custom.MyShapeRenderer.ShapeType;
import com.apptogo.roperace.level.LevelData;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.CustomAction;
import com.apptogo.roperace.manager.CustomActionManager;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class EndScreenGroup extends Group {

	private float margin = 100;
	private MyShapeRenderer shapeRenderer;
	private BasicScreen currentScreen;
	private HudLabel hudLabel;
	private Hoop hoop;
	private boolean showed;
	private boolean success;
	private Label scoreLabel;
	private Label totalLabel;
	
	private Container<Label> scoreLabelContainer;
	private SequenceAction scoreSequence;
	
	private CustomAction transferPointsAction;
	private CustomAction initMedalsAction;
	
	private ShadowedButton goldMedal;
	private ShadowedButton silverMedal;
	private ShadowedButton bronzeMedal;
	
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
			createMedals();
		createTitle();
		createButtons();
		createPointLabels();
		createActions();
	}
	
	private void createActions() {
		transferPointsAction = new CustomAction(0.01f, 0) {
			
			@Override
			public void perform() {
				
				setScoreValue(getScoreValue()-1);
				setTotalValue(getTotalValue()+1);
				
				if(getScoreValue() <= 0)
					unregister();
			}
		};
		
		initMedalsAction = new CustomAction(1f) {
			
			@Override
			public void perform() {
				if(bronzeMedal != null){
					bronzeMedal.setVisible(true);
					bronzeMedal.addAction(Actions.moveTo(bronzeMedal.getX(), getHeight() + bronzeMedal.getHeight() + 20, 0.5f, Interpolation.exp5In));
					CustomActionManager.getInstance().registerAction(new CustomAction(0.5f) {
						@Override
						public void perform() {
							setScoreValue(LevelData.BRONZE_POINTS);
							updateScoreSize();
							bumpScoreLabel();
							scoreLabel.setColor(ColorSet.BRONZE.getMainColor());
						}
					});
				}
				if(silverMedal != null){
					silverMedal.setVisible(true);
					silverMedal.toFront();
					SequenceAction sequence = new SequenceAction();
				    sequence.addAction(Actions.delay(0.5f));
				    sequence.addAction(Actions.moveTo(silverMedal.getX(), getHeight() + silverMedal.getHeight() + 20, 0.5f, Interpolation.exp5In));
					silverMedal.addAction(sequence);
					CustomActionManager.getInstance().registerAction(new CustomAction(1f) {
						@Override
						public void perform() {
							setScoreValue(LevelData.SILVER_POINTS);
							updateScoreSize();
							bumpScoreLabel();
							scoreLabel.setColor(ColorSet.SILVER.getMainColor());
						}
					});
				}
				if(goldMedal != null){
					goldMedal.setVisible(true);
					goldMedal.toFront();
					SequenceAction sequence = new SequenceAction();
				    sequence.addAction(Actions.delay(1f));
				    sequence.addAction(Actions.moveTo(goldMedal.getX(), getHeight() + goldMedal.getHeight() + 20, 0.5f, Interpolation.exp5In));
				    goldMedal.addAction(sequence);
					CustomActionManager.getInstance().registerAction(new CustomAction(1.5f) {
						@Override
						public void perform() {
							setScoreValue(LevelData.GOLD_POINTS);
							updateScoreSize();
							bumpScoreLabel();
							scoreLabel.setColor(ColorSet.GOLD.getMainColor());
						}
					});
				}
			}
		};
		CustomActionManager.getInstance().registerAction(initMedalsAction);
	}

	
	private void createPointLabels() {
		Image diamondImage = Image.getFromTexture("diamond");
		diamondImage.setSize(diamondImage.getWidth()*0.7f, diamondImage.getHeight()*0.7f);
		this.addActor(diamondImage);
		totalLabel = Label.get("200", "big");
		totalLabel.setColor(ColorSet.PURPLE.getMainColor());
		this.addActor(totalLabel);		
		totalLabel.position(getWidth()/2 - totalLabel.getWidth()/2 + diamondImage.getWidth()/2, getHeight()/2 - totalLabel.getHeight() + 100);
		diamondImage.setPosition(getWidth()/2 - totalLabel.getWidth()/2 - diamondImage.getWidth()/2, getHeight() - 220 - diamondImage.getHeight() / 2);
		
		if(success){
			scoreLabel = Label.get("0", "big");
			scoreLabel.position(margin + 150 - scoreLabel.getWidth()/2, getHeight()/2 - scoreLabel.getHeight() + 100);
			scoreLabel.setColor(ColorSet.GRAY.getMainColor());
			this.addActor(scoreLabel);
			
			scoreLabelContainer = new Container<Label>(scoreLabel);
			scoreLabelContainer.setTransform(true);
			scoreLabelContainer.setPosition(margin + 150 - scoreLabel.getWidth()/2, getHeight()/2 - scoreLabel.getHeight() + 100);
			scoreLabelContainer.align(Align.left);
			addActor(scoreLabelContainer);
			updateScoreSize();
			
			scoreSequence = new SequenceAction();
			ScaleByAction s1 = new ScaleByAction();
			DelayAction d = new DelayAction(0.05f);
			ScaleByAction s2 = new ScaleByAction();
			s1.setAmount(0.5f);
			s1.setDuration(0.05f);
			s1.setInterpolation(Interpolation.exp5In);
			s2.setAmount(-0.5f);
			s2.setDuration(0.05f);
			s2.setInterpolation(Interpolation.exp5Out);
		    scoreSequence.addAction(s1);
		    scoreSequence.addAction(d);
		    scoreSequence.addAction(s2);

		    //move total to the right
			totalLabel.position(totalLabel.getX() + getWidth()/2 - margin - 150, totalLabel.getY());
			diamondImage.position(diamondImage.getX() + getWidth()/2 - margin - 150, diamondImage.getY());
		}
	}
	
	private void bumpScoreLabel(){
		scoreLabelContainer.addAction(scoreSequence);
		scoreSequence.restart();
	}
	
	private void updateScoreSize(){
		scoreLabelContainer.setSize(scoreLabel.getWidth(), scoreLabel.getHeight());
		scoreLabelContainer.setOrigin(scoreLabelContainer.getWidth()/2, scoreLabelContainer.getHeight()/2);
	}
	
	private void createMedals() {
		if(hudLabel.getCurrentColorSet() == ColorSet.GOLD){
			goldMedal = new ShadowedButton("medal", ColorSet.GOLD);
			goldMedal.setPosition(getWidth()/2 - goldMedal.getWidth() / 2, getHeight() + 500);
			goldMedal.applyColorToContent(ColorSet.GOLD);
			goldMedal.setVisible(false);
			this.addActor(goldMedal);
		}
		
		if(hudLabel.getCurrentColorSet() == ColorSet.GOLD || hudLabel.getCurrentColorSet() == ColorSet.SILVER){
			silverMedal = new ShadowedButton("medal", ColorSet.SILVER);
			silverMedal.setPosition(getWidth()/2 - silverMedal.getWidth() / 2, getHeight() + 500);
			silverMedal.applyColorToContent(ColorSet.SILVER);
			silverMedal.setVisible(false);
			this.addActor(silverMedal);
		}
		
		if(hudLabel.getCurrentColorSet() == ColorSet.GOLD || hudLabel.getCurrentColorSet() == ColorSet.SILVER || hudLabel.getCurrentColorSet() == ColorSet.BRONZE){
			bronzeMedal = new ShadowedButton("medal", ColorSet.BRONZE);
			bronzeMedal.setPosition(getWidth()/2 - bronzeMedal.getWidth() / 2, getHeight() + 500);
			bronzeMedal.applyColorToContent(ColorSet.BRONZE);
			bronzeMedal.setVisible(false);
			this.addActor(bronzeMedal);
		}

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
			ok.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					CustomActionManager.getInstance().registerAction(transferPointsAction);
				}
			});
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

	private void setTotalValue(int value){
		totalLabel.setText(String.valueOf(value));
	}
	
	private void setScoreValue(int value){
		scoreLabel.setText(String.valueOf(value));
	}
	
	private int getTotalValue(){
		return Integer.valueOf(totalLabel.getText().toString());
	}
	
	private int getScoreValue(){
		return Integer.valueOf(scoreLabel.getText().toString());
	}
}
