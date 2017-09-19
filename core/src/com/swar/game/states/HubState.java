package com.swar.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.swar.game.Game;
import com.swar.game.entities.Player;
import com.swar.game.entities.Ship;
import com.swar.game.entities.Weapon;
import com.swar.game.managers.GameContactListener;
import com.swar.game.managers.GameStateManagement;
import com.swar.game.utils.constants;

import static com.swar.game.utils.constants.*;

/**
 * Created by Koma on 25.01.2017.
 */
public class HubState extends GameState {

    private Stage stage = new Stage();

    private TextureRegion reg;
    private int currentPositionShip = 1;
    private int chosenShip = 1;
    private int currentPositionWeapon = 1;
    private int chosenWeapon = 1;


    private GameContactListener cl;
    private World world;
    private Player player;
    private Body playerBody;

    private Image currentWeapon;
    private Image currentShip;

    int GAME_WIDTH;
    int GAME_HEIGHT;

    public HubState(GameStateManagement gsm) {
        super(gsm);
        this.GAME_WIDTH = constants.GAME_WIDTH *2;
        this.GAME_HEIGHT = constants.GAME_HEIGHT*2;
        Gdx.input.setInputProcessor(stage);

        cl = gsm.cl;
        world = new World(new Vector2(0, 0), false);//потому как создается игрок в хабе

        player = gsm.player;
        playerBody = gsm.playerBody;

        this.reg = new TextureRegion(Game.res.getTexture("bgs"), 0, 0, GAME_WIDTH/2, GAME_HEIGHT/2);


        currentWeapon = new Image(Game.res.getTexture("weapon_" + String.valueOf(currentPositionWeapon)));
        currentShip = new Image(Game.res.getTexture("ship_" + String.valueOf(currentPositionShip)));


        buildTable();
    }

    Skin skin = new Skin(new TextureAtlas("ui/ui.pack"));
    private void buildTable(){
        Table table;
        //creating font
        BitmapFont white = new BitmapFont(Gdx.files.internal("fonts/white16.fnt"));


        table = new Table(skin);
        table.setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);


