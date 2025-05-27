package com.passfort.models;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.SecretKey;

public class PasswordManager {
    private static volatile PasswordManager instance;
    private String usernameHash;
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

    private String getUsernameHash(String username) { //the hash will be truncated for shorter user folder names
        StringBuilder hashString = new StringBuilder();
        try {
            MessageDigest function = MessageDigest.getInstance("SHA-256");
            byte[] hash = function.digest(username.getBytes(StandardCharsets.UTF_8));

            int i = 0;
            for(byte b : hash) {
                if(i > 7) //only taking the first 16 hexadecimal characters
                    break;
                hashString.append(String.format("%02X", b));
                i++;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashString.toString();        
    }
    private boolean userExists(String usernameHash) {
        File dataDir = new File("data/" + usernameHash);
        return dataDir.exists();

    }
    public boolean login(String username, String password) {
        String hash = getUsernameHash(username);

        if(!userExists(hash))
            return false;
        
        usernameHash = hash;
        key = Cryptography.generateKey(username, password);
        //decrypting and parsing data
        credentials = Cryptography.decryptFromFile(key, username);

        return true;
    }
    public boolean createAccount(String username, String password) {
        String hash = getUsernameHash(username);

        if(userExists(hash))
            return false;

        usernameHash = hash;
        key = Cryptography.generateKey(username, password);

        return true;
    }
}
