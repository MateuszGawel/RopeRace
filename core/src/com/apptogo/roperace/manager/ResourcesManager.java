package com.apptogo.roperace.manager;

import java.util.Comparator;

import com.apptogo.roperace.exception.ResourcesManagerException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class ResourcesManager {
	private static ResourcesManager INSTANCE;

	public static void create() {
		INSTANCE = new ResourcesManager();
	}

	public static void destroy() {
		INSTANCE.manager.clear();
		INSTANCE = null;
	}

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}

	public AssetManager manager;
	public Skin skin;

	private ResourcesManager() {
		manager = new AssetManager();
		manager.finishLoading();
	}

	/**
	 * Loads all resources
	 */
	public void loadResources() {
		//textures
		//e.g	manager.load("atlas.pack", TextureAtlas.class);

		//sounds
		//e.g	manager.load("sound.ogg", Sound.class);

		//textures
		//e.g	manager.load("texture.png", Texture.class);
	}

	/**
	 * Loads skin from file
	 */
	public void loadSkin() {
		manager.finishLoading();
		skin = new Skin(Gdx.files.internal("skin.json"), manager.get("menu_atlas.pack", TextureAtlas.class));
	}

	/**
	 * @param regionName
	 * @return atlasRegion by name
	 */
	public AtlasRegion getAtlasRegion(String regionName) {
		AtlasRegion region = null;

		for (TextureAtlas atlas : manager.getAll(TextureAtlas.class, new Array<TextureAtlas>())) {
			region = atlas.findRegion(regionName);

			if (region != null) {
				break;
			}
		}
		if (region == null) {
			throw new ResourcesManagerException("Couldn't find region: " + regionName);
		}
		return region;
	}

	/**
	 * @param soundName
	 * @return sound by name
	 */
	public Sound getSound(String soundName) {
		return manager.get(soundName + ".ogg");
	}

	/**
	 * Returns resource without checking its type
	 * 
	 * @param name of resource
	 * @return resource of type T
	 */
	public <T> T get(String name) {
		return manager.get(name);
	}

	/**
	 * Method for getting all regions for animation
	 * 
	 * @param pattern of atlasRegion name
	 * @return array of atlasRegions sorted by its number
	 */
	public Array<AtlasRegion> getRegions(String pattern) {
		Array<AtlasRegion> regions = new Array<AtlasRegion>();

		for (TextureAtlas atlas : manager.getAll(TextureAtlas.class, new Array<TextureAtlas>())) {
			for (AtlasRegion region : atlas.getRegions())
				if (region.name.startsWith(pattern))
					regions.add(region);
		}

		Comparator<AtlasRegion> comparator = new Comparator<AtlasRegion>() {
			@Override
			public int compare(AtlasRegion arg0, AtlasRegion arg1) {
				int no0 = Integer.parseInt(arg0.name.replaceAll("[^\\d]", ""));
				int no1 = Integer.parseInt(arg1.name.replaceAll("[^\\d]", ""));

				return (no0 - no1);
			}
		};

		regions.sort(comparator);

		return regions;
	}
}
