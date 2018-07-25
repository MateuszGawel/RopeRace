package com.apptogo.roperace.plugin;

import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TouchSteeringPlugin extends SteeringPlugin {

	private Vector2 touchedPlace = new Vector2(0,0);

	protected enum TouchState {
		NOT_TOUCHED, JUST_TOUCHED, KEEP_TOUCHED, JUST_UNTOUCHED
	}

	private TouchState touchState = TouchState.NOT_TOUCHED;

	public TouchSteeringPlugin(final GameScreen screen) {
		super(screen);

		final Actor shootListener = new Actor();
		shootListener.setSize(400, 400);
		shootListener.setPosition(-Main.SCREEN_WIDTH /2 - 100, -Main.SCREEN_HEIGHT / 2 - 100);
		shootListener.setDebug(true);
		shootListener.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (touchState == TouchState.NOT_TOUCHED) {
					touchState = TouchState.JUST_TOUCHED;
					touchedPlace.set(x, y);
				}
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				touchState = TouchState.JUST_UNTOUCHED;
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				if(rope.isRopeAttached()) {
					float draggedX = x < shootListener.getWidth() ? x : shootListener.getWidth();
					float draggedY = y < shootListener.getHeight() ? y : shootListener.getHeight();
					screen.getPlayer().getBody().applyForceToCenter((draggedX - touchedPlace.x)/50, (draggedY - touchedPlace.y)/50, true);

//					if(draggedY > touchedPlace.y){
//						rope.shortenRope(draggedY/10000);
//					}
//					else if (draggedY < touchedPlace.y){
//						rope.extendRope(draggedY/10000);
//					}
//					else{
//						rope.stopRope();
//					}
				}
			}
		});

		screen.getSteeringHudStage().addActor(shootListener);
	}

	@Override
	public void run() {
		if(Main.isAndroid()){
			handleTouchState();
		}
		else{
			handleKeyboard();
		}
	}

	/**
	 * temporary method
	 */
	private void handleKeyboard() {
		if (Gdx.input.getInputProcessor() != null) {
			switch (touchState) {
			case JUST_TOUCHED:
				touchState = TouchState.KEEP_TOUCHED;
				rope.shoot(viewfinder.getAngle());
				break;
			case KEEP_TOUCHED:
				if (!Gdx.input.isKeyPressed(Keys.SPACE)) {
					touchState = TouchState.JUST_UNTOUCHED;
				}
				break;
			case JUST_UNTOUCHED:
				touchState = TouchState.NOT_TOUCHED;
				rope.destroyCurrentJoint();
				break;
			case NOT_TOUCHED:
				if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
					touchState = TouchState.JUST_TOUCHED;
				}
				break;
			default:
				break;
			}
		}
	}

	private void handleTouchState() {
		if (Gdx.input.getInputProcessor() != null) {
			switch (touchState) {
			case JUST_TOUCHED:
				touchState = TouchState.KEEP_TOUCHED;
				rope.shoot(viewfinder.getAngle());
				break;
			case JUST_UNTOUCHED:
				touchState = TouchState.NOT_TOUCHED;
				rope.destroyCurrentJoint();
				break;
			case KEEP_TOUCHED:
				break;
			case NOT_TOUCHED:
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void setUpDependencies() {
		// TODO Auto-generated method stub

	}

}
