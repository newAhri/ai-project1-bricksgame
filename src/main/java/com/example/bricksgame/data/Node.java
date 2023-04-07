package com.example.bricksgame.data;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private GameState gameState;
    private boolean isMaxPlayer;
    private int score;
    private List<Node> children;
    private boolean moreMovesLeft = true;

    public Node(GameState gameState, boolean isMaxPlayer) {
        this.gameState = gameState;
        this.isMaxPlayer = isMaxPlayer;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isMaxPlayer() {
        return isMaxPlayer;
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