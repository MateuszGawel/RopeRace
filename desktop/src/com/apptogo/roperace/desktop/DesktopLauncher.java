package com.apptogo.roperace.desktop;

import com.apptogo.roperace.callback.GameCallback;
import com.apptogo.roperace.main.Main;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) Main.SCREEN_WIDTH;
		config.height = (int) Main.SCREEN_HEIGHT;
		config.samples = 3;
		
		GameCallback gameCallback = new GameCallbackImpl();

		new LwjglApplication(new Main(gameCallback), config);
	}
}
