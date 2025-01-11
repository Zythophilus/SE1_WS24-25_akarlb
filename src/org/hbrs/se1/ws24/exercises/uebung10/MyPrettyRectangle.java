package org.hbrs.se1.ws24.exercises.uebung10;

public class MyPrettyRectangle implements Shape {

    private MyPoint lu;
    private MyPoint ro;

    public MyPrettyRectangle(double x1, double y1, double x2, double y2) {
        this.lu = new MyPoint(x1, y1);
        this.ro = new MyPoint(x2, y2);

    }

    public MyPrettyRectangle(MyPoint lu, MyPoint ro) {
        this.lu = lu;
        this.ro = ro;
    }

    public MyPoint getLu() {
        return lu;
    }

    public void setLu(MyPoint lu) {
        this.lu = lu;
    }

    public MyPoint getRo() {
        return ro;
    }

    public void setRo(MyPoint ro) {
        this.ro = ro;
    }

    public boolean contains(MyPrettyRectangle rect) {
        return (this.lu.getX() <= rect.lu.getX() &&
                this.lu.getY() <= rect.lu.getY() &&
                this.ro.getX() >= rect.ro.getX() &&
                this.ro.getY() >= rect.ro.getY());
    }

    public MyPoint getCenter() {
        return new MyPoint((lu.getX() + ro.getX()) / 2, (lu.getY() + ro.getY()) / 2);
    }

    public double getArea() {
        return (ro.getX() - lu.getX()) * (ro.getY() - lu.getY());
    }

    public double getPerimeter() {
        return  2 * ((ro.getX() - lu.getX()) + (ro.getY() - lu.getY()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        MyPrettyRectangle rect = (MyPrettyRectangle) obj;
        return this.ro.equals(rect.getRo()) && this.lu.equals(rect.getLu());

    }

    @Override
    public String toString() {
        return "LU: " + this.lu + " | RO: " + this.ro;
    }
}
