package com.swar.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swar.game.managers.Content;
import com.swar.game.managers.GameStateManagement;
import com.swar.game.resources.bulletData.bulletDB;
import com.swar.game.resources.shipData.shipDB;
import com.swar.game.resources.weaponData.weaponDB;

import static com.swar.game.utils.constants.GAME_HEIGHT;
import static com.swar.game.utils.constants.GAME_WIDTH;

public class Game extends ApplicationAdapter {

	private SpriteBatch batch;
	private OrthographicCamera maincamera;
	private OrthographicCamera camera_hud;


	private boolean DEBUG = false;

	private final float SCALE = 2.0f;// раньше было 2f
	private GameStateManagement gsm;

	//DB
	public static weaponDB gameWeapons;
	public static bulletDB gameBullets;
	public static shipDB gameShips;

	public static Content res;

	@Override
	public void create () {
		gameBullets = new bulletDB();//создаем экземпляры даз банных
		gameWeapons = new weaponDB();
		gameShips = new shipDB();

		res = new Content();
		resLoader(res);


		batch = new SpriteBatch();
		maincamera = new OrthographicCamera();
		maincamera.setToOrtho(false, GAME_WIDTH/2, GAME_HEIGHT/2);

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

		res.loadTexture("sprites/asteroid_1.png", "asteroid_1");
		res.loadTexture("sprites/asteroid_2.png", "asteroid_2");
		res.loadTexture("sprites/asteroid_3.png", "asteroid_3");

		res.loadTexture("sprites/bonus_1.png", "bonus_1");

		res.loadTexture("sprites/bullet_1.png", "bullet_1");
		res.loadTexture("sprites/bullet_2.png", "bullet_2");

		res.loadTexture("sprites/ship_1.png", "ship_1");
		res.loadTexture("sprites/ship_1_left.png", "ship_1_l");
		res.loadTexture("sprites/ship_1_right.png", "ship_1_r");

		res.loadTexture("sprites/ship_2.png", "ship_2");
		res.loadTexture("sprites/ship_2_left.png", "ship_2_l");
		res.loadTexture("sprites/ship_2_right.png", "ship_2_r");

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
		gsm.resize((int)(width / 2), (int)(height / 2));//ресайзит всё. важный элемент
	}


	public OrthographicCamera getCamera(){
		return maincamera;
	}
	public OrthographicCamera getCameraHud(){
		return camera_hud;
	}
	public SpriteBatch getBatch(){
		return batch;
	}

}
