package com.swar.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.swar.game.Types.State;
import com.swar.game.managers.GameStateManagement;
import com.swar.game.utils.constants;

import static com.swar.game.utils.constants.GAME_NAME;

/**
 * Created by Koma on 14.10.2017.
 */
public class ShopState extends GameState {

    private Stage stage = new Stage();
    private Table table;

    int GAME_WIDTH;
    int GAME_HEIGHT;
    int SCALE = 4;

    float buttonScale = 1.5f;
    public ShopState(GameStateManagement gsm) {
        super(gsm);
        this.GAME_WIDTH = constants.GAME_WIDTH * SCALE;
        this.GAME_HEIGHT = constants.GAME_HEIGHT * SCALE;



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
        buttonExit.setTransform(true);
        buttonExit.setScale(buttonScale);
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.exit();
            }
        });

        TextButton buttonPlay = new TextButton("Classic", textButtonStyle);
        buttonPlay.setTransform(true);
        buttonPlay.setScale(buttonScale);
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setPlay();
            }
        });

        TextButton buttonPlaySurvival = new TextButton("Survival", textButtonStyle);
        buttonPlaySurvival.setTransform(true);
        buttonPlaySurvival.setScale(buttonScale);
        buttonPlaySurvival.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setPlaySurvival();
            }
        });

        TextButton buttonSettings = new TextButton("Settings", textButtonStyle);
        buttonSettings.setTransform(true);
        buttonSettings.setScale(buttonScale);
        buttonSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setSettings();
            }
        });


        buttonExit.pad(GAME_WIDTH/24);//отступ

        Label.LabelStyle headingStyle = new Label.LabelStyle(white, Color.WHITE);

        Label heading = new Label(GAME_NAME, headingStyle);
        heading.setFontScale(GAME_WIDTH/160);

        // putting stuff together

        table.add(heading).pad(GAME_WIDTH/5);

        table.row();
        table.add(buttonPlay).width(GAME_WIDTH/5).height(GAME_WIDTH/7).pad(GAME_WIDTH/30);

        table.row();
        table.add(buttonPlaySurvival).width(GAME_WIDTH/5).height(GAME_WIDTH/7).pad(GAME_WIDTH/30);


        table.row();
        table.add(buttonSettings).width(GAME_WIDTH/5).height(GAME_WIDTH/7).pad(GAME_WIDTH/30);


        table.row();
        table.add(buttonExit).width(GAME_WIDTH/5).height(GAME_WIDTH/7);
        stage.addActor(table);


    }

    private void setUpGui(){


    }

    private void setPlay(){
        stage.dispose();
        gsm.setState(State.HUB);
    }
    private void setSettings(){
        stage.dispose();
        gsm.setState(State.SETTINGS);
    }

    private void setPlaySurvival(){
        stage.dispose();
        gsm.setState(State.PLAYSURVIVAL);
    }



    public void update(float dt) {
        stage.act(dt);
    }

    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        this.batch.setProjectionMatrix(this.maincamera.combined);


        stage.draw();
    }

    public void dispose() {

    }

}