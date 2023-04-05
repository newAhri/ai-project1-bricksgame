package com.example.bricksgame;

public class AttachPoint {
    private int index;
    private double X;
    private double Y;
    private boolean horizontalBrickPlacable;
    private boolean verticalBrickPlacable;
    private boolean isFree = true;

    public AttachPoint() {
    }

    public AttachPoint(int index, double x, double y, boolean horizontalBrickPlacable, boolean verticalBrickPlacable) {
        this.index = index;
        X = x;
        Y = y;
        this.horizontalBrickPlacable = horizontalBrickPlacable;
        this.verticalBrickPlacable = verticalBrickPlacable;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public boolean isHorizontalBrickPlacable() {
        return horizontalBrickPlacable;
    }

    public boolean isVerticalBrickPlacable() {
        return verticalBrickPlacable;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }
}
