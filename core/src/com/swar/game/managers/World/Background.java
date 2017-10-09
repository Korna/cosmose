package com.swar.game.managers.World;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.swar.game.utils.constants.GAME_HEIGHT;
import static com.swar.game.utils.constants.GAME_WIDTH;

/**
 * Created by Koma on 17.01.2017.
 */
public class Background {
    private TextureRegion image;
    private OrthographicCamera gameCam;
    private float scale;
    private float x;
    private float y;
    private int numDrawX;
    private int numDrawY;
    private float dx;
    private float dy;

    public Background(TextureRegion image, OrthographicCamera gameCam, float scale) {
        this.image = image;
        this.gameCam = gameCam;
        this.scale = scale;
        this.numDrawX = GAME_WIDTH / image.getRegionWidth() + 1;
        this.numDrawY = GAME_HEIGHT / image.getRegionHeight() + 1;
    }

    public void setVector(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update(float dt) {
        this.x += this.dx * this.scale * dt;
        this.y += this.dy * this.scale * dt;
    }

    public void render(SpriteBatch sb) {
        float x = (this.x + this.gameCam.viewportWidth / 2.0F - this.gameCam.position.x) * this.scale % (float)this.image.getRegionWidth();
        float y = (this.y + this.gameCam.viewportHeight / 2.0F - this.gameCam.position.y) * this.scale % (float)this.image.getRegionHeight();
        sb.begin();
        int colOffset = x > 0.0F?-1:0;
        int rowOffset = y > 0.0F?-1:0;

        for(int row = 0; row < this.numDrawY; ++row) {
            for(int col = 0; col < this.numDrawX; ++col) {
                sb.draw(this.image, x + (float)((col + colOffset) * this.image.getRegionWidth()), y + (float)((rowOffset + row) * this.image.getRegionHeight()));
            }
        }

        sb.end();
    }
}
