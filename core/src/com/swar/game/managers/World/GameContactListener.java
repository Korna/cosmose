package com.swar.game.managers.World;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.swar.game.entities.Asteroid;
import com.swar.game.entities.Bullet;

import static com.swar.game.utils.constants.*;

/**
 * Created by Koma on 15.01.2017.
 */
public class GameContactListener implements ContactListener {

    private Array<Body> bodiesToRemove;
    private Array<Body> bulletsToRemove;
    public Body shadowToRemove = null;

    private int hp = 0;
    private int credits = 0;
    private int score = 0;
    private int energy = 0;

    public GameContactListener(){
        super();

        bodiesToRemove = new Array<Body>();
        bulletsToRemove = new Array<Body>();
    }

    //when 2 fixtures start to collide
    Fixture fa;
    Fixture fb;

    private void fixtureSolver(Fixture A, Fixture B, String A_name, String B_name, Runnable A_action, Runnable B_action){
        if(A.getUserData().equals(A_name) && B.getUserData().equals(B_name))
            A_action.run();

        if(A.getUserData().equals(B_name) && B.getUserData().equals(A_name))
            B_action.run();
    }

    Runnable SHIP_AND_BONUS = new Runnable() {
        @Override
        public void run() {

        }
    };

    public void beginContact(Contact c){
        fa = c.getFixtureA();
        fb = c.getFixtureB();
        Body ba = fa.getBody();
        Body bb = fb.getBody();



        if (isAAFirst(ASTEROID, PLAYER_SHIP)) {
            bodiesToRemove.add(ba);
            minushp();
            return;
        }
        if (isABFirst(ASTEROID, PLAYER_SHIP)) {
            bodiesToRemove.add(bb);
            minushp();
            return;
        }

        if(isAAFirst(ASTEROID, BULLET_PIERCING)){

            Asteroid asteroid = (Asteroid) ba.getUserData();
            Bullet bullet = (Bullet) bb.getUserData();
            asteroid.setHp(asteroid.getHp() - (int) bullet.getBulletModel().getDamage());

            if(asteroid.getHp() < 0){
                bodiesToRemove.add(ba);
                credits++;
                score +=5;
            }

            System.out.println("Got hit");
            return;
        }
        if(isABFirst(ASTEROID, BULLET_PIERCING)){
            Bullet bullet = (Bullet) ba.getUserData();

            Asteroid asteroid = (Asteroid) bb.getUserData();
            asteroid.setHp(asteroid.getHp() - bullet.getBulletModel().getDamage());

            if(asteroid.getHp() < 0){
                bodiesToRemove.add(bb);
                credits++;
                score +=5;
            }

            System.out.println("Got hit");
            return;
        }
        if(isAAFirst(ASTEROID, BULLET_DESTROYABLE)){
            bodiesToRemove.add(bb);


            Asteroid asteroid = (Asteroid) ba.getUserData();
            Bullet bullet = (Bullet) bb.getUserData();
            asteroid.setHp(asteroid.getHp() - (int) bullet.getBulletModel().getDamage());

            if(asteroid.getHp() < 0){
                bodiesToRemove.add(ba);
                 credits++;
                score +=5;
            }

            System.out.println("Got hit");
            return;
        }
        if(isABFirst(ASTEROID, BULLET_DESTROYABLE)){
            bodiesToRemove.add(ba);
            Bullet bullet = (Bullet) ba.getUserData();

            Asteroid asteroid = (Asteroid) bb.getUserData();
            asteroid.setHp(asteroid.getHp() - bullet.getBulletModel().getDamage());

            if(asteroid.getHp() < 0){
                bodiesToRemove.add(bb);
                credits++;
                score +=5;
            }

            System.out.println("Got hit");
            return;
        }

        if(isAAFirst(BULLET_DESTROYABLE, BORDER_HORIZONTAL)){
            bodiesToRemove.add(ba);
            return;
        }
        if(isABFirst(BULLET_DESTROYABLE, BORDER_HORIZONTAL)){
            bodiesToRemove.add(bb);
            return;
        }

        if(isAAFirst(BULLET_PIERCING, BORDER_HORIZONTAL)){
            bodiesToRemove.add(ba);
            return;
        }
        if(isABFirst(BULLET_PIERCING, BORDER_HORIZONTAL)){
            bodiesToRemove.add(bb);
            return;
        }


        if(isAAFirst(BONUS, PLAYER_SHIP)){
            bodiesToRemove.add(ba);
            credits+=100;
            score += 50;
            energy += 10;
        }
        if(isABFirst(BONUS, PLAYER_SHIP)){
            bodiesToRemove.add(bb);
            credits+=100;
            score += 50;
            energy += 10;
        }

        /*
        if(isAAFirst(SHADOW, BulletDestroyable)){
            shadowToRemove = ba;
        }
        if(isABFirst(SHADOW, BulletDestroyable)){
            shadowToRemove = bb;
        }

        if(isAAFirst(SHADOW, BulletPiercing)){
            shadowToRemove = ba;
        }
        if(isABFirst(SHADOW, BulletPiercing)){
            shadowToRemove = bb;
        }*/



        //эту часть вставляем в end contact для норм отрисовки
        if(!fb.getUserData().equals(PLAYER_SHIP) && fa.getUserData().equals(BORDER_HORIZONTAL)){
            if (fb.getUserData().equals(ASTEROID)){
                bodiesToRemove.add(bb);
                return;
            }

        }else
            if(!fa.getUserData().equals(PLAYER_SHIP) && fb.getUserData().equals(BORDER_HORIZONTAL)) {
                  if (fa.getUserData().equals(ASTEROID)){
                      bodiesToRemove.add(ba);
                      return;
                  }
            }



    }

    private boolean isAAFirst(String a, String b){
        if(fa.getUserData().equals(a) && fb.getUserData().equals(b))
            return true;
        else
            return false;
    }


    private boolean isABFirst(String a, String b){
        if(fa.getUserData().equals(b) && fb.getUserData().equals(a))
            return true;
        else
            return false;
    }

    //when fixtures no longer collide
    public void endContact(Contact c){
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa == null || fb == null){
            System.out.println("null object found");
            return;
        }

    }

    public Array<Body> getBodiesToRemove() { return bodiesToRemove; }

    public int getScoreAndClear(){
        int scoreToReturn = this.score;
        this.score = 0;

        return scoreToReturn;
    }

    public int getEnergyAndClear(){
        int energyToReturn = this.energy;
        this.energy = 0;

        return energyToReturn;
    }

    public void clearList(){
       // bodiesToRemove.clear();
        bodiesToRemove = new Array<>();

    }
    public void preSolve(Contact c, Manifold m){}
    public void postSolve(Contact c, ContactImpulse ci){}

    private void minushp(){ hp-=10;}

    public int getHp(){
        int savedHp = hp;
        hp = 0;
        return savedHp;}
    public int getCredits(){
        int savedCr = credits;
        credits = 0;
        return savedCr;}


}