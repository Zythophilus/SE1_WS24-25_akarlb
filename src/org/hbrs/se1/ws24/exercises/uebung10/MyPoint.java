package org.hbrs.se1.ws24.exercises.uebung10;

public class MyPoint {

    private double x;
    private double y;

    public MyPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        MyPoint point = (MyPoint) obj;

        return this.x == point.getX() && this.y == point.getY();

    }
    @Override
    public String toString() {

        return "MyPoint(" + x + ", " + y + ")";

    }
}
