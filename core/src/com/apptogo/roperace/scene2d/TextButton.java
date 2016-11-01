package com.apptogo.roperace.scene2d;

import com.apptogo.roperace.manager.ResourcesManager;
import com.apptogo.roperace.plugin.SoundPlugin;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TextButton extends com.badlogic.gdx.scenes.scene2d.ui.TextButton {
    public static TextButton get(String label, String buttonName) {
        return new TextButton(label, ResourcesManager.getInstance().skin, buttonName);
    }

    public static TextButton get(String label) {
        return new TextButton(label, ResourcesManager.getInstance().skin);
    }

    public TextButton(String label, Skin skin, String buttonName) {
        super(label, skin, buttonName);
        this.getLabelCell().padLeft(90).padBottom(14);
        addClickSound();
    }

    public TextButton(String label, Skin skin) {
        super(label, skin);
        addClickSound();
    }

    public TextButton position(float x, float y)
    {
        this.setPosition(x, y);
        return this;
    }

    public TextButton size(float width, float height)
    {
        this.setSize(width, height);

        return this;
    }

    public TextButton centerX()
    {
        this.setPosition(-this.getWidth() / 2f, this.getY());
        return this;
    }

    public TextButton centerY()
    {
        this.setPosition(this.getX(), -this.getHeight() / 2f);
        return this;
    }

    public TextButton setListener(EventListener listener)
    {
        this.addListener(listener);
        return this;
    }
    
    private void addClickSound(){
    	addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundPlugin.playSingleSound("click");
			}
		});
    }
}
