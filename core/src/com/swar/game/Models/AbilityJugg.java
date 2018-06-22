package com.swar.game.Models;

import com.badlogic.gdx.math.Vector2;
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

    public Vector2 position;

    @Override
    public void use(BodyBuilder bodyBuilder, ObjectHandler objectHandler, Player player) {
        if(time > 0){
            setPosition(player.getBody().getPosition());
            createObject(bodyBuilder, objectHandler);
        }
    }

    @Override
    public void update(float delta) {}

    public void setPosition(Vector2 position){
        this.position = position;
    }
    @Override
    public boolean createObject(BodyBuilder bodyBuilder, ObjectHandler objectHandler) {
        BulletType type = BulletType.bullet_6;
        Body body = bodyBuilder.createBulletPlayer(position.x, position.y, BULLET_PIERCING, type);

        Bullet b;
        b = new Bullet(body, type, BulletType.getbullet(type), +2000);
        body.setUserData(b);
        objectHandler.add(b);
        --time;

        return true;
    }
}
