package com.fortpass.models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            try (
                FileOutputStream fos = new FileOutputStream(new File(String.format("data/%s/credentials.sneakysneaky", user)));
                CipherOutputStream cos = new CipherOutputStream(fos, cipher);
                OutputStreamWriter writer = new OutputStreamWriter(cos, StandardCharsets.UTF_8);
            )
            {
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
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            try (
                FileInputStream fis = new FileInputStream(new File(String.format("data/%s/credentials.sneakysneaky", user)));
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
            SecretKeyFactory factory = SecretKeyFactory.getInstance("AES"); //AES/ECB/PKCS5Padding = AES
            return factory.generateSecret(spec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUserDir(String username) {
        StringBuilder hashString = new StringBuilder();
        try {
            MessageDigest function = MessageDigest.getInstance("SHA-256");
            byte[] hash = function.digest(username.getBytes(StandardCharsets.UTF_8));

            int i = 0;
            for(byte b : hash) {
                if(i > 15) //only taking the first 16 hexadecimal characters
                    break;
                hashString.append(String.format("%02X", b));
                i++;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashString.toString();        
    }
}
