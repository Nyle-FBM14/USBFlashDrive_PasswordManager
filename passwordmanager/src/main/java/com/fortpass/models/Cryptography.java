package com.fortpass.models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

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

    public static String toXMLElement(String tag, byte[] content) {
        return String.format("<%s>%s</%s>", tag, encode(content), tag);
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
}
