package com.swar.game.managers.World;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Koma on 07.10.2017.
 */
public class Builder {
    World world;

    public Builder(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
