package com.vk.challenge.utils;

import android.graphics.Point;

/**
 * Created by nikita on 08.09.17.
 */

public class MathUtils {

    public static int distanceBetween(Point p1, Point p2) {
        return distanceBetween(p1.x, p1.y, p2.x, p2.y);
    }
    public static int distanceBetween(float x1, float y1, float x2, float y2) {
        return (int) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static Point midPoint(float x1, float y1, float x2, float y2) {
        float mX = (x1 + x2) / 2;
        float mY = (y1 + y2) / 2;
        return new Point((int) mX, (int) mY);
    }
}
