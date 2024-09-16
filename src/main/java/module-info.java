module stinglamade.midiorchestra {
    requires javafx.controls;
    requires javafx.fxml;


    opens stinglamade.midiorchestra to javafx.fxml;
    exports stinglamade.midiorchestra;
}