package com.swar.game.states;

import android.util.Log;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.swar.game.ShipType;
import com.swar.game.entities.*;
import com.swar.game.managers.*;
import com.swar.game.utils.Randomizer;
import com.swar.game.utils.Singleton;

import java.util.ArrayList;
import java.util.HashSet;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.swar.game.utils.constants.*;

/**
 * Created by Koma on 17.01.2017.
 */
public class PlayClassicState extends GameState{

    private Box2DDebugRenderer b2dr;
    private HUD hud;

    private GameContactListener cl;
    private World world;
    private Player player;
    private Player shadowPlayer;



    private ObjectHandler objectHandler;

    private BodyBuilder bodyBuilder;
    InterfaceManager interfaceManager;
    boolean CONFIG_VIBRATION;
    final static int GAME_TIME = 60;

    private boolean available = false;

    private Randomizer randomizer = new Randomizer();

    int index = 0;//переменная для сохранения индекса кадра

    public PlayClassicState(GameStateManagement gsm) {
        super(gsm);
        cl = new GameContactListener();

        world = gsm.world;
        player = gsm.player;
        bodyBuilder = new BodyBuilder(world);

        GameConfig gameConfig = new GameConfig();
        CONFIG_VIBRATION = gameConfig.isVibraion();


        Body body = bodyBuilder.createShadow(GAME_WIDTH / 2, 15, GAME_WIDTH/15, GAME_WIDTH/10);
        //здесь по индексу передаём корабль из ДБ
        shadowPlayer = new Player(body, null, ShipType.getShip(ShipType.ship_2));
       // body.setUserData(shadowPlayer);

       // Gdx.input.setInputProcessor(new GameInputProcessor());

        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();
        batch = new SpriteBatch();


        bodyBuilder.createBorder(BORDER_HORIZONTAL, GAME_WIDTH, 0, GAME_WIDTH, 1);
        bodyBuilder.createBorder(BORDER_HORIZONTAL, GAME_WIDTH, GAME_HEIGHT, GAME_WIDTH, 1);
        bodyBuilder.createBorder("border", 1, GAME_HEIGHT, 1, GAME_HEIGHT);
        bodyBuilder.createBorder("border", GAME_WIDTH, GAME_HEIGHT, 1, GAME_HEIGHT);



        hud = new HUD(player, State.PLAY);
        available = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);


