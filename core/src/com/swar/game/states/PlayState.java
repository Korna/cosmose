package com.swar.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
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

import java.util.Iterator;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.swar.game.utils.constants.*;

/**
 * Created by Koma on 17.01.2017.
 */
public class PlayState extends GameState{
    private boolean debug = true; //отрисовывать ли контуры столкновения объектов



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


        Gdx.input.setInputProcessor(new GameInputProcessor());


        createBorders();

        this.listAsteroid = new Array();
        this.listBulletPlayer = new Array();


        tex_background = Game.res.getTexture("background_1");

        hud = new HUD(player);

    }




    @Override
    public void update(float delta) {

        int bulletSpeed = 350;
        int asteroidSpeed = -50;

        player.update(delta);

       // handleInput();

        inputUpdate(delta);

        world.step(STEP, 6, 2);

        if(random.nextInt(15) == 1)
            createAsteroid();


        //удаление астероидов
        Array<Body> bodies = cl.getBodiesToRemove();
        for (int i = 0; i < bodies.size; i++){
            Body b = bodies.get(i);

                listAsteroid.removeValue((Asteroid) b.getUserData(), true);
                world.destroyBody(b);

               // System.out.printf("Error: unknown contact\n");


        }
        bodies.clear();//чистим лист

        for(int i = 0; i < listAsteroid.size; i++){
            listAsteroid.get(i).update(delta);
            listAsteroid.get(i).getBody().setLinearVelocity(listAsteroid.get(i).getBody().getLinearVelocity().x, asteroidSpeed);

        }

        for(int i = 0; i < listBulletPlayer.size; i++){
            listBulletPlayer.get(i).update(delta);
            listBulletPlayer.get(i).getBody().setLinearVelocity(listBulletPlayer.get(i).getBody().getLinearVelocity().x, bulletSpeed);
        }

        batch.setProjectionMatrix(maincamera.combined);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
        batch.draw(tex_background, 0, 0);
        batch.end();


        if(debug)
            b2dr.render(world, maincamera.combined);

        //rendering crystals
        for(int i = 0; i < listAsteroid.size; i++){
            listAsteroid.get(i).render(batch);
        }

        for(int i = 0; i < listBulletPlayer.size; i++){
            listBulletPlayer.get(i).render(batch);
        }

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


    /*
        public void handleInput(){

            if(GameInput.isPressed(GameInput.BUTTON1)){
                if(cl.isPlayerOnGround()){
                    player.getBody().applyForceToCenter(0, 27, false);
                }

                //  System.out.println("pressed z");
            }
            if(GameInput.isPressed(GameInput.BUTTON2)){
                //    System.out.println("pressed x");
            }
        }
    */
    public void inputUpdate(float delta){
        int horizontalForse = 0;
        int verticalForse = 0;
        int shipSpeed = player.getSpeed();

        player.ship();

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
        int x = random.nextInt(300) + 15;
        int y = 480;

        bdef.position.set(x, y);
        CircleShape cshape = new CircleShape();
        cshape.setRadius(10F);
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

    private void createBulletPlayer() {


        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        FixtureDef fdef = new FixtureDef();

        //позиционирование выстрела
        float x = player.getBody().getPosition().x;
        float y = player.getBody().getPosition().y + 5;

        bdef.position.set(x, y);
        CircleShape cshape = new CircleShape();
        cshape.setRadius(1.5F);
        fdef.shape = cshape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = BIT_BULLET;
        fdef.filter.maskBits = BIT_ENEMY | BIT_BORDER;
        fdef.isSensor = true;
        Body body = this.world.createBody(bdef);
        body.createFixture(fdef).setUserData("bulletPlayer");
        Bullet b = new Bullet(body);
        this.listBulletPlayer.add(b);
        body.setUserData(b);
    }

    private void createBorders(){



        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;



        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GAME_WIDTH/ 4
                //  / PPM
                , 1
                //  / PPM
        );
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_BORDER;

        def.position.set(GAME_WIDTH/ 4
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
        shape.setAsBox(GAME_WIDTH/ 4, 1);
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_BORDER;
        def.position.set(GAME_WIDTH/ 4, GAME_HEIGHT);
        pBody = world.createBody(def);
        pBody.createFixture(fdef).setUserData("borderBottom");
        shape.dispose();


        def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        shape = new PolygonShape();
        shape.setAsBox(1, GAME_HEIGHT/ 4);
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_BORDER;
        def.position.set(1, GAME_HEIGHT/ 4);
        pBody = world.createBody(def);
        pBody.createFixture(fdef).setUserData("border");
        shape.dispose();


        def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        shape = new PolygonShape();
        shape.setAsBox(1, GAME_HEIGHT/ 4);
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_BORDER;
        def.position.set(GAME_WIDTH/ 2, GAME_HEIGHT/ 4);
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
        shape.setAsBox(width / 2
              //  / PPM
                , height / 2
              //  / PPM
        );

        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;//тут игрок тоже является ящиком, пох, пока без лишних параметров в функцию
        fdef.filter.maskBits = BIT_ENEMY | BIT_OBJECT;


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
