package com.apptogo.roperace.game;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.apptogo.roperace.actors.Rope;
import com.apptogo.roperace.enums.ColorSet;
import com.apptogo.roperace.exception.PluginException;
import com.apptogo.roperace.level.LevelData;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.CustomAction;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.plugin.KeyboardSteeringPlugin;
import com.apptogo.roperace.plugin.TouchSteeringPlugin;
import com.apptogo.roperace.scene2d.Label;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;

public class HudLabel extends Group{
	
	private LevelData levelData;
	private Label label;
	private boolean gameOver;
	
	private ColorSet currentColorSet;
	private boolean counting = false;
	private GameActor player;
	private Rope rope;
	
	private Container<Label> labelContainer;
	private SequenceAction sequence;
	private DecimalFormat decimalFormat;
	private int starCounter;
	private CustomAction timeCountingAction;
	
	public HudLabel(LevelData levelData, GameActor player){
		this.player = player;
		debug();
		
		this.levelData = levelData;
		
		createLabel();
		
		switch(levelData.getType()){
		case STARS:
			createStarsLabel();
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
	
	private void createLabel(){
		label = Label.get(String.valueOf(levelData.getBronzeReq()), "big");
		label.position(-Main.SCREEN_WIDTH/2+10, Main.SCREEN_HEIGHT/2 - 70);
		addActor(label);
		
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
		otherSymbols.setDecimalSeparator('.');
		decimalFormat = new DecimalFormat("0.0", otherSymbols);
		
		
		labelContainer = new Container<Label>(label);
		labelContainer.setTransform(true);
		labelContainer.setPosition(-Main.SCREEN_WIDTH/2+10, Main.SCREEN_HEIGHT/2 - 70);
		labelContainer.align(Align.left);
		addActor(labelContainer);
		
		sequence = new SequenceAction();
		ScaleByAction s1 = new ScaleByAction();
		DelayAction d = new DelayAction(0.05f);
		ScaleByAction s2 = new ScaleByAction();
		s1.setAmount(0.3f);
		s1.setDuration(0.05f);
		s1.setInterpolation(Interpolation.exp5In);
		s2.setAmount(-0.3f);
		s2.setDuration(0.05f);
		s2.setInterpolation(Interpolation.exp5Out);
		
	    sequence.addAction(s1);
	    sequence.addAction(d);
	    sequence.addAction(s2);
	}
	
	private void bumpLabel(){

		labelContainer.addAction(sequence);
		sequence.restart();
	}
	
	private void updateSize(){
		labelContainer.setSize(label.getWidth(), label.getHeight());
		labelContainer.setOrigin(labelContainer.getWidth()/2, labelContainer.getHeight()/2);
	}
	
	private void createTimeLabel(){
		currentColorSet = ColorSet.GOLD;
		label.setText("0");
		timeCountingAction = new CustomAction(0.1f, 0) {

			@Override
			public void perform() {
				double currentValue = Double.valueOf(label.getText().toString());
				
				if(counting){
					label.setText(decimalFormat.format(currentValue + 0.1d));
				}
				updateSize();
				
				if (currentValue == levelData.getGoldReq()) {
					currentColorSet = ColorSet.SILVER;
					bumpLabel();
				} else if (currentValue == levelData.getSilverReq()) {
					currentColorSet = ColorSet.BRONZE;
					bumpLabel();
				} else if (currentValue == levelData.getSilverReq()) {
					label.setText("0");
					unregister();
					gameOver = true;
				}
				label.setColor(currentColorSet.getMainColor());

			}
		};
		CustomActionManager.getInstance().registerAction(timeCountingAction);
	}
	
	private void createStarsLabel(){
		counting = true;
	}
	
	private void createRopesLabel(){
		counting = true;
		currentColorSet = ColorSet.GOLD;
		try{
			rope = player.getPlugin(TouchSteeringPlugin.class).getRope();
		}
		catch(PluginException e){
			rope = player.getPlugin(KeyboardSteeringPlugin.class).getRope();
		}
	}
	
	private void handleTimeLabel(){
	}
	
	private void handleStarsLabel(){
		if(counting){
			label.setText(String.valueOf(starCounter));
			updateSize();
			
			if(currentColorSet == ColorSet.SILVER && starCounter >= levelData.getGoldReq()){
				currentColorSet = ColorSet.GOLD;
				bumpLabel();
			}
			else if(currentColorSet == ColorSet.BRONZE && starCounter >= levelData.getSilverReq()){
				currentColorSet = ColorSet.SILVER;
				bumpLabel();
			}
			else if(currentColorSet == ColorSet.GRAY && starCounter >= levelData.getBronzeReq()){
				currentColorSet = ColorSet.BRONZE;
				bumpLabel();
			}
			else if(currentColorSet == null){
				currentColorSet = ColorSet.GRAY;
				return;
			}
			
			label.setColor(currentColorSet.getMainColor());
		}
	}
	
	private void handleRopesLabel(){
		if(counting){
			int ropeCounter = rope.getShootCounter();
			label.setText(String.valueOf(ropeCounter));
			updateSize();
			
			if(ropeCounter > levelData.getBronzeReq()){
				label.setText(String.valueOf(ropeCounter-1));
				updateSize();
				gameOver = true;
			}
			else if(currentColorSet == ColorSet.SILVER && ropeCounter == levelData.getSilverReq()+1){
				currentColorSet = ColorSet.BRONZE;
				bumpLabel();
			}
			else if(currentColorSet == ColorSet.GOLD && ropeCounter == levelData.getGoldReq()+1){
				currentColorSet = ColorSet.SILVER;
				bumpLabel();
			}

			label.setColor(currentColorSet.getMainColor());
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		switch(levelData.getType()){
		case STARS:
			handleStarsLabel();
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

	public void setCounting(boolean counting) {
		this.counting = counting;
		if(!counting && timeCountingAction != null){
			timeCountingAction.unregister();
		}
	}

	public void onStarCollected() {
		starCounter++;
	}

	public ColorSet getCurrentColorSet() {
		return currentColorSet;
	}
	
}
