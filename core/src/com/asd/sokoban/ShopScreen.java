package com.asd.sokoban;

/**
 * Created by fabii on 03.04.2018.
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;


public class ShopScreen implements Screen, InputProcessor {

    private SokobanGame game;
    private BitmapFont font;
    private SpriteBatch batch;
    private Texture background;

    private TextureAtlas atlas;
    private Skin skin;


    //Scene 2d related stuff
    private Stage stage;
    private Label label;
    private Table table;
    private Button logoButton;
    private ImageButton playButton;
    private ImageButton shopButton;

    //Constants
    //TODO: resize buttons according to screen resolution
    private final float BUTTON_SIZE = Gdx.graphics.getWidth()/ 5;
    private final float BUTTON_PADDING = 10;

    Preferences preferences = Gdx.app.getPreferences("SokoPreferences");



    public ShopScreen(SokobanGame game) {
        this.game = game;
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("sokofont.fnt"), false);


        background = new Texture(Gdx.files.internal("grass_cartoon.jpg"));
        //logoTexture = new Texture(Gdx.files.internal("sokotext.png"));

    }

    @Override
    public void show() {
        //scene 2d ui
        stage = new Stage();
        skin = new Skin();
        table = new Table(); //table layout
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);
        //display the label
        final Skin uiSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Label label0 = new Label("Select skin", uiSkin);

        label0.setPosition((Gdx.graphics.getWidth() / 2) - (label0.getWidth() / 2), Gdx.graphics.getHeight() - label0.getHeight());
        stage.addActor(label0);

        //add the back button
        TextButton backButton = new TextButton("Back", uiSkin);
        // changed size - button is larget
        backButton.setHeight(BUTTON_SIZE);
        backButton.setWidth(BUTTON_SIZE);
        backButton.setPosition(0,Gdx.graphics.getHeight() - backButton.getHeight());
        backButton.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, final int button) {

                                       game.switchToFirstScreen();
                                       return true;
                                   }
                               }
        );
        stage.addActor(backButton);

        //load the heroes atlas
        atlas = new TextureAtlas(Gdx.files.internal("heroes"));
        skin.addRegions(atlas);

        for(int i=0; i<9;i++) {
            final ImageButton imageButton = new ImageButton(skin.getDrawable(String.valueOf(i)));
            imageButton.setName(String.valueOf(i));

            imageButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, final int button) {

                    System.out.println(imageButton.getName());
                    new Dialog("", uiSkin) {

                        {
                            if(Integer.parseInt(imageButton.getName()) <= preferences.getInteger("maxLevel", 0) ) {
                                preferences.putInteger("skin", Integer.parseInt(imageButton.getName()));
                                preferences.flush();
                                text("Success!");
                                button("Ok");
                            } else {
                                text("Level " + (Integer.parseInt(imageButton.getName()) + 3) + " required!");
                                button("Ok");
                            }

                        }

                        @Override
                        protected void result(final Object object) {

                        }

                    }.show(stage);
                    return true;
                }
            });

            table.add(imageButton).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(BUTTON_PADDING);

            if((i + 1) % 3 == 0)
                table.row();
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