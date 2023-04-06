package com.example.bricksgame;

import com.example.bricksgame.data.*;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class MiniMax {

    Tree tree;
    GameControl gameControl = new GameControl();

    public void constructTree(List<AttachPoint> attachPointList, List<BrickRectangle> brickRectangleList){

        tree = new Tree();
        Node root = new Node(attachPointList, brickRectangleList, true);
        tree.setRoot(root);
        constructTree(root);
        checkWin();
    }

    private void constructTree(Node parentNode) {

        List <GameState> gameStateList = gameControl
                .getListOfPossibleGameStates(parentNode.getAttachPointList(), parentNode.getBrickRectangleList());
        boolean isChildMaxPlayer = !parentNode.isMaxPlayer();
        gameStateList.forEach(g -> {
            Node newChildNode = new Node(g.getAttachPointList(), g.getBrickRectangleList(),isChildMaxPlayer);
            parentNode.addChild(newChildNode);
            if (g.isMoreMovesLeft()){
                constructTree(newChildNode);
            } else {
                newChildNode.setMoreMovesLeft(false);
            }
        });
    }

    public void checkWin() {

        Node root = tree.getRoot();
        checkWin(root);
    }

    private void checkWin(Node node) {

        List<Node> children = node.getChildren();
        boolean isMaxPlayer = node.isMaxPlayer();
        children.forEach(child -> {
            if (!child.isMoreMovesLeft()) {
                child.setScore(isMaxPlayer ? 1 : -1);
            } else {
                checkWin(child);
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

}
