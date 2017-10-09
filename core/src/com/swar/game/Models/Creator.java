package com.swar.game.Models;

import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.ObjectHandler;

/**
 * Created by Koma on 07.10.2017.
 */
public interface Creator {
    public boolean createObject(BodyBuilder bodyBuilder, ObjectHandler objectHandler);
}
