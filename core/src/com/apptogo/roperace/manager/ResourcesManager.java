package com.apptogo.roperace.manager;

import java.util.Comparator;

import com.apptogo.roperace.exception.ResourcesManagerException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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
		preloadResources();
		
		//prepare loader for tiled maps
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
	}
	
	private void preloadResources(){
		manager.load("logo.png", Texture.class);
		manager.finishLoading();
	}
	
	/**
	 * Loads all resources
	 */
	public void loadResources() {
		//textures
//		manager.load("menu_atlas.pack", TextureAtlas.class);
		manager.load("atlas.pack", TextureAtlas.class);

		//sounds
		//e.g	manager.load("sound.ogg", Sound.class);

		//textures
//		manager.load("roperace-logo.png", Texture.class);
//		manager.load("play-button.png", Texture.class);
//		manager.load("play-button_shadow.png", Texture.class);
//		manager.load("touchpad-background.png", Texture.class);
//		manager.load("touchpad-knob.png", Texture.class);
//		manager.load("circle.png", Texture.class);
//		manager.load("world1.png", Texture.class);
//		manager.load("world1_shadow.png", Texture.class);
//		manager.load("world2.png", Texture.class);
//		manager.load("world2_shadow.png", Texture.class);
//		manager.load("ball.png", Texture.class);
//		manager.load("viewfinder.png", Texture.class);
//		manager.load("chain.png", Texture.class);
//		manager.load("chain-end.png", Texture.class);
//		manager.load("hoop-top.png", Texture.class);
//		manager.load("hoop-bottom.png", Texture.class);
//		
//		manager.load("circle-small.png", Texture.class);
//		manager.load("back-button.png", Texture.class);
//		manager.load("back-button_shadow.png", Texture.class);
//		manager.load("worlds-button.png", Texture.class);
//		manager.load("worlds-button_shadow.png", Texture.class);
//		manager.load("restart.png", Texture.class);
//		manager.load("restart_shadow.png", Texture.class);
//		manager.load("ok.png", Texture.class);
//		manager.load("ok_shadow.png", Texture.class);
//		manager.load("bonus.png", Texture.class);
//		manager.load("bonus_shadow.png", Texture.class);
//		manager.load("locker.png", Texture.class);
//		manager.load("locker_shadow.png", Texture.class);
//		manager.load("balls.png", Texture.class);
//		manager.load("balls_shadow.png", Texture.class);
//		
//		manager.load("1.png", Texture.class);
//		manager.load("2.png", Texture.class);
//		manager.load("3.png", Texture.class);
//		manager.load("4.png", Texture.class);
//		manager.load("5.png", Texture.class);
//		manager.load("6.png", Texture.class);
//		manager.load("7.png", Texture.class);
//		manager.load("8.png", Texture.class);
//		manager.load("9.png", Texture.class);
//		manager.load("1_shadow.png", Texture.class);
//		manager.load("2_shadow.png", Texture.class);
//		manager.load("3_shadow.png", Texture.class);
//		manager.load("4_shadow.png", Texture.class);
//		manager.load("5_shadow.png", Texture.class);
//		manager.load("6_shadow.png", Texture.class);
//		manager.load("7_shadow.png", Texture.class);
//		manager.load("8_shadow.png", Texture.class);
//		manager.load("9_shadow.png", Texture.class);
//		
//		manager.load("ball1.png", Texture.class);
//		manager.load("ball2.png", Texture.class);
//		manager.load("ball3.png", Texture.class);
//		manager.load("ball4.png", Texture.class);
//		
//		manager.load("medal.png", Texture.class);
//		manager.load("medal_shadow.png", Texture.class);
//		
//		manager.load("diamond.png", Texture.class);
//		manager.load("star.png", Texture.class);
		
		manager.finishLoading();
		
		loadSkin();
	}		

	/**
	 * Loads skin from file
	 */
	public void loadSkin() {
		skin = new Skin(Gdx.files.internal("skin.json"), manager.get("atlas.pack", TextureAtlas.class));
		manager.finishLoading();
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
	 * Search for texture png
	 * 
	 * @param textureName
	 * @return texture by name
	 */
	public Texture getTexture(String textureName) {
		return manager.get(textureName + ".png");
	}
	
	/**
	 * @param soundName
	 * @return sound by name
	 */
	public Sound getSound(String soundName) {
		return manager.get(soundName + ".ogg");
	}
	
	/**
	 * Loads given level. e.g for 1 it loads levels/level1.tmx
	 * then returns it 
	 * 
	 * @param levelNumber
	 * @return TiledMap of level
	 */
	public TiledMap loadAndGetTiledMap(int levelNumber, int worldNo) {
		String levelPath = "levels/level-" + worldNo + "-" + levelNumber + ".tmx";
		manager.load(levelPath, TiledMap.class);
		manager.finishLoading();
		return manager.get(levelPath);
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

		if(regions.size == 0){
			Texture texture = getTexture(pattern);
			AtlasRegion region = new AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
			regions.add(region);
		}
	
		
		Comparator<AtlasRegion> comparator = new Comparator<AtlasRegion>() {
			@Override
			public int compare(AtlasRegion arg0, AtlasRegion arg1) {
				try{
					int no0 = Integer.parseInt(arg0.name.replaceAll("[^\\d]", ""));
					int no1 = Integer.parseInt(arg1.name.replaceAll("[^\\d]", ""));
					return (no0 - no1);
				}
				catch(NumberFormatException e){
					//TODO add logging here
				}
				
				return 0;
				
			}
		};

		regions.sort(comparator);

		return regions;
	}
}
