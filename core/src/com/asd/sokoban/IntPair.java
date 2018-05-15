package com.asd.sokoban;

/**
 * Created by fabii on 23.04.2018.
 */
public class IntPair implements Comparable<IntPair> {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public IntPair(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public int compareTo(IntPair p) {
        if (p.getX() == x && p.getY() == y) {
            return 0;
        } else return p.getX() < x ? 1 : -1;
    }
}