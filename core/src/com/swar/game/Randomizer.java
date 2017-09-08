package com.swar.game;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.swar.game.utils.constants.GAME_WIDTH;

/**
 * Created by Koma on 03.09.2017.
 */
public class Randomizer {

    public boolean chanceAsteroid(){
        if(random.nextInt(16) == 1)
            return true;
        else
            return false;
    }

    public int getCoordinateAsteroid(){
        return random.nextInt(GAME_WIDTH + 10) - 10;
    }

    public boolean chanceBonus(){
        if(random.nextInt(10) == 1)
            return true;
        else
            return false;
    }

    public static int getAsteroidTexture(){
        return random.nextInt(3) + 1;
    }



}
