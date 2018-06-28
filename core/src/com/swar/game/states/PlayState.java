package com.swar.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.swar.game.Types.BonusType;
import com.swar.game.Types.ShipType;
import com.swar.game.entities.*;
import com.swar.game.entities.Bonuses.EnergyBonus;
import com.swar.game.entities.Bonuses.HealthBonus;
import com.swar.game.entities.Bonuses.UpgradeBonus;
import com.swar.game.managers.GameConfig;
import com.swar.game.managers.GameStateManagement;
import com.swar.game.managers.VibrationManager;
import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.GameContactListener;
import com.swar.game.managers.World.ObjectHandler;
import com.swar.game.utils.Randomizer;

import java.util.ArrayList;
import java.util.HashSet;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.swar.game.utils.constants.*;
import static java.lang.Math.abs;

/**
 * Created by Koma on 25.06.2018.
 */
public abstract class PlayState extends GameState{

    protected World world;
    protected GameContactListener cl;
    protected Player player;
    protected BodyBuilder bodyBuilder;
    protected ObjectHandler objectHandler;
    protected boolean CONFIG_VIBRATION;
    protected Player shadowPlayer;

    private float accumulator = 0;

    final protected Randomizer randomizer = new Randomizer();

    GameConfig gameConfig;

    protected PlayState(GameStateManagement gsm) {
        super(gsm);

        gameConfig = new GameConfig();
        CONFIG_VIBRATION = gameConfig.isVibraion();

        world = gsm.world;
        player = gsm.player;
        cl = new GameContactListener(player);

        bodyBuilder = new BodyBuilder(world);

        Body body = bodyBuilder.createShadow(GAME_WIDTH / 2, 15, GAME_WIDTH/15, GAME_WIDTH/10);
        //здесь по индексу передаём корабль из ДБ
        shadowPlayer = new Player(body,  ShipType.getShip(ShipType.ship_2));
        // body.setUserData(shadowPlayer);

        world.setContactListener(cl);
        objectHandler = new ObjectHandler(new Array<Asteroid>(), new Array<Bullet>(), new Array<Sprite>(), new Array<Enemy>(), world);
        rangedColor(0);
    }

    protected void doWorldStep(float deltaTime){
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;

        while(accumulator >= STEP){
            world.step(STEP, 6, 2);
            accumulator -= STEP;
        }
    }

    protected abstract void eventGenerator();


    protected void updateAsteroids(float delta){
        for(int i = 0; i < objectHandler.listAsteroid.size; ++i) {
            Asteroid asteroid = objectHandler.listAsteroid.get(i);
            Vector2 targetPosition = new Vector2(0, asteroid.speed *1.1f);
            asteroid.getBody().setLinearVelocity(targetPosition);
            asteroid.update(delta);
        }
    }

    protected void updateEnemies(float delta){
        for(int i = 0; i < objectHandler.listEnemy.size; ++i) {
            Enemy enemy = objectHandler.listEnemy.get(i);
            Vector2 targetPosition = new Vector2(0, enemy.speed *0.9f);
            enemy.getBody().setLinearVelocity(targetPosition);
            enemy.update(delta);

            if(randomizer.chanceAsteroid())
                enemy.createObject(bodyBuilder, objectHandler);

        }
    }

    protected void updateBullets(float delta){
        for(int i = 0; i < objectHandler.listBulletPlayer.size; ++i){
            Bullet bullet = objectHandler.listBulletPlayer.get(i);
            bullet.getBody().setLinearVelocity(bullet.currentSpeed, bullet.getSpeed());
            bullet.update(delta);
        }
    }

    protected void updateDisappearables(float delta){
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

    protected void updateObjectMovements(float delta){

        //TODO сделать потоки безопасными

        updateAsteroids(delta);


        updateEnemies(delta);

        updateBullets(delta);


        updateDisappearables(delta);

    }

    protected void removeObjects(){
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
    }


    private float f1;
    private float f2;
    private float f3;
    private float[] rangedColor(float time){
        float cfg = 0.1f;
        float low = 0.05f;

        f1 = random.nextFloat()*0.075f;
        f1 = lowCheck(f1, low);

        f2 = random.nextFloat()*0.025f;
        f2 = lowCheck(f2, low);

        f3 = random.nextFloat() * cfg + 0.1f;
        f3 = lowCheck(f3, low);

        return new float[]{f1, f2, f3};
    }

    private float lowCheck(float clr, float low){
        if(clr < low)
            clr += low;
        return clr;
    }
    private void setBackgroundColor(){
        Gdx.gl.glClearColor(f1, f2, f3, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render() {
        setBackgroundColor();
    }

    protected void inputAction(boolean shot, float horizontal, float vertical, boolean ability){
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
}
