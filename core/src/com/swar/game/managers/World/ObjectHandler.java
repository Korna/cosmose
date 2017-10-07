package com.swar.game.managers.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.swar.game.entities.Asteroid;
import com.swar.game.entities.Bonus;
import com.swar.game.entities.Bullet;
import com.swar.game.entities.Enemy;

/**
 * Created by Koma on 02.10.2017.
 */
public class ObjectHandler {

    public Array<Asteroid> listAsteroid;
    public Array<Bullet> listBulletPlayer;
    public Array<Bonus> listBonus;
    public Array<Enemy> listEnemy;

    private World world;

    public ObjectHandler(Array<Asteroid> listAsteroid, Array<Bullet> listBulletPlayer, Array<Bonus> listBonus, Array<Enemy> enemyList, World world) {
        this.listAsteroid = listAsteroid;
        this.listBulletPlayer = listBulletPlayer;
        this.listBonus = listBonus;
        this.listEnemy = enemyList;
        this.world = world;
    }

    public void clearAll(){

        for(Asteroid asteroid : listAsteroid)
            world.destroyBody(asteroid.getBody());
        for(Bullet bullet : listBulletPlayer)
            world.destroyBody(bullet.getBody());
        for(Bonus bonus : listBonus)
            world.destroyBody(bonus.getBody());
        for(Enemy enemy : listEnemy)
            world.destroyBody(enemy.getBody());

        listBonus.clear();
        listBulletPlayer.clear();
        listAsteroid.clear();
        listEnemy.clear();

    }

    public void add(Asteroid a){
        this.listAsteroid.add(a);
    }

    public void add(Bullet b){
        this.listBulletPlayer.add(b);
    }
    public void add(Bonus b){
        this.listBonus.add(b);
    }
    public void add(Enemy e){
        this.listEnemy.add(e);
    }


    public void remove(Asteroid a){
        listAsteroid.removeValue(a, true);
    }
    public void remove(Bonus b){
        listBonus.removeValue(b, true);
    }
    public void remove(Bullet b){
        listBulletPlayer.removeValue(b, true);
    }
    public void remove(Enemy e){
        listEnemy.removeValue(e, true);
    }

    public void render(SpriteBatch batch){
        for(Asteroid asteroid : listAsteroid)
            asteroid.render(batch);

        for(Bonus bonus : listBonus)
            bonus.render(batch);

        for(Bullet bullet : listBulletPlayer)
            bullet.render(batch);

        for(Enemy enemy : listEnemy)
            enemy.render(batch);
    }

}
