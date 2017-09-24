package com.swar.game.states;

/**
 * Created by Koma on 19.09.2017.
 */

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
public class PlaySurvivalState extends GameState{

    private Box2DDebugRenderer b2dr;
    private HUD hud;

    private GameContactListener cl;
    private World world;
    private Player player;


    private Array<Asteroid> listAsteroid;
    private Array<Bullet> listBulletPlayer;
    private Array<Bonus> listBonus;



    boolean available = false;
    public PlaySurvivalState(GameStateManagement gsm) {
        super(gsm);
        cl = new GameContactListener();

        world = gsm.world;
        player = gsm.player;


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




    private Randomizer randomizer = new Randomizer();

    int index = 0;



    @Override
    public void update(float delta) {
        player.timeInGame += delta;

        player.ship.setHp(player.ship.getHp() + cl.getHp());


        if(player.ship.getHp() <= 0){
            player.setDead(true);



            RecordModel model = new RecordModel();
            model.setScore(cl.getScoreAndClear());
            model.setTime(player.timeInGame);
            model.setGameType("Survival");
            instance.recordModels.add(model);

            gsm.setState(GameStateManagement.State.DEATH);

            return;
        }


        inputUpdate(delta);
        player.update(delta);

        int energy = cl.getEnergyAndClear();
        player.ship.setEnergy(player.ship.getEnergy() + energy);


        if(randomizer.chanceAsteroid(player.timeInGame))
            createAsteroid(randomizer.getCoordinateAsteroid(),GAME_HEIGHT-30);



        //удаление астероидов
        Array<Body> bodies = cl.getBodiesToRemove();

        //TODO оптимизировать трансформацию AL в A
        final ArrayList<Body> list = new ArrayList<>();
        for(Body b : bodies){
            list.add(b);
        }
        final HashSet<Body> set = new HashSet<>(list);

        for(Body body : set){
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

            world.destroyBody(body);


        }

        cl.clearList();
        //TODO сделать потоки безопасными

        for(int i = 0; i < listAsteroid.size; ++i) {//можно мб просто отмечать для удаления объекты? ставить маркер. а не ждать пока итератор пройдет по новой КОЛЛЕКЦИИ
            Asteroid asteroid = listAsteroid.get(i);
            final float cfg = 0.2f + player.timeInGame/500f;

            Vector2 targetPosition = new Vector2(0, asteroid.speed *cfg);
            asteroid.getBody().setLinearVelocity(targetPosition);

            asteroid.update(delta);
        }



        for(int i = 0; i<listBulletPlayer.size; ++i){//можно мб просто отмечать для удаления объекты? ставить маркер. а не ждать пока итератор пройдет по новой КОЛЛЕКЦИИ
            Bullet bullet = listBulletPlayer.get(i);
            bullet.getBody().setLinearVelocity(bullet.currentSpeed, bullet.speedY);
            bullet.update(delta);
        }


        //удаление бонусов спустя время
        for(int i = 0; i < listBonus.size; ++i){
            Bonus bonus = listBonus.get(i);
            bonus.setExistTime(bonus.getExistTime() + delta);
            if(bonus.getExistTime() > 30){
                world.destroyBody(bonus.getBody());

                listBonus.removeIndex(i);
                --i;
            }

        }



        batch.setProjectionMatrix(maincamera.combined);

        doWorldStep(delta);
    }

    float f1 = random.nextFloat();
    float f2 = random.nextFloat();
    float f3 = random.nextFloat() + 0.2f;
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
                if(player.ship.getEnergy() > 0){
                    createBulletPlayer();
                    Gdx.input.vibrate(VIBRATION_LONG);
                    player.ship.setEnergy(player.ship.getEnergy() - 1);
                }

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
        cshape.setRadius(GAME_WIDTH/20);

        fdef.shape = cshape;
        fdef.filter.categoryBits = BIT_ENEMY;
        fdef.filter.maskBits = BIT_PLAYER | BIT_BULLET | BIT_BORDER;
        fdef.isSensor = true;

        Body body = this.world.createBody(bdef);
        body.createFixture(fdef).setUserData(ASTEROID);

        Asteroid a = new Asteroid(body);
        this.listAsteroid.add(a);
        body.setUserData(a);
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
        this.listBonus.add(b);
        body.setUserData(b);

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
        pBody.createFixture(fdef).setUserData(BORDER_HORIZONTAL);
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
        pBody.createFixture(fdef).setUserData(BORDER_HORIZONTAL);
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




    public SpriteBatch getBatch(){
        return batch;
    }
}
