package com.example.bricksgame;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(createContent(), 800, 500));
        primaryStage.show();
    }

    private Parent createContent() {
        Path gameFieldPath = createGameFieldPath();

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setMargin(gameFieldPath, new Insets(10, 10, 10, 10));

        ObservableList list = hBox.getChildren();

        list.add(gameFieldPath);

        return hBox;
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


}