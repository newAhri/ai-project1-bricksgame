package com.example.bricksgame;

import com.example.bricksgame.data.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main extends Application {

    private List<AttachPoint> attachPointList;
    private List<BrickRectangle> brickRectangleList;
    private StackPane gameFieldAndBricksChoiceStackPane;
    private GameControl gameControl = new GameControl();
    private boolean isPlayersTurn = true;
    private boolean isPlayable = true;
    private MiniMax miniMax = new MiniMax();
    private Tree tree;
    private Node currentNode;


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        createListOfAttachPoints();
        primaryStage.setScene(new Scene(createContent(), 1200, 420));
        primaryStage.show();

        tree = miniMax.getTree(new GameState(new ArrayList<>(attachPointList), brickRectangleList));
        currentNode = tree.getRoot();
    }

    private void playGame() {


        checkState();
        if (!isPlayable) {
            finishGame();
            return;
        }

        if (!isPlayersTurn) {

            List<Node> childrenNodes = currentNode.getChildren();

            for (Node child : childrenNodes) {
                if (child.getScore() == (currentNode.isMaxPlayer() ? -1 : 1)) {
                    currentNode = child;
                } else {
                    currentNode = childrenNodes.get(0);
                }
            }

            BrickType brickRectangleType = currentNode
                    .getGameState()
                    .getUsedBrickRectangle()
                    .getBrickType();

            BrickRectangle brickRectangle = brickRectangleList.stream()
                    .filter(brick -> brick.getBrickType() == brickRectangleType)
                    .findFirst()
                    .get();
            AttachPoint attachPoint = currentNode.getGameState().getUsedAttachPoint();

            moveBrickRectangle(brickRectangle, attachPoint);

            attachPointList = gameControl.updateAttachPointList(attachPoint, brickRectangle, attachPointList);
            brickRectangleList.remove(brickRectangle);

            resizeBrickRectangle(brickRectangle, ToSize.BIG);
            isPlayersTurn = true;
            checkState();

            if (!isPlayable) {
                finishGame();
                return;
            }
        }

    }

    private void checkState() {

        List<BrickType> leftBrickTypeList = brickRectangleList
                .stream()
                .map(BrickRectangle::getBrickType)
                .distinct()
                .collect(Collectors.toList());
        if (leftBrickTypeList.isEmpty() || !currentNode.isMoreMovesLeft()) {
            isPlayable = false;
        }
    }

    private Parent createContent() {

        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: rgb(249,166,72)");
        hBox.setSpacing(10);

        createStackPaneGameFieldAndBricksChoiceRectangle();
        hBox.getChildren().addAll(gameFieldAndBricksChoiceStackPane);

        brickRectangleList = getBrickRectangleList();
        placeBricksInChoiceRectangle();
        placeInitialSingleBrickRectanglesInGameField();

        return hBox;
    }

    private void createStackPaneGameFieldAndBricksChoiceRectangle() {

        gameFieldAndBricksChoiceStackPane = new StackPane();
        gameFieldAndBricksChoiceStackPane.setPrefSize(700, 500);

        Path gameFieldPath = createGameFieldPath();

        Rectangle gameFieldRectangle = new Rectangle(10, 10, 400, 400);
        Rectangle bricksChoiceRectangle = new Rectangle(10, 10, 200, 400);
        gameFieldRectangle.setFill(Color.WHITE);
        bricksChoiceRectangle.setFill(Color.WHITE);

        gameFieldAndBricksChoiceStackPane.setMargin(gameFieldRectangle, new Insets(10, 10, 10, 10));
        gameFieldAndBricksChoiceStackPane.setAlignment(gameFieldRectangle, Pos.CENTER_LEFT);
        gameFieldAndBricksChoiceStackPane.setMargin(gameFieldPath, new Insets(10, 10, 10, 10));
        gameFieldAndBricksChoiceStackPane.setAlignment(gameFieldPath, Pos.CENTER_LEFT);
        gameFieldAndBricksChoiceStackPane.setMargin(bricksChoiceRectangle, new Insets(10, 10, 10, 10));
        gameFieldAndBricksChoiceStackPane.setAlignment(bricksChoiceRectangle, Pos.CENTER_RIGHT);

        ObservableList list = gameFieldAndBricksChoiceStackPane.getChildren();

        list.addAll(gameFieldRectangle, gameFieldPath, bricksChoiceRectangle);


    }

    private void placeInitialSingleBrickRectanglesInGameField() {

        Random random = new Random();
        int singleBricksAmount = 3;
        List<Integer> indexList = new ArrayList<>(singleBricksAmount);
        IntStream.generate(() -> random.nextInt(16))
                .distinct()
                .limit(singleBricksAmount)
                .forEach(indexList::add);

        for (int i = 0; i < singleBricksAmount; i++) {
            int index = indexList.get(i);
            BrickRectangle singleBrickRectangle = new BrickRectangle(BrickType.SINGLE);
            AttachPoint attachPoint = attachPointList.get(index);
            gameFieldAndBricksChoiceStackPane.getChildren().add(singleBrickRectangle);
            gameFieldAndBricksChoiceStackPane.setAlignment(singleBrickRectangle, Pos.TOP_RIGHT);
            moveBrickRectangle(singleBrickRectangle, attachPoint);
            attachPointList = gameControl.updateAttachPointList(attachPoint, singleBrickRectangle, attachPointList);
            System.out.println();
        }
    }

    private void placeBricksInChoiceRectangle() {

        ObservableList list = gameFieldAndBricksChoiceStackPane.getChildren();


        int index = 0;
        for (int i = 0; i < brickRectangleList.size() / 3 + 1; i++) {
            for (int j = 0; j < 3; j++) {
                if (index < brickRectangleList.size()) {
                    BrickRectangle brickRectangle = brickRectangleList.get(i * 3 + j);
                    list.add(brickRectangle);
                    gameFieldAndBricksChoiceStackPane.setAlignment(brickRectangle, Pos.TOP_RIGHT);

                    brickRectangle.setTranslateX(j * 60 - 150);
                    brickRectangle.setTranslateY(i * 60 + 40);

                    makeDraggable(brickRectangle);
                    resizeBrickRectangle(brickRectangle, ToSize.SMALL);
                    index++;
                } else {
                    break;
                }

            }
        }
    }

    private Path createGameFieldPath() {

        Path gameFieldPath = new Path();
        int shift;
        for (int i = 0; i < 5; i++) {
            shift = 10 + 100 * i;
            gameFieldPath.getElements().add(new MoveTo(shift, 10));
            gameFieldPath.getElements().add(new LineTo(shift, 410));
        }
        for (int i = 0; i < 5; i++) {
            shift = 10 + 100 * i;
            gameFieldPath.getElements().add(new MoveTo(10, shift));
            gameFieldPath.getElements().add(new LineTo(410, shift));
        }
        return gameFieldPath;

    }

    public List<BrickRectangle> getBrickRectangleList() {

        List<BrickRectangle> brickRectangleList = new ArrayList<>();
        Random random = new Random();
        int sumArea = 0;
        int type;
        while (sumArea <= 13) {
            type = random.nextInt(2) + 1;
            switch (type) {
                case 1:
                    brickRectangleList.add(new BrickRectangle(BrickType.HORIZONTAL));
                    sumArea += 2;
                    break;
                case 2:
                    brickRectangleList.add(new BrickRectangle(BrickType.VERTICAL));
                    sumArea += 2;
                    break;
            }
        }
        return brickRectangleList;
    }

    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;

    private void makeDraggable(BrickRectangle brickRectangle) {

        brickRectangle.setOnMousePressed(e -> {

            if (brickRectangle.isMovable() & isPlayersTurn) {
                resizeBrickRectangle(brickRectangle, ToSize.BIG);

                orgSceneX = e.getSceneX();
                orgSceneY = e.getSceneY();
                orgTranslateX = ((BrickRectangle) (e.getSource())).getTranslateX();
                orgTranslateY = ((BrickRectangle) (e.getSource())).getTranslateY();
            }
        });

        brickRectangle.setOnMouseDragged(e -> {

            if (brickRectangle.isMovable() & isPlayersTurn) {
                double offsetX = e.getSceneX() - orgSceneX;
                double offsetY = e.getSceneY() - orgSceneY;
                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;

                ((BrickRectangle) (e.getSource())).setTranslateX(newTranslateX);
                ((BrickRectangle) (e.getSource())).setTranslateY(newTranslateY);
            }
        });

        brickRectangle.setOnMouseReleased(e -> {

            try {
                if (brickRectangle.isMovable() & isPlayersTurn) {
                    AttachPoint attachPoint = gameControl.checkIsBrickOnAttachPoint(brickRectangle, attachPointList);
                    gameControl.checkIsBrickPlacable(brickRectangle, attachPoint);
                    moveBrickRectangle(brickRectangle, attachPoint);
                    brickRectangleList.remove(brickRectangle);
                    attachPointList = gameControl.updateAttachPointList(attachPoint, brickRectangle, attachPointList);

                    GameState currentGameState = new GameState(attachPointList, brickRectangleList);
                    currentGameState.setUsedAttachPoint(attachPoint);
                    currentGameState.setUsedBrickRectangle(brickRectangle);
                    for (Node child : currentNode.getChildren()) {
                        if (Utils.gameStatesAreEqual(child.getGameState(), currentGameState)) {
                            child.setGameState(currentGameState);
                            currentNode = child;
                            break;
                        }
                    }
                    isPlayersTurn = false;
                    playGame();

                }

            } catch (Exception ex) {
                brickRectangle.setTranslateX(orgTranslateX);
                brickRectangle.setTranslateY(orgTranslateY);
                resizeBrickRectangle(brickRectangle, ToSize.SMALL);
            }
        });
    }

    private void resizeBrickRectangle(BrickRectangle brickRectangle, ToSize toSize) {
        if (toSize == ToSize.BIG) {
            brickRectangle.setWidth(brickRectangle.getWidth() * 4);
            brickRectangle.setHeight(brickRectangle.getHeight() * 4);
        } else {
            brickRectangle.setWidth(brickRectangle.getWidth() / 4);
            brickRectangle.setHeight(brickRectangle.getHeight() / 4);
        }
    }

    private void finishGame() {
        System.out.println("finish");


    }

    private void moveBrickRectangle(BrickRectangle brickRectangle, AttachPoint attachPoint) {

        if (brickRectangle.getBrickType() == BrickType.HORIZONTAL) {
            brickRectangle.setTranslateX(attachPoint.getX() + 100);
        } else {
            brickRectangle.setTranslateX(attachPoint.getX());
            System.out.println(attachPoint.getX());
        }
        brickRectangle.setTranslateY(attachPoint.getY());
        System.out.println(attachPoint.getY());
        brickRectangle.setMovable(false);
    }


    private void createListOfAttachPoints() {

        attachPointList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                attachPointList.add(new AttachPoint(i * 4 + j
                        , -589 + j * 100
                        , 10 + i * 100
                        , j != 3
                        , i != 3));
            }
        }
    }
}