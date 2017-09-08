package com.swar.game.managers;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.swar.game.utils.constants.*;

/**
 * Created by Koma on 15.01.2017.
 */
public class GameContactListener implements ContactListener {

    private Array<Body> bodiesToRemove;
    private Array<Body> bulletsToRemove;

    private int hp = 100;
    private int credits = 0;
    public GameContactListener(){
        super();

        bodiesToRemove = new Array<Body>();
        bulletsToRemove = new Array<Body>();
    }

    //when 2 fixtures start to collide
    Fixture fa;
    Fixture fb;
    public void beginContact(Contact c){
        fa = c.getFixtureA();
        fb = c.getFixtureB();

        if( fa.getUserData().equals(BONUS) && fb.getUserData().equals(PLAYER_SHIP)){
            //remove crystal

            bodiesToRemove.add(fa.getBody());
            credits+=100;

        }else
            if(fa.getUserData().equals(PLAYER_SHIP)&& fb.getUserData().equals(BONUS)){
                bodiesToRemove.add(fb.getBody());
                credits+=100;
            }


        if( fa.getUserData().equals(ASTEROID) && fb.getUserData().equals(PLAYER_SHIP)){
            //remove crystal

            bodiesToRemove.add(fa.getBody());
            minushp();

        }else
            if(fa.getUserData().equals(PLAYER_SHIP)&& fb.getUserData().equals(ASTEROID)){
                bodiesToRemove.add(fb.getBody());
                minushp();
            }


        if(fb.getUserData().equals(ASTEROID) && fa.getUserData().equals(BULLET_PIERCING)){
            System.out.printf("hit asteroid\n");
            bodiesToRemove.add(fb.getBody());
            credits++;
           // bulletsToRemove.add(fa.getBody());

        }else
            if(fa.getUserData().equals(ASTEROID) && fb.getUserData().equals(BULLET_PIERCING)){
                System.out.printf("hit asteroid\n");
                bodiesToRemove.add(fa.getBody());
                credits++;
              //  bulletsToRemove.add(fb.getBody());
            }

        if(fb.getUserData().equals(ASTEROID) && fa.getUserData().equals(BULLET_DESTROYABLE)){
            System.out.printf("hit asteroid\n");
            bodiesToRemove.add(fb.getBody());
            bodiesToRemove.add(fa.getBody());
            credits++;
            // bulletsToRemove.add(fa.getBody());

        }else
            if(fa.getUserData().equals(ASTEROID) && fb.getUserData().equals(BULLET_DESTROYABLE)){
                System.out.printf("hit asteroid\n");
                bodiesToRemove.add(fa.getBody());
                bodiesToRemove.add(fb.getBody());
                credits++;
                //  bulletsToRemove.add(fb.getBody());
            }


        //эту часть вставляем в end contact для норм отрисовки
        if(!fb.getUserData().equals(PLAYER_SHIP) && fa.getUserData().equals("borderBottom")){
            if (fb.getUserData().equals(ASTEROID))
                bodiesToRemove.add(fb.getBody());


        }else
            if(!fa.getUserData().equals(PLAYER_SHIP) && fb.getUserData().equals("borderBottom")) {
                  if (fa.getUserData().equals(ASTEROID))
                      bodiesToRemove.add(fa.getBody());

            }

    }

    private boolean bodiesCollide(String a, String b){
        if(fa.getUserData().equals(a) && fb.getUserData().equals(b))
            return true;
        else
            if(fb.getUserData().equals(a) && fa.getUserData().equals(b))
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

    public void clearList(){
        bodiesToRemove.clear();
    }
    public void preSolve(Contact c, Manifold m){}
    public void postSolve(Contact c, ContactImpulse ci){}

    private void minushp(){ hp-=10;}
    private void plushp(){ hp+=10;}

    public int getHp(){return hp;}
    public int getCredits(){return credits;}

    public boolean isPlayerDead(){
        return hp <=0;
    }

}