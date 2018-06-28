package com.swar.game.managers.World;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.swar.game.Models.Killable;
import com.swar.game.entities.Asteroid;
import com.swar.game.entities.Bonuses.Effectable;
import com.swar.game.entities.Bullet;
import com.swar.game.entities.Player;

import static com.swar.game.utils.constants.*;

/**
 * Created by Koma on 15.01.2017.
 */
public class GameContactListener implements ContactListener {

    private Array<Body> bodiesToRemove;
    private Array<Body> bulletsToRemove;
    public Body shadowToRemove = null;

    private Array<Vector2> blasts = new Array<>();

    private float hp = 0;
    private int credits = 0;
    private int score = 0;
    private float energy = 0;

    private Player player;
    public GameContactListener(Player player){
        super();

        this.player = player;

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

    public void beginContact(Contact c) throws NullPointerException{
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
            blasts.add(bb.getPosition());

            Killable asteroid = (Killable) ba.getUserData();
            Bullet bullet = (Bullet) bb.getUserData();
            asteroid.setHp(asteroid.getHp() - (int) bullet.getBulletModel().getDamage());

            if(asteroid.getHp() <= 0){
                bodiesToRemove.add(ba);
                credits++;
                score +=5;
            }

            System.out.println("Got hit");
            return;
        }
        if(isABFirst(ASTEROID, BULLET_PIERCING) || isABFirst(ENEMY, BULLET_PIERCING)){
            blasts.add(bb.getPosition());

            Bullet bullet = (Bullet) ba.getUserData();

            Killable asteroid = (Killable) bb.getUserData();
            asteroid.setHp(asteroid.getHp() - bullet.getBulletModel().getDamage());

            if(asteroid.getHp() <= 0){
                bodiesToRemove.add(bb);
                credits++;
                score +=5;
            }

            System.out.println("Got hit");
            return;
        }
        if(isAAFirst(ASTEROID, BULLET_DESTROYABLE) || isAAFirst(ENEMY, BULLET_DESTROYABLE)){
            blasts.add(bb.getPosition());
            bodiesToRemove.add(bb);


            Killable asteroid = (Killable) ba.getUserData();
            Bullet bullet = (Bullet) bb.getUserData();
            try {
                asteroid.setHp(asteroid.getHp() - bullet.getBulletModel().getDamage());

                if(asteroid.getHp() <= 0){
                    bodiesToRemove.add(ba);
                     credits++;
                    score +=5;
                }
            }catch(NullPointerException npe){
                System.out.println(npe.toString());
            }

            System.out.println("Got hit");
            return;
        }
        if(isABFirst(ASTEROID, BULLET_DESTROYABLE) || isABFirst(ENEMY, BULLET_DESTROYABLE)
                || isABFirst(ASTEROID, BULLET_EXPLOSIVE) || isABFirst(ENEMY, BULLET_EXPLOSIVE)){
            bodiesToRemove.add(ba);
            Bullet bullet = (Bullet) ba.getUserData();
            Asteroid asteroid = (Asteroid) bb.getUserData();

            try {
                asteroid.setHp(asteroid.getHp() - bullet.getBulletModel().getDamage());
                if(asteroid.getHp() <= 0){
                    bodiesToRemove.add(bb);
                    credits++;
                    score +=5;
                }
            }catch(NullPointerException npe){
                System.out.println(npe.toString());
            }

            System.out.println("Got hit");
            return;
        }

        if(isAAFirst(ENEMY, BULLET_EXPLOSIVE) ||  isAAFirst(ASTEROID, BULLET_EXPLOSIVE)){
            bodiesToRemove.add(bb);
            return;
        }

        if(isABFirst(ENEMY, BULLET_EXPLOSIVE) ||  isABFirst(ASTEROID, BULLET_EXPLOSIVE)){
            bodiesToRemove.add(ba);
            return;
        }

        if(player==null)
            System.out.println("NULL");

        if(isAAFirst(BONUS, PLAYER_SHIP)){
            bodiesToRemove.add(ba);
            Effectable bonus = (Effectable) ba.getUserData();
            bonus.pickUp(player);
            return;
        }
        if(isABFirst(BONUS, PLAYER_SHIP)){
            bodiesToRemove.add(bb);
            try {
                Effectable bonus = (Effectable) bb.getUserData();
                bonus.pickUp(player);
            }catch(NullPointerException npe){
                System.out.println(npe.toString());
            }
            return;
        }


    }

    //when fixtures no longer collide
    public void endContact(Contact c){
        fa = c.getFixtureA();
        fb = c.getFixtureB();
        Body ba = fa.getBody();
        Body bb = fb.getBody();

        if(isAAFirst(BULLET_DESTROYABLE, BORDER_HORIZONTAL) || isAAFirst(BULLET_PIERCING, BORDER_HORIZONTAL) || isAAFirst(BULLET_EXPLOSIVE, BORDER_HORIZONTAL)
                || isAAFirst(BULLET_ENEMY, BORDER_HORIZONTAL)
                || isAAFirst(ENEMY, BORDER_HORIZONTAL)
                ){
            bodiesToRemove.add(ba);
            return;
        }
        if(isABFirst(BULLET_DESTROYABLE, BORDER_HORIZONTAL) || isABFirst(BULLET_PIERCING, BORDER_HORIZONTAL) || isABFirst(BULLET_EXPLOSIVE, BORDER_HORIZONTAL)
                || isABFirst(BULLET_ENEMY, BORDER_HORIZONTAL)
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
    public Array<Vector2> getBlasts(){
        return blasts;
    }
    public int getScoreAndClear(){
        int scoreToReturn = this.score;
        this.score = 0;

        return scoreToReturn;
    }

    public float getEnergyAndClear(){
        float energyToReturn = this.energy;
        this.energy = 0;

        return energyToReturn;
    }

    public void clearList(){
       // blasts.clear();
       // bodiesToRemove.clear();
        blasts = new Array<>();
        bodiesToRemove = new Array<>();

    }
    public void preSolve(Contact c, Manifold m){}
    public void postSolve(Contact c, ContactImpulse ci){}

    private void minushp(){ hp-=10;}

    public float getHp(){
        float savedHp = hp;
        hp = 0;
        return savedHp;}
    public int getCredits(){
        int savedCr = credits;
        credits = 0;
        return savedCr;}


}