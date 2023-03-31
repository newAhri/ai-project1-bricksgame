module com.example.bricksgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bricksgame to javafx.fxml;
    exports com.example.bricksgame;
}