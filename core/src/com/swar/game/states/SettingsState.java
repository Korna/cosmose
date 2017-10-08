package com.swar.game.states;

/**
 * Created by Koma on 24.09.2017.
 */


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.swar.game.Types.State;
import com.swar.game.managers.GameConfig;
import com.swar.game.managers.GameStateManagement;
import com.swar.game.utils.constants;

/**
 * Created by Koma on 08.09.2017.
 */
public class SettingsState extends GameState {
    private Stage stage = new Stage();
    private Table table;

    int GAME_WIDTH;
    int GAME_HEIGHT;

    GameConfig gameConfig = new GameConfig();

    int SCALE = 4;
    public SettingsState(GameStateManagement gsm) {
        super(gsm);
        this.GAME_WIDTH = constants.GAME_WIDTH * SCALE;
        this.GAME_HEIGHT = constants.GAME_HEIGHT * SCALE;


        buildTable();
    }

    private void buildTable(){

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


        TextButton buttonExit = new TextButton("BACK", textButtonStyle);
        TextButton buttonVButtons = new TextButton("VIRTUAL BUTTONS:" + gameConfig.isvButtons(), textButtonStyle);
        TextButton buttonVibration = new TextButton("VIBRATION:" + gameConfig.isVibraion(), textButtonStyle);


        buttonExit.pad(GAME_WIDTH/24);//отступ

        Label.LabelStyle headingStyle = new Label.LabelStyle(white, Color.WHITE);

        Label heading = new Label("Settings", headingStyle);
        heading.setFontScale(GAME_WIDTH/160);

        table.add(heading).pad(GAME_WIDTH/4);
        table.row();
        table.add(buttonVButtons).width(GAME_WIDTH/4).height(GAME_WIDTH/7).pad(GAME_WIDTH/35);


        table.row();
        table.add(buttonVibration).width(GAME_WIDTH/4).height(GAME_WIDTH/7);

        table.row();
        table.add(buttonExit).width(GAME_WIDTH/4).height(GAME_WIDTH/7);


        stage.addActor(table);

        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.dispose();
                gsm.setState(State.MAINMENU);
            }
        });

        buttonVButtons.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                gameConfig.setvButtons(!gameConfig.isvButtons());
                clearStage();
                buildTable();
            }
        });
        buttonVibration.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                gameConfig.setVibraion(!gameConfig.isVibraion());
                clearStage();
                buildTable();
            }
        });

    }
    private void clearStage(){
        Array<Actor> list = stage.getActors();
        list.get(0).remove();
    }


    public void update(float dt) {
        stage.act(dt);
    }

    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.batch.setProjectionMatrix(this.maincamera.combined);

        stage.draw();

        BitmapFont font;

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        float x = GAME_WIDTH/(SCALE*2) - GAME_WIDTH/(SCALE*2);
        float y = GAME_HEIGHT/(SCALE*2);



    }

    public void dispose() {

    }





}