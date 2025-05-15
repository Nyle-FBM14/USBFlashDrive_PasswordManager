package com.fortpass.models;

import java.util.Arrays;

import javax.crypto.SecretKey;

public class Credential {
    private byte[] service;
    private byte[] username; //username or email used to sign in
    private byte[] password;
    private byte[] emailLinked; //the email linked to the service
    
    public Credential(byte[] service, byte[] password) {
        this.service = service;
        this.password = password;
    }
    public Credential(byte[] service, byte[] username, byte[] password) {
        this.service = service;
        this.username = username;
        this.password = password;
    }
    public Credential(byte[] service, byte[] username, byte[] password, byte[] emailLinked) {
        this.service = service;
        this.username = username;
        this.password = password;
        this.emailLinked = emailLinked;
    }

    public byte[] getService(SecretKey key) {
        return (byte[]) Cryptography.decrypt(service, key, "AES");
    }
    public void setService(byte[] service, SecretKey key) {
        this.service = Cryptography.encrypt(service, key, "AES");
    }
    public byte[] getUsername(SecretKey key) {
        return (byte[]) Cryptography.decrypt(username, key, "AES");
    }
    public void setUsername(byte[] username, SecretKey key) {
        this.username = Cryptography.encrypt(username, key, "AES");
    }
    public byte[] getPassword(SecretKey key) {
        return (byte[]) Cryptography.decrypt(password, key, "AES");
    }
    public void setPassword(byte[] password, SecretKey key) {
        this.password = Cryptography.encrypt(password, key, "AES");
    }
    public byte[] getEmailLinked(SecretKey key) {
        return (byte[]) Cryptography.decrypt(emailLinked, key, "AES");
    }
    public void setEmailLinked(byte[] emailLinked, SecretKey key) {
        this.emailLinked = Cryptography.encrypt(emailLinked, key, "AES");
    }
    
    public String getXML() {
        StringBuilder xml = new StringBuilder("<Credential>");
        if(service != null)
            xml.append(Cryptography.toXMLElement("service", service));
        if(username != null)
            xml.append(Cryptography.toXMLElement("username", username));
        if(password != null)
            xml.append(Cryptography.toXMLElement("password", password));
        if(emailLinked != null)
            xml.append(Cryptography.toXMLElement("emailLinked", emailLinked));
        xml.append("</Credential>");
        return xml.toString();
    }

    @Override
    public boolean equals(Object o) {
        Credential c = (Credential) o;
        return (Arrays.equals(service, c.service) && Arrays.equals(username, c.username) && Arrays.equals(password, c.password) && Arrays.equals(emailLinked, c.emailLinked));
    }
}
