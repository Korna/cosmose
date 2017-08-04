package com.swar.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.swar.game.Game;
import com.swar.game.entities.GameButton;
import com.swar.game.entities.Player;
import com.swar.game.managers.GameContactListener;
import com.swar.game.managers.GameStateManagement;

import static com.swar.game.managers.GameStateManagement.State.PLAY;
import static com.swar.game.utils.constants.*;
import static com.uwsoft.editor.renderer.physics.PhysicsBodyLoader.SCALE;

/**
 * Created by Koma on 25.01.2017.
 */
public class HubState extends GameState {

    private Stage stage = new Stage();
    private Table table;
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

    public HubState(GameStateManagement gsm) {
        super(gsm);

        Gdx.input.setInputProcessor(stage);

        cl = gsm.cl;
        world = gsm.world;
        player = gsm.player;
        playerBody = gsm.playerBody;

        this.reg = new TextureRegion(Game.res.getTexture("bgs"), 0, 0, 320, 240);


        currentWeapon = new Image(Game.res.getTexture("weapon_" + String.valueOf(currentPositionWeapon)));
        currentShip = new Image(Game.res.getTexture("ship_" + String.valueOf(currentPositionShip)));

        //creating font
        BitmapFont white = new BitmapFont(Gdx.files.internal("data/fonts/white16.fnt"));

        TextureAtlas mainMenuAtlas = new TextureAtlas("data/ui/ui.pack");
        Skin skin = new Skin(mainMenuAtlas);
        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        // creating buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button.normal.up");
        textButtonStyle.down = skin.getDrawable("button.normal.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = white;


        TextButton buttonBack = new TextButton("BACK", textButtonStyle);
        buttonBack.pad(20);//отступ
        TextButton buttonPlay = new TextButton("PLAY", textButtonStyle);

        Button.ButtonStyle plus = new Button.ButtonStyle();
        plus.up = skin.getDrawable("button.plus");
        Button buttonPlus= new Button();
        buttonPlus.setStyle(plus);

        Button.ButtonStyle minus = new Button.ButtonStyle();
        minus.up = skin.getDrawable("button.minus");
        Button buttonMinus = new Button();
        buttonMinus.setStyle(minus);

        Button.ButtonStyle set = new Button.ButtonStyle();
        set.up = skin.getDrawable("button.ok.up");
        set.down = skin.getDrawable("button.ok.down");
        Button buttonSet = new Button();
        buttonSet.setStyle(set);

        Button.ButtonStyle arrowLeft_ship = new Button.ButtonStyle();
        arrowLeft_ship.up = skin.getDrawable("button.left");
        Button buttonArrowLeft_ship = new Button();
        buttonArrowLeft_ship.setStyle(arrowLeft_ship);

        Button.ButtonStyle arrowRight_ship = new Button.ButtonStyle();
        arrowRight_ship.up = skin.getDrawable("button.right");
        Button buttonArrowRight_ship = new Button();
        buttonArrowRight_ship.setStyle(arrowRight_ship);

        Button.ButtonStyle arrowLeft_weapon = new Button.ButtonStyle();
        arrowLeft_weapon.up = skin.getDrawable("button.left");
        Button buttonArrowLeft_weapon = new Button();
        buttonArrowLeft_weapon.setStyle(arrowLeft_weapon);

        Button.ButtonStyle arrowRight_weapon = new Button.ButtonStyle();
        arrowRight_weapon.up = skin.getDrawable("button.right");
        Button buttonArrowRight_weapon = new Button();
        buttonArrowRight_weapon.setStyle(arrowRight_weapon);

        TextButton.TextButtonStyle textBoardStyle = new TextButton.TextButtonStyle();
        Button board = new Button(textBoardStyle);
        textBoardStyle.up = skin.getDrawable("board");

        Label.LabelStyle headingStyle = new Label.LabelStyle(white, Color.WHITE);
        Label heading = new Label("Hangar", headingStyle);
        heading.setFontScale(4);


        // putting stuff together

        table.add(heading).pad(100).padTop(10);
        table.row().height(100);


        table.add(buttonMinus).width(55).height(35);
        table.add(buttonPlus).width(55).height(35);

        table.row().height(30);
        table.add(buttonArrowLeft_weapon).width(55).height(35);
        table.add(currentWeapon).fill();
        table.add(buttonArrowRight_weapon).width(55).height(35);

        table.row().height(30);
        table.add(buttonArrowLeft_ship).width(55).height(35);
        table.add(currentShip).fill();
        table.add(buttonArrowRight_ship).width(55).height(35);

        table.row().height(30);
        table.add(buttonSet).width(55).height(35).center().right();

        table.row().height(30);
        table.add(buttonBack).height(35);
        table.add(buttonPlay).height(35);

        table.debug();//показывает линии
        stage.addActor(table);

        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setBack();

            }
        });

        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setPlay();
            }
        });

        buttonArrowRight_ship.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.printf("%d\n", currentPositionShip);
                currentPositionShip++;
                imageUpdate();
            }
        });

        buttonArrowLeft_ship.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.printf("%d\n", currentPositionWeapon);
                currentPositionShip--;
                imageUpdate();

            }
        });

        buttonSet.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                chosenShip = currentPositionShip;
                System.out.printf("Now is%d\n", chosenShip);

            }
        });

    }



    private void setPlay() {
        gsm.setState(GameStateManagement.State.PLAY);
    }

    private void setBack() {
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

        currentWeapon.invalidate();
        currentShip.invalidate();

        currentWeapon.validate();
        currentShip.validate();

  //      table.reset();
        table.invalidate();
        table.validate();
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
        playerBody = createPlayer(GAME_WIDTH / 4, 10, 20, 30);

        player = new Player(playerBody, cl, chosenShip, null);//здесь по индексу передаём корабль из ДБ
        player.initSprite(playerBody);

        gsm.cl = cl;
        gsm.world = world;
        gsm.player = player;
        gsm.playerBody = playerBody;


    }

    private Body createPlayer(int x, int y, int width, int height){

        Body pBody;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2
                //  / PPM
                , height / 2
                //  / PPM
        );

        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;//тут игрок тоже является ящиком, пох, пока без лишних параметров в функцию
        fdef.filter.maskBits = BIT_ENEMY | BIT_OBJECT;


        def.position.set(x
                //    / PPM
                , y
                //      / PPM
        );
        def.fixedRotation = true;

        pBody = world.createBody(def);



        //pBody.createFixture(shape, 0.8f);

        pBody.createFixture(fdef).setUserData("player");

        shape.dispose();

        return pBody;
    }

}