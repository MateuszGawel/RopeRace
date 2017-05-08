package com.apptogo.roperace.scene2d;

import com.apptogo.roperace.manager.ResourcesManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;

public class Image extends com.badlogic.gdx.scenes.scene2d.ui.Image {

	private TextureRegion region;

	public static Image get(String regionName) {
		return new Image(ResourcesManager.getInstance().getAtlasRegion(regionName));
	}
	
	public static Image getFrodmTexture(String textureName) {
		return new Image(ResourcesManager.getInstance().getTexture(textureName));
	}

	public static Image get(Texture texture) {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return new Image(texture);
	}

	public Image(TextureRegion region) {
		super(region);
		this.region = region;
	}

	public Image(Texture region) {
		super(region);
		this.region = new TextureRegion(region);
	}

	public Image position(float x, float y) {
		this.setPosition(x, y);
		return this;
	}

	public Image size(float width, float height) {
		this.setSize(width, height);

		return this;
	}

	public Image width(float width) {
		this.setWidth(width);

		return this;
	}

	public Image centerX() {
		this.setPosition(-this.getWidth() / 2f, this.getY());
		return this;
	}

	public Image centerX(float offset) {
		this.setPosition(-this.getWidth() / 2f + offset, this.getY());
		return this;
	}

	public Image centerY() {
		this.setPosition(this.getX(), -this.getHeight() / 2f);
		return this;
	}

	public Image centerY(float offset) {
		this.setPosition(this.getX(), -this.getHeight() / 2f + offset);
		return this;
	}

	public Image visible(boolean visible) {
		setVisible(visible);
		return this;
	}

	public Image scale(float scale) {
		setSize(getWidth() * scale, getHeight() * scale);
		return this;
	}

	public Image action(Action action) {
		this.addAction(action);
		return this;
	}

	public Image back() {
		this.toBack();
		return this;
	}

	public TextureRegion getRegion() {
		return region;
	}

}
