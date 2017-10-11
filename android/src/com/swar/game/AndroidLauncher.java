package com.swar.game;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.swar.game.managers.GameConfig;


public class AndroidLauncher extends AndroidApplication {
	private SharedPreferences sPref;

	private Game instance = null;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		boolean[] list = loadBoolConfig();

		float posY = calibrate();

 		instance = new Game(list[0], list[1], posY);
		initialize(instance, config);
	}

	void clearFile(){
		sPref = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor ed = sPref.edit();
		ed.clear();
		ed.commit();
	}

	float calibrate(){
		float prefs;

		prefs = sPref.getFloat("yPos", 3.5f);

		return prefs;
	}


	boolean[] loadBoolConfig(){
		sPref = getPreferences(MODE_PRIVATE);
		boolean vButtons = sPref.getBoolean("vButtons", false);
		boolean vibration = sPref.getBoolean("vibration", true);

		boolean[] list = new boolean[]{vButtons, vibration};
		Log.e("loaded", ":" + vButtons + vibration);
		return list;
	}

	void saveConfig(boolean vButtons, boolean vibration, float posY){
		sPref = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor ed = sPref.edit();
		ed.putBoolean("vButtons", vButtons);
		ed.putBoolean("vibration", vibration);
		ed.putFloat("yPos", posY);

		Log.e("saved", ":" + vButtons + vibration + posY);
		ed.commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearFile();
		GameConfig gameConfig = new GameConfig();
		saveConfig(gameConfig.isvButtons(), gameConfig.isVibraion(), gameConfig.getPosY());

	}

}
