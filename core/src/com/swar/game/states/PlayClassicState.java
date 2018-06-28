package com.swar.game.states;

//import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.swar.game.Models.Weapon;
import com.swar.game.Types.State;
import com.swar.game.entities.Asteroid;
import com.swar.game.entities.Enemy;
import com.swar.game.entities.HUD;
import com.swar.game.managers.*;
import com.swar.game.utils.Journal;

import static com.swar.game.utils.constants.*;

/**
 * Created by Koma on 17.01.2017.
 */
public class PlayClassicState extends PlayState{

    private Box2DDebugRenderer b2dr;
    private HUD hud;
    private IInterfaceManager interfaceManager;




    final static int GAME_TIME = 60;

    final private Journal instance = Journal.getInstance();


    int index = 0;//переменная для сохранения индекса кадра

    public PlayClassicState(GameStateManagement gsm) {
        super(gsm);



        try {
            b2dr = new Box2DDebugRenderer();
        }catch(NullPointerException npe){
            System.out.println(npe.toString());
            DEBUG_RENDER = false;
        }
        try {
            batch = new SpriteBatch();
        }catch(UnsatisfiedLinkError ule){
            System.out.println(ule.toString());
        }

        bodyBuilder.createBorder(BORDER_HORIZONTAL, GAME_WIDTH, 0, GAME_WIDTH, 1);
        bodyBuilder.createBorder(BORDER_HORIZONTAL, GAME_WIDTH, GAME_HEIGHT, GAME_WIDTH, 1);
        bodyBuilder.createBorder(BORDER_VERTICAL, 1, GAME_HEIGHT, 1, GAME_HEIGHT);
        bodyBuilder.createBorder(BORDER_VERTICAL, GAME_WIDTH, GAME_HEIGHT, 1, GAME_HEIGHT);



        hud = new HUD(player, State.PLAY_CLASSIC);





        try{
            if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer))//на андроиде ли или нет
                interfaceManager = new InterfaceManagerAndroid(0.5f, gameConfig.getPosY(), 0.25f);
            else
                interfaceManager = new InterfaceManagerPC();
        }catch (NullPointerException npe){
            System.out.println(npe.toString());
            interfaceManager = new IInterfaceManager() {
                @Override
                public void inputUpdate() {

                }

                @Override
                public boolean getShot() {
                    return false;
                }

                @Override
                public boolean getAbility() {
                    return false;
                }

                @Override
                public float getVForce() {
                    return 0;
                }

                @Override
                public float getHFloat() {
                    return 0;
                }
            };
        }


    }






    private boolean abilityOn = false;
    private int damageEffect = 0;
    private final static int effectTime = 5;

    @Override
    public void update(float delta) {

        if(isEndOfCycle(delta))
            return;



        //shadowMovement();

        if(!instance.firstRun && false){//TODO make shadowplayer work properly
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

        removeObjects();



        updateObjectMovements(delta);


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
        float energy = cl.getEnergyAndClear();
        player.ship.setEnergy(player.ship.getEnergy() + energy);


        try {
            batch.setProjectionMatrix(maincamera.combined);
        }catch(NullPointerException npe){
            System.out.println(npe.toString());
        }
        doWorldStep(delta);
    }
    protected void eventGenerator(){
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




    @Override
    public void render() {
        super.render();

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

        if(!instance.firstRun && !shadowPlayer.isDead() && false)//TODO make shadowplayer work properly
            shadowPlayer.render(batch);


        hud.render(batch_hud);
    }





    @Override
    public void dispose() {
        //tex_background.dispose();
        try {
            b2dr.dispose();
        }catch(NullPointerException npe){
            System.out.println(npe.toString());
        }
        try{
            world.dispose();
        }catch(NullPointerException npe){
            System.out.println(npe.toString());
        }
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








    public SpriteBatch getBatch(){
        return batch;
    }
}
