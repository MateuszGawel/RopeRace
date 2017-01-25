package com.apptogo.roperace.game;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.apptogo.roperace.level.LevelData;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.CustomAction;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.scene2d.ColorSet;
import com.apptogo.roperace.scene2d.Label;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;

public class HudLabel extends Group{
	
	private LevelData levelData;
	private Label label;
	private boolean gameOver;
	private ColorSet currentColorSet;
	private boolean counting = true;
	
	public HudLabel(LevelData levelData){
		debug();
		
		this.levelData = levelData;
		
		switch(levelData.getType()){
		case DIAMONDS:
			createDiamondsLabel();
			break;
		case ROPES:
			createRopesLabel();
			break;
		case TIME:
			createTimeLabel();
			break;
		default:
			break;
		
		}
		
	}
	
	private void createTimeLabel(){
		currentColorSet = ColorSet.GOLD;
		label = Label.get(String.valueOf(levelData.getBronzeReq()), "big");
		label.position(-Main.SCREEN_WIDTH/2+10, Main.SCREEN_HEIGHT/2 - 70);
		label.setColor(currentColorSet.getMainColor());
		addActor(label);
		
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
		otherSymbols.setDecimalSeparator('.');
		final DecimalFormat df = new DecimalFormat("0.0", otherSymbols);
		
		
		final Container<Label> labelContainer = new Container<Label>(label);
		labelContainer.setTransform(true);
		labelContainer.setPosition(-Main.SCREEN_WIDTH/2+10, Main.SCREEN_HEIGHT/2 - 70);
		labelContainer.align(Align.left);
		addActor(labelContainer);
		
		final SequenceAction sequence = new SequenceAction();
	    sequence.addAction(Actions.scaleBy(0.3f, 0.3f, 0.05f, Interpolation.exp5In));
	    sequence.addAction(Actions.delay(0.05f));
	    sequence.addAction(Actions.scaleBy(-0.3f, -0.3f, 0.05f, Interpolation.exp5Out));
		
		CustomActionManager.getInstance().registerAction(new CustomAction(0.1f, 0) {
			
			@Override
			public void perform() {
				
				if(!counting)
					unregister();
				
				double currentValue = Double.valueOf(label.getText().toString());
				label.setText(df.format(currentValue-0.1d));
				labelContainer.setSize(label.getWidth(), label.getHeight());
				labelContainer.setOrigin(labelContainer.getWidth()/2, labelContainer.getHeight()/2);
				
				if(currentValue == levelData.getBronzeReq() - levelData.getGoldReq()){
					currentColorSet = ColorSet.SILVER;
					labelContainer.addAction(sequence);
				}
				else if(currentValue == levelData.getBronzeReq() - levelData.getSilverReq()){
					currentColorSet = ColorSet.BRONZE;
					sequence.restart();
					labelContainer.addAction(sequence);
				}
				else if(currentValue == 3){
					//start ticking
				}
				else if(currentValue <= 0){
					//level failed
					label.setText("0");
					unregister();
					gameOver = true;
				}
				label.setColor(currentColorSet.getMainColor());
			}
		});
	}
	
	private void createDiamondsLabel(){
		
	}
	
	private void createRopesLabel(){
		
	}
	
	private void handleTimeLabel(){
	}
	
	private void handleDiamondsLabel(){
		
	}
	
	private void handleRopesLabel(){
		
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		switch(levelData.getType()){
		case DIAMONDS:
			handleDiamondsLabel();
			break;
		case ROPES:
			handleRopesLabel();
			break;
		case TIME:
			handleTimeLabel();
			break;
		default:
			break;
		
		}
		
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public ColorSet getMedalColor() {
		return currentColorSet;
	}

	public void setCounting(boolean counting) {
		this.counting = counting;
	}
	
	
}
