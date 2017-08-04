package com.swar.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.swar.game.Game;

import static com.swar.game.utils.constants.GAME_HEIGHT;
import static com.swar.game.utils.constants.GAME_WIDTH;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Warrior";
		config.width = GAME_WIDTH;
		config.height = GAME_HEIGHT;
		config.resizable = false;
		new LwjglApplication(new Game(), config);
	}
}
