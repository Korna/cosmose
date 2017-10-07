package com.swar.game.managers.World;


import com.badlogic.gdx.physics.box2d.*;

import static com.swar.game.utils.constants.*;

/**
 * Created by Koma on 01.10.2017.
 */
public class BodyBuilder extends Builder {


    public BodyBuilder(World world) {
        super(world);
    }


    public Body createAsteroid(float x, float y) {
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

        return body;
    }


    public Body createBonus(float x, float y){
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x, y);

        CircleShape cshape = new CircleShape();
        cshape.setRadius(GAME_WIDTH/70);

        fdef.shape = cshape;
        fdef.filter.categoryBits = BIT_OBJECT;
        fdef.filter.maskBits = BIT_PLAYER;
        fdef.isSensor = true;

        Body body = this.world.createBody(bdef);
        body.createFixture(fdef).setUserData(BONUS);
        return body;
    }


    public Body createBulletPlayer(float x, float y, String type) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        FixtureDef fdef = new FixtureDef();

        //позиционирование выстрела


        bdef.position.set(x, y);
        CircleShape cshape = new CircleShape();
        cshape.setRadius(GAME_WIDTH/190);
        fdef.shape = cshape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = BIT_BULLET;
        fdef.filter.maskBits = BIT_ENEMY | BIT_BORDER | BIT_SHADOW;

        Body body = this.world.createBody(bdef);


        body.createFixture(fdef).setUserData(type);

        return body;
    }

    public Body createShadow(int x, int y, float width, float height){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);



        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_SHADOW;
        fdef.filter.maskBits =  BIT_BULLET | BIT_BORDER;


        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
        def.fixedRotation = true;


        Body pBody;
        pBody = world.createBody(def);


        pBody.createFixture(fdef).setUserData(SHADOW);
        shape.dispose();

        return pBody;
    }

    public Body createBorder(String type, int x, int y, int height, int weight){

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;



        PolygonShape shape = new PolygonShape();
        shape.setAsBox(height, weight);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_BORDER;

        def.position.set(x, y);

        Body pBody = world.createBody(def);
        pBody.createFixture(fdef).setUserData(BORDER_HORIZONTAL);
        shape.dispose();

        return pBody;
    }



}
