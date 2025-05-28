package com.passfort.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.crypto.SecretKey;

import com.passfort.models.Credential;
import com.passfort.models.Cryptography;
import com.passfort.models.PasswordManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainPageController implements Initializable {

    @FXML
    private TextField hashInput;

    @FXML
    private Slider hashLength;

    @FXML
    private TextField hashOutput;

    @FXML
    private VBox vboxMain;

    @FXML
    void computeHash(ActionEvent event) {
        hashOutput.setText(Cryptography.customHash(hashInput.getText(), (byte) hashLength.getValue()));
    }

    @FXML
    void open2faMenu(MouseEvent event) {
        System.out.println("To be implemented.");
    }

    @FXML
    void openVault(MouseEvent event) {
        System.out.println("To be implemented.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PasswordManager pm = PasswordManager.getPasswordManagerInstance();
        pm.testData();
        ArrayList<Credential> credentials = pm.getCredentials();
        for(Credential c : credentials) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/passfort/views/credentialCard.fxml"));
            try {
                GridPane gridPane = fxmlLoader.load();
                CredentialCardController credentialCardController = fxmlLoader.getController();
                credentialCardController.setData(c, pm.getKey());
                vboxMain.getChildren().add(gridPane);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
