package com.swar.game.managers.World;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.swar.game.Models.Killable;
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



        if (isAAFirst(ASTEROID, PLAYER_SHIP) || isAAFirst(ENEMY, PLAYER_SHIP) || isAAFirst(BULLET_ENEMY, PLAYER_SHIP)) {
            bodiesToRemove.add(ba);
            minushp();
            return;
        }
        if (isABFirst(ASTEROID, PLAYER_SHIP) || isABFirst(ENEMY, PLAYER_SHIP) || isABFirst(BULLET_ENEMY, PLAYER_SHIP)) {
            bodiesToRemove.add(bb);
            minushp();
            return;
        }


        if(isAAFirst(ASTEROID, BULLET_PIERCING) || isAAFirst(ENEMY, BULLET_PIERCING)){

            Killable asteroid = (Killable) ba.getUserData();
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
        if(isABFirst(ASTEROID, BULLET_PIERCING) || isABFirst(ENEMY, BULLET_PIERCING)){
            Bullet bullet = (Bullet) ba.getUserData();

            Killable asteroid = (Killable) bb.getUserData();
            asteroid.setHp(asteroid.getHp() - bullet.getBulletModel().getDamage());

            if(asteroid.getHp() < 0){
                bodiesToRemove.add(bb);
                credits++;
                score +=5;
            }

            System.out.println("Got hit");
            return;
        }
        if(isAAFirst(ASTEROID, BULLET_DESTROYABLE) || isAAFirst(ENEMY, BULLET_DESTROYABLE)){
            bodiesToRemove.add(bb);


            Killable asteroid = (Killable) ba.getUserData();
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
        if(isABFirst(ASTEROID, BULLET_DESTROYABLE) || isABFirst(ENEMY, BULLET_DESTROYABLE)){
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





    }

    //when fixtures no longer collide
    public void endContact(Contact c){
        fa = c.getFixtureA();
        fb = c.getFixtureB();
        Body ba = fa.getBody();
        Body bb = fb.getBody();

        if(isAAFirst(BULLET_DESTROYABLE, BORDER_HORIZONTAL) || isAAFirst(BULLET_PIERCING, BORDER_HORIZONTAL) || isAAFirst(BULLET_ENEMY, BORDER_HORIZONTAL)
                || isAAFirst(ENEMY, BORDER_HORIZONTAL)
                ){
            bodiesToRemove.add(ba);
            return;
        }
        if(isABFirst(BULLET_DESTROYABLE, BORDER_HORIZONTAL) || isABFirst(BULLET_PIERCING, BORDER_HORIZONTAL) || isABFirst(BULLET_ENEMY, BORDER_HORIZONTAL)
                || isABFirst(ENEMY, BORDER_HORIZONTAL)
                ){
            bodiesToRemove.add(bb);
            return;
        }



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