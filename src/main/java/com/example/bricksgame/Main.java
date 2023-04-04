package com.example.bricksgame;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {
    private List<AttachPoint> attachPointList;
    private List<BrickRectangle> brickRectangleList;


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(createContent(), 1200, 420));
        primaryStage.show();
        createListOfAttachPoints();
    }

    private Parent createContent() {

        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: rgb(249,166,72)");
        hBox.setSpacing(10);

        StackPane gameFieldAndBricksChoiceStackPane = createStackPaneGameFieldAndBricksChoice();

        hBox.getChildren().addAll(gameFieldAndBricksChoiceStackPane);


        /*hBox.setOnMouseClicked(e -> {
            System.out.println("hbox" + e.getSceneX() + " " + e.getSceneY());
        });
        gameFieldAndBricksChoiceStackPane.setOnMouseClicked(e -> {
            System.out.println("stackpane" + e.getSceneX() + " " + e.getSceneY());
        });*/

        return hBox;
    }

    private StackPane createStackPaneGameFieldAndBricksChoice() {

        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(700, 500);

        Path gameFieldPath = createGameFieldPath();

        Rectangle gameFieldRectangle = new Rectangle(10, 10, 400, 400);
        Rectangle bricksChoiceRectangle = new Rectangle(10, 10, 200, 400);
        gameFieldRectangle.setFill(Color.WHITE);
        bricksChoiceRectangle.setFill(Color.WHITE);

        stackPane.setMargin(gameFieldRectangle, new Insets(10, 10, 10, 10));
        stackPane.setAlignment(gameFieldRectangle, Pos.CENTER_LEFT);
        stackPane.setMargin(gameFieldPath, new Insets(10, 10, 10, 10));
        stackPane.setAlignment(gameFieldPath, Pos.CENTER_LEFT);
        stackPane.setMargin(bricksChoiceRectangle, new Insets(10, 10, 10, 10));
        stackPane.setAlignment(bricksChoiceRectangle, Pos.CENTER_RIGHT);

        ObservableList list = stackPane.getChildren();


        list.addAll(gameFieldRectangle, gameFieldPath, bricksChoiceRectangle);

        brickRectangleList = getBrickRectangleList();

        for (int i = 0; i < brickRectangleList.size() / 3; i++) {
            for (int j = 0; j < 3; j++) {
                BrickRectangle brickRectangle = brickRectangleList.get(i * 3 + j);

                list.add(brickRectangle);
                stackPane.setAlignment(brickRectangle, Pos.TOP_RIGHT);
                // stackPane.setMargin(rec, new Insets(10, 10, 10, 10));

                brickRectangle.setWidth(brickRectangle.getWidth() / 4);
                brickRectangle.setHeight(brickRectangle.getHeight() / 4);

                brickRectangle.setTranslateX(j * 60 - 140);
                brickRectangle.setTranslateY(i * 60 + 60);
                makeDraggable(brickRectangle);
            }


        }

        return stackPane;
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
        while (sumArea <= 16) {
            type = random.nextInt(4) + 1;
            switch (type) {
                case 1:
                    brickRectangleList.add(new BrickRectangle(BrickType.HORIZONTAL));
                    sumArea += 2;
                    break;
                case 2:
                    brickRectangleList.add(new BrickRectangle(BrickType.VERTICAL));
                    sumArea += 2;
                    break;
                case 3:
                    brickRectangleList.add(new BrickRectangle(BrickType.SINGLE));
                    sumArea += 1;
                    break;
            }
        }
        return brickRectangleList;
    }

    private double startX;
    private double startY;

    /*private void makeDraggable(Node node) {
        node.setOnMousePressed(e -> {
            startX = e.getSceneX();
            startY = e.getSceneY();

        });

        node.setOnMouseDragged(e -> {
            node.setLayoutX(e.getSceneX() - startX);
            node.setLayoutY(e.getSceneY() - startY);
        });

    }*/

    private void makeDraggable(BrickRectangle brickRectangle) {

        brickRectangle.setOnMousePressed(e -> {
            System.out.println(e.getSceneX() + " " + e.getSceneY());
            System.out.println(e.getScreenX() + " " + e.getScreenY());

            
            if (brickRectangle.isMovable()) {
                brickRectangle.setWidth(brickRectangle.getWidth() * 4);
                brickRectangle.setHeight(brickRectangle.getHeight() * 4);
                startX = e.getSceneX() - brickRectangle.getTranslateX();
                startY = e.getSceneY() - brickRectangle.getTranslateY();


                System.out.println("mouse pressed: startx" + startX + "   starty" + startY);
                System.out.println("               scenex" + e.getSceneX() + "   sceney" + e.getSceneY());
                System.out.println("               transx" + brickRectangle.getTranslateX() + "   transy" + brickRectangle.getTranslateY());
                System.out.println("               layoutx" + brickRectangle.getLayoutX() + "   layouty" + brickRectangle.getLayoutY());
            }
        });

        brickRectangle.setOnMouseDragged(e -> {
            if (brickRectangle.isMovable()) {
                brickRectangle.setTranslateX(e.getSceneX() - startX);
                brickRectangle.setTranslateY(e.getSceneY() - startY);
            }
        });
        brickRectangle.setOnMouseReleased(e -> {
            try {
                if (brickRectangle.isMovable()) {
                    AttachPoint attachPoint = checkIsBrickOnAttachPoint(brickRectangle);
                    checkIsBrickPlacable(brickRectangle, attachPoint);
                    checkIsAttachPointFree(attachPoint);
                    brickRectangle.setTranslateX(attachPoint.getX());
                    brickRectangle.setTranslateY(attachPoint.getY());
                    brickRectangle.setMovable(false);
                    AttachPoint busyAttachPoint = attachPoint;
                    busyAttachPoint.setFree(false);
                    attachPointList.set(attachPointList.indexOf(attachPoint), busyAttachPoint);
                }

            } catch (Exception ex) {
                brickRectangle.setTranslateX(startX);
                brickRectangle.setTranslateY(startY);
                brickRectangle.setWidth(brickRectangle.getWidth() / 4);
                brickRectangle.setHeight(brickRectangle.getHeight() / 4);
            }
        });

    }


    private void createListOfAttachPoints() {
        attachPointList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                attachPointList.add(new AttachPoint(10 + j * 100, 10 + i * 100, j != 3, i != 3));
            }
        }
    }

    private AttachPoint checkIsBrickOnAttachPoint(BrickRectangle brickRectangle) throws Exception {

        double recX = brickRectangle.getTranslateX();
        double recY = brickRectangle.getTranslateY();
        double pointX, pointY;
        boolean attachPointIsFound = false;
        AttachPoint currentAttachPoint = new AttachPoint();

        for (int i = 0; i < attachPointList.size(); i++) {
            currentAttachPoint = attachPointList.get(i);
            pointX = currentAttachPoint.getX();
            pointY = currentAttachPoint.getY();
            boolean rectangleInAttachArea = (recX - pointX) * (recX - pointX) + (recY - pointY) * (recY - pointY) < 50 * 50;
            if (rectangleInAttachArea) {
                attachPointIsFound = true;
                break;
            }
        }
        if (!attachPointIsFound) throw new Exception();
        return currentAttachPoint;
    }

    private void checkIsBrickPlacable(BrickRectangle brickRectangle, AttachPoint attachPoint) throws Exception {
        if (brickRectangle.getBrickType() == BrickType.SINGLE) return;
        boolean isHorizontalBrickPlacable = brickRectangle.getBrickType() == BrickType.HORIZONTAL & attachPoint.isHorizontalBrickPlacable();
        boolean isVerticalBrickPlacable = brickRectangle.getBrickType() == BrickType.VERTICAL & attachPoint.isVerticalBrickPlacable();
        if (isHorizontalBrickPlacable | isVerticalBrickPlacable) return;
        throw new Exception();
    }

    private void checkIsAttachPointFree(AttachPoint attachPoint) throws Exception {
        if (!attachPoint.isFree()) throw new Exception();
    }


}