package com.swar.game.Models;

import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Types.BulletType;
import com.swar.game.entities.Bullet;
import com.swar.game.entities.Player;
import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.ObjectHandler;

import static com.swar.game.utils.constants.BULLET_PIERCING;

/**
 * Created by Koma on 09.10.2017.
 */
public class AbilityJugg implements Ability, Creator{

    float time = 1f;
    float reload = 3f;

    @Override
    public void use(BodyBuilder bodyBuilder, ObjectHandler objectHandler, Player player) {
        if(time > 0){
            BulletType type = BulletType.bullet_6;

            Body body = bodyBuilder.createBulletPlayer(player.getBody().getPosition().x, player.getBody().getPosition().y, BULLET_PIERCING, type);
            Bullet b;
            b = new Bullet(body, type, BulletType.getbullet(type), +2000);
            body.setUserData(b);
            objectHandler.add(b);
            --time;
            
        }
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean createObject(BodyBuilder bodyBuilder, ObjectHandler objectHandler) {


        return false;
    }
}
