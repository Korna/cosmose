package com.swar.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swar.game.managers.Content.Content;
import com.swar.game.managers.GameConfig;
import com.swar.game.managers.GameStateManagement;

import static com.swar.game.utils.constants.GAME_HEIGHT;
import static com.swar.game.utils.constants.GAME_WIDTH;

public class Game extends ApplicationAdapter {
	private GameConfig gameConfig;

	private SpriteBatch batch;
	private OrthographicCamera maincamera;

	private boolean DEBUG = false;

	private final float SCALE = 4.0f;// раньше было 2f
	private GameStateManagement gsm;

	public static Content res;

	public Game(boolean vButtons, boolean vibration, float posY){
		gameConfig = new GameConfig(vibration, vButtons, posY);
	}


	@Override
	public void create () {
		res = new Content();
		res.resLoader();


		batch = new SpriteBatch();
		maincamera = new OrthographicCamera();
		maincamera.setToOrtho(false, GAME_WIDTH/SCALE, GAME_HEIGHT/SCALE);

		gsm = new GameStateManagement(this);
	}


	@Override
	public void render () {
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		gsm.dispose();
	}

	@Override
	public void resize (int width, int height) {
		gsm.resize(width/4, height/4);//ресайзит всё. важный элемент
	}


	public OrthographicCamera getCamera(){
		return maincamera;
	}
	public SpriteBatch getBatch(){
		return batch;
	}


}
