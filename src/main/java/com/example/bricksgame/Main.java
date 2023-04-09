package com.example.bricksgame;

import com.example.bricksgame.data.*;
import com.example.bricksgame.enums.BrickType;
import com.example.bricksgame.enums.ToSize;
import com.example.bricksgame.tree.Node;
import com.example.bricksgame.tree.Tree;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main extends Application {

    private StackPane gameFieldAndBricksChoiceStackPane;
    private Stage primaryStage;
    private HBox hBox;
    private VBox statsVBox;
    private List<AttachPoint> attachPointList;
    private List<BrickRectangle> brickRectangleList;
    private Node currentNode;
    private boolean isPlayersTurn = true;
    private boolean isPlayable = true;
    private final MiniMax miniMax = new MiniMax();
    private final GameControl gameControl = new GameControl();
    Random random = new Random();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {

        primaryStage = stage;
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(createStartMenu(), 420, 420));
        primaryStage.show();

    }

    public void startGame() {
        primaryStage.close();
        primaryStage.setScene(new Scene(createContent(), 1200, 420));
        primaryStage.centerOnScreen();

        Tree tree = miniMax.getTree(new GameState(new ArrayList<>(attachPointList), brickRectangleList));
        currentNode = tree.getRoot();
        primaryStage.show();
        if (!isPlayersTurn) {
            computerMove();
        }
    }

    private Parent createContent() {

        hBox = new HBox();
        hBox.setSpacing(10);
        Image image = new Image(getClass().getResourceAsStream("/pictures/brickWall.png"));

        BackgroundImage bImg = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Background bGround = new Background(bImg);
        hBox.setBackground(bGround);

        createStackPaneGameFieldAndBricksChoiceRectangle();
        hBox.getChildren().addAll(gameFieldAndBricksChoiceStackPane);

        brickRectangleList = getBrickRectangleList();

        createListOfAttachPoints();
        placeBricksInChoiceRectangle();
        placeInitialSingleBrickRectanglesInGameField();
        placeStatsPane();

        return hBox;
    }

    private Parent createStartMenu() {

        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: rgb(0, 185, 185)");
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        Image image = new Image(getClass().getResourceAsStream("/pictures/bricksTitle.png"));
        ImageView title = new ImageView(image);

        vBox.getChildren().addAll(title);
        vBox = (VBox) populatePaneWithStartSettings(vBox);

        return vBox;
    }

    private Pane populatePaneWithStartSettings(Pane pane) {

        Text choiceText = new Text("First starts:");
        choiceText.setFont(Font.font("monospace", FontWeight.BOLD, 20));

        ToggleGroup choiceGroup = new ToggleGroup();
        RadioButton playerRadio = new RadioButton("Player");
        playerRadio.setToggleGroup(choiceGroup);
        RadioButton computerRadio = new RadioButton("Computer");
        computerRadio.setToggleGroup(choiceGroup);

        Button playButton = new Button("Start");
        playButton.setOnAction(e -> {
            if (computerRadio.isSelected()) {
                isPlayersTurn = false;
                isPlayable = true;
                startGame();
            } else if (playerRadio.isSelected()) {
                isPlayersTurn = true;
                isPlayable = true;
                startGame();
            }

        });
        playButton.setMaxSize(100, 200);

        pane.getChildren().addAll(choiceText, playerRadio, computerRadio, playButton);
        return pane;
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

    private void placeStatsPane() {
        statsVBox = new VBox();
        statsVBox.setAlignment(Pos.CENTER);
        statsVBox.setSpacing(10);
        statsVBox.setPadding(new Insets(150, 150, 100, 150));
        hBox.getChildren().addAll(statsVBox);
    }

    private void setWinTextOnFinish() {

        Text youWinText = new Text("YOU WIN!");
        youWinText.setFont(Font.font("monospace", FontWeight.BOLD, 30));
        youWinText.setFill(Color.rgb(36, 78, 146));

        Text youLoseText = new Text("you lose...");
        youLoseText.setFont(Font.font("monospace", FontWeight.BOLD, 30));
        youLoseText.setFill(Color.rgb(146, 36, 36));

        ObservableList list = statsVBox.getChildren();


        if (isPlayersTurn) {
            list.add(youLoseText);
        } else {
            list.add(youWinText);
        }

    }

    private void createStackPaneGameFieldAndBricksChoiceRectangle() {

        gameFieldAndBricksChoiceStackPane = new StackPane();
        gameFieldAndBricksChoiceStackPane.setPrefSize(700, 500);

        Path gameFieldPath = createGameFieldPath();

        Rectangle gameFieldRectangle = new Rectangle(10, 10, 400, 400);
        Rectangle bricksChoiceRectangle = new Rectangle(10, 10, 200, 400);
        gameFieldRectangle.setFill(Color.WHITE);
        bricksChoiceRectangle.setFill(Color.WHITE);
        bricksChoiceRectangle.setStroke(Color.BLACK);

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

        int singleBricksAmount = 2;
        List<Integer> indexList = getListOfRandomIndexes(singleBricksAmount);

        for (int i = 0; i < singleBricksAmount; i++) {
            int index = indexList.get(i);
            BrickRectangle singleBrickRectangle = new BrickRectangle(BrickType.SINGLE);
            AttachPoint attachPoint = attachPointList.get(index);
            gameFieldAndBricksChoiceStackPane.getChildren().add(singleBrickRectangle);
            gameFieldAndBricksChoiceStackPane.setAlignment(singleBrickRectangle, Pos.TOP_RIGHT);
            moveBrickRectangle(singleBrickRectangle, attachPoint);
            attachPointList = gameControl.updateAttachPointList(attachPoint, singleBrickRectangle, attachPointList);
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

    public List<BrickRectangle> getBrickRectangleList() {

        List<BrickRectangle> brickRectangleList = new ArrayList<>();
        int sumArea = 0;
        int type;
        while (sumArea < 14) {
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

    private void computerMove() {

        checkState();
        if (!isPlayable) {
            finishGame();
            return;
        }

        List<Node> childrenNodes = currentNode.getChildren();
        currentNode = getBestChildNodeByScore(childrenNodes);
        BrickRectangle brickRectangle = getBrickRectangleForMove();
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

    private void finishGame() {

        setWinTextOnFinish();

        Text replayText = new Text("Replay");
        replayText.setFont(Font.font("monospace", FontWeight.BOLD, 25));
        statsVBox.getChildren().addAll(replayText);
        statsVBox = (VBox) populatePaneWithStartSettings(statsVBox);
        statsVBox.setAlignment(Pos.CENTER);
        statsVBox.setSpacing(10);
        statsVBox.setPadding(new Insets(150, 140, 90, 150));

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
                    computerMove();

                }

            } catch (Exception ex) {
                brickRectangle.setTranslateX(orgTranslateX);
                brickRectangle.setTranslateY(orgTranslateY);
                resizeBrickRectangle(brickRectangle, ToSize.SMALL);
            }
        });
    }

    private List<Integer> getListOfRandomIndexes(int singleBricksAmount){
        List<Integer> indexList = new ArrayList<>(singleBricksAmount);
        IntStream.generate(() -> random.nextInt(16))
                .distinct()
                .limit(singleBricksAmount)
                .forEach(indexList::add);

        return indexList;
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

    private void moveBrickRectangle(BrickRectangle brickRectangle, AttachPoint attachPoint) {

        if (brickRectangle.getBrickType() == BrickType.HORIZONTAL) {
            brickRectangle.setTranslateX(attachPoint.getX() + 100);
        } else {
            brickRectangle.setTranslateX(attachPoint.getX());
        }
        brickRectangle.setTranslateY(attachPoint.getY());
        brickRectangle.setMovable(false);
    }

    private BrickRectangle getBrickRectangleForMove(){

        BrickType brickRectangleType = currentNode
                .getGameState()
                .getUsedBrickRectangle()
                .getBrickType();

        return brickRectangleList.stream()
                .filter(brick -> brick.getBrickType() == brickRectangleType)
                .findFirst()
                .get();
    }

    private Node getBestChildNodeByScore(List<Node> childrenNodes){

        Comparator<Node> byScoreComparator = Comparator.comparing(Node::getScore);
        return childrenNodes.stream()
                .max(currentNode.isMaxPlayer() ? byScoreComparator.reversed() : byScoreComparator)
                .orElseThrow(NoSuchElementException::new);
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