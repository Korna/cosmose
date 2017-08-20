package com.swar.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.swar.game.Game;
import com.swar.game.entities.Asteroid;
import com.swar.game.entities.Bullet;
import com.swar.game.entities.HUD;
import com.swar.game.entities.Player;
import com.swar.game.managers.GameContactListener;
import com.swar.game.managers.GameInputProcessor;
import com.swar.game.managers.GameStateManagement;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.swar.game.utils.constants.*;

/**
 * Created by Koma on 17.01.2017.
 */
public class PlayState extends GameState{
    private final boolean DEBUG_RENDER = true; //отрисовывать ли контуры столкновения объектов

    private Box2DDebugRenderer b2dr;
    private HUD hud;

    private GameContactListener cl;
    private World world;
    private Player player;
    private Body playerBody;

    private Array<Asteroid> listAsteroid;
    private Array<Bullet> listBulletPlayer;

    public Texture tex_background;
    public PlayState(GameStateManagement gsm) {
        super(gsm);

        cl = gsm.cl;
        world = gsm.world;
        player = gsm.player;
        playerBody = gsm.playerBody;

        Gdx.input.setInputProcessor(new GameInputProcessor());

        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();
        batch = new SpriteBatch();

        createBorders(world);

        this.listAsteroid = new Array<>();
        this.listBulletPlayer = new Array<>();

        tex_background = Game.res.getTexture("background_1");

        hud = new HUD(player);
    }



    private final int bulletSpeed = GAME_WIDTH / 2;
    private final int asteroidSpeed = -(GAME_WIDTH / 10);

    @Override
    public void update(float delta) {


        player.update(delta);

        inputUpdate(delta);



        if(random.nextInt(16) == 1)
            createAsteroid();



        //удаление астероидов
        Array<Body> bodies = cl.getBodiesToRemove();

        for(Body body : bodies){
            listAsteroid.removeValue((Asteroid) body.getUserData(), true);



            Array<Body> temp = new Array<>();
            world.getBodies(temp);//TODO заменить систему нахождения уникальных объектов. сейчас происходит перебор всех объектов чтоб понять, не удален ли уже объект(можно ли еще удалить)
            //потенциально можно заменить на хешмапу, чтоб объекты стакались и реврайтились(надо тщательно проверять)
            for(Body tBody : temp){
                if(tBody == body)
                    world.destroyBody(body);
            }

            System.out.printf("deleted\n");
        }

        cl.clearList();

        new Thread(new Runnable(){
            @Override public void run(){

                for(Asteroid asteroid : listAsteroid){
                    asteroid.update(delta);
                    asteroid.getBody().setLinearVelocity(asteroid.getBody().getLinearVelocity().x, asteroidSpeed);

                }

        }}).start();



        new Thread(new Runnable(){
            @Override public void run(){

                for(Bullet bullet : listBulletPlayer){
                    bullet.update(delta);
                    bullet.getBody().setLinearVelocity(bullet.getBody().getLinearVelocity().x, bulletSpeed);
                }

        }}).start();

        batch.setProjectionMatrix(maincamera.combined);

        doWorldStep(delta);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
        batch.draw(tex_background, 0, 0, GAME_WIDTH/2, GAME_HEIGHT/2);

        batch.end();


        if(DEBUG_RENDER)
            b2dr.render(world, maincamera.combined);



        for(Asteroid asteroid : listAsteroid)
            asteroid.render(batch);

        for(Bullet bullet : listBulletPlayer)
            bullet.render(batch);


        player.render(batch);
        hud.render(batch_hud);
    }

    @Override
    public void dispose() {
        tex_background.dispose();
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


    public void inputUpdate(float delta){
        int horizontalForse = 0;
        int verticalForse = 0;
        int shipSpeed = player.getSpeed();

        player.ship();

        if(Gdx.input.isTouched()){
            createBulletPlayer();
            Gdx.input.vibrate(9);
        }



        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
            --horizontalForse;
            player.ship_l();

        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
            ++horizontalForse;
            player.ship_r();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){
            ++verticalForse;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
            --verticalForse;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            createBulletPlayer();
        }

        player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, verticalForse * shipSpeed);
        player.getBody().setLinearVelocity(horizontalForse * shipSpeed, player.getBody().getLinearVelocity().y);

    }
    private void createAsteroid() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.DynamicBody;
        int x = random.nextInt(GAME_WIDTH/2 + 15) - 15;
        int y = GAME_HEIGHT/2 - 50;
        bdef.position.set(x, y);

        CircleShape cshape = new CircleShape();
        cshape.setRadius(GAME_WIDTH/40);

        fdef.shape = cshape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = BIT_ENEMY;
        fdef.filter.maskBits = BIT_PLAYER | BIT_BULLET | BIT_BORDER;
        fdef.isSensor = true;

        Body body = this.world.createBody(bdef);
        body.createFixture(fdef).setUserData("asteroid");
        Asteroid a = new Asteroid(body);
        this.listAsteroid.add(a);
        body.setUserData(a);
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
        fdef.filter.maskBits = BIT_ENEMY | BIT_BORDER;
        fdef.isSensor = true;
        Body body = this.world.createBody(bdef);
        body.createFixture(fdef).setUserData("bulletPlayer");
        Bullet b = new Bullet(body, player.bulletIndex);
        this.listBulletPlayer.add(b);
        ++bulletAmount;

        System.out.println(bulletAmount);

        body.setUserData(b);
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


    public Body createPlayer(int x, int y, int width, int height){

        Body pBody;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width
              //  / PPM
                , height
              //  / PPM
        );

        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;//тут игрок тоже является ящиком, пох, пока без лишних параметров в функцию
        fdef.filter.maskBits = BIT_ENEMY | BIT_OBJECT | BIT_BORDER;

        def.position.set(x
            //    / PPM
                , y
                  //      / PPM
        );
        def.fixedRotation = true;

        pBody = world.createBody(def);



        //pBody.createFixture(shape, 0.8f);

        pBody.createFixture(fdef).setUserData("player");

        shape.dispose();

        return pBody;
    }

    public SpriteBatch getBatch(){
        return batch;
    }
}
