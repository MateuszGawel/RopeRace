package com.apptogo.roperace.manager;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class CustomAction extends Actor {

	private float stateTime = 0;
	private float timeElapsed = 0;
	private float delay;
	
	private int loopCount = 0;
	private int loops = 1;
	
	@SuppressWarnings("unused")
	protected Object[] args;

	/**
	 * During creation you have to override perform method
	 * It will be called once after delay elapsed
	 * 
	 * @param delay after which perform() is called
	 */
	public CustomAction(float delay) {
		this.delay = delay;
	}

	/**
	 * During creation you have to override perform method
	 * It will be called after delay as many times as loops defines
	 * 
	 * @param delay after which perform() is called
	 * @param loops counter how many times perform is called. 0 for infinite
	 */
	public CustomAction(float delay, int loops) {
		this(delay);
		this.loops = loops;
	}

	/**
	 * During creation you have to override perform method
	 * It will be called after delay as many times as loops defines
	 * 
	 * @param delay after which perform() is called
	 * @param loops counter how many times perform is called. 0 for infinite
	 * @param args custom arguments to be used in perform()
	 */
	public CustomAction(float delay, int loops, Object... args) {
		this(delay, loops);
		this.args = args;
	}

	/**
	 * override this method. Define behaviour.
	 * Actor will be automatically removed from manager after it finishes
	 */
	public abstract void perform();

	/**
	 * You can override it to define own behaviour on action finish.
	 * Method is called after action is finished.
	 * In case of multiple loops it's called after last loop.
	 */
	public void onFinish() {

	}

	@Override
	public void act(float delta) {
		//hack when you have some big lags like holding window clicked. It won't count time in this case.
		if (delta <= 0.1)
			stateTime += delta;

		//measure of time
		if ((stateTime - timeElapsed) / delay >= 1 && hasParent()) {
			loopCount++;
			timeElapsed += delay;
			
			//main logic will be here
			perform();

			//when not looping, finish after one call. When looping, check how many loops left
			if (loops == 1 || (loops > 0 && loopCount >= loops)) {
				onFinish();
				remove();
				resetAction();
			}
		}
	}

	/**
	 * Use this method to reset state of CustomAction.
	 * It's useful when you have actions defined in poolable objects.
	 */
	public void resetAction() {
		stateTime = 0;
		loopCount = 0;
		timeElapsed = 0;
	}

	/**
	 * @return boolean if action is already registered in CustomActionManager.
	 */
	public boolean isRegistered() {
		return this.hasParent();
	}

	/**
	 * @return how many times action already looped.
	 */
	public int getLoopCount() {
		return loopCount;
	}
	
	/**
	 * Unregisters and removes action
	 */
	public void unregister(){
		onFinish();
		remove();
		resetAction();
	}
}
