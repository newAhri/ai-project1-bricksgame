package com.example.bricksgame.data;

import java.util.List;

public class GameState {
    private boolean isMoreMovesLeft = true;
    private List<AttachPoint> attachPointList;
    private List<BrickRectangle> brickRectangleList;

    public GameState(List<AttachPoint> attachPointList, List<BrickRectangle> brickRectangleList) {
        this.attachPointList = attachPointList;
        this.brickRectangleList = brickRectangleList;
    }

    public GameState(boolean moreMovesLeft) {
        this.isMoreMovesLeft = moreMovesLeft;
    }

    public boolean isMoreMovesLeft() {
        return isMoreMovesLeft;
    }

    public void setMoreMovesLeft(boolean moreMovesLeft) {
        isMoreMovesLeft = moreMovesLeft;
    }

    public List<AttachPoint> getAttachPointList() {
        return attachPointList;
    }

    public void setAttachPointList(List<AttachPoint> attachPointList) {
        this.attachPointList = attachPointList;
    }

    public List<BrickRectangle> getBrickRectangleList() {
        return brickRectangleList;
    }

    public void setBrickRectangleList(List<BrickRectangle> brickRectangleList) {
        this.brickRectangleList = brickRectangleList;
    }
}
