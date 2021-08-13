module shianasoftwareoneproject {
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;

    opens model;
    opens view;
    opens controller;
    opens mainpackage;

}