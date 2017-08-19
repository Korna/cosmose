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
import com.swar.game.entities.Player;
import com.swar.game.managers.GameContactListener;
import com.swar.game.managers.GameStateManagement;

import static com.swar.game.utils.constants.*;

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

        this.reg = new TextureRegion(Game.res.getTexture("bgs"), 0, 0, GAME_WIDTH/2, GAME_HEIGHT/2);


        currentWeapon = new Image(Game.res.getTexture("weapon_" + String.valueOf(currentPositionWeapon)));
        currentShip = new Image(Game.res.getTexture("ship_" + String.valueOf(currentPositionShip)));

        //creating font
        BitmapFont white = new BitmapFont(Gdx.files.internal("fonts/white16.fnt"));

        TextureAtlas mainMenuAtlas = new TextureAtlas("ui/ui.pack");
        Skin skin = new Skin(mainMenuAtlas);
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
        buttonBack.pad(GAME_WIDTH/30);//отступ
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
        //hangar text
        table.add(heading).pad(GAME_WIDTH/7).padTop(GAME_WIDTH/70);
        table.row().height(GAME_WIDTH/7);

        //minus
        table.add(buttonMinus).width(GAME_WIDTH/14).height(GAME_WIDTH/15);
        //plus
        table.add(buttonPlus).width(GAME_WIDTH/14).height(GAME_WIDTH/15);


        table.row().height(GAME_WIDTH/15);

        table.add(buttonArrowLeft_weapon).width(GAME_WIDTH/14).height(GAME_WIDTH/15);
        table.add(currentWeapon).fill();
        table.add(buttonArrowRight_weapon).width(GAME_WIDTH/14).height(GAME_WIDTH/15);

        table.row().height(GAME_WIDTH/15);

        table.add(buttonArrowLeft_ship).width(GAME_WIDTH/14).height(GAME_WIDTH/15);
        table.add(currentShip).fill();
        table.add(buttonArrowRight_ship).width(GAME_WIDTH/14).height(GAME_WIDTH/15);

        table.row().height(GAME_WIDTH/15);

        table.add(buttonSet).width(GAME_WIDTH/14).height(GAME_WIDTH/15).center().right();

        table.row().height(GAME_WIDTH/15);

        table.add(buttonBack).height(GAME_WIDTH/15);
        table.add(buttonPlay).height(GAME_WIDTH/15);

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


        //WEAPON
        buttonArrowRight_weapon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.printf("%d\n", currentPositionWeapon);
                currentPositionWeapon++;
                imageUpdate();
            }
        });
        buttonArrowLeft_weapon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.printf("%d\n", currentPositionWeapon);
                currentPositionWeapon--;
                imageUpdate();

            }
        });

        //SHIP
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
                System.out.printf("%d\n", currentPositionShip);
                currentPositionShip--;
                imageUpdate();

            }
        });

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
        playerBody = createPlayer(GAME_WIDTH / 4, 15, GAME_WIDTH/20, GAME_WIDTH/15);

        player = new Player(playerBody, cl, chosenShip, null, chosenWeapon);//здесь по индексу передаём корабль из ДБ
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
        shape.setAsBox(width
                //  / PPM
                , height
                //  / PPM
        );

        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_ENEMY | BIT_OBJECT | BIT_BORDER;


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