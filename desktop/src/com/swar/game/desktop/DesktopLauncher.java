package com.swar.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.swar.game.Game;

import static com.swar.game.utils.constants.GAME_NAME;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = GAME_NAME;
		config.width = 640;
		config.height = 960;
		config.resizable = false;
		new LwjglApplication(new Game(false, false), config);
	}
}
