package org.hbrs.se1.ws24.exercises.uebung10;

import java.util.List;

public class RectangleBoundingBox implements BoundingBox {

    private MyPrettyRectangle shape;

    public RectangleBoundingBox(List<MyPrettyRectangle> rects) {
        double minX = Double.POSITIVE_INFINITY,
                minY = Double.POSITIVE_INFINITY,
                maxX = Double.NEGATIVE_INFINITY,
                maxY = Double.NEGATIVE_INFINITY;

        for (MyPrettyRectangle rect : rects) {

            minX = Math.min(minX, rect.getLu().getX());
            maxY = Math.max(maxY, rect.getRo().getY());

            maxX = Math.max(maxX, rect.getRo().getX());
            minY = Math.min(minY, rect.getLu().getY());
        }

        this.shape = new MyPrettyRectangle(minX, minY, maxX, maxY);
    }

    public RectangleBoundingBox() {
        this.shape = new MyPrettyRectangle(0.0, 0.0, 0.0, 0.0);
    }


    public MyPrettyRectangle getShape() {
        return this.shape;
    }
}
