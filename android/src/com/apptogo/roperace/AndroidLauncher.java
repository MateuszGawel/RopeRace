package com.apptogo.roperace;

import com.apptogo.roperace.callback.GameCallback;
import com.apptogo.roperace.main.Main;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		GameCallback gameCallback = new GameCallbackImpl();
		
		initialize(new Main(gameCallback), config);
	}
}
