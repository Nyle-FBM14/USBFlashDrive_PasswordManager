module com.fortpass {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.fortpass to javafx.fxml;
    exports com.fortpass;
    opens com.fortpass.controllers to javafx.fxml;
    exports com.fortpass.controllers;
}
