package com.swar.game.states;

import android.util.Log;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.swar.game.Randomizer;
import com.swar.game.Singleton;
import com.swar.game.entities.*;
import com.swar.game.managers.GameContactListener;
import com.swar.game.managers.GameInputProcessor;
import com.swar.game.managers.GameStateManagement;

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

    private Array<Asteroid> listAsteroid;
    private Array<Bullet> listBulletPlayer;
    private Array<Bonus> listBonus;


    final static int GAME_TIME = 15;

    boolean available = false;
    public PlayClassicState(GameStateManagement gsm) {
        super(gsm);
        cl = new GameContactListener();

        world = gsm.world;
        player = gsm.player;



        Body body = createShadow(GAME_WIDTH / 2, 15, GAME_WIDTH/15, GAME_WIDTH/10);
        shadowPlayer = new Player(body, null, 2, null, 1);//здесь по индексу передаём корабль из ДБ


        Gdx.input.setInputProcessor(new GameInputProcessor());

        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();
        batch = new SpriteBatch();

        createBorders(world);

        this.listAsteroid = new Array<>();
        this.listBulletPlayer = new Array<>();
        this.listBonus = new Array<>();



        hud = new HUD(player);
        available = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);

    }



    private final float asteroidSpeed = -(GAME_WIDTH);

    private Randomizer randomizer = new Randomizer();

    int index = 0;



    @Override
    public void update(float delta) {
        player.timeInGame += delta;
        if(player.timeInGame >= GAME_TIME){
            player.timeInGame = 0;
            instance.firstRun = false;

            for(Asteroid asteroid : listAsteroid)
                world.destroyBody(asteroid.getBody());
            for(Bullet bullet : listBulletPlayer)
                world.destroyBody(bullet.getBody());
            for(Bonus bonus : listBonus)
                world.destroyBody(bonus.getBody());

            listBonus.clear();
            listBulletPlayer.clear();
            listAsteroid.clear();

            gsm.setState(GameStateManagement.State.HUB);

            return;
        }

        player.ship.setHp(player.ship.getHp() + cl.getHp());

        if(player.ship.getHp() <= 0){
            player.setDead(true);

            for(Asteroid asteroid : listAsteroid)
                world.destroyBody(asteroid.getBody());

            for(Bullet bullet : listBulletPlayer)
                world.destroyBody(bullet.getBody());
            for(Bonus bonus : listBonus)
                world.destroyBody(bonus.getBody());

            listBonus.clear();
            listBulletPlayer.clear();
            listAsteroid.clear();

            gsm.setState(GameStateManagement.State.DEATH);

            return;
        }


        inputUpdate(delta);
        player.update(delta);



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
        }
        if(!instance.firstRun)
            shadowPlayer.update(delta);





        if(randomizer.chanceAsteroid())
            createAsteroid(randomizer.getCoordinateAsteroid(),GAME_HEIGHT-45);



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

            world.destroyBody(body);
            try{
                listAsteroid.removeValue((Asteroid) body.getUserData(), true);
                try {
                    if (randomizer.chanceBonus()) {
                        createBonus(body.getPosition().x, body.getPosition().y);
                    }
                }catch(Exception e){
                    System.out.printf(e.toString() + "\n");
                }
            }catch(Exception e){
                try {
                    listBulletPlayer.removeValue((Bullet) body.getUserData(), true);
                }catch(Exception bonus){
                    listBonus.removeValue((Bonus) body.getUserData(), true);
                }
            }




        }

        cl.clearList();
        //TODO сделать потоки безопасными

                for(int i = 0; i < listAsteroid.size; ++i) {
                    Asteroid asteroid = listAsteroid.get(i);
                    Vector2 targetPosition = new Vector2(0, asteroidSpeed * 100000);


                    asteroid.getBody().applyForce(targetPosition, asteroid.getBody().getWorldCenter(), true);
                    asteroid.update(delta);
                }




                for(int i = 0; i<listBulletPlayer.size; ++i){
                    Bullet bullet = listBulletPlayer.get(i);
                    bullet.getBody().setLinearVelocity(bullet.currentSpeed, bullet.speedY);
                    bullet.update(delta);
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


        for(Asteroid asteroid : listAsteroid)
            asteroid.render(batch);

        for(Bonus bonus : listBonus)
            bonus.render(batch);

        for(Bullet bullet : listBulletPlayer)
            bullet.render(batch);

        player.render(batch);
        if(!instance.firstRun && !shadowPlayer.isDead())
            shadowPlayer.render(batch);

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
        position.x = maincamera.position.x + (player.getPosition().x * PPM - maincamera.position.x) * .4f;
        position.y = maincamera.position.y + (player.getPosition().y * PPM - maincamera.position.y) * .4f;
        maincamera.position.set(position);
        maincamera.update();
    }




    final int playerHandle = 5;
    final float playerZone = 0.5f;
    public void inputUpdate(float delta){
        int horizontalForce = 0;
        int verticalForce = 0;
        int shipSpeed = player.getSpeed();

        player.ship();

        if(available){
            float accelX = Gdx.input.getAccelerometerX();
            float accelY = Gdx.input.getAccelerometerY();

            if(accelX > playerZone){
                --horizontalForce;
                player.ship_l();
            }
            else
            if(accelX < -playerZone){
                ++horizontalForce;
                player.ship_r();
            }

            if(accelY > (playerZone + playerHandle)){
                --verticalForce;
            }
            else
            if(accelY < (-playerZone + playerHandle)){
                ++verticalForce;
            }

            if(Gdx.input.justTouched()){
                createBulletPlayer();
                Gdx.input.vibrate(VIBRATION_LONG);
            }

        }else{


            if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
                --horizontalForce;
                player.ship_l();
            }
            else
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
                ++horizontalForce;
                player.ship_r();
            }

            if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){
                ++verticalForce;
            }
            else
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
                --verticalForce;
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                createBulletPlayer();
            }

        }


        player.getBody().setLinearVelocity(horizontalForce * shipSpeed, verticalForce * shipSpeed);
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

    private void createAsteroid(float x, float y) {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.position.set(x, y);

        CircleShape cshape = new CircleShape();
        cshape.setRadius(GAME_WIDTH/40);

        fdef.shape = cshape;
        fdef.filter.categoryBits = BIT_ENEMY;
        fdef.filter.maskBits = BIT_PLAYER | BIT_BULLET | BIT_BORDER;
        fdef.isSensor = true;

        Body body = this.world.createBody(bdef);
        body.createFixture(fdef).setUserData(ASTEROID);

        Asteroid a = new Asteroid(body);
        body.setUserData(a);
        this.listAsteroid.add(a);

    }

    private void createBonus(float x, float y){
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x, y);

        CircleShape cshape = new CircleShape();
        cshape.setRadius(GAME_WIDTH/80);

        fdef.shape = cshape;
        fdef.filter.categoryBits = BIT_OBJECT;
        fdef.filter.maskBits = BIT_PLAYER;
        fdef.isSensor = true;

        Body body = this.world.createBody(bdef);
        body.createFixture(fdef).setUserData(BONUS);

        Bonus b = new Bonus(body);
        body.setUserData(b);
        this.listBonus.add(b);


    }

    private int bulletAmount = 0;
    private void createBulletPlayer() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        FixtureDef fdef = new FixtureDef();

        //позиционирование выстрела
        float x = player.getBody().getPosition().x;
        float y = player.getBody().getPosition().y + 5;

        bdef.position.set(x, y);
        CircleShape cshape = new CircleShape();
        cshape.setRadius(GAME_WIDTH/190);
        fdef.shape = cshape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = BIT_BULLET;
        fdef.filter.maskBits = BIT_ENEMY | BIT_BORDER | BIT_SHADOW;

        Body body = this.world.createBody(bdef);


        //параметры пули
        Bullet b;
        if(player.bulletIndex==1){
            body.createFixture(fdef).setUserData(BULLET_PIERCING);
            b = new Bullet(body, player.bulletIndex, true, true);
        }
        else{
            body.createFixture(fdef).setUserData(BULLET_DESTROYABLE);
            b = new Bullet(body, player.bulletIndex, false, false);
        }

        body.setUserData(b);
        this.listBulletPlayer.add(b);
        System.out.println(++bulletAmount);
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

    private void createBorders(World world){



        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;



        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GAME_WIDTH
                //  / PPM
                , 1
                //  / PPM
        );
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_BORDER;

        def.position.set(GAME_WIDTH
                //    / PPM
                , 0
                //      / PPM
        );

        Body pBody = world.createBody(def);
        pBody.createFixture(fdef).setUserData("borderBottom");
        shape.dispose();

        def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        shape = new PolygonShape();
        shape.setAsBox(GAME_WIDTH, 1);
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_BORDER;
        def.position.set(GAME_WIDTH, GAME_HEIGHT);
        pBody = world.createBody(def);
        pBody.createFixture(fdef).setUserData("borderBottom");
        shape.dispose();


        def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        shape = new PolygonShape();
        shape.setAsBox(1, GAME_HEIGHT);
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_BORDER;
        def.position.set(1, GAME_HEIGHT);
        pBody = world.createBody(def);
        pBody.createFixture(fdef).setUserData("border");
        shape.dispose();


        def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        shape = new PolygonShape();
        shape.setAsBox(1, GAME_HEIGHT);
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_BORDER;
        def.position.set(GAME_WIDTH, GAME_HEIGHT);
        pBody = world.createBody(def);
        pBody.createFixture(fdef).setUserData("border");
        shape.dispose();

    }


    private Body createShadow(int x, int y, float width, float height){
        Body pBody;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_SHADOW;
        fdef.filter.maskBits =  BIT_BULLET | BIT_BORDER;


        def.position.set(x, y);
        def.fixedRotation = true;

        pBody = world.createBody(def);




        pBody.createFixture(fdef).setUserData(SHADOW);

        shape.dispose();

        return pBody;
    }

    public SpriteBatch getBatch(){
        return batch;
    }
}
