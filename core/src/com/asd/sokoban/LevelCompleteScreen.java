package com.asd.sokoban;

/**
 * Created by fabii on 23.04.2018.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import javax.xml.soap.Text;


public class LevelCompleteScreen implements Screen, InputProcessor {

    private SokobanGame game;
    private BitmapFont font;
    private SpriteBatch batch;
    private Rectangle normal, hardcore, pointer;
    private Texture background;
    private Texture square;
    private int level;

    //resize buttons according to screen resolution
    private final float BUTTON_SIZE = Gdx.graphics.getWidth() / 2;
    private final float BUTTON_PADDING = 10;

    //Scene 2d related stuff
    private Skin skin;
    private Stage stage;
    private TextureAtlas buttonAtlas;
    //actors
    private Label label;
    private Table table;
    //styles
    private TextButton.TextButtonStyle buttonStyle;
    private Label.LabelStyle labelStyle;

    //preferences
    Preferences preferences = Gdx.app.getPreferences("SokoPreferences");

    public LevelCompleteScreen(SokobanGame game, int level) {
        this.game = game;
        this.level = level;

        font = new BitmapFont(Gdx.files.internal("HVD_Comic_Serif_Pro-32.fnt"), false);

        background = new Texture(Gdx.files.internal("grass_cartoon.jpg"));
        batch = new SpriteBatch();
    }

    @Override
    public void show() {

        //scene 2d ui
        stage = new Stage();
        //set input proccessor
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();
        table = new Table(); //table layout
        table.setFillParent(true);



        //texture atlas
        buttonAtlas = new TextureAtlas("Soko_buttons");
        skin.addRegions(buttonAtlas);


        //styles
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable("button");
        buttonStyle.down = skin.getDrawable("buttonreversed");
        buttonStyle.font = font;

        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        //add the buttons
        TextButton levelButton = new TextButton("Select Level", buttonStyle);
        levelButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                game.switchToFirstScreen();
                return true;
            }
        });

        TextButton nextButton = new TextButton("Next Level", buttonStyle);
        nextButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                game.switchToGameScreen(++level); //goes to the next level
                return true;
            }
        });
        //add the label
        Label label = new Label("Level " + String.valueOf(level) + " completed!", labelStyle);

        //set the new max level
        int maxLevel = preferences.getInteger("maxLevel", 0);
        if(level >= maxLevel) {
            preferences.putInteger("maxLevel", level+1);
        } else if(maxLevel == 0) {
            preferences.putInteger("maxLevel", 2);
        }

        preferences.flush();




        //add the actors to the table
        table.add(label).row();
        table.add(nextButton).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(10).row();
        table.add(levelButton).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(10).row();

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
