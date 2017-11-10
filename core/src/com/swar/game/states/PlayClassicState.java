package com.swar.game.states;

//import android.util.Log;

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
import com.swar.game.Models.Weapon;
import com.swar.game.Types.BonusType;
import com.swar.game.Types.ShipType;
import com.swar.game.Types.State;
import com.swar.game.entities.*;
import com.swar.game.entities.Bonuses.EnergyBonus;
import com.swar.game.entities.Bonuses.HealthBonus;
import com.swar.game.entities.Bonuses.UpgradeBonus;
import com.swar.game.managers.*;
import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.GameContactListener;
import com.swar.game.managers.World.ObjectHandler;
import com.swar.game.utils.Journal;
import com.swar.game.utils.Randomizer;

import java.util.ArrayList;
import java.util.HashSet;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.swar.game.utils.constants.*;
import static java.lang.Math.abs;

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
    private IInterfaceManager interfaceManager;
    private boolean CONFIG_VIBRATION;



    final static int GAME_TIME = 60;
    final private Randomizer randomizer = new Randomizer();
    final private Journal instance = Journal.getInstance();


    int index = 0;//переменная для сохранения индекса кадра

    public PlayClassicState(GameStateManagement gsm) {
        super(gsm);

        world = gsm.world;
        player = gsm.player;
        cl = new GameContactListener(player);

        bodyBuilder = new BodyBuilder(world);

        GameConfig gameConfig = new GameConfig();
        CONFIG_VIBRATION = gameConfig.isVibraion();


        Body body = bodyBuilder.createShadow(GAME_WIDTH / 2, 15, GAME_WIDTH/15, GAME_WIDTH/10);
        //здесь по индексу передаём корабль из ДБ
        shadowPlayer = new Player(body,  ShipType.getShip(ShipType.ship_2));
        // body.setUserData(shadowPlayer);

        world.setContactListener(cl);

        b2dr = new Box2DDebugRenderer();
        batch = new SpriteBatch();


        bodyBuilder.createBorder(BORDER_HORIZONTAL, GAME_WIDTH, 0, GAME_WIDTH, 1);
        bodyBuilder.createBorder(BORDER_HORIZONTAL, GAME_WIDTH, GAME_HEIGHT, GAME_WIDTH, 1);
        bodyBuilder.createBorder(BORDER_VERTICAL, 1, GAME_HEIGHT, 1, GAME_HEIGHT);
        bodyBuilder.createBorder(BORDER_VERTICAL, GAME_WIDTH, GAME_HEIGHT, 1, GAME_HEIGHT);



        hud = new HUD(player, State.PLAY);



        objectHandler = new ObjectHandler(new Array<Asteroid>(), new Array<Bullet>(), new Array<Sprite>(), new Array<Enemy>(), world);

        if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer))//на андроиде ли или нет
            interfaceManager = new InterfaceManagerAndroid(0.5f, gameConfig.getPosY(), 0.25f);
        else
            interfaceManager = new InterfaceManagerPC();

        rangedColor(0);
    }






    private boolean abilityOn = false;
    private int damageEffect = 0;
    private final static int effectTime = 5;

    @Override
    public void update(float delta) {

        if(isEndOfCycle(delta))
            return;



        shadowMovement();

        if(!instance.firstRun){
            float[] move = {0,0};
            try {
                move = instance.moveHistoryList.get(index);
                shadowPlayer.getBody().setLinearVelocity(move[0], move[1]);

            }catch(IndexOutOfBoundsException e){
                shadowPlayer.getBody().setLinearVelocity(0, 0);
                try {
                    //Log.e("shadow:", "indexOutOfBOunds");
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
        if(cl.shadowToRemove!=null){
            world.destroyBody(cl.shadowToRemove);
            cl.shadowToRemove = null;
            shadowPlayer.setDead(true);
        }





        eventGenerator();


        Array<Body> explosionBodies = new Array<>();

        Array<Vector2> blasts = cl.getBlasts();
        for(Vector2 vector2 : blasts){
            makeBlast(vector2);
        }

        //удаление астероидов
        Array<Body> bodies = cl.getBodiesToRemove();
        //TODO оптимизировать трансформацию AL в A
        ArrayList<Body> list = new ArrayList<>();
        for(Body b : bodies){
            list.add(b);
        }
        HashSet<Body> set = new HashSet<>(list);
        for(Body body : set){
            String str = "Deleted";
            try {
                str = (String) body.getFixtureList().get(0).getUserData();
            }catch(Exception e){
                try {
                    String clazz = body.getUserData().getClass().getName();
                   // Log.e("Class:" + clazz, e.toString());
                }catch(NullPointerException npe){
                   // Log.e("npe", npe.toString());//когда взрываются уже отнесенные к удалению объекты
                }

            }
            switch(str){
                case ASTEROID:
                    Asteroid asteroid = (Asteroid) body.getUserData();
                    objectHandler.remove(asteroid);
                    try {
                        BonusType bonusType = randomizer.chanceBonusAsteroid();
                        Body bonusBody = null;
                        if(bonusType!= null)
                        switch(bonusType) {
                            case bonus_1:
                                bonusBody = bodyBuilder.createBonus(body.getPosition().x, body.getPosition().y);

                                EnergyBonus eb = new EnergyBonus(bonusBody);
                                bonusBody.setUserData(eb);
                                objectHandler.add(eb);

                                break;
                            case bonus_2:
                                bonusBody = bodyBuilder.createBonus(body.getPosition().x, body.getPosition().y);

                                HealthBonus hb = new HealthBonus(bonusBody);
                                bonusBody.setUserData(hb);
                                objectHandler.add(hb);

                                break;
                                default:
                                    break;
                        }

                    } catch (Exception e) {
                      //  Log.w("kill aster", e.toString());
                    }


                    break;

                case BULLET_DESTROYABLE:
                case BULLET_PIERCING:
                case BULLET_ENEMY:
                    Bullet b1 = (Bullet) body.getUserData();
                    objectHandler.remove(b1);
                    break;
                case BULLET_EXPLOSIVE:
                    Bullet b = (Bullet) body.getUserData();
                    Vector2 bv2 = b.getPosition();
                    System.out.println("x:" + bv2.x + " y:" + bv2.y);

                    for(Asteroid a : objectHandler.listAsteroid){
                        Vector2 v2 = a.getBody().getPosition();
                        if(abs(bv2.x - v2.x) < 50)
                            if(abs(bv2.y - v2.y) < 50){
                                a.setHp(a.getHp() - b.getBulletModel().getDamage());
                                if(a.getHp() <= 0){
                                   //world.destroyBody(a.getBody());
                                   // objectHandler.remove(a);
                                    explosionBodies.add(a.getBody());
                                }
                            }
                    }
                    for(Enemy e : objectHandler.listEnemy){
                        Vector2 v2 = e.getBody().getPosition();
                        if(abs(bv2.x - v2.x) < 50)
                            if(abs(bv2.y - v2.y) < 50){
                                e.setHp(e.getHp() - b.getBulletModel().getDamage());
                                if(e.getHp() <= 0){

                                   // world.destroyBody(e.getBody());
                                   // objectHandler.remove(e);
                                    explosionBodies.add(e.getBody());
                                }
                            }
                    }

                    objectHandler.remove(b);


                    makeExplosion(body.getPosition(), 3f);

                    break;
                case BONUS:
                    objectHandler.remove((Sprite) body.getUserData());
                    break;
                case ENEMY:
                    objectHandler.remove((Enemy) body.getUserData());
                    makeExplosion(body.getPosition(), 1.5f);

                    BonusType bonusType = randomizer.getRandomBonus();
                    Body bonusBody = null;
                    if(bonusType!= null)
                        switch(bonusType) {
                            case bonus_1:
                                bonusBody = bodyBuilder.createBonus(body.getPosition().x, body.getPosition().y);

                                EnergyBonus eb = new EnergyBonus(bonusBody);
                                bonusBody.setUserData(eb);
                                objectHandler.add(eb);

                                break;
                            case bonus_2:
                                bonusBody = bodyBuilder.createBonus(body.getPosition().x, body.getPosition().y);

                                HealthBonus hb = new HealthBonus(bonusBody);
                                bonusBody.setUserData(hb);
                                objectHandler.add(hb);

                                break;
                            case bonus_3:
                                bonusBody = bodyBuilder.createBonus(body.getPosition().x, body.getPosition().y);

                                UpgradeBonus ub = new UpgradeBonus(bonusBody);
                                bonusBody.setUserData(ub);
                                objectHandler.add(ub);
                            default:
                                break;
                        }
                    break;
                    default:
                        break;
            }
            if(!str.equals("Deleted"))
                world.destroyBody(body);

        }

        cl.clearList();
        for(Body body : explosionBodies){
            cl.getBodiesToRemove().add(body);
        }

        updateProgress(delta);


        //работа с игроком
        float totalDamage = cl.getHp() +player.ship.armor;
        if(totalDamage < 0){
            player.ship.setHp(player.ship.getHp() + totalDamage);
            damageEffect = effectTime;
            if(CONFIG_VIBRATION)
                VibrationManager.vibrate(VibrationManager.MEDIUM);
        }
        player.setCredits(player.getCredits() + cl.getCredits());


        if(player.ship.getHp() <= 0){
            player.setDead(true);
            objectHandler.clearAll();
            gsm.setState(State.DEATH);
            return;
        }


        interfaceManager.inputUpdate();
        if(interfaceManager.getAbility()){
            VibrationManager.vibrate(VibrationManager.LONG);
            abilityOn = true;
        }


        if(abilityOn)
            player.ship.ability.update(delta);

        inputAction(interfaceManager.getShot(), interfaceManager.getHFloat(), interfaceManager.getVForce(), abilityOn);
        player.update(delta);


        for(Weapon weapon : player.ship.weapons){
            weapon.setTimeAfterShot(weapon.getTimeAfterShot() + delta);
        }
        int energy = cl.getEnergyAndClear();
        player.ship.setEnergy(player.ship.getEnergy() + energy);



        batch.setProjectionMatrix(maincamera.combined);
        doWorldStep(delta);
    }
    private void eventGenerator(){
        if(randomizer.chanceAsteroid()){
            Body asteroidBody = bodyBuilder.createAsteroid(randomizer.getCoordinateAsteroid(),GAME_HEIGHT-32);

            Asteroid a = new Asteroid(asteroidBody);
            asteroidBody.setUserData(a);

            objectHandler.add(a);
        }
        if(randomizer.chanceAsteroid()){
            if(randomizer.chanceAsteroid()) {
                Body enemyBody = bodyBuilder.createEnemy(randomizer.getCoordinateAsteroid(), GAME_HEIGHT-32);

                Enemy e = new Enemy(enemyBody);
                enemyBody.setUserData(e);

                objectHandler.add(e);
            }
        }

    }

    private boolean isEndOfCycle(float delta){
        player.timeInGame += delta;
        if(player.timeInGame >= GAME_TIME){
            player.timeInGame = 0;
            instance.firstRun = false;

            objectHandler.clearAll();

            gsm.setState(State.HUB);

            return true;
        }else
            return false;
    }

    private void updateProgress(float delta){

        //TODO сделать потоки безопасными

        for(int i = 0; i < objectHandler.listAsteroid.size; ++i) {
            Asteroid asteroid = objectHandler.listAsteroid.get(i);
            Vector2 targetPosition = new Vector2(0, asteroid.speed *1.1f);
            asteroid.getBody().setLinearVelocity(targetPosition);
            asteroid.update(delta);
        }

        for(int i = 0; i < objectHandler.listEnemy.size; ++i) {
            Enemy enemy = objectHandler.listEnemy.get(i);
            Vector2 targetPosition = new Vector2(0, enemy.speed *0.9f);
            enemy.getBody().setLinearVelocity(targetPosition);
            enemy.update(delta);

            if(randomizer.chanceAsteroid())
                enemy.createObject(bodyBuilder, objectHandler);

        }



        for(int i = 0; i < objectHandler.listBulletPlayer.size; ++i){
            Bullet bullet = objectHandler.listBulletPlayer.get(i);
            bullet.getBody().setLinearVelocity(bullet.currentSpeed, bullet.getSpeed());
            bullet.update(delta);
        }




        //удаление бонусов спустя время
        for(int i = 0; i < objectHandler.listDisappearable.size; ++i){
            Sprite bonus = objectHandler.listDisappearable.get(i);
            bonus.update(delta);
            com.swar.game.entities.Bonuses.Dissapearable dissapearable = (com.swar.game.entities.Bonuses.Dissapearable) bonus;


            dissapearable.setExistTime(dissapearable.getExistTime() + delta);
            if(dissapearable.getExistTime() > dissapearable.getMaxExistTime()){
                world.destroyBody(bonus.getBody());

                objectHandler.listDisappearable.removeIndex(i);
                --i;
            }else{
                bonus.getBody().setLinearVelocity(0, -10);

            }
            bonus.update(delta);
        }

    }

    private void makeExplosion(Vector2 body, float scale){
        Body explosionBody = bodyBuilder.createExplosion(body.x, body.y);
        Explosion e = new Explosion(explosionBody, scale, "explosion_1");
        explosionBody.setUserData(e);
        objectHandler.add(e);
    }

    private void makeBlast(Vector2 body){
        Body explosionBody = bodyBuilder.createExplosion(body.x, body.y);
        Blast e = new Blast(explosionBody, 0.5f, "explosion_2");
        explosionBody.setUserData(e);
        objectHandler.add(e);
    }

    private float f1;
    private float f2;
    private float f3;
    private float[] rangedColor(float time){
        float cfg = 0.9f;
        float low = 0.3f;

        f1 = random.nextFloat()*0.7f;
        f1 = lowCheck(f1, low);

        f2 = random.nextFloat()*0.6f;
        f2 = lowCheck(f2, low);

        f3 = random.nextFloat() * cfg;
        f3 = lowCheck(f3, low);

        return new float[]{f1, f2, f3};
    }

    private float lowCheck(float clr, float low){
        if(clr < low)
            clr += low;
        return clr;
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(f1, f2, f3, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(damageEffect > 0){
            Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.setColor(1, 1, 1, 0.05f);

            System.out.println("hit!\n");
            --damageEffect;
        }
        if(damageEffect==0){
            batch.setColor(1, 1, 1, 1.0f);
        }


        if(DEBUG_RENDER)
            b2dr.render(world, maincamera.combined);

        objectHandler.render(batch);

        player.render(batch);
        if(!instance.firstRun && !shadowPlayer.isDead())
            shadowPlayer.render(batch);


        hud.render(batch_hud);
    }

    private void inputAction(boolean shot, float horizontal, float vertical, boolean ability){
        if(horizontal < 0)
            player.ship_l();
        if(horizontal > 0)
            player.ship_r();
        if(horizontal == 0)
            player.ship();
        if(shot){
            if(player.ship.getEnergy() > 0){
                boolean hadShot = player.createObject(bodyBuilder, objectHandler);
                if(hadShot)
                    if(CONFIG_VIBRATION)
                        VibrationManager.vibrate(VibrationManager.SHORT);
            }
        }
        if(ability){
            player.ship.ability.use(bodyBuilder, objectHandler, player);
        }


        player.getBody().setLinearVelocity(horizontal * player.getSpeed(), vertical * player.getSpeed());
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
