package com.swar.game;

import com.swar.game.managers.InterfaceManagerAndroid;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Koma on 01.11.2017.
 */
public class InterfaceManagerTest {
    @Test
    public void calculateLess() throws Exception {
        float playerZone = 2.0f;
        float playerHandle = 0f;
        float playerNear = 0f;
        InterfaceManagerAndroid interfaceManager = new InterfaceManagerAndroid(playerZone, playerHandle, playerNear);
        boolean result = interfaceManager.calculateZone(playerZone, playerHandle, 1.9f);
        assertEquals(result, true);
    }

    @Test
    public void calculateEqual() throws Exception {
        float playerZone = 2.0f;
        float playerHandle = 0f;
        float playerNear = 0f;
        InterfaceManagerAndroid interfaceManager = new InterfaceManagerAndroid(playerZone, playerHandle, playerNear);
        boolean result = interfaceManager.calculateZone(playerZone, playerHandle, 2.0f);
        assertEquals(result, false);
    }

    @Test
    public void calculateMore(){
        float playerZone = 1f;
        float playerHandle = 1f;
        float playerNear = 1f;
        InterfaceManagerAndroid interfaceManager = new InterfaceManagerAndroid(playerZone, playerHandle, playerNear);
        boolean result = interfaceManager.calculateZone(playerZone, playerHandle, 2.1f);
        assertEquals(result, false);
    }

}