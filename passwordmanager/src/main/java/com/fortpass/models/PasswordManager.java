package com.fortpass.models;

import java.util.ArrayList;

import javax.crypto.SecretKey;

public class PasswordManager {
    //private String usernameHash;
    //private String passwordHash;
    private SecretKey key;
    private ArrayList<Credential> credentials;

    private void setKey(String username, String password) {
        key = Cryptography.generateKey(username, password);
    }
    private void decryptData() {

    }
    private
}
