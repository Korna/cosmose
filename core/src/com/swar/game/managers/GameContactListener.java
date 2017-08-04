package com.swar.game.managers;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Koma on 15.01.2017.
 */
public class GameContactListener implements ContactListener {
    private int numFootContacts;
    private Array<Body> bodiesToRemove;
    private Array<Body> bulletsToRemove;
    private int hp = 100;
    private int credits;
    public GameContactListener(){
        super();

        bodiesToRemove = new Array<Body>();
        bulletsToRemove = new Array<Body>();
    }

    //when 2 fixtures start to collide
    public void beginContact(Contact c){

        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        if(fa == null || fb == null){
            System.out.println("null object found");
            return;
        }


        if(fb.getUserData().equals("player") && fa.getUserData().equals("asteroid")){
            //remove crystal

            bodiesToRemove.add(fa.getBody());
            minushp();

        }
        if(fa.getUserData().equals("player")&& fb.getUserData().equals("asteroid")){
            //remove crystal
            bodiesToRemove.add(fb.getBody());
            minushp();
        }


        if(fb.getUserData().equals("asteroid") && fa.getUserData().equals("bulletPlayer")){
            System.out.printf("hit asteroid\n");
            bodiesToRemove.add(fb.getBody());
            credits++;
           // bulletsToRemove.add(fa.getBody());

        }
        if(fa.getUserData().equals("asteroid") && fb.getUserData().equals("bulletPlayer")){
            System.out.printf("hit asteroid\n");
            bodiesToRemove.add(fa.getBody());
            credits++;
          //  bulletsToRemove.add(fb.getBody());
        }

        //эту часть вставляем в end contact для норм отрисовки
        if(!fb.getUserData().equals("player") && fa.getUserData().equals("borderBottom")){

            if (fb.getUserData().equals("asteroid")){
                bodiesToRemove.add(fb.getBody());
            }

        }
        if(!fa.getUserData().equals("player") && fb.getUserData().equals("borderBottom")) {

              if (fa.getUserData().equals("asteroid")){
                  bodiesToRemove.add(fa.getBody());
        }


        }

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
    public boolean isPlayerOnGround(){return numFootContacts > 0;}
    public Array<Body> getBodiesToRemove() { return bodiesToRemove; }

    public void preSolve(Contact c, Manifold m){}
    public void postSolve(Contact c, ContactImpulse ci){}

    private void minushp(){ hp--;}

    public int getHp(){return hp;}
    public int getCredits(){return credits;}
    public void zeroHp(){hp = 0;}

}