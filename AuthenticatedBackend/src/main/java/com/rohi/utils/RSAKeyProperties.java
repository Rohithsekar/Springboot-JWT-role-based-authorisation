package com.rohi.utils;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RSAKeyProperties {

    Logger log = LoggerFactory.getLogger(RSAKeyProperties.class);
    
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSAKeyProperties(){
        log.debug("inside RSAKeyProperties constructor");
        KeyPair pair = KeyGeneratorUtility.generateRsaKey();
        this.publicKey = (RSAPublicKey) pair.getPublic();
        this.privateKey = (RSAPrivateKey) pair.getPrivate();
    }

    public RSAPublicKey getPublicKey(){
        log.debug("inside getPublickey method");
        return this.publicKey;
    }

    public void setPublicKey(RSAPublicKey publicKey){
        this.publicKey = publicKey;
    }

    public RSAPrivateKey getPrivateKey(){
        log.debug("inside getProvateKey method");
        return this.privateKey;
    }

    public void setPrivateKey(RSAPrivateKey privateKey){
        this.privateKey = privateKey;
    }

}
