package com.ma.nova.othello_try_3;

import android.graphics.Point;

/**
 * Created by user on 2015/10/24.
 */
public class Coordinates {
    private Point point[];

    public Coordinates(Point a, Point b,Point c,Point d)
    {
        point = new Point[4];
        point[0] = a;
        point[1] = b;
        point[2] = c;
        point[3] = d;
    }

    public Point getPoint(int index) {
        return point[index];
    }
}
