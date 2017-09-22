package com.swar.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.swar.game.Game;

import static com.swar.game.utils.constants.GAME_HEIGHT;
import static com.swar.game.utils.constants.GAME_WIDTH;

/**
 * Created by Koma on 17.01.2017.
 */
public class HUD {

    private Player player;
    private TextureRegion[] hearts;
    private Texture credit, hull;
    private BitmapFont font, fontTime, fontEnergy, fontHp;
    public HUD(Player player) {

        this.player = player;
        credit = Game.res.getTexture("hudCredits");

        font = new BitmapFont();
        font.setColor(Color.GOLD);

        fontHp = new BitmapFont();
        fontHp.setColor(Color.FIREBRICK);

        fontTime = new BitmapFont();
        fontTime.setColor(Color.WHITE);

        fontEnergy = new BitmapFont();
        fontEnergy.setColor(Color.NAVY);

    }

    public void render(SpriteBatch sb){

        sb.begin();
      //  sb.draw(credit, GAME_WIDTH / 6.5f, GAME_HEIGHT - (GAME_HEIGHT  / 32));


        fontHp.draw(sb, String.valueOf(player.ship.getHp()) + "%", GAME_WIDTH / 16, GAME_HEIGHT - (GAME_HEIGHT  / 32));

        font.draw(sb, String.valueOf(player.getCredits()) + "$", GAME_WIDTH / 5, GAME_HEIGHT - (GAME_HEIGHT  / 32));

        fontEnergy.draw(sb, String.valueOf(player.ship.getEnergy()) + "@", GAME_WIDTH / 16, GAME_HEIGHT - (GAME_HEIGHT  / 16));

        fontTime.draw(sb, String.valueOf(((int) player.timeInGame) + " / 10"), GAME_WIDTH / 3, GAME_HEIGHT - (GAME_HEIGHT  / 32));


        sb.end();

    }

}