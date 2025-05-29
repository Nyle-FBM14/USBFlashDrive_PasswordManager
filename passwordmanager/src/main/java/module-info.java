module com.fortpass {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.xml;

    opens com.passfort to javafx.fxml;
    exports com.passfort;
    opens com.passfort.controllers to javafx.fxml;
    exports com.passfort.controllers;
    opens com.passfort.models to javafx.fxml;
    exports com.passfort.models;
}
