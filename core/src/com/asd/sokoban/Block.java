package com.asd.sokoban;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
/**
 * Created by fabii on 26.03.2018.
 */

public class Block{
    private static final float SIZE = 1f;
    private Rectangle bounds = new Rectangle();
    private Vector2 position = new Vector2();

    //Constructor
    public Block(Vector2 pos) {
        this.position = pos;
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
    }


    //Getters and setters
    public float getSize() {
        return SIZE;
    }
    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
