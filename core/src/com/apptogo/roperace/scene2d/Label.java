package com.apptogo.roperace.scene2d;

import com.apptogo.roperace.manager.ResourcesManager;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Label extends com.badlogic.gdx.scenes.scene2d.ui.Label {
    public static Label get(String text) {
        return new Label(text, ResourcesManager.getInstance().skin);
    }

    public static Label get(String text, String labelStyle) {
    	ResourcesManager.getInstance().skin.getFont(labelStyle).getData().markupEnabled = true;
        return new Label(text, ResourcesManager.getInstance().skin, labelStyle);
    }

    public Label(String text, Skin skin) {
        super(text, skin);
    }

    public Label(String text, Skin skin, String labelStyle) {
        super(text, skin, labelStyle);
    }

    public Label position(float x, float y)
    {
        this.setPosition(x, y);
        return this;
    }

    public Label size(float width, float height)
    {
        this.setSize(width, height);

        return this;
    }

    public Label centerX()
    {
        this.setPosition(-this.getWidth() / 2f, this.getY());
        return this;
    }
    
    public Label centerX(float offset)
    {
        this.setPosition(-this.getWidth() / 2f + offset, this.getY());
        return this;
    }

    public Label centerY()
    {
        this.setPosition(this.getX(), -this.getHeight() / 2f);
        return this;
    }
    
    public Label centerY(float offset)
    {
        this.setPosition(this.getX(), -this.getHeight() / 2f + offset);
        return this;
    }

    public Label setListener(EventListener listener)
    {
        this.addListener(listener);
        return this;
    }
}
