package com.swar.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swar.game.Types.State;

import static com.swar.game.utils.constants.GAME_HEIGHT;
import static com.swar.game.utils.constants.GAME_WIDTH;

/**
 * Created by Koma on 17.01.2017.
 */
public class HUD {

    private Player player;
    private BitmapFont font, fontTime, fontEnergy, fontHp;

    private State state;
    public HUD(Player player, State state) {

        this.player = player;
        try {
            font = new BitmapFont();
            font.setColor(Color.GOLD);

            fontHp = new BitmapFont();
            fontHp.setColor(new Color(0.7f, 0, 0.1f, 1));

            fontTime = new BitmapFont();
            fontTime.setColor(Color.WHITE);

            fontEnergy = new BitmapFont();
            fontEnergy.setColor(new Color(0.1f, 0, 0.7f, 1));
        }catch(NullPointerException npe){
            System.out.println(npe.toString());
        }
        this.state = state;
    }

    public void render(SpriteBatch sb){

        sb.begin();
        switch(state){
            case PLAY_CLASSIC:
                font.draw(sb, String.valueOf(player.getCredits()) + " $", GAME_WIDTH / 3, GAME_HEIGHT - (GAME_HEIGHT  / 32));
            case PLAYSURVIVAL:
                fontHp.draw(sb, String.valueOf(player.ship.getHp()) + " HP", GAME_WIDTH / 16, GAME_HEIGHT - (GAME_HEIGHT  / 32));
                fontEnergy.draw(sb, String.valueOf((int) player.ship.getEnergy()) + " ENERGY", GAME_WIDTH / 16, GAME_HEIGHT - (GAME_HEIGHT  / 16));
                fontTime.draw(sb, String.valueOf(((int) player.timeInGame) + " SEC"), GAME_WIDTH / 2, GAME_HEIGHT - (GAME_HEIGHT  / 32));
                break;

        }
        sb.end();

    }

}