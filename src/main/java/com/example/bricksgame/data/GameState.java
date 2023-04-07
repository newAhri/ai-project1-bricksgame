package com.example.bricksgame.data;

import java.util.List;

public class GameState {
    private List<AttachPoint> attachPointList;
    private List<BrickRectangle> brickRectangleList;
    private BrickRectangle usedBrickRectangle;
    private AttachPoint usedAttachPoint;

    public GameState(List<AttachPoint> attachPointList, List<BrickRectangle> brickRectangleList) {
        this.attachPointList = attachPointList;
        this.brickRectangleList = brickRectangleList;
    }

    public GameState(GameState gameState) {
        this.attachPointList = gameState.getAttachPointList();
        this.brickRectangleList = gameState.getBrickRectangleList();
        this.usedBrickRectangle = gameState.getUsedBrickRectangle();
        this.usedAttachPoint = gameState.getUsedAttachPoint();
    }


    public List<AttachPoint> getAttachPointList() {
        return attachPointList;
    }

    public List<BrickRectangle> getBrickRectangleList() {
        return brickRectangleList;
    }


    public BrickRectangle getUsedBrickRectangle() {
        return usedBrickRectangle;
    }

    public void setUsedBrickRectangle(BrickRectangle usedBrickRectangle) {
        this.usedBrickRectangle = usedBrickRectangle;
    }

    public AttachPoint getUsedAttachPoint() {
        return usedAttachPoint;
    }

    public void setUsedAttachPoint(AttachPoint usedAttachPoint) {
        this.usedAttachPoint = usedAttachPoint;
    }
}
