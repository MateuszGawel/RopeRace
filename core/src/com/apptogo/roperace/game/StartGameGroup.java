package com.apptogo.roperace.game;


import com.apptogo.roperace.enums.ColorSet;
import com.apptogo.roperace.level.LevelData;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.CustomAction;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.scene2d.Label;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class StartGameGroup extends Group {

	private LevelData levelData;
	
	private Label titleLabel;
	private SequenceAction titleSequence;
	
	private Label scoreLabel;
	private SequenceAction scoreSequence;
	
	private boolean finished;

	private CustomAction removeAction;
	
	public StartGameGroup(LevelData levelData){
		setSize(Main.SCREEN_WIDTH, 0);
		setPosition(-Main.SCREEN_WIDTH / 2, -Main.SCREEN_HEIGHT / 2);
		this.levelData = levelData;
	}

	public void init() {
		createTitleLabel();
		createScoreLabel();
		notifyAfterDelay(4);
	}


	private void createTitleLabel() {
		titleLabel = Label.get(levelData.getType().toString() + " RACE", "big");
		titleLabel.setColor(Color.GRAY);
		
		titleLabel.setPosition(-titleLabel.getWidth(), Main.SCREEN_HEIGHT / 2 + 50);
		addActor(titleLabel);
		
		titleSequence = new SequenceAction();
		titleSequence.addAction(Actions.moveTo(getWidth()/2 - titleLabel.getWidth()/2, titleLabel.getY(), 2, Interpolation.exp5Out));
		titleSequence.addAction(Actions.moveTo(getWidth(), titleLabel.getY(), 2, Interpolation.exp5In));
	    
	    titleLabel.addAction(titleSequence);
	}
	
	private void createScoreLabel() {
		
		String bronze = "[#"+ColorSet.BRONZE.getMainColor().toString()+"]";
		String silver = "[#"+ColorSet.SILVER.getMainColor().toString()+"]";
		String gold = "[#"+ColorSet.GOLD.getMainColor().toString()+"]";
		
		scoreLabel = Label.get(bronze + (int)levelData.getBronzeReq() + "       " + silver + (int)levelData.getSilverReq() + "       " + gold + (int)levelData.getGoldReq(), "big");
		scoreLabel.position(getWidth(), Main.SCREEN_HEIGHT/2 - 50);

		addActor(scoreLabel);
		
		scoreSequence = new SequenceAction();
		scoreSequence.addAction(Actions.moveTo(getWidth()/2 - scoreLabel.getWidth()/2, scoreLabel.getY(), 2, Interpolation.exp5Out));
		scoreSequence.addAction(Actions.moveTo(-scoreLabel.getWidth(), scoreLabel.getY(), 2, Interpolation.exp5In));
		
		scoreLabel.addAction(scoreSequence);	
	}
	
	protected void removeSelf(){
		remove();
		finished = true;
		removeAction.unregister();
	}
	
	private void notifyAfterDelay(float delay){
		removeAction = new CustomAction(delay) {
			@Override
			public void perform() {
				removeSelf();
			}
		};
		CustomActionManager.getInstance().registerAction(removeAction);
	}

	public boolean isFinished() {
		return finished;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if(Gdx.input.justTouched()){
			removeSelf();
		}
	}
	

}
