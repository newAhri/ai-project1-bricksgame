package com.example.bricksgame;

import com.example.bricksgame.data.*;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class MiniMax {

    private Tree tree;
    private GameControl gameControl = new GameControl();
    private int i = 0;


    public void constructTree(GameState gameState) {

        tree = new Tree();
        Node root = new Node
                (new GameState(gameState.getAttachPointList(), gameState.getBrickRectangleList()), true);
        tree.setRoot(root);
        constructTree(root);
        setChildNodeScores();
    }

    private void constructTree(Node parentNode) {

        GameState parentNodeGameState = parentNode.getGameState();
        List<GameState> gameStateList = gameControl
                .getListOfPossibleGameStates(parentNodeGameState);
        if (gameStateList.isEmpty()) {
            parentNode.setMoreMovesLeft(false);
        } else {
            boolean isChildMaxPlayer = !parentNode.isMaxPlayer();
            gameStateList.forEach(gameState -> {
                Node newChildNode = new Node(new GameState(gameState), isChildMaxPlayer);
                System.out.println(i++);
                parentNode.addChild(newChildNode);
                constructTree(newChildNode);
            });
        }
    }

    public void setChildNodeScores() {

        Node root = tree.getRoot();
        setChildNodeScores(root);
    }

    private void setChildNodeScores(Node node) {

        List<Node> children = node.getChildren();
        boolean isMaxPlayer = node.isMaxPlayer();
        children.forEach(child -> {
            if (!child.isMoreMovesLeft()) {
                child.setScore(isMaxPlayer ? 1 : -1);
            } else {
                setChildNodeScores(child);
            }
        });
        Node bestChild = findBestChild(isMaxPlayer, children);
        node.setScore(bestChild.getScore());
    }

    private Node findBestChild(boolean isMaxPlayer, List<Node> children) {

        Comparator<Node> byScoreComparator = Comparator.comparing(Node::getScore);
        return children.stream()
                .max(isMaxPlayer ? byScoreComparator : byScoreComparator.reversed())
                .orElseThrow(NoSuchElementException::new);
    }

    public Tree getTree(GameState gameState) {
        constructTree(gameState);
        return tree;
    }
}
