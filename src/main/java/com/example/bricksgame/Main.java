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

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(createContent(), 1200, 500));
        primaryStage.show();
    }

    private Parent createContent() {

        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: rgb(249,166,72)");
        hBox.setSpacing(10);

        StackPane gameFieldAndBricksChoiceStackPane = createStackPaneGameFieldAndBricksChoice();

        hBox.getChildren().addAll(gameFieldAndBricksChoiceStackPane);

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
                makeDraggable(rec);
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

    private void makeDraggable(Node node) {
        node.setOnMouseClicked(e -> {
            startX = e.getSceneX() - node.getTranslateX();
            startY = e.getSceneY() - node.getTranslateY();

        });

        node.setOnMouseDragged(e -> {
            node.setTranslateX(e.getSceneX() - startX);
            node.setTranslateY(e.getSceneY() - startY);
        });

    }


}