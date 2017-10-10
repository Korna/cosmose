package com.swar.game.Models;

import com.swar.game.entities.Player;
import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.ObjectHandler;

/**
 * Created by Koma on 09.10.2017.
 */
public interface Ability {
    public void use(BodyBuilder bodyBuilder, ObjectHandler objectHandler, Player player);
    public void update(float delta);
}
