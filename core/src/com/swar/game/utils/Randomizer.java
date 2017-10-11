package com.swar.game.utils;

import com.swar.game.Types.BonusType;

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

    public boolean chanceAsteroid(float time){
        float cfg = time /1000;

        if(random.nextFloat() < (0.05+cfg)){
            return true;
        }
            return false;
    }


    public int getCoordinateAsteroid(){
        return random.nextInt(GAME_WIDTH + 10) - 10;
    }

    public boolean chanceBonusOld(){
        if(random.nextFloat() < 0.05)
            return true;
        else
            return false;
    }


    public BonusType chanceBonusAsteroid(){
        float chance = random.nextFloat();
        if(chance < 0.05)
            return BonusType.bonus_1;
        else
            if(chance > 0.95)
                return BonusType.bonus_2;
        return null;
    }

    public BonusType chanceBonusEnemy(){
        float chance = random.nextFloat();
        if(chance < 0.5f)
            return BonusType.bonus_1;
        else
            return BonusType.bonus_2;

    }
    public static int getAsteroidTexture(){
        return random.nextInt(3) + 1;
    }



}
