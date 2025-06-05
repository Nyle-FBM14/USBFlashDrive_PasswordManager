package com.passfort.controllers;

import com.passfort.App;
import com.passfort.models.PasswordManager;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class AccountPageController {

    @FXML
    private Label labelMessage;
    
    @FXML
    private TextField tfNewPassword;

    @FXML
    private TextField tfOldPassword;

    @FXML
    private TextField tfPassword;

    private PasswordManager pm = PasswordManager.getPasswordManagerInstance();

    private void showMessage(String message) {
        PauseTransition pt = new PauseTransition(Duration.seconds(2));
        pt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                labelMessage.setVisible(false);
            }
        });

        labelMessage.setText(message);
        labelMessage.setVisible(true);
        pt.play();
    }

    @FXML
    void backToMainPage(ActionEvent event) {
        App.setRoot("mainPage");
    }

    @FXML
    void changePassword(ActionEvent event) {
        if(pm.changePassword(tfOldPassword.getText(), tfNewPassword.getText())) {
            showMessage("Password changed successfully.");
        }
        else {
            showMessage("Password not changed.");
        }
    }

    @FXML
    void deleteAccount(ActionEvent event) {
        if(pm.deleteAccount(tfPassword.getText())) {
            showMessage("Account deleted successfully.");
            App.setRoot("loginPage");
        }
        else {
            showMessage("Account not deleted.");
        }
    }

}
