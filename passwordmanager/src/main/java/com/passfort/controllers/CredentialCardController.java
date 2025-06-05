package com.passfort.controllers;

import javax.crypto.SecretKey;

import com.passfort.models.Credential;
import com.passfort.models.PasswordManager;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CredentialCardController {

    @FXML
    private PasswordField pfPassword;
    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfEmailLinked;

    @FXML
    private TextField tfService;

    @FXML
    private TextField tfUsername;

    private PasswordManager pm =  PasswordManager.getPasswordManagerInstance();
    private Credential credential;
    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent content = new ClipboardContent();
    private boolean showingPassword = false;

    public void setData(Credential c, SecretKey key) {
        tfService.setText(c.getService(key));
        tfUsername.setText(c.getUsername(key));
        pfPassword.setText(c.getPassword(key));
        tfPassword.setText(pfPassword.getText());
        tfEmailLinked.setText(c.getEmailLinked(key));
        this.credential = c;
    }

    @FXML
    void copyEmailLinked(MouseEvent event) {
        content.putString(tfEmailLinked.getText());
        clipboard.setContent(content);
    }

    @FXML
    void copyPassword(MouseEvent event) {
        content.putString(pfPassword.getText());
        clipboard.setContent(content);
    }

    @FXML
    void copyUsername(MouseEvent event) {
        content.putString(tfUsername.getText());
        clipboard.setContent(content);
    }

    @FXML
    void deleteCredential(MouseEvent event) {
        GridPane grid = (GridPane) ((Node) event.getSource()).getParent();
        VBox vbox = (VBox) grid.getParent();
        ObservableList<Node> credentials = vbox.getChildren();
        credentials.remove(grid);
        pm.deleteCredential(credential);
    }

    @FXML
    void editCredential(MouseEvent event) {
        System.out.println("Editable");
        if(tfService.isEditable()) {
            System.out.println("Confirming edit.");
            if(showingPassword) {
                pfPassword.setText(tfPassword.getText());
                pfPassword.toFront();
                showingPassword = false;
            }
            else {
                tfPassword.setText(pfPassword.getText());
            }
            pm.editCredential(tfService.getText(), tfUsername.getText(), pfPassword.getText(), tfEmailLinked.getText(), credential);
        }
        tfService.setEditable(!tfService.isEditable());
        tfUsername.setEditable(!tfUsername.isEditable());
        pfPassword.setEditable(!pfPassword.isEditable());
        tfPassword.setEditable(!tfPassword.isEditable());
        tfEmailLinked.setEditable(!tfEmailLinked.isEditable());
    }

    @FXML
    void showPassword(MouseEvent event) {
        //pm.seeData();
        if(showingPassword) {
            pfPassword.setText(tfPassword.getText());
            pfPassword.toFront();
        }
        else {
            tfPassword.setText(pfPassword.getText());
            tfPassword.toFront();
        }
        showingPassword = !showingPassword;
    }
}
