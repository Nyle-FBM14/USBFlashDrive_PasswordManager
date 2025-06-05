package com.passfort.controllers;

import com.passfort.App;
import com.passfort.models.PasswordManager;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class LoginController {

    private PasswordManager pm = PasswordManager.getPasswordManagerInstance();

    @FXML
    private Label labelError;
    
    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField tfUsername;

    private void showErrorMessage(String message) {
        PauseTransition pt = new PauseTransition(Duration.seconds(2));
        pt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                labelError.setVisible(false);
            }
        });

        labelError.setText(message);
        labelError.setVisible(true);
        pt.play();
    }
    
    @FXML
    void createAccount(ActionEvent event) {
        if(pm.createAccount(tfUsername.getText(), pfPassword.getText())) {
            System.out.println("Account created.");
            App.setRoot("mainPage");
        }
        else {
            showErrorMessage("Choose a different username.");
        }
        
    }

    @FXML
    void login(ActionEvent event) {
        if(pm.login(tfUsername.getText(), pfPassword.getText())) {
            System.out.println("Login successful.");
            App.setRoot("mainPage");
        }
        else {
            showErrorMessage("User does not exist.");
        }
    }
}