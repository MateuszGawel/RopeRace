package com.apptogo.roperace.game;

import java.util.Random;

import com.apptogo.roperace.actors.Hoop;
import com.apptogo.roperace.custom.MyShapeRenderer;
import com.apptogo.roperace.custom.MyShapeRenderer.ShapeType;
import com.apptogo.roperace.enums.ColorSet;
import com.apptogo.roperace.level.LevelData;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.manager.CustomAction;
import com.apptogo.roperace.manager.CustomActionManager;
import com.apptogo.roperace.save.SaveManager;
import com.apptogo.roperace.scene2d.Image;
import com.apptogo.roperace.scene2d.Label;
import com.apptogo.roperace.scene2d.ShadowedButton;
import com.apptogo.roperace.scene2d.ShadowedButton.ButtonSize;
import com.apptogo.roperace.screen.BasicScreen;
import com.apptogo.roperace.screen.GameScreen;
import com.apptogo.roperace.screen.MenuScreen;
import com.apptogo.roperace.screen.WorldSelectionScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
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

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------ FIELDS -------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	private float margin = 100;
	private boolean showed;
	private boolean success;
	private int levelNo;
	private int worldNo;

	private MyShapeRenderer shapeRenderer;
	private BasicScreen currentScreen;
	private HudLabel hudLabel;
	private Hoop hoop;

	private Label scoreLabel;
	private Label totalLabel;
	private Container<Label> scoreLabelContainer;
	private SequenceAction scoreSequence;

	private CustomAction transferPointsAction;
	private CustomAction initMedalsAction;
	private CustomAction shakeAction;

	private ShadowedButton bonus;
	private ShadowedButton goldMedal;
	private ShadowedButton silverMedal;
	private ShadowedButton bronzeMedal;
	private ColorSet earnedMedal;

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ------------------------------------------------- INIT --------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- 
	 * @param level **/

	public EndScreenGroup(HudLabel hudLabel, Hoop hoop, int level, int worldNo) {
		setSize(Main.SCREEN_WIDTH - 2 * margin, Main.SCREEN_HEIGHT - 2 * margin);
		setPosition(-Main.SCREEN_WIDTH / 2 + margin, -1000);
		setOrigin(getWidth() / 2, getHeight() / 2);
		shapeRenderer = new MyShapeRenderer();
		currentScreen = Main.getInstance().getCurrentScreen();
		this.hudLabel = hudLabel;
		this.hoop = hoop;
		this.levelNo = level;
		this.worldNo = worldNo;
	}

	public void init() {
		MoveToAction action = new MoveToAction();
		action.setPosition(-Main.SCREEN_WIDTH / 2 + margin, -Main.SCREEN_HEIGHT / 2 + margin);
		action.setDuration(1f);
		action.setInterpolation(Interpolation.elasticOut);
		this.addAction(action);

		if (!hudLabel.isGameOver() && hudLabel.getCurrentColorSet().getMedalNumber() != 0) {
			success = true;
		}

		if (success)
			createMedals();
		createTitle();
		createPointLabels();
		createButtons();
		createActions();
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ----------------------------------------------- CREATION ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	private void createMedals() {

		bronzeMedal = new ShadowedButton("medal", ColorSet.BRONZE);
		bronzeMedal.setPosition(getWidth() / 2 - bronzeMedal.getWidth() / 2, getHeight() + 500);
		bronzeMedal.applyColorToContent(ColorSet.BRONZE);
		bronzeMedal.setVisible(false);
		this.addActor(bronzeMedal);

		silverMedal = new ShadowedButton("medal", ColorSet.SILVER);
		silverMedal.setPosition(getWidth() / 2 - silverMedal.getWidth() / 2, getHeight() + 500);
		silverMedal.applyColorToContent(ColorSet.SILVER);
		silverMedal.setVisible(false);
		this.addActor(silverMedal);

		goldMedal = new ShadowedButton("medal", ColorSet.GOLD);
		goldMedal.setPosition(getWidth() / 2 - goldMedal.getWidth() / 2, getHeight() + 500);
		goldMedal.applyColorToContent(ColorSet.GOLD);
		goldMedal.setVisible(false);
		this.addActor(goldMedal);

		//modify position if already earned
		earnedMedal = SaveManager.getInstance().getMedalForLevel(levelNo, worldNo);
		if (earnedMedal.getMedalNumber() > 0) {
			bronzeMedal.setPosition(bronzeMedal.getX(), bronzeMedal.getHeight() + 20);
			bronzeMedal.setVisible(true);
		}
		if (earnedMedal.getMedalNumber() > 1) {
			silverMedal.setPosition(silverMedal.getX(), silverMedal.getHeight() + 20);
			silverMedal.setVisible(true);
		}
		if (earnedMedal.getMedalNumber() > 2) {
			goldMedal.setPosition(goldMedal.getX(), goldMedal.getHeight() + 20);
			goldMedal.setVisible(true);
		}
	}

	private void createTitle() {
		String labelText = "";
		if (success)
			labelText = "Congratulations";
		else
			labelText = "Game Over";

		Label scoreLabel = Label.get(labelText, "big");

		scoreLabel.position(getWidth() / 2 - scoreLabel.getWidth() / 2, getHeight() - scoreLabel.getHeight() - 20);
		scoreLabel.setColor(currentScreen.getCurrentColorSet().getMainColor());
		this.addActor(scoreLabel);
	}

	private void createButtons() {
		ColorSet currentColorset = Main.getInstance().getCurrentScreen().getCurrentColorSet();

		ShadowedButton restart = new ShadowedButton("restart", currentColorset, ButtonSize.SMALL);
		restart.setPosition(getWidth() - restart.getWidth()*1.5f, margin / 2);
		restart.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (CustomActionManager.getInstance().getRegisteredActionCount() == 0) {
					Main.getInstance().setScreen(new GameScreen(levelNo, worldNo));
				}
			}
		});
		this.addActor(restart);

		ShadowedButton back = new ShadowedButton("back-button", currentColorset, ButtonSize.SMALL);
		back.setPosition(back.getWidth() / 2, margin / 2);
		back.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (CustomActionManager.getInstance().getRegisteredActionCount() == 0) {
					Main.getInstance().setScreen(new MenuScreen());
				}
			}
		});
		this.addActor(back);
		
		if (success) {
			final ShadowedButton ok = new ShadowedButton("ok", currentColorset, ButtonSize.SMALL);
			ok.setPosition(getWidth() / 2 - ok.getWidth() / 2, margin / 2);
			ok.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(canClickOk()){
						MoveToAction action = new MoveToAction();
						action.setPosition(getWidth() / 2 - ok.getWidth() / 2, margin / 2);
						action.setDuration(1f);
						action.setInterpolation(Interpolation.bounceOut);
						ok.addAction(action);
						
						clickOk();
					}
				}
				
			});
			this.addActor(ok);

			if (/* and determine when it should ba available*/hudLabel.getCurrentColorSet().getMedalNumber() > earnedMedal.getMedalNumber()) {
				bonus = new ShadowedButton("bonus", currentColorset, ButtonSize.SMALL);
				bonus.setPosition(getWidth() / 2 - bonus.getWidth() - 10, margin / 2);
				ok.setPosition(getWidth() / 2 + 10, margin / 2);
				bonus.setOrigin(Align.center);

				final Action pulse = Actions.forever(Actions.sequence(Actions.scaleTo(1.1f, 1.1f, 0.5f), Actions.scaleTo(0.9f, 0.9f, 0.5f)));
				bonus.addAction(pulse);

				bonus.addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (CustomActionManager.getInstance().getRegisteredActionCount() == 0) {
							bonus.removeAction(pulse);
							bonus.setVisible(false);
							
							MoveToAction action = new MoveToAction();
							action.setPosition(getWidth() / 2 - ok.getWidth() / 2, margin / 2);
							action.setDuration(1f);
							action.setInterpolation(Interpolation.bounceOut);
							ok.addAction(action);
							
							CustomActionManager.getInstance().registerAction(new CustomAction(0.01f, getScoreValue()) {

								@Override
								public void perform() {
									setScoreValue(getScoreValue() + 1);
								}

								@Override
								public void onFinish() {
									if(canClickOk())
										clickOk();
								}
							});
						}
					}
				});
				this.addActor(bonus);

			}
		}

	}

	private void createPointLabels() {
		Image diamondImage = Image.get("diamond");
		diamondImage.setSize(diamondImage.getWidth() * 0.7f, diamondImage.getHeight() * 0.7f);
		this.addActor(diamondImage);
		totalLabel = Label.get(String.valueOf(SaveManager.getInstance().getPoints()), "big");
		totalLabel.setColor(ColorSet.PURPLE.getMainColor());
		this.addActor(totalLabel);
		totalLabel.position(getWidth() / 2 - totalLabel.getWidth() / 2 + diamondImage.getWidth() / 2, getHeight() / 2 - totalLabel.getHeight() + 100);
		diamondImage.setPosition(getWidth() / 2 - totalLabel.getWidth() / 2 - diamondImage.getWidth() / 2, getHeight() - 220 - diamondImage.getHeight() / 2);

		if (success) {
			scoreLabel = Label.get("00", "big");
			scoreLabel.position(margin + 150 - scoreLabel.getWidth() / 2, getHeight() / 2 - scoreLabel.getHeight() + 100);
			scoreLabel.setColor(ColorSet.GRAY.getMainColor());
			scoreLabel.setVisible(false);
			this.addActor(scoreLabel);

			scoreLabelContainer = new Container<Label>(scoreLabel);
			scoreLabelContainer.setTransform(true);
			scoreLabelContainer.setPosition(margin + 150 - scoreLabel.getWidth() / 2, getHeight() / 2 - scoreLabel.getHeight() + 100);
			scoreLabelContainer.align(Align.left);
			addActor(scoreLabelContainer);

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
			totalLabel.position(totalLabel.getX() + getWidth() / 2 - margin - 150, totalLabel.getY());
			diamondImage.position(diamondImage.getX() + getWidth() / 2 - margin - 150, diamondImage.getY());
		}
	}

	private void createActions() {
		transferPointsAction = new CustomAction(0.01f, 0) {

			@Override
			public void perform() {
				bonus.setVisible(false);

				setScoreValue(getScoreValue() - 1);
				setTotalValue(getTotalValue() + 1);

				if (getScoreValue() <= 0) {
					unregister();
				}
			}

			@Override
			public void onFinish() {
				CustomActionManager.getInstance().registerAction(new CustomAction(1f) {

					@Override
					public void perform() {
						switchLevel();
					}
				});
			}
			
		};

		initMedalsAction = new CustomAction(1f) {

			private float delay = 0f;

			@Override
			public void perform() {
				if (hudLabel.getCurrentColorSet().getMedalNumber() > 0 && earnedMedal.getMedalNumber() < 1) {
					bronzeMedal.setVisible(true);
					bronzeMedal.addAction(Actions.moveTo(bronzeMedal.getX(), bronzeMedal.getHeight() + 20, 0.5f, Interpolation.exp5In));
					delay += 0.5f;
					CustomActionManager.getInstance().registerAction(new CustomAction(delay) {
						@Override
						public void perform() {
							setScoreValue(getScoreValue() + LevelData.BRONZE_POINTS);
							bumpScoreLabel();
							scoreLabel.setColor(ColorSet.BRONZE.getMainColor());
							CustomActionManager.getInstance().registerAction(shakeAction);
						}
					});
				}
				if (hudLabel.getCurrentColorSet().getMedalNumber() > 1 && earnedMedal.getMedalNumber() < 2) {
					silverMedal.setVisible(true);
					SequenceAction sequence = new SequenceAction();
					sequence.addAction(Actions.delay(delay));
					sequence.addAction(Actions.moveTo(silverMedal.getX(), silverMedal.getHeight() + 20, 0.5f, Interpolation.exp5In));
					silverMedal.addAction(sequence);
					delay += 0.5f;
					CustomActionManager.getInstance().registerAction(new CustomAction(delay) {
						@Override
						public void perform() {
							setScoreValue(getScoreValue() + LevelData.SILVER_POINTS);
							bumpScoreLabel();
							scoreLabel.setColor(ColorSet.SILVER.getMainColor());
							CustomActionManager.getInstance().registerAction(shakeAction);
						}
					});
				}
				if (hudLabel.getCurrentColorSet().getMedalNumber() > 2 && earnedMedal.getMedalNumber() < 3) {
					goldMedal.setVisible(true);
					SequenceAction sequence = new SequenceAction();
					sequence.addAction(Actions.delay(delay));
					sequence.addAction(Actions.moveTo(goldMedal.getX(), goldMedal.getHeight() + 20, 0.5f, Interpolation.exp5In));
					goldMedal.addAction(sequence);
					delay += 0.5f;
					CustomActionManager.getInstance().registerAction(new CustomAction(delay) {
						@Override
						public void perform() {
							setScoreValue(getScoreValue() + LevelData.GOLD_POINTS);
							bumpScoreLabel();
							scoreLabel.setColor(ColorSet.GOLD.getMainColor());
							CustomActionManager.getInstance().registerAction(shakeAction);
						}
					});
				}
			}

		};
		
		if(earnedMedal != null){
			CustomActionManager.getInstance().registerAction(initMedalsAction);
		}

		shakeAction = new CustomAction(0f, 7, this) {

			private Float origPositionX;
			private Float origPositionY;

			@Override
			public void perform() {
				Group endScreenGroup = (Group) args[0];

				if (origPositionX == null || origPositionY == null) {
					origPositionX = endScreenGroup.getX();
					origPositionY = endScreenGroup.getY();
				}
				float xTilt = (new Random().nextFloat() - 0.5f) * 5;
				float yTilt = (new Random().nextFloat() - 0.5f) * 5;
				float degrees = new Random().nextFloat() - 0.5f;

				endScreenGroup.setPosition(endScreenGroup.getX() + xTilt, endScreenGroup.getY() + yTilt);
				endScreenGroup.setRotation(degrees);
			}

			@Override
			public void onFinish() {
				Group endScreenGroup = (Group) args[0];
				endScreenGroup.setPosition(origPositionX, origPositionY);
				endScreenGroup.setRotation(0);
			}

		};
	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ----------------------------------------------- HELPERS -------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	private void bumpScoreLabel() {
		scoreLabelContainer.setSize(scoreLabel.getWidth(), scoreLabel.getHeight());
		scoreLabelContainer.setOrigin(scoreLabelContainer.getWidth() / 2, scoreLabelContainer.getHeight() / 2);
		scoreLabelContainer.addAction(scoreSequence);
		scoreSequence.restart();
	}
	
	private void clickOk() {
		if (getScoreValue() > 0) {
			CustomActionManager.getInstance().registerAction(transferPointsAction);
			SaveManager.getInstance().addPoints(getScoreValue());
			SaveManager.getInstance().completeLevel(levelNo, worldNo, hudLabel.getCurrentColorSet());
		} else {
			switchLevel();
		}
	}
	
	private boolean canClickOk(){
		return CustomActionManager.getInstance().getRegisteredActionCount() == 0 && success && !transferPointsAction.isRegistered();
	}
	
	private void switchLevel(){
		MoveToAction action = new MoveToAction();
		action.setPosition(-Main.SCREEN_WIDTH / 2 + margin, 500);
		action.setDuration(0.5f);
		action.setInterpolation(Interpolation.pow5In);
		this.addAction(action);

		CustomActionManager.getInstance().registerAction(new CustomAction(0.7f) {

			@Override
			public void perform() {
				if (levelNo == 9)
					Main.getInstance().setScreen(new WorldSelectionScreen());
				else
					Main.getInstance().setScreen(new GameScreen(levelNo + 1, worldNo));
			}
		});

	}

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** ----------------------------------------------- ACT/DRAW ------------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	@Override
	public void act(float delta) {
		super.act(delta);
		if ((hudLabel.isGameOver() || hoop.isFinished()) && !showed) {
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

	/** ---------------------------------------------------------------------------------------------------------- **/
	/** -------------------------------------------- GETTERS/SETTERS---------------------------------------------- **/
	/** ---------------------------------------------------------------------------------------------------------- **/

	public Hoop getHoop() {
		return hoop;
	}

	public void setHoop(Hoop hoop) {
		this.hoop = hoop;
	}

	private void setTotalValue(int value) {
		totalLabel.setText(String.valueOf(value));
	}

	private void setScoreValue(int value) {
		if (!scoreLabel.isVisible())
			scoreLabel.setVisible(true);
		scoreLabel.setText(String.valueOf(value));
	}

	private int getTotalValue() {
		return Integer.valueOf(totalLabel.getText().toString());
	}

	private int getScoreValue() {
		if (scoreLabel == null)
			return 0;
		return Integer.valueOf(scoreLabel.getText().toString());
	}
}
