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
import com.swar.game.utils.constants;

/**
 * Created by Koma on 17.01.2017.
 */
public class MenuState extends GameState {

    private Stage stage = new Stage();
    private Table table;
    private TextureRegion reg;
    int GAME_WIDTH;
    int GAME_HEIGHT;
    public MenuState(GameStateManagement gsm) {
        super(gsm);
        this.GAME_WIDTH = constants.GAME_WIDTH *2;
        this.GAME_HEIGHT = constants.GAME_HEIGHT*2;
        this.reg = new TextureRegion(Game.res.getTexture("background_menu"), 0, 0, GAME_WIDTH, GAME_HEIGHT/3);


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
       // buttonExit.getLabel().setScale(GAME_WIDTH/240);
        TextButton buttonPlay = new TextButton("PLAY", textButtonStyle);
     //   buttonPlay.getLabel().setScale(GAME_WIDTH/240);

        buttonExit.pad(GAME_WIDTH/24);//отступ

        Label.LabelStyle headingStyle = new Label.LabelStyle(white, Color.WHITE);

        Label heading = new Label("Project White", headingStyle);
        heading.setFontScale(GAME_WIDTH/120);
       // putting stuff together

        table.add(heading).pad(GAME_WIDTH/4);
        table.row();
        table.add(buttonPlay).width(GAME_WIDTH/4).height(GAME_WIDTH/7).pad(GAME_WIDTH/35);
        table.row();
        table.add(buttonExit).width(GAME_WIDTH/4).height(GAME_WIDTH/7);
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
        gsm.dispose();
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