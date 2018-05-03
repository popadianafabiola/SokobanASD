package com.asd.sokoban;

/**
 * Created by fabii on 23.04.2018.
 */



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class WorldRenderer {

    public static final float CAMERA_WIDTH = 9f;
    public static final float CAMERA_HEIGHT = 16f;

    private World world;
    private OrthographicCamera camera;
    private boolean debug = false;

    SpriteBatch batch;
    Texture boxTexture;
    Texture blockTexture;
    Texture playerTexture;
    Texture targetTexture;
    Texture backgroundTexture;
    Texture greenBoxTexture;

    TextureAtlas atlas;
    TextureRegion region;

    Preferences preferences = Gdx.app.getPreferences("SokoPreferences");

    public WorldRenderer(World world, boolean debug) {
        this.world = world;
        this.camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.camera.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.camera.update();
        this.debug = debug;
        atlas = new TextureAtlas(Gdx.files.internal("heroes"));
        region = atlas.findRegion(String.valueOf(preferences.getInteger("skin", 0)));

        //Load textures and stuff
        loadTextures();
        batch = new SpriteBatch();

    }

    private void loadTextures() {
        boxTexture = new Texture(Gdx.files.internal("crate.png"));
        blockTexture = new Texture(Gdx.files.internal("rock.jpg"));
        targetTexture = new Texture(Gdx.files.internal("target.png"));
        backgroundTexture = new Texture(Gdx.files.internal("grass_cartoon.jpg"));
        greenBoxTexture = new Texture(Gdx.files.internal("crate_green.png"));
        //playerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);

    }

    public void render() {


        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        //draw background
        batch.draw(backgroundTexture, 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);


        //draw margins
        for (Block block : world.getBlocks()) {
            Rectangle rect = block.getBounds();
            float x1 = block.getPosition().x + rect.x;
            float y1 = block.getPosition().y + rect.y;
            batch.draw(blockTexture, x1, y1, rect.width, rect.height);
        }

        //draw targets
        for(Target target : world.getTargets()) {
            Rectangle rect = target.getBounds();
            float x1 = target.getPosition().x + rect.x;
            float y1 = target.getPosition().y + rect.y;

            batch.draw(targetTexture,x1 ,y1, rect.width, rect.height);
        }


        //draw boxes
        for (Box box : world.getBoxes()) {

            Rectangle rect = box.getBounds();
            float x1 = box.getPosition().x + rect.x;
            float y1 = box.getPosition().y + rect.y;
            //if is on a target, then turn green
            boolean found = false;
            for(Target target : world.getTargets()) {
                if(box.getPosition().x == target.getPosition().x && box.getPosition().y == target.getPosition().y) {
                    found = true;
                    break;
                }
            }

            if(found) {
                batch.draw(greenBoxTexture, x1, y1, rect.width, rect.height);

            } else {
                batch.draw(boxTexture, x1, y1, rect.width, rect.height);
            }

        }


        //draw player
        Player player = world.getPlayer();
        Rectangle rect = player.getBounds();
        float x1 = player.getPosition().x + rect.x;
        float y1 = player.getPosition().y + rect.y;
        batch.draw(region, x1, y1, rect.width, rect.height);
        //debugRenderer.setColor(new Color(1, 1, 0, 1));
        // debugRenderer.rect(x1, y1, rect.width, rect.height);
        // debugRenderer.end();


        batch.end();

    }




}