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
import com.swar.game.Models.RecordModel;
import com.swar.game.Models.Weapon;
import com.swar.game.State;
import com.swar.game.entities.*;
import com.swar.game.managers.GameConfig;
import com.swar.game.managers.GameStateManagement;
import com.swar.game.managers.InterfaceManager;
import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.GameContactListener;
import com.swar.game.managers.World.ObjectHandler;
import com.swar.game.utils.Randomizer;
import com.swar.game.utils.Singleton;

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



    private ObjectHandler objectHandler;
    private InterfaceManager interfaceManager;
    private BodyBuilder bodyBuilder;

    boolean CONFIG_VIBRATION;
    boolean available = false;

    public PlaySurvivalState(GameStateManagement gsm) {
        super(gsm);
        cl = new GameContactListener();

        world = gsm.world;
        player = gsm.player;

        GameConfig gameConfig = new GameConfig();
        CONFIG_VIBRATION = gameConfig.isVibraion();

       // Gdx.input.setInputProcessor(new GameInputProcessor());

        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();
        batch = new SpriteBatch();
        bodyBuilder = new BodyBuilder(world);



        bodyBuilder.createBorder(BORDER_HORIZONTAL, GAME_WIDTH, 0, GAME_WIDTH, 1);
        bodyBuilder.createBorder(BORDER_HORIZONTAL, GAME_WIDTH, GAME_HEIGHT, GAME_WIDTH, 1);
        bodyBuilder.createBorder("border", 1, GAME_HEIGHT, 1, GAME_HEIGHT);
        bodyBuilder.createBorder("border", GAME_WIDTH, GAME_HEIGHT, 1, GAME_HEIGHT);





        hud = new HUD(player, State.PLAYSURVIVAL);
        available = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);

        objectHandler = new ObjectHandler(new Array<>(), new Array<>(), new Array<>(), world);
        interfaceManager = new InterfaceManager(available, 0.5f, 4.5f);
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

            gsm.setState(State.DEATH);

            return;
        }


        interfaceManager.inputUpdate();
        inputAction(interfaceManager.shot, interfaceManager.horizontalForce, interfaceManager.verticalForce);

        player.update(delta);
        for(Weapon weapon : player.ship.weapons){
            weapon.setTimeAfterShot(weapon.getTimeAfterShot() + delta);
        }

        int energy = cl.getEnergyAndClear();
        player.ship.setEnergy(player.ship.getEnergy() + energy);


        if(randomizer.chanceAsteroid(player.timeInGame)){

            Body asteroidBody = bodyBuilder.createAsteroid(randomizer.getCoordinateAsteroid(),GAME_HEIGHT-30);

            Asteroid a = new Asteroid(asteroidBody);
            asteroidBody.setUserData(a);

            objectHandler.add(a);
        }




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
                objectHandler.remove((Asteroid) body.getUserData());
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

        for(int i = 0; i < objectHandler.listAsteroid.size; ++i) {//можно мб просто отмечать для удаления объекты? ставить маркер. а не ждать пока итератор пройдет по новой КОЛЛЕКЦИИ
            Asteroid asteroid = objectHandler.listAsteroid.get(i);
            final float cfg = 0.2f + player.timeInGame/500f;

            Vector2 targetPosition = new Vector2(0, asteroid.speed *cfg);
            asteroid.getBody().setLinearVelocity(targetPosition);

            asteroid.update(delta);
        }



        for(int i = 0; i<objectHandler.listBulletPlayer.size; ++i){//можно мб просто отмечать для удаления объекты? ставить маркер. а не ждать пока итератор пройдет по новой КОЛЛЕКЦИИ
            Bullet bullet = objectHandler.listBulletPlayer.get(i);
            bullet.getBody().setLinearVelocity(bullet.currentSpeed, bullet.speedY);
            bullet.update(delta);
        }




        //удаление бонусов спустя время
        for(int i = 0; i < objectHandler.listBonus.size; ++i){
            Bonus bonus = objectHandler.listBonus.get(i);
            bonus.setExistTime(bonus.getExistTime() + delta);
            if(bonus.getExistTime() > 30){
                world.destroyBody(bonus.getBody());

                objectHandler.listBonus.removeIndex(i);
                --i;
            }

        }

        for(Bonus bonus :objectHandler.listBonus){
            bonus.update(delta);
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

    private void inputAction(boolean shotCommand, int horizontal, int vertical){
        if(horizontal < 0)
            player.ship_l();
        if(horizontal > 0)
            player.ship_r();
        if(horizontal == 0)
            player.ship();
        if(shotCommand){
            if(player.ship.getEnergy() > 0){
                boolean hadShot = player.createObject(bodyBuilder, objectHandler);
                if(hadShot)
                    player.ship.setEnergy(player.ship.getEnergy() - 1);
            }
        }

        player.getBody().setLinearVelocity(horizontal * player.getSpeed(), vertical * player.getSpeed());
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
