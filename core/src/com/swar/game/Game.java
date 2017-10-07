package com.swar.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swar.game.managers.Content;
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

	public Game(boolean vButtons, boolean vibration){
		gameConfig = new GameConfig(vibration, vButtons);
	}


	@Override
	public void create () {


		res = new Content();
		resLoader(res);


		batch = new SpriteBatch();
		maincamera = new OrthographicCamera();
		maincamera.setToOrtho(false, GAME_WIDTH/SCALE, GAME_HEIGHT/SCALE);

		gsm = new GameStateManagement(this);


	}

	private void resLoader(Content res){
		res.loadTexture("images/splash.png", "splash");
		res.loadTexture("images/background.png", "background_1");
		res.loadTexture("images/background_menu.png", "background_menu");
		res.loadTexture("images/gameHud.png", "hudCredits");

		res.loadTexture("images/hud.png", "hud");
		res.loadTexture("images/bgs.png", "bgs");

		res.loadTexture("sprites/weapon_1.png", "weapon_1");
		res.loadTexture("sprites/weapon_2.png", "weapon_2");
		res.loadTexture("sprites/weapon_3.png", "weapon_3");
		res.loadTexture("sprites/weapon_4.png", "weapon_4");

		res.loadTexture("sprites/asteroid_1.png", "asteroid_1");
		res.loadTexture("sprites/asteroid_2.png", "asteroid_2");
		res.loadTexture("sprites/asteroid_3.png", "asteroid_3");
		res.loadTexture("sprites/enemy.png", "enemy");


		res.loadTexture("sprites/bonus_1.png", "bonus_1");

		res.loadTexture("sprites/bullet_1.png", "bullet_1");
		res.loadTexture("sprites/bullet_2.png", "bullet_2");
		res.loadTexture("sprites/bullet_3.png", "bullet_3");
		res.loadTexture("sprites/bullet_4.png", "bullet_4");

		res.loadTexture("sprites/ship_1.png", "ship_1");
		res.loadTexture("sprites/ship_1_left.png", "ship_1_l");
		res.loadTexture("sprites/ship_1_right.png", "ship_1_r");

		res.loadTexture("sprites/ship_2.png", "ship_2");
		res.loadTexture("sprites/ship_2_left.png", "ship_2_l");
		res.loadTexture("sprites/ship_2_right.png", "ship_2_r");

		res.loadTexture("sprites/ship_3.png", "ship_3");
		res.loadTexture("sprites/ship_3_left.png", "ship_3_l");
		res.loadTexture("sprites/ship_3_right.png", "ship_3_r");

		res.loadTexture("sprites/ship_4.png", "ship_4");
		res.loadTexture("sprites/ship_4_left.png", "ship_4_l");
		res.loadTexture("sprites/ship_4_right.png", "ship_4_r");

	}

	@Override
	public void render () {

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
			Gdx.app.exit();

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
