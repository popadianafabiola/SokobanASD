package com.asd.sokoban;

/**
 * Created by fabii on 14.04.2018.
 */



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Scaling;

import javax.xml.soap.Text;



public class LevelSelectScreen implements Screen, InputProcessor {

    private SokobanGame game;
    private BitmapFont font;
    private SpriteBatch batch;
    private Texture background;

    //Scene 2d related stuff
    private Skin skin;
    private Stage stage;
    private Label label;
    private TextureAtlas buttonAtlas;
    private TextureAtlas lockedButtonAtlas;
    private TextButtonStyle buttonStyle;
    private Label.LabelStyle labelStyle;
    private Table table;

    //Constants
    //  resize buttons according to screen resolution
    private final float BUTTON_SIZE = Gdx.graphics.getWidth()/ 5;
    private final float BUTTON_PADDING = 10;

    //Preferences to store unlocked levels
    Preferences preferences = Gdx.app.getPreferences("SokoPreferences");

    public LevelSelectScreen(SokobanGame game) {
        this.game = game;
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("HVD_Comic_Serif_Pro-32.fnt"), false);

        background = new Texture(Gdx.files.internal("grass_cartoon.jpg"));
    }

    @Override
    public void show() {
        //scene 2d ui
        stage = new Stage();
        skin = new Skin();
        table = new Table(); //table layout
        table.setFillParent(true);

        //lable style
        labelStyle = new Label.LabelStyle(font, Color.WHITE);
        labelStyle.font = font;


        label = new Label("Level Select", labelStyle);
        // label.setWidth(Gdx.graphics.getWidth() / 2);
        label.setPosition((Gdx.graphics.getWidth() / 2) - (label.getWidth() / 2), Gdx.graphics.getHeight() - label.getHeight());
        stage.addActor(label);

        Skin uiSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        //add the back button
        TextButton backButton = new TextButton("Back", uiSkin);
        float scale = (Gdx.graphics.getWidth() / 6)/backButton.getWidth();
        backButton.setPosition(0,Gdx.graphics.getHeight() - backButton.getHeight() * scale);
        backButton.setTransform(true);
        backButton.setScale(scale,scale);
        backButton.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, final int button) {

                                       game.switchToFirstScreen();
                                       return true;
                                   }
                               }
        );

        stage.addActor(backButton);

        //set input proccessor
        Gdx.input.setInputProcessor(stage);

        //texture atlas
        buttonAtlas = new TextureAtlas("planets_pack");
        lockedButtonAtlas = new TextureAtlas("locked_planets_pack");
        skin.addRegions(buttonAtlas);
        skin.addRegions(lockedButtonAtlas);

        buttonStyle = new TextButtonStyle();
        buttonStyle.up = skin.getDrawable("planet1");
        buttonStyle.down = skin.getDrawable("planet1");
        buttonStyle.font = font;

        //set the preferences
        int maxLevel = preferences.getInteger("maxLevel", 1);

        for(int i = 1; i<=maxLevel; i++) {

            final TextButton textButton = new TextButton(String.valueOf(i), buttonStyle);

            textButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    game.switchToGameScreen(Integer.parseInt(textButton.getLabel().getText().toString()));
                    return true;
                }
            });

            table.add(textButton).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(BUTTON_PADDING);

            if(i % 3 == 0) {
                table.row();
            }


        }

        for(int i = maxLevel + 1; i<=15; i++) {
            ImageButton button = new ImageButton(skin.getDrawable("planet1_locked"));
            table.add(button).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(BUTTON_PADDING);

            if(i % 3 == 0) {
                table.row();
            }
        }

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
