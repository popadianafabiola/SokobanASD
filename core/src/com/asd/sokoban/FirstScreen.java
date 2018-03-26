package com.asd.sokoban;

/**
 * Created by fabii on 26.03.2018.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;


public class FirstScreen implements Screen, InputProcessor {

    private SokobanGame game;
    private BitmapFont font;
    private SpriteBatch batch;
    private Texture background;
    private Texture logoTexture;


    //Scene 2d related stuff
    private Skin skin;
    private Stage stage;
    private Label label;
    private Table table;
    private Button logoButton;
    private ImageButton playButton;
    private ImageButton shopButton;

    //Constants
    private final float BUTTON_SIZE = Gdx.graphics.getWidth()/ 5;
    private final float BUTTON_PADDING = 10;

    public FirstScreen(SokobanGame game) {
        this.game = game;
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("sokofont.fnt"), false);

        background = new Texture(Gdx.files.internal("jungle.jpg"));
        logoTexture = new Texture(Gdx.files.internal("sokotext.png"));

    }

    @Override
    public void show() {
        //scene 2d ui
        stage = new Stage();
        skin = new Skin();
        table = new Table(); //table layout
        table.setFillParent(true);

        skin.add("sokobutton", new Texture(Gdx.files.internal("sokotext.png")), Texture.class);

        //image button
        logoButton = new Button(skin.getDrawable("sokobutton"));
        logoButton.setPosition(Gdx.graphics.getWidth() / 2 - logoButton.getWidth() / 2, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 4));
        stage.addActor(logoButton);

        //buttons
        skin.add("playbutton", new Texture(Gdx.files.internal("play.png")), Texture.class);
        skin.add("shopbutton", new Texture(Gdx.files.internal("shop.png")), Texture.class);

        playButton = new ImageButton(skin.getDrawable("playbutton"));

        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                //game.switchToMainMenuScreen();
                return true;
            }
        });

        table.add(playButton).pad(BUTTON_SIZE / 2).row();

        shopButton = new ImageButton(skin.getDrawable("shopbutton"));

        shopButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                //game.switchToShopScreen();
                return true;
            }
        });


        table.add(shopButton);

        //set input proccessor
        Gdx.input.setInputProcessor(stage);


        stage.addActor(table);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0 , 0 , Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
// TODO Auto-generated method stub
    }
    @Override
    public void hide() {
// TODO Auto-generated method stub
    }
    @Override
    public void pause() {
// TODO Auto-generated method stub
    }
    @Override
    public void resume() {
// TODO Auto-generated method stub
    }
    @Override
    public void dispose() {
//game.dispose();
        // background.dispose();
        //  shape.dispose();
        //  batch.dispose();
        //   font.dispose();
// TODO Auto-generated method stub
    }

    @Override
    public boolean keyDown(int keycode) {
// TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
// TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
// TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
// TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
// TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
// TODO Auto-generated method stub
        return false;
    }
}