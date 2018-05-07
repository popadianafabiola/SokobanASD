package com.asd.sokoban;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.*;
import java.util.List;


public class GameScreen implements Screen, InputProcessor {


    private SokobanGame game;
    private World world;
    private WorldRenderer renderer;
    private int level;
    private SinglyLinkedList<State> states;
    private BitmapFont font;



    //Scene 2d related
    private Skin skin;
    private Stage stage;
    private TextureAtlas undoButtonAtlas;
    private TextureAtlas exitButtonAtlas;
    private ImageButton.ImageButtonStyle undoButtonStyle;
    private ImageButton.ImageButtonStyle exitButtonStyle;

    private ArrayList<Box> initialBoxes;

    private Skin dialogSkin;

    InputMultiplexer multiplexer = new InputMultiplexer();

    //constructor
    public GameScreen(SokobanGame game, int level) {
        this.game = game;
        this.level = level;
        stage = new Stage();
        skin = new Skin();

    }


    @Override
    public void show() {

        world = new World(level);
        renderer = new WorldRenderer(world, true);

        dialogSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        font = new BitmapFont();

        //hold the initial player position
        states = new SinglyLinkedList<State>();
        addState(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y, -1, 0, 0);

        //hold the initial boxes position
        initialBoxes = new ArrayList<Box>();
        initBoxes();


        undoButtonAtlas = new TextureAtlas("Soko_undo_buttons");
        exitButtonAtlas = new TextureAtlas("Soko_exit_buttons");

        skin.addRegions(undoButtonAtlas);
        skin.addRegions(exitButtonAtlas);

        undoButtonStyle = new ImageButton.ImageButtonStyle();
        undoButtonStyle.up = skin.getDrawable("button");
        undoButtonStyle.down = skin.getDrawable("buttonreversed");

        exitButtonStyle = new ImageButton.ImageButtonStyle();
        exitButtonStyle.up = skin.getDrawable("exit_button");
        exitButtonStyle.down = skin.getDrawable("exit_buttonreversed");


        //add undo button to stage
        ImageButton undoButton = new ImageButton(undoButtonStyle);

        //add the exit button
        ImageButton exitButton = new ImageButton(exitButtonStyle);


        float buttonHeight = Gdx.graphics.getHeight() / WorldRenderer.CAMERA_HEIGHT;
        float buttonWidht = Gdx.graphics.getWidth() / WorldRenderer.CAMERA_WIDTH;
        //undo button position
        undoButton.setPosition(0, Gdx.graphics.getHeight() - buttonHeight);
        undoButton.setHeight(buttonHeight);
        undoButton.setWidth(buttonWidht);

        //exit button position
        exitButton.setPosition(Gdx.graphics.getWidth() - buttonWidht, Gdx.graphics.getHeight() - buttonHeight);
        exitButton.setHeight(buttonHeight);
        exitButton.setWidth(buttonWidht);

        //input listeners
        undoButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                undoMove();
                return true;
            }
        });

        exitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                new Dialog("Confirm exit", dialogSkin) {

                    {
                        text("Are you sure?");
                        button("Yes", "goodbye");
                        button("No", "glad you stay");
                    }

                    @Override
                    protected void result(final Object object) {
                        if(object.toString().equals("goodbye")) {
                            game.switchToLevelSelectScreen();
                        }
                    }

                }.show(stage);
                return true;
            }
        });

        stage.addActor(undoButton);
        stage.addActor(exitButton);




        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(multiplexer);




    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        stage.act(delta);
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        //renderer.setSize(width, height);
        //this.width = width;
        // this.height = height;
    }


    //handle input
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        //auxiliary variables
        int newX = (int) WorldRenderer.CAMERA_WIDTH * x / Gdx.graphics.getWidth();
        int newY = (int) WorldRenderer.CAMERA_HEIGHT - 1 - ((int)WorldRenderer.CAMERA_HEIGHT * y / Gdx.graphics.getHeight());

        int boxId = 0;
        int boxX = 0;
        int boxY = 0;

        boolean foundBox = false;

        if (newX == world.getPlayer().getPosition().x || newY == world.getPlayer().getPosition().y) {
            if (newX != world.getPlayer().getPosition().x) {
                if (newX > world.getPlayer().getPosition().x) {
                    //then it moves to right
                    moveRight(newX, newY);

                } else {
                    moveLeft(newX, newY);
                }

            } else {


                if (newY != world.getPlayer().getPosition().y) {
                    if (newY > world.getPlayer().getPosition().y) {
                        //then it moves up
                        moveUp(newX, newY);
                    } else {
                        //then y is smaller, so it goes down
                        moveDown(newX, newY);
                    }

                }
            }

        } else {
            //the move is not linear, so we try to find a path

            //check if is a valid place for diagonal move
            if(world.map[newX][newY] == 0) {
                //now lets see if there is a path using the lee's algorithm, which is basically breadth first search
                List<IntPair> queue = new ArrayList<IntPair>();
                queue.add(new IntPair((int)world.getPlayer().getPosition().x,(int) world.getPlayer().getPosition().y));
                boolean[][] visited = new boolean[100][100];
                int foundX = 0;
                int foundY = 0;

                //System.out.println("current position: " + world.getPlayer().getPosition().x + " " + world.getPlayer().getPosition().y);
                while(queue.isEmpty() == false) {
                    for(int i = 0; i< queue.size(); i++) {

                        int vertexX = queue.get(i).getX();
                        int vertexY = queue.get(i).getY();


                        //System.out.println(vertexX + " " + vertexY);
                        //check if its solution
                        if(vertexX == newX && vertexY == newY) {
                            //store the found values
                            foundX = vertexX;
                            foundY = vertexY;
                            //System.out.println("there is a path");


                            /*for(int k=1;k<=17;k++)
                            {
                                for(int j=1;j<=17;j++)
                                    System.out.print(world.map[k][j] + " ");
                                System.out.print("\n");
                            }
                            */

                        }
                        //check the neighbours
                        try {
                            if (world.map[vertexX][vertexY + 1] == 0)
                                if (visited[vertexX][vertexY + 1] == false)
                                    queue.add(new IntPair(vertexX, vertexY + 1));

                            if (world.map[vertexX][vertexY - 1] == 0)
                                if (visited[vertexX][vertexY - 1] == false)
                                    queue.add(new IntPair(vertexX, vertexY - 1));

                            if (world.map[vertexX + 1][vertexY] == 0)
                                if (visited[vertexX + 1][vertexY] == false)
                                    queue.add(new IntPair(vertexX + 1, vertexY));

                            if (world.map[vertexX - 1][vertexY] == 0)
                                if (visited[vertexX - 1][vertexY] == false)
                                    queue.add(new IntPair(vertexX - 1, vertexY));




                        }catch(Exception ex) {
                            //The move is out of bounds
                            foundX = 0;
                            foundY = 0;
                        }

                        //end of checking neighbours
                        visited[vertexX][vertexY] = true;
                        queue.remove(i);

                    }
                }



                if(foundX != 0) {
                    //set player's new position
                    world.getPlayer().setPosition(new Vector2(foundX, foundY));
                    addState(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y, -1,  0, 0);
                }

            }


        }
        //checks if the player has done the level
        checkWin();

        return true;
    }



    private void moveRight(int newX, int newY) {

        int boxId = 0;
        int boxX = 0;
        int boxY = 0;
        boolean done = false;
        int walk = 0;
        int boxWalk = 0;
        while (!done) {

            if (boxId == 0) { //there is no box
                //check if it has arrived to the destination
                if (world.getPlayer().getPosition().x + walk == newX) {
                    break;
                }

                //check if the move is possible
                switch(world.map[(int) world.getPlayer().getPosition().x + walk + 1][(int) world.getPlayer().getPosition().y]) {
                    case -1:
                        //there is a wall
                        done = true;
                        break;
                    case 0:
                        //its all clear
                        walk++;
                        break;
                    default:
                        //then it must be a box
                        boxId = world.map[(int) world.getPlayer().getPosition().x + walk + 1][(int) world.getPlayer().getPosition().y];
                        break;
                }



            } else { //there is a box
                //check if it has arrived to the destination
                if(world.getBoxes().get(boxId - 1).getPosition().x + boxWalk == newX) {
                    break;
                }

                //check if the move is possible
                switch(world.map[(int)world.getBoxes().get(boxId - 1).getPosition().x + boxWalk + 1][(int)world.getBoxes().get(boxId - 1).getPosition().y]) {
                    case -1:
                        //if there is a wall
                        done = true;
                        break;
                    case 0:
                        //its all clear;
                        boxWalk++;
                        walk++;
                        break;
                    default:
                        //them its another box
                        done = true;
                        break;
                }

            }

        }

        //update player position
        world.getPlayer().setPosition(new Vector2(world.getPlayer().getPosition().x + walk, world.getPlayer().getPosition().y));
        //update box position
        if(boxId != 0) {
            //free the old box position on the map
            world.map[(int)world.getBoxes().get(boxId - 1).getPosition().x][(int)world.getBoxes().get(boxId - 1).getPosition().y] = 0;
            //move the box
            world.getBoxes().get(boxId - 1).setPosition(new Vector2(world.getBoxes().get(boxId - 1).getPosition().x + boxWalk, world.getBoxes().get(boxId - 1).getPosition().y));
            //mark the new box position on the map
            world.map[(int)world.getBoxes().get(boxId - 1).getPosition().x][(int)world.getBoxes().get(boxId - 1).getPosition().y] = boxId;

            //add state to the stack with the box
            addState(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y, boxId - 1, world.getBoxes().get(boxId - 1).getPosition().x, world.getBoxes().get(boxId -1).getPosition().y);

        } else {
            //add state with no box
            addState(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y, -1,  0, 0);

        }

    }

    private void moveLeft(int newX, int newY) {

        int boxId = 0;
        int boxX = 0;
        int boxY = 0;
        boolean done = false;
        int walk = 0;
        int boxWalk = 0;
        while (!done) {

            if (boxId == 0) { //there is no box
                //check if it has arrived to the destination
                if (world.getPlayer().getPosition().x - walk == newX) {
                    break;
                }

                //check if the move is possible
                switch (world.map[(int) world.getPlayer().getPosition().x - walk - 1][(int) world.getPlayer().getPosition().y]) {
                    case -1:
                        //there is a wall
                        done = true;
                        break;
                    case 0:
                        //its all clear
                        walk++;
                        break;
                    default:
                        //then it must be a box
                        boxId = world.map[(int) world.getPlayer().getPosition().x - walk - 1][(int) world.getPlayer().getPosition().y];
                        break;
                }


            } else { //there is a box
                //check if it has arrived to the destination
                if (world.getBoxes().get(boxId - 1).getPosition().x - boxWalk == newX) {
                    break;
                }

                //check if the move is possible
                switch (world.map[(int) world.getBoxes().get(boxId - 1).getPosition().x - boxWalk - 1][(int) world.getBoxes().get(boxId - 1).getPosition().y]) {
                    case -1:
                        //if there is a wall
                        done = true;
                        break;
                    case 0:
                        //its all clear;
                        boxWalk++;
                        walk++;
                        break;
                    default:
                        //them its another box
                        done = true;
                        break;
                }

            }

        }

        //update player position
        world.getPlayer().setPosition(new Vector2(world.getPlayer().getPosition().x - walk, world.getPlayer().getPosition().y));
        //update box position
        if (boxId != 0) {
            //free the old box position on the map
            world.map[(int) world.getBoxes().get(boxId - 1).getPosition().x][(int) world.getBoxes().get(boxId - 1).getPosition().y] = 0;
            //move the box
            world.getBoxes().get(boxId - 1).setPosition(new Vector2(world.getBoxes().get(boxId - 1).getPosition().x - boxWalk, world.getBoxes().get(boxId - 1).getPosition().y));
            //mark the new box position on the map
            world.map[(int) world.getBoxes().get(boxId - 1).getPosition().x][(int) world.getBoxes().get(boxId - 1).getPosition().y] = boxId;

            //add state to the stack with the box
            addState(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y, boxId - 1, world.getBoxes().get(boxId - 1).getPosition().x, world.getBoxes().get(boxId - 1).getPosition().y);

        } else {
            //add state with no box
            addState(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y, -1, 0, 0);
        }
    }

    private void moveUp(int newX, int newY) {

        int boxId = 0;
        int boxX = 0;
        int boxY = 0;
        boolean done = false;
        int walk = 0;
        int boxWalk = 0;
        while (!done) {

            if (boxId == 0) { //there is no box
                //check if it has arrived to the destination
                if (world.getPlayer().getPosition().y + walk == newY) {
                    break;
                }

                //check if the move is possible
                switch (world.map[(int) world.getPlayer().getPosition().x][(int) world.getPlayer().getPosition().y + walk + 1]) {
                    case -1:
                        //there is a wall
                        done = true;
                        break;
                    case 0:
                        //its all clear
                        walk++;
                        break;
                    default:
                        //then it must be a box
                        boxId = world.map[(int) world.getPlayer().getPosition().x][(int) world.getPlayer().getPosition().y + walk + 1 ];
                        break;
                }


            } else { //there is a box
                //check if it has arrived to the destination
                if (world.getBoxes().get(boxId - 1).getPosition().y + boxWalk == newY) {
                    break;
                }

                //check if the move is possible
                switch (world.map[(int) world.getBoxes().get(boxId - 1).getPosition().x ][(int) world.getBoxes().get(boxId - 1).getPosition().y + boxWalk + 1]) {
                    case -1:
                        //if there is a wall
                        done = true;
                        break;
                    case 0:
                        //its all clear;
                        boxWalk++;
                        walk++;
                        break;
                    default:
                        //them its another box
                        done = true;
                        break;
                }

            }

        }

        //update player position
        world.getPlayer().setPosition(new Vector2(world.getPlayer().getPosition().x , world.getPlayer().getPosition().y + walk));
        //update box position
        if (boxId != 0) {
            //free the old box position on the map
            world.map[(int) world.getBoxes().get(boxId - 1).getPosition().x][(int) world.getBoxes().get(boxId - 1).getPosition().y] = 0;
            //move the box
            world.getBoxes().get(boxId - 1).setPosition(new Vector2(world.getBoxes().get(boxId - 1).getPosition().x , world.getBoxes().get(boxId - 1).getPosition().y + boxWalk));
            //mark the new box position on the map
            world.map[(int) world.getBoxes().get(boxId - 1).getPosition().x][(int) world.getBoxes().get(boxId - 1).getPosition().y] = boxId;

            //add state to the stack with the box
            addState(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y, boxId - 1, world.getBoxes().get(boxId - 1).getPosition().x, world.getBoxes().get(boxId - 1).getPosition().y);

        } else {
            //add state with no box
            addState(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y, -1, 0, 0);

        }
    }

    private void moveDown(int newX, int newY) {

        int boxId = 0;
        int boxX = 0;
        int boxY = 0;
        boolean done = false;
        int walk = 0;
        int boxWalk = 0;
        while (!done) {

            if (boxId == 0) { //there is no box
                //check if it has arrived to the destination
                if (world.getPlayer().getPosition().y - walk == newY) {
                    break;
                }

                //check if the move is possible
                switch (world.map[(int) world.getPlayer().getPosition().x][(int) world.getPlayer().getPosition().y - walk - 1]) {
                    case -1:
                        //there is a wall
                        done = true;
                        break;
                    case 0:
                        //its all clear
                        walk++;
                        break;
                    default:
                        //then it must be a box
                        boxId = world.map[(int) world.getPlayer().getPosition().x][(int) world.getPlayer().getPosition().y - walk - 1 ];
                        break;
                }


            } else { //there is a box
                //check if it has arrived to the destination
                if (world.getBoxes().get(boxId - 1).getPosition().y - boxWalk == newY) {
                    break;
                }

                //check if the move is possible
                switch (world.map[(int) world.getBoxes().get(boxId - 1).getPosition().x ][(int) world.getBoxes().get(boxId - 1).getPosition().y - boxWalk - 1]) {
                    case -1:
                        //if there is a wall
                        done = true;
                        break;
                    case 0:
                        //its all clear;
                        boxWalk++;
                        walk++;
                        break;
                    default:
                        //them its another box
                        done = true;
                        break;
                }

            }

        }

        //update player position
        world.getPlayer().setPosition(new Vector2(world.getPlayer().getPosition().x , world.getPlayer().getPosition().y - walk));
        //update box position
        if (boxId != 0) {
            //free the old box position on the map
            world.map[(int) world.getBoxes().get(boxId - 1).getPosition().x][(int) world.getBoxes().get(boxId - 1).getPosition().y] = 0;
            //move the box
            world.getBoxes().get(boxId - 1).setPosition(new Vector2(world.getBoxes().get(boxId - 1).getPosition().x , world.getBoxes().get(boxId - 1).getPosition().y - boxWalk));
            //mark the new box position on the map
            world.map[(int) world.getBoxes().get(boxId - 1).getPosition().x][(int) world.getBoxes().get(boxId - 1).getPosition().y] = boxId;

            //add state to the stack with the box
            addState(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y, boxId - 1, world.getBoxes().get(boxId - 1).getPosition().x, world.getBoxes().get(boxId - 1).getPosition().y);

        } else {
            //add state with no box
            addState(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y, -1, 0, 0);

        }
    }

    private void checkWin() {
        for(Target target : world.getTargets()) {
            if(world.map[(int)target.getPosition().x][(int)target.getPosition().y]  == 0) {
                return;
            }

        }
        //multiplexer.setProcessors(null);
        //Gdx.input.setInputProcessor(null);
        //this.dispose();

        game.switchToLevelCompleteScreen(level);



    }

    private void addState(float playerX, float playerY, float boxId, float boxX, float boxY) {
        State state = new State();
        state.setPlayerX(playerX);
        state.setPlayerY(playerY);
        state.setBoxId(boxId);
        state.setBoxX(boxX);
        state.setBoxY(boxY);

        states.add(state);
    }

    private void undoMove() {

        if(states.size() > 1) {
            if (states.get(states.size() - 1).getBoxId() == -1) {
                //there is no box
                states.remove(states.get(states.size() - 1)); //remove the last element
                //update the map
                // world.map[(int)world.getPlayer().getPosition().x][(int)world.getPlayer().getPosition().y] = 0; //clear the spot
                world.getPlayer().setPosition(new Vector2(states.get(states.size() - 1).getPlayerX(), states.get(states.size() - 1).getPlayerY()));
                //does not matter any more: world.map[(int)world.getPlayer().getPosition().x][(int)world.getPlayer().getPosition().y]


            } else {
                //there is a box
                boolean found = false;
                float newBoxX = 0;
                float newBoxY = 0;
                float id = states.get(states.size() -1).getBoxId();
                //now we see the last time the boxed was moved
                for(int i=states.size() - 2; i >=0; i--) {
                    if(states.get(i).getBoxId() == id) {
                        found = true;
                        //save the previous box position
                        newBoxX = states.get(i).getBoxX();
                        newBoxY = states.get(i).getBoxY();
                        break;
                    }
                }

                if(found == true) {
                    //update the map
                    world.map[(int)world.getBoxes().get((int)id ).getPosition().x][(int)world.getBoxes().get((int)id).getPosition().y] = 0; //clears the spot
                    world.getBoxes().get((int) id ).setPosition(new Vector2(newBoxX, newBoxY)); //new box position
                    world.map[(int)world.getBoxes().get((int)id ).getPosition().x][(int)world.getBoxes().get((int)id ).getPosition().y] = (int)id + 1;//mark the spot
                    states.remove(states.get(states.size() -1));
                    //world.map[(int)world.getPlayer().getPosition().x][(int)world.getPlayer().getPosition().y] = 0;
                    world.getPlayer().setPosition(new Vector2(states.get(states.size() -1).getPlayerX(), states.get(states.size() -1).getPlayerY()));

                } else {
                    //set the initial position
                    //System.out.println(initialBoxes.get((int)id).getPosition().x + " " + initialBoxes.get((int)id).getPosition().y);
                    world.map[(int)world.getBoxes().get((int)id ).getPosition().x][(int)world.getBoxes().get((int)id ).getPosition().y] = 0; //clears the spot
                    world.getBoxes().get((int)id ).setPosition(new Vector2(initialBoxes.get((int)id).getPosition().x, initialBoxes.get((int)id).getPosition().y));//box new position
                    world.map[(int)world.getBoxes().get((int)id ).getPosition().x][(int)world.getBoxes().get((int)id ).getPosition().y] = (int)id + 1;

                    states.remove(states.get(states.size() -1));
                    // world.map[(int)world.getPlayer().getPosition().x][(int)world.getPlayer().getPosition().y] = 0;
                    world.getPlayer().setPosition(new Vector2(states.get(states.size() - 1).getPlayerX(), states.get(states.size() - 1).getPlayerY()));
                }
            }
        }

    }
    //TODO: check if move is already at the top of the stack
    //stores the initial position of the boxes
    private void initBoxes() {
        for(Box box : world.getBoxes()) {
            //copy the VALUES and not the REFERENCE
            initialBoxes.add(new Box(new Vector2(box.getPosition().x, box.getPosition().y), box.getId()));
        }
    }


    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
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
        Gdx.input.setInputProcessor(null);
    }
    //TODO INPUT
    @Override
    public boolean touchDragged(int x, int y, int pointer) {
// TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
// TODO Auto-generated method stub
        return false;

    }
    @Override
    public boolean mouseMoved(int x, int y) {
        return false;
    }
    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
// TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean keyDown(int keycode) {
        return true;
    }
}