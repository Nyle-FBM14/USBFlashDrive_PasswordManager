package com.fortpass.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController {

    @FXML
    private PasswordField textfieldPassword;

    @FXML
    private TextField textfieldUsername;

    @FXML
    void enterKey(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            System.out.println("enter");
        }
        
    }

    @FXML
    void login(ActionEvent event) {
        System.out.println("click");
    }

}