package com.swar.game.Types;

import com.swar.game.Models.*;

import java.util.ArrayList;

/**
 * Created by Koma on 20.09.2017.
 */
public enum ShipType {
    ship_1, ship_2, ship_3, ship_4;

    public static Ship getShip(ShipType shipType){
        Ship shipModel = null;
        switch(shipType){
            case ship_1:
                shipModel = new Ship(125.0f, 100, 0,
                        "ship_1", "", new float[]{1, 2}, new float[]{1, 2},
                        1, 1, new ArrayList<Weapon>(), 32, 32, new AbilityStop() {

                });
                break;
            case ship_2:
                shipModel = new Ship(150.0f, 100, 1,
                        "ship_2", "", new float[]{1,2}, new float[]{1,2},
                        1, 1, new ArrayList<Weapon>(), 32, 32, new AbilityJugg());
                break;
            case ship_3:
                shipModel = new Ship(200.0f, 100, 3,
                        "ship_3", "", new float[]{1,2}, new float[]{1,2},
                        1, 2, new ArrayList<Weapon>(), 32, 32, new AbilityExplode());
                break;
            case ship_4:
                shipModel = new Ship(75.0f, 100, 5,
                        "ship_4", "", new float[]{1,2}, new float[]{1,2},
                        2, 2, new ArrayList<Weapon>(), 32, 32, new AbilityShield());
                break;

        }

        return shipModel;
    }

}
