package com.passfort.controllers;

import javax.crypto.SecretKey;

import com.passfort.models.Credential;
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
    private TextField tfEmailLinked;

    @FXML
    private TextField tfService;

    @FXML
    private TextField tfUsername;

    private Credential credential;
    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent content = new ClipboardContent();

    public void setData(Credential c, SecretKey key) {
        tfService.setText(c.getService(key));
        tfUsername.setText(c.getUsername(key));
        pfPassword.setText(c.getPassword(key));
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
        System.out.println(grid.getId());
        System.out.println(credentials.indexOf(grid));
        //System.out.println(credentials.get(Integer.parseInt(grid.getId())).equals(credentials.get(Integer.parseInt(grid.getId()) + 1)));
        System.out.println(credentials.get(0).equals(credentials.get(0)));
        System.out.println(credentials.get(0).equals(credentials.get(2)));
        System.out.println(credentials.get(0).equals(credentials.get(3)));
        System.out.println(credentials.get(1).equals(credentials.get(3)));
        System.out.println(credentials.get(2).equals(credentials.get(2)));
        System.out.println("here");
        System.out.println(credentials.remove(grid));
    }

    @FXML
    void editCredential(MouseEvent event) {
        System.out.println("Editable");
    }

    @FXML
    void showPassword(MouseEvent event) {
        
    }

}
