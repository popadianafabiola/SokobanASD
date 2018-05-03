package com.asd.sokoban;

/**
 * Created by fabii on 23.04.2018.
 */


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import com.asd.sokoban.Box;
import com.asd.sokoban.Block;
import com.asd.sokoban.Target;
import com.asd.sokoban.Player;

public class World {
    Array<Box> boxes = new Array<Box>();
    Array<Block> blocks = new Array<Block>();
    Array<Target> targets = new Array<Target>();
    Player player;
    int[][] map = new int[20][20];



    //Constructor
    public World(int level) {
        createWorld(level);
    }

    private void createWorld(int level) {

        //load level from file
        FileHandle file = Gdx.files.internal("levels/level" + String.valueOf(level) + ".txt");
        //split the rows
        String[] lines = file.readString().split("\n");
        for(String row : lines) {
            String[] chars = row.split(" ");
            try {
                int x = Integer.parseInt(chars[1]);
                int y = Integer.parseInt(chars[2]);
                if (chars[0].equals("1")) {
                    blocks.add(new Block(new Vector2(x, y)));
                    map[x][y] = -1;
                } else if (chars[0].equals("0")) {
                    player = new Player(new Vector2(x, y));
                } else if (chars[0].equals("2")) {
                    boxes.add(new Box(new Vector2(x, y), boxes.size + 1));
                    map[x][y] = boxes.size;
                } else if (chars[0].equals("3")) {
                    targets.add(new Target(new Vector2(x, y)));
                }
            }
            catch(Exception ex) {
                System.out.println("invalid object");
            }
        }

    }

    //Getters and setters
    public Array<Box> getBoxes() {
        return boxes;
    }

    public void setBoxes(Array<Box> boxes) {
        this.boxes = boxes;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Array<Block> getBlocks() {
        return blocks;
    }


    public void setBlocks(Array<Block> blocks) {
        this.blocks = blocks;
    }

    public Array<Target> getTargets() {
        return targets;
    }

    public void setTargets(Array<Target> targets) {
        this.targets = targets;
    }
}