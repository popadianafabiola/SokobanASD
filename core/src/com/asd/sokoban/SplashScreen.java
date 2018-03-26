package com.asd.sokoban;

/**
 * Created by fabii on 26.03.2018.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

public class SplashScreen implements Screen {

    SokobanGame game;
    Texture background = new Texture(Gdx.files.internal("splash.png"));
    SpriteBatch batch = new SpriteBatch();
    boolean trigger = false;

    public SplashScreen (SokobanGame game){
        this.game = game;
    }
    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        if (trigger){
            //do nothing yet
        }else{
            Timer.schedule(new Timer.Task() {
                               public void run() {
                                   game.switchToFirstScreen();
                               }
                           }
                    , 2.5f); // 2.5 second pause to admire our logo
            trigger=true;
        }
    }
    @Override
    public void resize(int width, int height) {
// TODO Auto-generated method stub
    }
    @Override
    public void show() {
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
        batch.dispose();
        background.dispose();
    }
}
