package com.swar.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.swar.game.Game;
import com.swar.game.managers.GameStateManagement;

import static com.swar.game.utils.constants.GAME_HEIGHT;
import static com.swar.game.utils.constants.GAME_WIDTH;

/**
 * Created by Koma on 17.01.2017.
 */
public class MenuState extends GameState {

    private Stage stage = new Stage();
    private Table table;
    private TextureRegion reg;


    public MenuState(GameStateManagement gsm) {
        super(gsm);
        this.reg = new TextureRegion(Game.res.getTexture("background_menu"), 0, 0, GAME_WIDTH/2, GAME_HEIGHT/3);


        Gdx.input.setInputProcessor(stage);
        TextureAtlas mainMenuAtlas = new TextureAtlas("ui/ui.pack");//если есть менеджер ассетов - загрузите ваш атлас с изображениями
        Skin skin = new Skin(mainMenuAtlas);
        //creating font
        BitmapFont white = new BitmapFont(Gdx.files.internal("fonts/white16.fnt"));

        table = new Table(skin);
        table.setBounds(0,0, GAME_WIDTH, GAME_HEIGHT);


        // creating buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button.normal.up");
        textButtonStyle.down = skin.getDrawable("button.normal.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = white;

        TextButton buttonExit = new TextButton("EXIT", textButtonStyle);
        TextButton buttonPlay = new TextButton("PLAY", textButtonStyle);
        buttonExit.pad(GAME_WIDTH/32);//отступ

        Label.LabelStyle headingStyle = new Label.LabelStyle(white, Color.WHITE);

        Label heading = new Label("Swar", headingStyle);
        heading.setFontScale(GAME_WIDTH/160);
       // putting stuff together

        table.add(heading).pad(GAME_WIDTH/6);
        table.row();
        table.add(buttonPlay).width(GAME_WIDTH/6).height(GAME_WIDTH/11).pad(GAME_WIDTH/50);
        table.row();
        table.add(buttonExit).width(GAME_WIDTH/6).height(GAME_WIDTH/11);
        table.debug();//показывает линии
        stage.addActor(table);

        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.exit();
            }
        });

        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setPlay();
            }
        });
    }

    private void setPlay(){
        gsm.setState(GameStateManagement.State.HUB);
    }


    public void update(float dt) {
        stage.act(dt);
    }

    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        this.batch.setProjectionMatrix(this.maincamera.combined);
        this.batch.begin();
        this.batch.draw(this.reg, 0.0F, 0.0F);
        this.batch.end();

        stage.draw();
    }

    public void dispose() {

    }

}