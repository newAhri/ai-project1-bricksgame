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
import java.util.stream.IntStream;

public class Main extends Application {

    private List<AttachPoint> attachPointList;
    private List<BrickRectangle> brickRectangleList;
    private StackPane gameFieldAndBricksChoiceStackPane;


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        createListOfAttachPoints();
        primaryStage.setScene(new Scene(createContent(), 1200, 420));
        primaryStage.show();
    }

    private Parent createContent() {

        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: rgb(249,166,72)");
        hBox.setSpacing(10);

        createStackPaneGameFieldAndBricksChoiceRectangle();

        hBox.getChildren().addAll(gameFieldAndBricksChoiceStackPane);


        /*hBox.setOnMouseClicked(e -> {
            System.out.println("hbox" + e.getSceneX() + " " + e.getSceneY());
        });
        gameFieldAndBricksChoiceStackPane.setOnMouseClicked(e -> {
            System.out.println("stackpane" + e.getSceneX() + " " + e.getSceneY());
        });*/

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

        for (int i = 0; i < singleBricksAmount; i++){
            int index = indexList.get(i);
            BrickRectangle singleBrickRectangle = new BrickRectangle(BrickType.SINGLE);
            AttachPoint attachPoint = attachPointList.get(index);
            gameFieldAndBricksChoiceStackPane.getChildren().add(singleBrickRectangle);
            gameFieldAndBricksChoiceStackPane.setAlignment(singleBrickRectangle, Pos.TOP_RIGHT);
            moveBrickRectangle(singleBrickRectangle, attachPoint);
            updateAttachPointList(attachPoint, singleBrickRectangle);
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
            System.out.println(e.getSceneX() + " " + e.getSceneY());
            System.out.println(e.getScreenX() + " " + e.getScreenY());


            if (brickRectangle.isMovable()) {
                brickRectangle.setWidth(brickRectangle.getWidth() * 4);
                brickRectangle.setHeight(brickRectangle.getHeight() * 4);

                orgSceneX = e.getSceneX();
                orgSceneY = e.getSceneY();
                orgTranslateX = ((BrickRectangle) (e.getSource())).getTranslateX();
                orgTranslateY = ((BrickRectangle) (e.getSource())).getTranslateY();
                System.out.println("mouse pressed: orgTranslateX" + orgTranslateX + "   orgTranslateY" + orgTranslateY);


            }
        });

        brickRectangle.setOnMouseDragged(e -> {
            if (brickRectangle.isMovable()) {
                double offsetX = e.getSceneX() - orgSceneX;
                double offsetY = e.getSceneY() - orgSceneY;
                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;

                ((BrickRectangle) (e.getSource())).setTranslateX(newTranslateX);
                ((BrickRectangle) (e.getSource())).setTranslateY(newTranslateY);

                System.out.println("mouse drag: offset" + offsetX + "   orgy" + offsetY);
                System.out.println("               transx" + newTranslateX + "   transy" + newTranslateY);

            }
        });
        brickRectangle.setOnMouseReleased(e -> {
            try {
                if (brickRectangle.isMovable()) {

                    /*System.out.println("mouse pressed: orgx" + orgTranslateX + "   orgy" + orgTranslateX);
                    System.out.println("               scenex" + e.getSceneX() + "   sceney" + e.getSceneY());
                    System.out.println("               transx" + brickRectangle.getTranslateX() + "   transy" + brickRectangle.getTranslateY());
                    System.out.println("               layoutx" + brickRectangle.getLayoutX() + "   layouty" + brickRectangle.getLayoutY());*/


                    AttachPoint attachPoint = checkIsBrickOnAttachPoint(brickRectangle);
                    checkIsBrickPlacable(brickRectangle, attachPoint);
                    checkIsAttachPointFree(attachPoint);
                    moveBrickRectangle(brickRectangle, attachPoint);
                    updateAttachPointList(attachPoint, brickRectangle);


                }

            } catch (Exception ex) {
                brickRectangle.setTranslateX(orgTranslateX);
                brickRectangle.setTranslateY(orgTranslateY);
                brickRectangle.setWidth(brickRectangle.getWidth() / 4);
                brickRectangle.setHeight(brickRectangle.getHeight() / 4);
            }
        });

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

    private void updateAttachPointList(AttachPoint busyAttachPoint, BrickRectangle brickRectangle) {

        busyAttachPoint.setFree(false);
        attachPointList.set(attachPointList.indexOf(busyAttachPoint), busyAttachPoint);

        if (brickRectangle.getBrickType() != BrickType.SINGLE) {
            AttachPoint neighbourBusyAttachPoint = new AttachPoint();

            if (brickRectangle.getBrickType() == BrickType.HORIZONTAL) {
                neighbourBusyAttachPoint = attachPointList.get(busyAttachPoint.getIndex() + 1);
            } else if (brickRectangle.getBrickType() == BrickType.VERTICAL) {
                neighbourBusyAttachPoint = attachPointList.get(busyAttachPoint.getIndex() + 4);
            }
            neighbourBusyAttachPoint.setFree(false);
            attachPointList.set(neighbourBusyAttachPoint.getIndex(), neighbourBusyAttachPoint);
        }


    }


    private void createListOfAttachPoints() {

        attachPointList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                attachPointList.add(new AttachPoint(i * 4 + j, -589 + j * 100, 10 + i * 100, j != 3, i != 3));
            }
        }
    }

    private AttachPoint checkIsBrickOnAttachPoint(BrickRectangle brickRectangle) throws Exception {

        double recX;
        if (brickRectangle.getBrickType() == BrickType.HORIZONTAL) {
            recX = brickRectangle.getTranslateX() - 100;
        } else {
            recX = brickRectangle.getTranslateX();
        }
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