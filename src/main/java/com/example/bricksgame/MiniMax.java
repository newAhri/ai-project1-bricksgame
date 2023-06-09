package com.example.bricksgame;

import com.example.bricksgame.data.GameState;
import com.example.bricksgame.tree.Node;
import com.example.bricksgame.tree.Tree;

import java.util.List;

public class MiniMax {

    private Tree tree;
    private GameControl gameControl = new GameControl();


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
        int sumOfChildrenScores = getSumOfChildrenScores(children);
        node.setScore(sumOfChildrenScores);
    }

    public Tree getTree(GameState gameState) {
        constructTree(gameState);
        return tree;
    }

    private int getSumOfChildrenScores(List<Node> children){
        return children
                .stream()
                .map(Node::getScore)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
