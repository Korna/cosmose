package com.swar.game.Models;

import com.badlogic.gdx.math.Vector2;
import com.swar.game.entities.*;
import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.ObjectHandler;

/**
 * Created by Koma on 09.10.2017.
 */
public class AbilityStop implements  Ability  {
    float time = 5f;


    @Override
    public void use(BodyBuilder bodyBuilder, ObjectHandler objectHandler, Player player) {
        if(time >= 0) {
            setSpeedForAll(objectHandler, 0, 0);
        }

    }

    private void setSpeedForAll(ObjectHandler objectHandler, float x, float y){

        for (int i = 0; i < objectHandler.listAsteroid.size; ++i) {
            Asteroid asteroid = objectHandler.listAsteroid.get(i);
            Vector2 targetPosition = new Vector2(x, y);
            asteroid.getBody().setLinearVelocity(targetPosition);

        }

        for (int i = 0; i < objectHandler.listEnemy.size; ++i) {
            Enemy enemy = objectHandler.listEnemy.get(i);
            Vector2 targetPosition = new Vector2(x, y);
            enemy.getBody().setLinearVelocity(targetPosition);


        }


        for (int i = 0; i < objectHandler.listBulletPlayer.size; ++i) {
            Bullet bullet = objectHandler.listBulletPlayer.get(i);
            bullet.getBody().setLinearVelocity(x, y);

        }


        //удаление бонусов спустя время
        for (int i = 0; i < objectHandler.listDisappearable.size; ++i) {
            Sprite bonus = objectHandler.listDisappearable.get(i);
            bonus.getBody().setLinearVelocity(x, y);

        }
    }

    private void setDefault(ObjectHandler objectHandler){
        for (int i = 0; i < objectHandler.listAsteroid.size; ++i) {
            Asteroid asteroid = objectHandler.listAsteroid.get(i);
            Vector2 targetPosition = new Vector2(0, asteroid.getSpeed());
            asteroid.getBody().setLinearVelocity(targetPosition);

        }

        for (int i = 0; i < objectHandler.listEnemy.size; ++i) {
            Enemy enemy = objectHandler.listEnemy.get(i);
            Vector2 targetPosition = new Vector2(0, enemy.getSpeed());
            enemy.getBody().setLinearVelocity(targetPosition);


        }


        for (int i = 0; i < objectHandler.listBulletPlayer.size; ++i) {
            Bullet bullet = objectHandler.listBulletPlayer.get(i);
            bullet.getBody().setLinearVelocity(0, bullet.getSpeed());

        }


        //удаление бонусов спустя время
        for (int i = 0; i < objectHandler.listDisappearable.size; ++i) {
            Sprite bonus = objectHandler.listDisappearable.get(i);
            bonus.getBody().setLinearVelocity(0, -10);

        }

    }

    @Override
    public void update(float delta) {
        this.time -= delta;
    }
}