        objectHandler = new ObjectHandler(new Array<>(), new Array<>(), new Array<>(), world);
        interfaceManager = new InterfaceManager(available, 0.5f, 4.5f);

    }








    @Override
    public void update(float delta) {
        player.timeInGame += delta;
        if(player.timeInGame >= GAME_TIME){
            player.timeInGame = 0;
            instance.firstRun = false;

            objectHandler.clearAll();

            gsm.setState(State.HUB);

            return;
        }

        player.ship.setHp(player.ship.getHp() + cl.getHp());

        if(player.ship.getHp() <= 0){
            player.setDead(true);




            objectHandler.clearAll();

            gsm.setState(State.DEATH);

            return;
        }


        interfaceManager.inputUpdate();
        inputAction(interfaceManager.shot, interfaceManager.horizontalForce, interfaceManager.verticalForce);

        shadowMovement();

        player.update(delta);

        int energy = cl.getEnergyAndClear();
        player.ship.setEnergy(player.ship.getEnergy() + energy);

        if(!instance.firstRun){
            float[] move = {0,0};
            try {
                move = instance.moveHistoryList.get(index);
                shadowPlayer.getBody().setLinearVelocity(move[0], move[1]);

            }catch(IndexOutOfBoundsException e){
                shadowPlayer.getBody().setLinearVelocity(0, 0);
                try {
                    Log.e("shadow:", "indexOutOfBOunds");
                }catch(RuntimeException re){
                    System.out.println("indexOutOfBOunds");
                }
            }finally{
                if(move[0] > 0)
                    shadowPlayer.ship_r();
                else
                if(move[0] < 0)
                    shadowPlayer.ship_l();
                else
                if(move[0] == 0)
                    shadowPlayer.ship();

                index++;
            }
            shadowPlayer.update(delta);

        }






        if(randomizer.chanceAsteroid()){
            Body asteroidBody = bodyBuilder.createAsteroid(randomizer.getCoordinateAsteroid(),GAME_HEIGHT-45);

            Asteroid a = new Asteroid(asteroidBody);
            asteroidBody.setUserData(a);

            objectHandler.add(a);
        }




        //удаление астероидов
        Array<Body> bodies = cl.getBodiesToRemove();

        if(cl.shadowToRemove!=null){
            world.destroyBody(cl.shadowToRemove);
            cl.shadowToRemove = null;
            shadowPlayer.setDead(true);
        }

        //TODO оптимизировать трансформацию AL в A
        ArrayList<Body> list = new ArrayList<>();
        for(Body b : bodies){
            list.add(b);

        }

        HashSet<Body> set = new HashSet<>(list);

        for(Body body : set){


            try{
                Asteroid asteroid = (Asteroid) body.getUserData();
                objectHandler.remove(asteroid);
                try {
                    if (randomizer.chanceBonus()) {
                        Body bonusBody = bodyBuilder.createBonus(body.getPosition().x, body.getPosition().y);

                        Bonus b = new Bonus(bonusBody);
                        bonusBody.setUserData(b);
                        objectHandler.add(b);
                    }
                }catch(Exception e){
                    System.out.printf(e.toString() + "\n");
                }
            }catch(Exception e){
                try {
                    objectHandler.remove((Bullet) body.getUserData());
                }catch(Exception bonus){
                    objectHandler.remove((Bonus) body.getUserData());
                }
            }


            world.destroyBody(body);

        }

        cl.clearList();
        //TODO сделать потоки безопасными

                for(int i = 0; i < objectHandler.listAsteroid.size; ++i) {
                    Asteroid asteroid = objectHandler.listAsteroid.get(i);
                    Vector2 targetPosition = new Vector2(0, asteroid.speed *1.1f);


                   // asteroid.getBody().applyForce(targetPosition, asteroid.getBody().getWorldCenter(), true);
                    asteroid.getBody().setLinearVelocity(targetPosition);
                    asteroid.update(delta);
                }




                for(int i = 0; i < objectHandler.listBulletPlayer.size; ++i){
                    Bullet bullet = objectHandler.listBulletPlayer.get(i);
                    bullet.getBody().setLinearVelocity(bullet.currentSpeed, bullet.speedY);
                    bullet.update(delta);
                }



        for(int i = 0; i < objectHandler.listBonus.size; ++i){
            Bonus bonus = objectHandler.listBonus.get(i);
            bonus.setExistTime(bonus.getExistTime() + delta);
            if(bonus.getExistTime() > 30){
                world.destroyBody(bonus.getBody());

                objectHandler.listBonus.removeIndex(i);
                --i;
            }

        }


        batch.setProjectionMatrix(maincamera.combined);

        doWorldStep(delta);
    }

    float f1 = random.nextFloat()+0.3f;
    float f2 = random.nextFloat()+0.3f;
    float f3 = random.nextFloat()+0.3f;
    @Override
    public void render() {
        Gdx.gl.glClearColor(f1, f2, f3, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        if(DEBUG_RENDER)
            b2dr.render(world, maincamera.combined);


        objectHandler.render(batch);

        player.render(batch);
        if(!instance.firstRun && !shadowPlayer.isDead())
            shadowPlayer.render(batch);

        hud.render(batch_hud);
    }

    private void inputAction(boolean shot, int horizontal, int vertical){
        if(horizontal < 0)
            player.ship_l();
        if(horizontal > 0)
            player.ship_r();
        if(horizontal == 0)
            player.ship();
        if(shot){
            if(player.ship.getEnergy() > 0){
                playerShot(available);

                player.ship.setEnergy(player.ship.getEnergy() - 1);
            }
        }

        player.getBody().setLinearVelocity(horizontal * player.getSpeed(), vertical * player.getSpeed());
    }

    private void playerShot(boolean vibrate){
        float x = player.getBody().getPosition().x;
        float y = player.getBody().getPosition().y;

        String type = player.ship.weapons.get(0).bulletModel.pierceType.name();


        Body bulletBody;
        Bullet b;

        if(player.ship.weapons.size() > 1){

            bulletBody = bodyBuilder.createBulletPlayer(x - 12, y, type);
            b = new Bullet(bulletBody, player.ship.weapons.get(0).bulletModel.bulletType, player.ship.weapons.get(0).bulletModel);
            bulletBody.setUserData(b);
            objectHandler.add(b);

            bulletBody = bodyBuilder.createBulletPlayer(x + 12, y, type);
            b = new Bullet(bulletBody, player.ship.weapons.get(1).bulletModel.bulletType, player.ship.weapons.get(0).bulletModel);
            bulletBody.setUserData(b);
            objectHandler.add(b);

        }else{

            bulletBody = bodyBuilder.createBulletPlayer(x, y + 5, type);
            b = new Bullet(bulletBody, player.ship.weapons.get(0).bulletModel.bulletType, player.ship.weapons.get(0).bulletModel);
            bulletBody.setUserData(b);
            objectHandler.add(b);
        }

        if(vibrate)
            Gdx.input.vibrate(VIBRATION_LONG);
    }

    @Override
    public void dispose() {
        //tex_background.dispose();
        b2dr.dispose();
        world.dispose();

    }

    public void cameraUpdate(float delta){
        Vector3 position = maincamera.position;
        position.x = maincamera.position.x + (player.getPosition().x * PPM - maincamera.position.x) * .4f;
        position.y = maincamera.position.y + (player.getPosition().y * PPM - maincamera.position.y) * .4f;
        maincamera.position.set(position);
        maincamera.update();
    }






    private void shadowMovement(){

        int horizontalForce = 0;
        int verticalForce = 0;
        int shipSpeed = player.getSpeed();


        if(instance.firstRun)
            instance.moveHistoryList.add(new float[] {horizontalForce * shipSpeed, verticalForce * shipSpeed});
        else{
            if(index - 1 >= 0){
                try {
                    instance.moveHistoryList.get(index - 1)[0] = horizontalForce * shipSpeed;
                    instance.moveHistoryList.get(index - 1)[1] = verticalForce * shipSpeed;
                }catch(IndexOutOfBoundsException e){
                    instance.moveHistoryList.add(new float[] {horizontalForce * shipSpeed, verticalForce * shipSpeed});
                    System.out.println(e.toString() + "\n");
                }
            }

        }
    }



    Singleton instance = Singleton.getInstance();


    private float accumulator = 0;
    private void doWorldStep(float deltaTime){
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while(accumulator >= STEP){
            world.step(STEP, 6, 2);
            accumulator -= STEP;
        }

    }





    public SpriteBatch getBatch(){
        return batch;
    }
}
