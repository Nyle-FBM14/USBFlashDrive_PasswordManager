module com.fortpass {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.xml;

    opens com.fortpass to javafx.fxml;
    exports com.fortpass;
    opens com.fortpass.controllers to javafx.fxml;
    exports com.fortpass.controllers;
    opens com.fortpass.models to javafx.fxml;
    exports com.fortpass.models;
}
