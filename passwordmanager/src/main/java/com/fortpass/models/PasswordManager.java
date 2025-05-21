package com.fortpass.models;

import java.util.ArrayList;

import javax.crypto.SecretKey;

public class PasswordManager {
    private static volatile PasswordManager instance;
    //private String usernameHash;
    //private String passwordHash;
    private SecretKey key;
    private ArrayList<Credential> credentials;

    private PasswordManager() {
        credentials = new ArrayList<>();
    }
    public static PasswordManager getPasswordManagerInstance() {
        PasswordManager temp = instance;
        if(temp == null) {
            synchronized(PasswordManager.class) {
                temp = instance;
                if(temp == null)
                    instance = temp = new PasswordManager();
            }
        }
        return temp;
    }

    private void login(String username, String password) {
        //set key
        key = Cryptography.generateKey(username, password);

        //decrypting data
        credentials = Cryptography.decryptFromFile(key, username);
        //parsing data
        
    }
}
