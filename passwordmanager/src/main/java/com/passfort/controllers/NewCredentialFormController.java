package com.passfort.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class NewCredentialFormController {

    @FXML
    private Slider hashLength;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField tfEmailLinked;

    @FXML
    private TextField tfService;

    @FXML
    private VBox tfUsername;

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void confirm(ActionEvent event) {

    }

    @FXML
    void generateHash(ActionEvent event) {

    }

    @FXML
    void generatePassword(ActionEvent event) {

    }

}
