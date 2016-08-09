package com.ma.nova.othello_try_3;

/**
 * Created by user on 2015/10/24.
 */
public class Cell {
    private int x,y;

    public Cell( int x,int y)
    {
        this.x = x;
        this.y = y;
    }

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
}
