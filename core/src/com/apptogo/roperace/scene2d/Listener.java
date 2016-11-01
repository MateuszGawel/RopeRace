package com.apptogo.roperace.scene2d;

import com.apptogo.roperace.main.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Listener {

	public static ClickListener click(final Main game, final Screen screen) {
        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(screen);
            }
        };

        return listener;
    }
	
	public static ClickListener preferences(final String preferencesName, final String valueKey, final boolean value)
    {
        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	Gdx.app.getPreferences(preferencesName).putBoolean(valueKey, value).flush();
            }
        };

        return listener;
    }

}
