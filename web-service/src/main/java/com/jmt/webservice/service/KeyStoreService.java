package com.jmt.webservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.KeyStore;
import java.io.InputStream;

@Service
public class KeyStoreService {

    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String KEYSTORE_PATH = "classpath:jwt.p12"; // Assuming you've put jwt.p12 in resources folder

    private static final String KEY_ALIAS = "jwtkey";
    @Value("${keystore.password}")
    private String KEYSTORE_PASSWORD;

    @Value("${key.password}")
    private String KEY_PASSWORD;

    public Key getJwtSigningKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);

            try (InputStream inputStream = getClass().getResourceAsStream(KEYSTORE_PATH)) {
                keyStore.load(inputStream, KEYSTORE_PASSWORD.toCharArray());
            }

            return keyStore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load key from keystore", e);
        }
    }
}
