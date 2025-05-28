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

    public SecretKey getKey() {
        return key;
    }
    public ArrayList<Credential> getCredentials() {
        return credentials;
    }
    public void editCredential(String service, String username, String password, String emailLinked, Credential credential) {
        if(service != null)
            credential.setService(service, key);
        if(username != null)
            credential.setUsername(username, key);
        if(password != null)
            credential.setPassword(password, key);
        if(emailLinked != null)
            credential.setEmailLinked(emailLinked, key);
    }
    public void deleteCredential(Credential c) {
        credentials.remove(c);
    }

    public void testData() {
        key = Cryptography.generateKey("nyle", "pass");
        this.usernameHash = "test";

        byte[] a = Cryptography.encrypt("Amazon", key, "AES");
        byte[] b = Cryptography.encrypt("Nyle", key, "AES");
        byte[] c = Cryptography.encrypt("password", key, "AES");
        byte[] d = Cryptography.encrypt("nyle@fbm.com", key, "AES");
        Credential cred = new Credential(a, b, c, d);
        credentials.add(cred);

        a = Cryptography.encrypt("Amazon", key, "AES");
        b = Cryptography.encrypt("Nyle", key, "AES");
        c = Cryptography.encrypt("password", key, "AES");
        d = Cryptography.encrypt("nyle@fbm.com", key, "AES");
        cred = new Credential(a, b, c, d);
        credentials.add(cred);

        a = Cryptography.encrypt("Facebook", key, "AES");
        b = Cryptography.encrypt("NyleF", key, "AES");
        c = Cryptography.encrypt("password1", key, "AES");
        d = Cryptography.encrypt("nylef@fbm.com", key, "AES");
        cred = new Credential(a, b, c, d);
        credentials.add(cred);

        a = Cryptography.encrypt("YouTube", key, "AES");
        b = Cryptography.encrypt("NyleFBM", key, "AES");
        c = Cryptography.encrypt("password123", key, "AES");
        d = Cryptography.encrypt("nyle123@fbm.com", key, "AES");
        cred = new Credential(a, b, c, d);
        credentials.add(cred);
    }

    public void seeData() {
        for(Credential c : credentials) {
            System.out.println("**************");
            System.out.println(c.getService(key));
            System.out.println(c.getUsername(key));
            System.out.println(c.getPassword(key));
            System.out.println(c.getEmailLinked(key));
            System.out.println("**************\n");
        }
    }
}
