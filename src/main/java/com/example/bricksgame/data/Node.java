package com.example.bricksgame.data;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<AttachPoint> attachPointList;
    private List<BrickRectangle> brickRectangleList;
    private boolean isMaxPlayer;
    private int score;
    private List<Node> children;
    private boolean moreMovesLeft = true;

    public Node(List<AttachPoint> attachPointList, List<BrickRectangle> brickRectangleList, boolean isMaxPlayer) {
        this.attachPointList = attachPointList;
        this.brickRectangleList = brickRectangleList;
        this.isMaxPlayer = isMaxPlayer;
    }

    public List<BrickRectangle> getBrickRectangleList() {
        return brickRectangleList;
    }

    public void setBrickRectangleList(List<BrickRectangle> brickRectangleList) {
        this.brickRectangleList = brickRectangleList;
    }

    public List<AttachPoint> getAttachPointList() {
        return attachPointList;
    }

    public void setAttachPointList(List<AttachPoint> attachPointList) {
        this.attachPointList = attachPointList;
    }

    public boolean isMaxPlayer() {
        return isMaxPlayer;
    }

    public void setMaxPlayer(boolean maxPlayer) {
        isMaxPlayer = maxPlayer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public boolean isMoreMovesLeft() {
        return moreMovesLeft;
    }

    public void setMoreMovesLeft(boolean moreMovesLeft) {
        this.moreMovesLeft = moreMovesLeft;
    }

    public void addChild(Node childNode){
        if (children == null){
            children = new ArrayList<>();
        }
        children.add(childNode);
    }
}