        // creating buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button.normal.up");
        textButtonStyle.down = skin.getDrawable("button.normal.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = white;


        TextButton buttonBack = new TextButton("BACK", textButtonStyle);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setBack();
            }
        });
        buttonBack.pad(GAME_WIDTH/30);//отступ

        TextButton buttonPlay = new TextButton("PLAY", textButtonStyle);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setPlay();
            }
        });


        Button buttonPlus= new Button();
        buttonPlus.setStyle(setUpButtonStyle("button.plus"));


        Button buttonMinus = new Button();
        buttonMinus.setStyle(setUpButtonStyle("button.minus"));

        Button.ButtonStyle set = new Button.ButtonStyle();
        set.up = skin.getDrawable("button.ok.up");
        set.down = skin.getDrawable("button.ok.down");
        Button buttonSet = new Button();
        buttonSet.setStyle(set);
        buttonSet.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                chosenShip = currentPositionShip;
                chosenWeapon = currentPositionWeapon;
                System.out.printf("Ship now is%d\n", chosenShip);
                System.out.printf("Weapon now is%d\n", chosenWeapon);

            }
        });


        Button.ButtonStyle arrowLeft_ship = new Button.ButtonStyle();
        arrowLeft_ship.up = skin.getDrawable("button.left");
        Button buttonArrowLeft_ship = new Button();
        buttonArrowLeft_ship.setStyle(arrowLeft_ship);
        buttonArrowLeft_ship.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.printf("%d\n", currentPositionShip);
                --currentPositionShip;
                imageUpdate();


                clearStage();
                buildTable();
            }
        });

        Button buttonArrowRight_ship = new Button();
        buttonArrowRight_ship.setStyle(setUpButtonStyle("button.right"));
        buttonArrowRight_ship.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.printf("%d\n", currentPositionShip);

                ++currentPositionShip;
                imageUpdate();

                clearStage();
                buildTable();
            }
        });

        Button buttonArrowLeft_weapon = new Button();
        buttonArrowLeft_weapon.setStyle(setUpButtonStyle("button.left"));
        buttonArrowLeft_weapon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.printf("%d\n", currentPositionWeapon);

                --currentPositionWeapon;
                imageUpdate();

                clearStage();
                buildTable();
            }
        });

        Button buttonArrowRight_weapon = new Button();
        buttonArrowRight_weapon.setStyle(setUpButtonStyle("button.right"));
        buttonArrowRight_weapon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.printf("%d\n", currentPositionWeapon);

                ++currentPositionWeapon;
                imageUpdate();

                clearStage();
                buildTable();
            }
        });

        TextButton.TextButtonStyle textBoardStyle = new TextButton.TextButtonStyle();
        Button board = new Button(textBoardStyle);
        textBoardStyle.up = skin.getDrawable("board");

        Label.LabelStyle headingStyle = new Label.LabelStyle(white, Color.WHITE);
        Label heading = new Label("Hangar", headingStyle);
        heading.setFontScale(4);


        final int BUTTON_HEIGHT = GAME_WIDTH/10;
        final int BUTTON_WIDTH = GAME_WIDTH/10;

        // putting stuff together
        //hangar text
        table.add(heading).pad(GAME_WIDTH/5).padTop(GAME_WIDTH/70);
        table.row().height(GAME_WIDTH/5);

        //minus
        table.add(buttonMinus).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
        //plus
        table.add(buttonPlus).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);


        table.row().height(GAME_WIDTH/12);

        table.add(buttonArrowLeft_weapon).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
        table.add(currentWeapon).fill();
        table.add(buttonArrowRight_weapon).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);

        table.row().height(GAME_WIDTH/12);

        table.add(buttonArrowLeft_ship).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
        table.add(currentShip).fill();
        table.add(buttonArrowRight_ship).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);

        table.row().height(GAME_WIDTH/12);

        table.add(buttonSet).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).center().right();

        table.row().height(GAME_WIDTH/12);

        table.add(buttonBack).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
        table.add(buttonPlay).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);

        table.debug();//показывает линии

        stage.addActor(table);





    }


    private Button.ButtonStyle setUpButtonStyle(String name){
        Button.ButtonStyle plus = new Button.ButtonStyle();
        plus.up = skin.getDrawable(name);
        return plus;
    }




    private void setPlay() {
        gsm.dispose();
        gsm.setState(GameStateManagement.State.PLAY);
    }

    private void setBack() {
        gsm.dispose();
        gsm.setState(GameStateManagement.State.MAINMENU);
    }
    public void handleInput() {
    }

    public void update(float dt) {
        this.handleInput();

        stage.act(dt);

    }

    public void imageUpdate(){
        currentWeapon = new Image(Game.res.getTexture("weapon_" + String.valueOf(currentPositionWeapon)));
        currentShip = new Image(Game.res.getTexture("ship_" + String.valueOf(currentPositionShip)));

    }
    private void clearStage(){
        Array<Actor> list = stage.getActors();
        list.get(0).remove();
    }

    public void render() {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        this.batch.setProjectionMatrix(this.maincamera.combined);
        this.batch.begin();

    //    this.batch.draw(this.reg, 0.0F, 0.0F);
        this.batch.end();

        stage.draw();
    }

    public void dispose() {
        playerBody = createPlayer(GAME_WIDTH / 4, 15, GAME_WIDTH/30, GAME_WIDTH/20);

        player = new Player(playerBody, cl, chosenShip,
                new Ship(200.0f, 100, 5, "", "", new float[]{1,2}, new float[]{1,2}, 1, 2, new Weapon[]{null}, 1, 2),
                chosenWeapon);//здесь по индексу передаём корабль из ДБ
        player.initSprite(playerBody);

        gsm.cl = cl;
        gsm.world = world;
        gsm.player = player;
        gsm.playerBody = playerBody;


    }

    private Body createPlayer(int x, int y, int width, int height){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
        def.fixedRotation = true;


        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_ENEMY | BIT_OBJECT | BIT_BORDER;


        Body pBody = world.createBody(def);


        pBody.createFixture(fdef).setUserData(PLAYER_SHIP);

        shape.dispose();

        return pBody;
    }

}