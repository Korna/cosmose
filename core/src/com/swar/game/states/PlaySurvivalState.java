package com.swar.game.states;

/**
 * Created by Koma on 19.09.2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.swar.game.Models.RecordModel;
import com.swar.game.Models.Weapon;
import com.swar.game.Types.State;
import com.swar.game.entities.*;
import com.swar.game.managers.*;
import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.GameContactListener;
import com.swar.game.managers.World.ObjectHandler;
import com.swar.game.utils.Journal;
import com.swar.game.utils.Randomizer;

import static com.swar.game.utils.constants.*;

/**
 * Created by Koma on 17.01.2017.
 */
public class PlaySurvivalState extends PlayState{

    private Box2DDebugRenderer b2dr;
    private HUD hud;








    private IInterfaceManager interfaceManager;



    boolean available = false;

    public PlaySurvivalState(GameStateManagement gsm) {
        super(gsm);

        world = gsm.world;
        player = gsm.player;
        cl = new GameContactListener(player);
        GameConfig gameConfig = new GameConfig();
        CONFIG_VIBRATION = gameConfig.isVibraion();

       // Gdx.input.setInputProcessor(new GameInputProcessor());

        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();
        batch = new SpriteBatch();
        bodyBuilder = new BodyBuilder(world);



        bodyBuilder.createBorder(BORDER_HORIZONTAL, GAME_WIDTH, 0, GAME_WIDTH, 1);
        bodyBuilder.createBorder(BORDER_HORIZONTAL, GAME_WIDTH, GAME_HEIGHT, GAME_WIDTH, 1);
        bodyBuilder.createBorder(BORDER_VERTICAL, 1, GAME_HEIGHT, 1, GAME_HEIGHT);
        bodyBuilder.createBorder(BORDER_VERTICAL, GAME_WIDTH, GAME_HEIGHT, 1, GAME_HEIGHT);





        hud = new HUD(player, State.PLAYSURVIVAL);
        available = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);

        objectHandler = new ObjectHandler(new Array<Asteroid>(), new Array<Bullet>(), new Array<Sprite>(), new Array<Enemy>(), world);
        if(available)
            interfaceManager = new InterfaceManagerAndroid(0.5f, 4.5f, 0.25f);
        else
            interfaceManager = new InterfaceManagerPC();

    }




    private Randomizer randomizer = new Randomizer();




    @Override
    public void update(float delta) {
        player.timeInGame += delta;

        float totalDamage = cl.getHp() + player.ship.armor;
        if(totalDamage < 0)
            player.ship.setHp(player.ship.getHp() + totalDamage);

        if(player.ship.getHp() <= 0){
            player.setDead(true);



            RecordModel model = new RecordModel();
            model.setScore(cl.getScoreAndClear());
            model.setTime(player.timeInGame);
            model.setGameType("Survival");
            instance.recordModels.add(model);

            gsm.setState(State.DEATH);

            return;
        }


        interfaceManager.inputUpdate();
        inputAction(interfaceManager.getShot(), interfaceManager.getHFloat(), interfaceManager.getVForce());

        player.update(delta);
        for(Weapon weapon : player.ship.weapons){
            weapon.setTimeAfterShot(weapon.getTimeAfterShot() + delta);
        }

        float energy = cl.getEnergyAndClear();
        player.ship.setEnergy(player.ship.getEnergy() + energy);


        eventGenerator();

        removeObjects();

        updateObjectMovements(delta);



        batch.setProjectionMatrix(maincamera.combined);

        doWorldStep(delta);
    }

    @Override
    protected void updateAsteroids(float delta){
        for(int i = 0; i < objectHandler.listAsteroid.size; ++i) {//можно мб просто отмечать для удаления объекты? ставить маркер. а не ждать пока итератор пройдет по новой КОЛЛЕКЦИИ
            Asteroid asteroid = objectHandler.listAsteroid.get(i);
            final float cfg = 0.2f + player.timeInGame/500f;

            Vector2 targetPosition = new Vector2(0, asteroid.speed *cfg);
            asteroid.getBody().setLinearVelocity(targetPosition);

            asteroid.update(delta);
        }
    }

    @Override
    public void render() {
        super.render();


        if(DEBUG_RENDER)
            b2dr.render(world, maincamera.combined);


        objectHandler.render(batch);

        player.render(batch);


        hud.render(batch_hud);
    }

    @Override
    public void dispose() {
        //tex_background.dispose();
        b2dr.dispose();
        world.dispose();

    }

    public void cameraUpdate(float delta){
        Vector3 position = maincamera.position;
        position.x = maincamera.position.x + (player.getPosition().x - maincamera.position.x) * .0004f;
        position.y = maincamera.position.y + (player.getPosition().y - maincamera.position.y) * .0004f;
        maincamera.position.set(position);
        maincamera.update();
    }

    private void inputAction(boolean shot, float horizontal, float vertical){
        if(horizontal < 0)
            player.ship_l();
        if(horizontal > 0)
            player.ship_r();
        if(horizontal == 0)
            player.ship();
        if(shot){
            if(player.ship.getEnergy() > 0){
                boolean hadShot = player.createObject(bodyBuilder, objectHandler);

            }
        }

        player.getBody().setLinearVelocity(horizontal * player.getSpeed(), vertical * player.getSpeed());
    }



    Journal instance = Journal.getInstance();





    @Override
    protected void eventGenerator() {
        if(randomizer.chanceAsteroid(player.timeInGame)){

            Body asteroidBody = bodyBuilder.createAsteroid(randomizer.getCoordinateAsteroid(),GAME_HEIGHT-30);

            Asteroid a = new Asteroid(asteroidBody);
            asteroidBody.setUserData(a);

            objectHandler.add(a);
        }
    }


    public SpriteBatch getBatch(){
        return batch;
    }
}
