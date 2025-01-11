package org.hbrs.se1.ws24.exercises.uebung10;

import java.util.ArrayList;
import java.util.List;

public class BoundingBoxFactory {

    public static BoundingBox createBoundingBox(Shape[] shapes, BoundingBoxType type) throws IllegalArgumentException {
        if (shapes == null) return null;
        switch(type) {
            case RECTANGLE -> {
                if (shapes.length == 0) {
                   return new RectangleBoundingBox();
                }

                List<MyPrettyRectangle> rectangles = new ArrayList<>();
                for (Shape shape : shapes) {
                    if (shape instanceof MyPrettyRectangle) {
                        rectangles.add((MyPrettyRectangle) shape);
                    } else {
                        throw new IllegalArgumentException("Alle Formen in der BoundingBox müssen der gleichen Art sein!");
                    }
                }
                return new RectangleBoundingBox(rectangles);
            }
            // case Circle
            // case Triangle
            // etc.

            default -> throw new IllegalArgumentException("BoundingBox nicht implementiert für: " + type);
        }

    }
}
