package com.example.bricksgame.data;

import javafx.scene.shape.Rectangle;

public class BrickRectangle extends Rectangle implements Cloneable{
    private BrickType brickType;
    private boolean isMovable = true;

    public BrickRectangle(BrickType brickType) {
        super();
        switch (brickType){
            case HORIZONTAL:
                super.setWidth(200);
                super.setHeight(100);
                break;
            case VERTICAL:
                super.setWidth(100);
                super.setHeight(200);
                break;
            case SINGLE:
                super.setWidth(100);
                super.setHeight(100);
                break;
        }
        this.brickType = brickType;
    }

    public BrickType getBrickType() {
        return brickType;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    @Override
    public BrickRectangle clone() {
        try{
            return (BrickRectangle)super.clone();
        } catch (CloneNotSupportedException ex){
            throw new AssertionError();
        }

    }
}
