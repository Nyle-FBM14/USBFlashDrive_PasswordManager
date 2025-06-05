package com.passfort.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

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

    //USERS
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
        
        try (BufferedReader reader = new BufferedReader(new FileReader("data/" + hash + "/password.passfort"))) {
            if(reader.readLine().equals(Cryptography.hash256(password))) {
                usernameHash = hash;
                key = Cryptography.generateKey(username, password);
                //decrypting and parsing data
                credentials = Cryptography.decryptFromFile(key, usernameHash);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean createAccount(String username, String password) {
        String hash = getUsernameHash(username);

        if(userExists(hash))
            return false;

        usernameHash = hash;
        key = Cryptography.generateKey(username, password);

        File dataDir = new File("data/" + usernameHash);
        dataDir.mkdir();

        try (FileWriter filewriter = new FileWriter("data/" + usernameHash + "/password.passfort")) {
            filewriter.write(Cryptography.hash256(password));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void logout() {
        Cryptography.encryptToFile(credentials, key, usernameHash);
        usernameHash = null;
        key = null;
        credentials = null;
    }

    //CHANGE?
    public SecretKey getKey() {
        return key;
    }
    public ArrayList<Credential> getCredentials() {
        return credentials;
    }

    //CREDENTIALS
    public void addCredential(String service, String username, String password, String emailLinked) {
        Credential c = new Credential(
            Cryptography.encrypt(service, key, "AES"),
            Cryptography.encrypt(username, key, "AES"),
            Cryptography.encrypt(password, key, "AES"),
            Cryptography.encrypt(emailLinked, key, "AES"));

        credentials.add(c);
        Cryptography.encryptToFile(credentials, key, usernameHash);
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

        Cryptography.encryptToFile(credentials, key, usernameHash);
    }
    public void deleteCredential(Credential c) {
        credentials.remove(c);
        Cryptography.encryptToFile(credentials, key, usernameHash);
    }

    //UTILITY
    public String generatePassword(int passwordLength) {
        final char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        final char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        final char[] NUMBER = "0123456789".toCharArray();
        final char[] SYMBOLS = "!@#$%^&*()-_=+[]{}<>,.?;:'\"\\/|~`".toCharArray();  //32 chars
        final char[] REGEX_ALL = "^[a-zA-Z0-9!@#$%^&*()_\\-=+\\[\\]{}<>,.?;:'\"\\\\/|~]+$".toCharArray();

        Random r = new Random();
        StringBuilder password = new StringBuilder();
        byte increment = (byte) (passwordLength/4);
        byte index = 0; //the places to put a uppercase, lowercase, number, and symbol

        for(int i = 0; i < passwordLength; i++) {
            //Makes sure that a password has at least uppercase letter, lowercase letter, number, and symbol. Formula ensures positive result: ((b % i) + i) % i
            if((i % increment) == 0) {
                switch (index) {
                    case 0:
                        password.append(UPPERCASE[r.nextInt(UPPERCASE.length)]);
                        break;
                    case 1:
                        password.append(LOWERCASE[r.nextInt(LOWERCASE.length)]);
                        break;
                    case 2:
                        password.append(NUMBER[r.nextInt(NUMBER.length)]);
                        break;
                    case 3:
                        password.append(SYMBOLS[r.nextInt(SYMBOLS.length)]);
                        break;
                    default:
                        break;
                }
                index++;
                continue;
            }
            password.append(REGEX_ALL[r.nextInt(REGEX_ALL.length)]);
        }
        return password.toString();
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
