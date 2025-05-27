package com.passfort.controllers;

import javax.crypto.SecretKey;

import com.passfort.models.Credential;
import com.passfort.models.Cryptography;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class CredentialCardController {

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField tfEmailLinked;

    @FXML
    private TextField tfService;

    @FXML
    private TextField tfUsername;

    private Credential credential;
    public void setData(Credential c, SecretKey key) {
        tfService.setText(c.getService(key));
        tfUsername.setText(c.getUsername(key));
        pfPassword.setText(c.getPassword(key));
        tfEmailLinked.setText(c.getEmailLinked(key));
        this.credential = c;
    }

    @FXML
    void copyEmailLinked(MouseEvent event) {

    }

    @FXML
    void copyPassword(MouseEvent event) {

    }

    @FXML
    void copyUsername(MouseEvent event) {

    }

    @FXML
    void deleteCredential(MouseEvent event) {

    }

    @FXML
    void editCredential(MouseEvent event) {

    }

    @FXML
    void showPassword(MouseEvent event) {

    }

}
