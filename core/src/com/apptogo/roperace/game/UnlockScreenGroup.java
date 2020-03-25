package com.apptogo.roperace.game;

import com.apptogo.roperace.custom.MyShapeRenderer;
import com.apptogo.roperace.custom.MyShapeRenderer.ShapeType;
import com.apptogo.roperace.enums.ColorSet;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.scene2d.Label;
import com.apptogo.roperace.scene2d.ShadowedButton;
import com.apptogo.roperace.scene2d.ShadowedButton.ButtonSize;
import com.apptogo.roperace.screen.BasicScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class UnlockScreenGroup extends Group {

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------ FIELDS -------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	private float margin = Main.SCREEN_HEIGHT/8;
	private MyShapeRenderer shapeRenderer;
	private BasicScreen currentScreen;
	
	//changable values
	private int cost;
	private int number;
	private String label;
	
	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------- INIT --------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	public UnlockScreenGroup(String label) {
		setSize(Main.SCREEN_WIDTH - 4 * margin, Main.SCREEN_HEIGHT - 2 * margin);
		setPosition(-getWidth()/2, -1000);
		setOrigin(getWidth() / 2, getHeight() / 2);
		setBounds(getX(), getY(), getWidth(), getHeight());
		setDebug(true);
		shapeRenderer = new MyShapeRenderer();
		currentScreen = Main.getInstance().getCurrentScreen();
		this.label = label;
	}

	public void init(int number, int cost) {
		this.cost = cost;
		this.number = number;

		clearChildren();
		MoveToAction action = new MoveToAction();
		action.setPosition(-getWidth()/2, -Main.SCREEN_HEIGHT / 2 + margin);
		action.setDuration(1f);
		action.setInterpolation(Interpolation.elasticOut);
		addAction(action);

		createTitle();
		createButtons();
		createLabel(cost);
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ----------------------------------------------- CREATION ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	

	private void createTitle() {
		Label scoreLabel = Label.get(label, "big");
		scoreLabel.position(getWidth() / 2 - scoreLabel.getWidth() / 2, getHeight() - scoreLabel.getHeight() - 20);
		scoreLabel.setColor(currentScreen.getCurrentColorSet().getMainColor());
		this.addActor(scoreLabel);
		
		if(currentScreen.getScoreValue() < cost){
			String msgText = "Not enough diamonds";
			Label msgLabel = Label.get(msgText, "small");
			msgLabel.position(getWidth() / 2 - msgLabel.getWidth() / 2, getHeight() - msgLabel.getHeight() - 20 - scoreLabel.getHeight());
			msgLabel.setColor(currentScreen.getCurrentColorSet().getMainColor());
			this.addActor(msgLabel);
		}
	}

	private void createButtons() {
		ColorSet currentColorset = Main.getInstance().getCurrentScreen().getCurrentColorSet();

		ShadowedButton back = new ShadowedButton("back-button", currentColorset, ButtonSize.SMALL);
		back.setPosition(getWidth() / 2 + back.getWidth() * .5f, margin / 3);
		back.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeSelf();
			}
		});
		this.addActor(back);

		if (currentScreen.getScoreValue() >= cost) {
			ShadowedButton ok = new ShadowedButton("ok", currentColorset, ButtonSize.SMALL);
			ok.setPosition(getWidth() / 2 - ok.getWidth() * 1.5f, margin / 3);
			ok.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					currentScreen.unlockAction(number, cost);
					removeSelf();

				}
			});
			this.addActor(ok);
		}
		else{
			back.setPosition(getWidth() / 2 - back.getWidth()/2, margin / 3);
		}
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ----------------------------------------------- HELPERS -------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	protected void removeSelf(){
		MoveToAction action = new MoveToAction();
		action.setPosition(-getWidth()/2, -1000);
		action.setDuration(1f);
		action.setInterpolation(Interpolation.exp10Out);
		this.addAction(action);
	}
	
	private void createLabel(int cost) {
		Label priceLabel = Label.get(String.valueOf(cost), "big");
		priceLabel.position(getWidth() / 2 - priceLabel.getWidth()/2, getHeight()/2);
		priceLabel.setColor(ColorSet.PURPLE.getMainColor());
		this.addActor(priceLabel);
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ----------------------------------------------- ACT/DRAW ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	@Override
	public void act(float delta) {
		super.act(delta);

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
		shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
		shapeRenderer.end();
		batch.begin();
		super.draw(batch, parentAlpha);
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------- GETTERS/SETTERS---------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	
}
