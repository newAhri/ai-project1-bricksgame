package com.example.bricksgame;

public class AttachPoint {
    private double X;
    private double Y;
    private boolean horizontalBrickPlacable;
    private boolean verticalBrickPlacable;
    private boolean isFree = true;

    public AttachPoint() {
    }

    public AttachPoint(double x, double y, boolean horizontalBrickPlacable, boolean verticalBrickPlacable) {
        X = x;
        Y = y;
        this.horizontalBrickPlacable = horizontalBrickPlacable;
        this.verticalBrickPlacable = verticalBrickPlacable;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
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
