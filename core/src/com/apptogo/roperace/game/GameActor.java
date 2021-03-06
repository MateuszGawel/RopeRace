package com.apptogo.roperace.game;

import java.util.HashMap;
import java.util.Map;

import com.apptogo.roperace.exception.AnimationException;
import com.apptogo.roperace.exception.PluginException;
import com.apptogo.roperace.main.Main;
import com.apptogo.roperace.physics.UserData;
import com.apptogo.roperace.plugin.AbstractPlugin;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool.Poolable;

public class GameActor extends AbstractActor implements Poolable {
	private static final Logger LOGGER = new Logger(GameActor.class.getName());

	private Body body;
	private float customOffsetX, customOffsetY;
	private boolean fixedRotation;
	
	public GameActor(String name) {
		super(name);
	}

	Map<String, AbstractPlugin> plugins = new HashMap<String, AbstractPlugin>();
	Array<AbstractPlugin> pluginsQueue = new Array<AbstractPlugin>();

	@Override
	public void act(float delta) {
		super.act(delta);

		try {
			setCurrentAnimation();

			//we add customOffset to adjust animation position (and actor) with body to make game enjoyable
			//we add animation deltaOffset and few lines below we subtracting it. Thanks that actor and graphic is always in the same position.
			//more information about deltaOffset in AnimationActor

			setPosition(body.getPosition().x + customOffsetX + currentAnimation.getDeltaOffset().x, body.getPosition().y + customOffsetY + currentAnimation.getDeltaOffset().y);
			setSize(currentAnimation.getWidth(), currentAnimation.getHeight());
			if(!fixedRotation){
				setRotation((float)Math.toDegrees(body.getAngle()));
			}
			setOrigin(UserData.get(body).width/2, UserData.get(body).height/2);
			
			currentAnimation.position(getX() - currentAnimation.getDeltaOffset().x, getY() - currentAnimation.getDeltaOffset().y);
			currentAnimation.setSize(UserData.get(body).width, UserData.get(body).height);
			currentAnimation.setRotation(getRotation());
			currentAnimation.setOrigin(getOriginX(), getOriginY());
			currentAnimation.act(delta);
		} catch (AnimationException e) {
			LOGGER.debug("Actor doesn't have animation so it won't be handled", e);
		}

		for (AbstractPlugin plugin : pluginsQueue) {
			plugin.run();
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(currentAnimation != null){
			currentAnimation.draw(batch, parentAlpha);
		}
	}

	public void addPlugin(AbstractPlugin plugin) {
		plugin.setActor(this);
		plugins.put(plugin.getClass().getSimpleName(), plugin);
		pluginsQueue.add(plugin);
	}

	public void removePlugin(String name) throws PluginException {
		AbstractPlugin plugin = plugins.get(name);
		if (plugin == null)
			throw new PluginException("Actor: '" + getName() + "' doesn't have plugin: '" + name);

		plugins.remove(name);
		pluginsQueue.removeValue(plugin, true);
	}

	public void modifyCustomOffsets(float deltaX, float deltaY) {
		customOffsetX += deltaX;
		customOffsetY += deltaY;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;

		customOffsetX = -UserData.get(body).width / 2f;
		customOffsetY = -UserData.get(body).height / 2f;
	}

	/**
	 * @param plugin name. Always getSimpleName() of plugin class
	 * @return plugin
	 * @throws PluginException 
	 */
	public <T extends AbstractPlugin> T getPlugin(String name) throws PluginException {
		@SuppressWarnings("unchecked")
		T plugin = (T) plugins.get(name);
		if (plugin == null)
			throw new PluginException("Actor: '" + getName() + "' doesn't have plugin: '" + name + "'");
		return plugin;
	}

	/**
	 * @param plugin class. Always getSimpleName() of plugin class
	 * @return plugin
	 * @throws PluginException 
	 */
	public <T extends AbstractPlugin> T getPlugin(Class<T> clazz) throws PluginException {
		return getPlugin(clazz.getSimpleName());
	}

	/* ----------- POOL STUFF ----------- */
	@Override
	public void reset() {
		body.setActive(false);
		remove();
	}

	public void init(int speedLevel, float arg) {
		init();
	}

	public void init(int speedLevel) {
		init();
	}

	public void init() {
		body.setActive(true);
		Main.getInstance().getCurrentScreen().getFrontStage().addActor(this);
	}

	@Override
	public boolean remove() {
		// TODO Auto-generated method stub
		return super.remove();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		super.clear();
	}

	public boolean isFixedRotation() {
		return fixedRotation;
	}

	public void setFixedRotation(boolean fixedRotation) {
		this.fixedRotation = fixedRotation;
	}
	
	
	
}
