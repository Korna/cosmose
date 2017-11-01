package com.swar.game;

import com.swar.game.managers.GameConfig;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Koma on 01.11.2017.
 */
public class GameConfigTest {


    @Test
    public void isVibraion() throws Exception {
        GameConfig gameConfig = new GameConfig(true,true, 1f);
        boolean v = gameConfig.isVibraion();
        assertEquals(v, true);
    }

    @Test
    public void setVibraion() throws Exception {
        GameConfig gameConfig = new GameConfig(true,true, 1f);
        gameConfig.setVibraion(false);
        boolean v = gameConfig.isVibraion();
        assertEquals(v, false);
    }

    @Test
    public void isvButtons() throws Exception {
        GameConfig gameConfig = new GameConfig(true,true, 1f);
        boolean v = gameConfig.isvButtons();
        assertEquals(v, true);
    }

    @Test
    public void setvButtons() throws Exception {
        GameConfig gameConfig = new GameConfig(true,true, 1f);
        gameConfig.setvButtons(false);
        boolean v = gameConfig.isvButtons();
        assertEquals(v, false);
    }

    @Test
    public void getPosY() throws Exception {
        GameConfig gameConfig = new GameConfig(true,true, 1f);
        float pos = gameConfig.getPosY();
        assertEquals(1f, pos);
    }

    @Test
    public void setPosY() throws Exception {
        GameConfig gameConfig = new GameConfig(true,true, 1f);
        gameConfig.setPosY(-2f);
        float pos = gameConfig.getPosY();
        assertEquals(-2f, pos);
    }

}