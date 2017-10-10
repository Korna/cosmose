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
            for (int i = 0; i < objectHandler.listAsteroid.size; ++i) {
                Asteroid asteroid = objectHandler.listAsteroid.get(i);
                Vector2 targetPosition = new Vector2(0, 0);
                asteroid.getBody().setLinearVelocity(targetPosition);

            }

            for (int i = 0; i < objectHandler.listEnemy.size; ++i) {
                Enemy enemy = objectHandler.listEnemy.get(i);
                Vector2 targetPosition = new Vector2(0, 0);
                enemy.getBody().setLinearVelocity(targetPosition);


            }


            for (int i = 0; i < objectHandler.listBulletPlayer.size; ++i) {
                Bullet bullet = objectHandler.listBulletPlayer.get(i);
                bullet.getBody().setLinearVelocity(0, 0);

            }


            //удаление бонусов спустя время
            for (int i = 0; i < objectHandler.listBonus.size; ++i) {
                Bonus bonus = objectHandler.listBonus.get(i);
                bonus.getBody().setLinearVelocity(0, 0);

            }

        }

    }

    @Override
    public void update(float delta) {
        this.time -= delta;
    }
}
