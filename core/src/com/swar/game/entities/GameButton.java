package com.swar.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.swar.game.Game;
import com.swar.game.managers.GameInput;

/**
 * Created by Koma on 25.01.2017.
 */
public class GameButton {
    private float x;
    private float y;
    private float width;
    private float height;
    private TextureRegion reg;
    Vector3 vec;
    private OrthographicCamera cam;
    private boolean clicked;
    private String text;
    private TextureRegion[] font;

    public GameButton(TextureRegion reg, float x, float y, OrthographicCamera cam) {
        this.reg = reg;
        this.x = x;
        this.y = y;
        this.cam = cam;
        this.width = (float)reg.getRegionWidth();
        this.height = (float)reg.getRegionHeight();
        this.vec = new Vector3();
        Texture tex = Game.res.getTexture("hudCredits");
        this.font = new TextureRegion[11];

        int i;
        for(i = 0; i < 6; ++i) {
            this.font[i] = new TextureRegion(tex, 32 + i * 9, 16, 9, 9);
        }

        for(i = 0; i < 5; ++i) {
            this.font[i + 6] = new TextureRegion(tex, 32 + i * 9, 25, 9, 9);
        }

    }

    public boolean isClicked() {
        return this.clicked;
    }

    public void setText(String s) {
        this.text = s;
    }

    public void update(float dt) {
        this.vec.set((float) GameInput.x, (float)GameInput.y, 0.0F);
        this.cam.unproject(this.vec);
        if(GameInput.isPressed() && this.vec.x > this.x - this.width / 2.0F && this.vec.x < this.x + this.width / 2.0F && this.vec.y > this.y - this.height / 2.0F && this.vec.y < this.y + this.height / 2.0F) {
            this.clicked = true;
        } else {
            this.clicked = false;
        }

    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(this.reg, this.x - this.width / 2.0F, this.y - this.height / 2.0F);
        if(this.text != null) {
            this.drawString(sb, this.text, this.x, this.y);
        }

        sb.end();
    }

    private void drawString(SpriteBatch sb, String s, float x, float y) {
        int len = s.length();
        float xo = (float)(len * this.font[0].getRegionWidth() / 2);
        float yo = (float)(this.font[0].getRegionHeight() / 2);

        for(int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            if(c == 47) {
                c = 10;
            } else {
                if(c < 48 || c > 57) {
                    continue;
                }

                c = (char)(c - 48);
            }

            sb.draw(this.font[c], x + (float)(i * 9) - xo, y - yo);
        }

    }
}