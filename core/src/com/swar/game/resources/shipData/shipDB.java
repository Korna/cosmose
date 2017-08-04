package com.swar.game.resources.shipData;

import com.swar.game.entities.Ship;
import com.swar.game.entities.Weapon;
import com.swar.game.Game;

/**
 * Created by Koma on 31.01.2017.
 */
public class shipDB { // в статик передалть

    private Ship shipList[] = new Ship[2];

    public shipDB(){
        float weaponPosX[] = {0};
        float weaponPosY[] = {+10};
        float weaponPosX2[] = {-10, +10};
        float weaponPosY2[] = {-10, -10};

        Weapon weaponList[] = {Game.gameWeapons.getWeapon(1)};

        Weapon weaponList2[] = {Game.gameWeapons.getWeapon(2)};

        shipList[0] = new Ship(250, 100, 5, "ship_1", "ship_1", weaponPosX, weaponPosY, 1, 1, weaponList, 30, 50);

        shipList[1] = new Ship(350, 100, 0, "ship_2", "ship_2", weaponPosX, weaponPosY, 1, 1, weaponList, 20, 30);

    }


    public Ship getShip(int index){return shipList[index];}
}
