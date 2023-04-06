package com.example.bricksgame;

import com.example.bricksgame.data.AttachPoint;
import com.example.bricksgame.data.BrickRectangle;
import com.example.bricksgame.data.BrickType;
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


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        createListOfAttachPoints();
        primaryStage.setScene(new Scene(createContent(), 1200, 420));
        primaryStage.show();
        playGame();
    }

    private void playGame() {

        checkState();

    }

    private void checkState() {

        List<BrickType> leftBrickTypeList = brickRectangleList
                .stream()
                .map(BrickRectangle::getBrickType)
                .distinct()
                .collect(Collectors.toList());
        if (leftBrickTypeList.isEmpty()) {
            isPlayable = false;
        } else {
            checkForPossibleMove(leftBrickTypeList);
        }
    }

    private void checkForPossibleMove(List<BrickType> leftBrickTypes) {

        List<BrickRectangle> leftBrickRectangles = leftBrickTypes
                .stream()
                .map(BrickRectangle::new)
                .collect(Collectors.toList());
        for (AttachPoint attachPoint : attachPointList) {
            for (BrickRectangle leftBrickRectangle : leftBrickRectangles) {
                try {
                    gameControl.checkIsBrickPlacable(leftBrickRectangle, attachPoint);
                    return;
                } catch (Exception ignored) {
                }
            }
        }
        isPlayable = false;
    }

    private Parent createContent() {

        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: rgb(249,166,72)");
        hBox.setSpacing(10);

        createStackPaneGameFieldAndBricksChoiceRectangle();
        hBox.getChildren().addAll(gameFieldAndBricksChoiceStackPane);

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

        brickRectangleList = getBrickRectangleList();
        placeBricksInChoiceRectangle();
        placeInitialSingleBrickRectanglesInGameField();
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
            gameControl.updateAttachPointList(attachPoint, singleBrickRectangle, attachPointList);
        }
    }

    private void placeBricksInChoiceRectangle() {

        ObservableList list = gameFieldAndBricksChoiceStackPane.getChildren();
        for (int i = 0; i < brickRectangleList.size() / 3; i++) {
            for (int j = 0; j < 3; j++) {
                BrickRectangle brickRectangle = brickRectangleList.get(i * 3 + j);

                list.add(brickRectangle);
                gameFieldAndBricksChoiceStackPane.setAlignment(brickRectangle, Pos.TOP_RIGHT);

                brickRectangle.setWidth(brickRectangle.getWidth() / 4);
                brickRectangle.setHeight(brickRectangle.getHeight() / 4);

                brickRectangle.setTranslateX(j * 60 - 150);
                brickRectangle.setTranslateY(i * 60 + 40);
                makeDraggable(brickRectangle);
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
                brickRectangle.setWidth(brickRectangle.getWidth() * 4);
                brickRectangle.setHeight(brickRectangle.getHeight() * 4);

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
                    gameControl.updateAttachPointList(attachPoint, brickRectangle, attachPointList);
                    checkState();
                    if (!isPlayable){
                        finishGame();
                    }
                    isPlayersTurn = false;

                }

            } catch (Exception ex) {
                brickRectangle.setTranslateX(orgTranslateX);
                brickRectangle.setTranslateY(orgTranslateY);
                brickRectangle.setWidth(brickRectangle.getWidth() / 4);
                brickRectangle.setHeight(brickRectangle.getHeight() / 4);
            }
        });
    }

    private void finishGame() {

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