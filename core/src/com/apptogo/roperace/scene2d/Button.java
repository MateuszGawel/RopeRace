package com.apptogo.roperace.scene2d;

import com.apptogo.roperace.manager.ResourcesManager;
import com.apptogo.roperace.plugin.SoundPlugin;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Button extends com.badlogic.gdx.scenes.scene2d.ui.Button {
    public static Button get(String buttonName) {
        return new Button(ResourcesManager.getInstance().skin, buttonName);
    }

    public static Button get() {
        return new Button(ResourcesManager.getInstance().skin);
    }

    public Button(Skin skin, String buttonName) {
        super(skin, buttonName);
        addClickSound();
    }

    public Button(Skin skin) {
        super(skin);
        addClickSound();
    }

    public Button() {
        super();
        addClickSound();
	}

	public Button position(float x, float y)
    {
        this.setPosition(x, y);
        return this;
    }

    public Button size(float width, float height)
    {
        this.setSize(width, height);

        return this;
    }
    
    public Button scale(float scale)
    {
    	setSize(getWidth()*scale, getHeight()*scale);
    	return this;
    }

    public Button centerX()
    {
        this.setPosition(-this.getWidth() / 2f, this.getY());
        return this;
    }

    public Button centerY()
    {
        this.setPosition(this.getX(), -this.getHeight() / 2f);
        return this;
    }

    public Button setListener(EventListener listener)
    {    	
        this.addListener(listener);
        return this;
    }

    public Button visible(boolean visible)
    {
        this.setVisible(visible);
        return this;
    }
    
    private void addClickSound(){
    	addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
//				SoundPlugin.playSingleSound("click");
			}
		});
    }
}
