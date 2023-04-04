package com.example.bricksgame;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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


        hBox.setOnMouseClicked(e -> {
            System.out.println(e.getSceneX() + " " + e.getSceneY());
        });

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

        List<Rectangle> brickList = getBrickRectangleList();

        for (int i = 0; i < brickList.size() / 3; i++) {
            for (int j = 0; j < 3; j++) {
                Rectangle rec = brickList.get(i * 3 + j);
                list.add(rec);
                stackPane.setAlignment(rec, Pos.TOP_RIGHT);
                stackPane.setMargin(rec, new Insets(10, 10, 10, 10));

                rec.setWidth(rec.getWidth() / 4);
                rec.setHeight(rec.getHeight() / 4);

                rec.setTranslateX(j * 60 - 140);
                rec.setTranslateY(i * 60 + 60);
                //makeResizeOnClick(rec);
                makeDraggable(rec);
            }


        }

        return stackPane;
    }

    private void makeResizeOnClick(Rectangle rectangle) {
        rectangle.setOnMouseClicked(e -> {
            rectangle.setWidth(rectangle.getWidth() * 4);
            rectangle.setHeight(rectangle.getHeight() * 4);
        });
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

    public List<Rectangle> getBrickRectangleList() {

        List<Rectangle> brickRectangleList = new ArrayList<>();
        Random random = new Random();
        int sumArea = 0;
        int type;
        while (sumArea <= 16) {
            type = random.nextInt(4) + 1;
            switch (type) {
                case 1:
                    brickRectangleList.add(new Rectangle(200, 100));
                    sumArea += 2;
                    break;
                case 2:
                    brickRectangleList.add(new Rectangle(100, 200));
                    sumArea += 2;
                    break;
                case 3:
                    brickRectangleList.add(new Rectangle(100, 100));
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

    private void makeDraggable(Rectangle rectangle) {
        rectangle.setOnMousePressed(e -> {
            System.out.println(e.getSceneX() + " " + e.getSceneY());
            rectangle.setWidth(rectangle.getWidth() * 4);
            rectangle.setHeight(rectangle.getHeight() * 4);
            startX = e.getSceneX() - rectangle.getTranslateX();
            startY = e.getSceneY() - rectangle.getTranslateY();
        });

        rectangle.setOnMouseDragged(e -> {
            rectangle.setTranslateX(e.getSceneX() - startX);
            rectangle.setTranslateY(e.getSceneY() - startY);
        });
        rectangle.setOnMouseReleased(e -> {
            try {
                AttachPoint attachPoint = checkIsBrickOnAttachPoint(rectangle);
                rectangle.setTranslateX(attachPoint.getX());
                rectangle.setTranslateY(attachPoint.getY());

            } catch (Exception ex) {
                rectangle.setTranslateX(startX);
                rectangle.setTranslateY(startY);
                rectangle.setWidth(rectangle.getWidth() / 4);
                rectangle.setHeight(rectangle.getHeight() / 4);
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

    private AttachPoint checkIsBrickOnAttachPoint(Rectangle rectangle) throws Exception {
        double recX = rectangle.getX();
        double recY = rectangle.getY();
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


}