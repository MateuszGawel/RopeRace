package com.apptogo.roperace.scene2d;

import com.apptogo.roperace.main.Main;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Widget extends Table {

    final float hiddenY = 2 * Main.SCREEN_HEIGHT;
    Image black;

    public Widget()
    {
        super();

        black = Image.get("widgetBlack").size(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT).centerX().centerY().visible(false);
        black.addListener(getToggleListener());

        Image image = Image.get("paperBig");
        setSize(image.getWidth(), image.getHeight());
        setBackground(image.getDrawable());

        setPosition(-getWidth() / 2f, hiddenY);
    }

    public void toggle()
    {
        if (getY() == hiddenY)
        {
            addAction(Actions.moveTo(getX(), -getHeight() / 2f, 0.5f, Interpolation.elasticOut));
            black.visible(true);
        }
        else
        {
            addAction(Actions.moveTo(getX(), hiddenY));
            black.visible(false);
        }
    }

    public ClickListener getToggleListener()
    {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                toggle();
            }
        };
    }

    public void addToStage(Stage stage)
    {
        stage.addActor(black);
        stage.addActor(this);
        black.toFront();
        toFront();
    }
}
