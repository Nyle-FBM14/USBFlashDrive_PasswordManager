package com.passfort.models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Cryptography {

    //UTILITIES
    public static byte[] serialize(Object object) {
        try {
            ByteArrayOutputStream byteArrMaker = new ByteArrayOutputStream();
            ObjectOutputStream serializer = new ObjectOutputStream(byteArrMaker);
            serializer.writeObject(object);
            serializer.flush();
            return byteArrMaker.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Object deserialize(byte[] buffer) {
        try {
            ByteArrayInputStream arrayToStream = new ByteArrayInputStream(buffer);
            ObjectInputStream deserializer = new ObjectInputStream(arrayToStream);
            return deserializer.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encode(byte[] bytes) { //byte array to string
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static byte[] decode(String string) { //string to byte array
        return Base64.getDecoder().decode(string);
    }

    /*
    public static String getHashString(String s) {
        try {
            MessageDigest function = MessageDigest.getInstance("SHA-256");
            byte[] hash = function.digest(s.getBytes(StandardCharsets.UTF_8));
            return encode(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    //CRYPTOGRAPHY
    public static byte[] encrypt(Object message, Key key, String instance) {
        try{
            Cipher cipher = Cipher.getInstance(instance);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(serialize(message));
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Object decrypt(byte[] encryptedMessage, Key key, String instance) {
        try {
            Cipher cipher = Cipher.getInstance(instance);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return deserialize(cipher.doFinal(encryptedMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void encryptToFile(ArrayList<Credential> credentials, SecretKey key, String user) {
        File credentialsFile = new File(String.format("data/%s/credentials.passfort", user));
        if(credentials.isEmpty()) {
            credentialsFile.delete();
            return;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            try (
                FileOutputStream fos = new FileOutputStream(credentialsFile);
                CipherOutputStream cos = new CipherOutputStream(fos, cipher);
                OutputStreamWriter writer = new OutputStreamWriter(cos, StandardCharsets.UTF_8);
            )
            {
                if(credentials.isEmpty())
                    return;

                writer.write("<Credentials>");
                for(Credential c : credentials) {
                    writer.write(c.getXML());
                }
                writer.write("</Credentials>");
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Credential> decryptFromFile(SecretKey key, String user) {
        ArrayList<Credential> credentials = new ArrayList<Credential>();

        File credentialsFile = new File(String.format("data/%s/credentials.passfort", user));
        if(!credentialsFile.exists()) {
            try {
                credentialsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return credentials;
        }
        
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            try (
                FileInputStream fis = new FileInputStream(credentialsFile);
                CipherInputStream cis = new CipherInputStream(fis, cipher);
                //InputStreamReader isr = new InputStreamReader(cis, StandardCharsets.UTF_8);
                //BufferedReader reader = new BufferedReader(isr);
            )
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(cis);
                
                NodeList nodes = doc.getElementsByTagName("Credential");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Credential c = new Credential(nodes.item(i));
                    credentials.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return credentials;
    }

    public static SecretKey generateKey(String username, String password) {
        PBEKeySpec spec = new PBEKeySpec((username + password).toCharArray(), "AveChristusRex".getBytes(StandardCharsets.UTF_8), 10000, 256);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); //AES/ECB/PKCS5Padding = AES
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String hash256(String plaintext) {
        try {
            StringBuilder hashString = new StringBuilder();
            MessageDigest function = MessageDigest.getInstance("SHA-256");
            byte[] hash = function.digest(plaintext.getBytes(StandardCharsets.UTF_8));

            for(byte b : hash) {
                hashString.append(String.format("%02X", b));
            }
            return hashString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String customHash(String plaintext, byte hashLength) {
        final char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        final char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        final char[] NUMBER = "0123456789".toCharArray();
        final char[] SYMBOLS = "!@#$%^&*()-_=+[]{}<>,.?;:'\"\\/|~`".toCharArray();  //32 chars
        final char[] REGEX_ALL = "^[a-zA-Z0-9!@#$%^&*()_\\-=+\\[\\]{}<>,.?;:'\"\\\\/|~]+$".toCharArray();

        StringBuilder hashString = new StringBuilder();
        byte increment = (byte) (hashLength/4); //the places to put a uppercase, lowercase, number, and symbol
        try {
            MessageDigest function = MessageDigest.getInstance("SHA-256");
            byte[] hash = function.digest(plaintext.getBytes(StandardCharsets.UTF_8));
            byte index = 0;
            for(int i = 0; i < hashLength; i++) {

                //Makes sure that a password has at least uppercase letter, lowercase letter, number, and symbol. Formula ensures positive result: ((b % i) + i) % i
                if((i % increment) == 0) {
                    switch (index) {
                        case 0:
                            hashString.append(UPPERCASE[(((hash[i] % UPPERCASE.length) + UPPERCASE.length) % UPPERCASE.length)]);
                            break;
                        case 1:
                            hashString.append(LOWERCASE[(((hash[i] % LOWERCASE.length) + LOWERCASE.length) % LOWERCASE.length)]);
                            break;
                        case 2:
                            hashString.append(NUMBER[(((hash[i] % NUMBER.length) + NUMBER.length) % NUMBER.length)]);
                            break;
                        case 3:
                            hashString.append(SYMBOLS[(((hash[i] % SYMBOLS.length) + SYMBOLS.length) % SYMBOLS.length)]);
                            break;
                        default:
                            break;
                    }
                    index++;
                    continue;
                }
                hashString.append(REGEX_ALL[(((hash[i] % REGEX_ALL.length) + REGEX_ALL.length) % REGEX_ALL.length)]);
            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
