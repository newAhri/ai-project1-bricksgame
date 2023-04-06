module com.example.bricksgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bricksgame to javafx.fxml;
    exports com.example.bricksgame;
    exports com.example.bricksgame.data;
    opens com.example.bricksgame.data to javafx.fxml;
}