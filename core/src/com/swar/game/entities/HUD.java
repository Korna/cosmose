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
    private BitmapFont font, fontTime;
    public HUD(Player player) {

        this.player = player;
        credit = Game.res.getTexture("hudCredits");

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        fontTime = new BitmapFont();
        fontTime.setColor(Color.FIREBRICK);

      //  Texture tex = Game.res.getTexture("hud");

     //   hearts = new TextureRegion[3];
     //   for(int i = 0; i < hearts.length; i++){
     //       hearts[i] = new TextureRegion(tex, 32 + (i * BLOCK_SIZE), 0, BLOCK_SIZE, BLOCK_SIZE);

     //   }
    }

    public void render(SpriteBatch sb){

        sb.begin();
        sb.draw(credit, GAME_WIDTH / 6.5f, GAME_HEIGHT - (GAME_HEIGHT  / 32));

       // sb.draw(hull, Gdx.graphics.getWidth() / 16, Gdx.graphics.getHeight() / 2 - (Gdx.graphics.getHeight() / 32));
        font.draw(sb, String.valueOf(player.ship.getHp()) + "%", GAME_WIDTH / 16, GAME_HEIGHT - (GAME_HEIGHT  / 32));
        font.draw(sb, String.valueOf(player.getCredits()) + "$", GAME_WIDTH / 6, GAME_HEIGHT - (GAME_HEIGHT  / 32));

        fontTime.draw(sb, String.valueOf(((int) player.timeInGame) + " / 10"), GAME_WIDTH / 4, GAME_HEIGHT - (GAME_HEIGHT  / 32));
        /*
        if(player.getPlayerhealth() == 2){
            sb.draw(hearts[0], 40, 200);//full hp
        }
        if(player.getPlayerhealth() == 1){
            sb.draw(hearts[1], 40, 200);//half hp
        }
        if(player.getPlayerhealth() == 0){
            sb.draw(hearts[2], 40, 200);//no hp
        }
*/

        sb.end();

    }

}