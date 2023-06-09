package com.example.bricksgame.data;

public class AttachPoint implements Cloneable {

    private int index;
    private double X;
    private double Y;
    private boolean horizontalBrickPlacable;
    private boolean verticalBrickPlacable;

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

    public void setHorizontalBrickPlacable(boolean horizontalBrickPlacable) {
        this.horizontalBrickPlacable = horizontalBrickPlacable;
    }

    public void setVerticalBrickPlacable(boolean verticalBrickPlacable) {
        this.verticalBrickPlacable = verticalBrickPlacable;
    }

    @Override
    public AttachPoint clone(){
        try{
            return (AttachPoint) super.clone();
        } catch (CloneNotSupportedException ex){
            throw new AssertionError();
        }
    }
}
