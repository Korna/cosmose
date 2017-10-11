package com.swar.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swar.game.Types.BonusType;
import com.swar.game.Types.BulletType;
import com.swar.game.Types.ShipType;
import com.swar.game.Types.WeaponType;
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

	public Game(boolean vButtons, boolean vibration, float posY){
		gameConfig = new GameConfig(vibration, vButtons, posY);
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

		for(WeaponType type : WeaponType.values()){
			String name = type.name();
			res.loadTexture("sprites/"+name+".png", name);
		}

		for(BulletType type : BulletType.values()){
			String name = type.name();
			res.loadTexture("sprites/" + name + ".png", name);
		}



		res.loadTexture("sprites/asteroid_1.png", "asteroid_1");
		res.loadTexture("sprites/asteroid_2.png", "asteroid_2");
		res.loadTexture("sprites/asteroid_3.png", "asteroid_3");
		res.loadTexture("sprites/enemy.png", "enemy");


		for(BonusType type : BonusType.values()){
			String name = type.name();
			res.loadTexture("sprites/" + name + ".png", name);
		}

		res.loadTexture("sprites/explosion_1.png", "explosion_1");
		res.loadTexture("sprites/explosion_2.png", "explosion_2");

		for(ShipType type : ShipType.values()){
			String name = type.name();
			res.loadTexture("sprites/" + name + ".png", name);
			res.loadTexture("sprites/"+name+"_left.png", name +"_l");
			res.loadTexture("sprites/"+name+"_right.png", name +"_r");
		}


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
