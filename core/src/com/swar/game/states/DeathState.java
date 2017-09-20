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
import com.swar.game.Singleton;
import com.swar.game.entities.RecordModel;
import com.swar.game.managers.GameStateManagement;
import com.swar.game.utils.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Koma on 08.09.2017.
 */
public class DeathState extends GameState {

    private Stage stage = new Stage();
    private Table table;

    int GAME_WIDTH;
    int GAME_HEIGHT;

    public DeathState(GameStateManagement gsm) {
        super(gsm);

        this.GAME_WIDTH = constants.GAME_WIDTH *2;
        this.GAME_HEIGHT = constants.GAME_HEIGHT*2;


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


        buttonExit.pad(GAME_WIDTH/24);//отступ

        Label.LabelStyle headingStyle = new Label.LabelStyle(white, Color.FIREBRICK);

        Label heading = new Label("Defeat", headingStyle);
        heading.setFontScale(GAME_WIDTH/160);

        table.add(heading).pad(GAME_WIDTH/4);
        table.row();
        table.add(buttonPlay).width(GAME_WIDTH/4).height(GAME_WIDTH/7).pad(GAME_WIDTH/35);
        table.row();
        table.add(buttonExit).width(GAME_WIDTH/4).height(GAME_WIDTH/7);

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
        gsm.setState(GameStateManagement.State.MAINMENU);
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

        batch.begin();
        float x = GAME_WIDTH/4 - GAME_WIDTH/10;
        float y = GAME_HEIGHT/4;
        Singleton instance = Singleton.getInstance();
        ArrayList<RecordModel> list = instance.recordModels;

        Collections.sort(list, new MyComparator());

        for(int i = 0; i < 5 && i < list.size(); ++i){
            font.draw(batch, (i+1) + ") " + list.get(i).toString(), x, y - (20*i));
        }
        

        batch.end();



    }

    public void dispose() {

    }


    class MyComparator implements Comparator<RecordModel> {

        @Override
        public int compare(RecordModel o1, RecordModel o2) {
            return Float.compare(o1.getScore() + o1.getTime(), o2.getScore() + o2.getTime());
        }

    }


}