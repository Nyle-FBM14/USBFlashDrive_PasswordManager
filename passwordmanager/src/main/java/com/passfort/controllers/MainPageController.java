package com.passfort.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.crypto.SecretKey;

import com.passfort.models.Credential;
import com.passfort.models.Cryptography;

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

    //test data
    ArrayList<Credential> credentials = new ArrayList<>();
    SecretKey key = Cryptography.generateKey("nyle", "pass");
    void fillCreds() {
        byte[] a = Cryptography.encrypt("Amazon", key, "AES");
        byte[] b = Cryptography.encrypt("Nyle", key, "AES");
        byte[] c = Cryptography.encrypt("password", key, "AES");
        byte[] d = Cryptography.encrypt("nyle@fbm.com", key, "AES");
        Credential cred = new Credential(a, b, c, d);

        credentials.add(cred);
        credentials.add(cred);
        credentials.add(cred);
        credentials.add(cred);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillCreds();
        //int i = 0;
        for(Credential c : credentials) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/passfort/views/credentialCard.fxml"));
            try {
                GridPane gridPane = fxmlLoader.load();
                //gridPane.setId(Integer.toString(i));
                CredentialCardController credentialCardController = fxmlLoader.getController();
                credentialCardController.setData(c, key);
                vboxMain.getChildren().add(gridPane);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //i++;
        }
    }
}
