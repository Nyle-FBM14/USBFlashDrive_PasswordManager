package com.passfort.controllers;

import com.passfort.models.Cryptography;
import com.passfort.models.PasswordManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewCredentialFormController {

    @FXML
    private Slider passwordLength;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField tfEmailLinked;

    @FXML
    private TextField tfService;

    @FXML
    private TextField tfUsername;

    private PasswordManager pm = PasswordManager.getPasswordManagerInstance();
    @FXML
    void cancel(ActionEvent event) {
        Stage form = (Stage) tfUsername.getScene().getWindow();
        form.close();
    }

    @FXML
    void confirm(ActionEvent event) {
        pm.addCredential(tfService.getText(), tfUsername.getText(), pfPassword.getText(), tfEmailLinked.getText());
        Stage form = (Stage) tfUsername.getScene().getWindow();
        form.close();
    }

    @FXML
    void generateHash(ActionEvent event) {
        pfPassword.setText(Cryptography.customHash(pfPassword.getText(), (byte) passwordLength.getValue()));
    }

    @FXML
    void generatePassword(ActionEvent event) {
        pfPassword.setText(pm.generatePassword((int) passwordLength.getValue()));
    }

}