package com.swar.game.Models;

import com.swar.game.entities.Player;
import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.ObjectHandler;

/**
 * Created by Koma on 09.10.2017.
 */
public class AbilityShield implements  Ability  {
    float time = 5f;


    @Override
    public void use(BodyBuilder bodyBuilder, ObjectHandler objectHandler, Player player) {
        if(time >= 0) {
            player.ship.armor = 100;
        }else
            player.ship.armor = 5;
    }

    @Override
    public void update(float delta) {
        this.time -= delta;
    }
}
