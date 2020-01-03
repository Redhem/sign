package com.obank.sign.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.obank.sign.exception.ScfException;

public class SignUtils {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    public static String sign(String data, String privateKey) {
        try {
            PrivateKey privateK = getPrivateKey(privateKey);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(data.getBytes());
            String sign = Base64.getEncoder().encodeToString(signature.sign());
            return sign;
        } catch (Exception e) {
            throw new ScfException(e.getMessage());
        }
    }

    public static String sign(File file, String privateKey) {
        try {
            PrivateKey pk = getPrivateKey(privateKey);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(pk);

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buf = new byte[1024 * 1024];
            int len = 0;

            while ((len = bis.read(buf)) > 0) {
                signature.update(buf, 0, len);
            }

            bis.close();

            String sign = Base64.getEncoder().encodeToString(signature.sign());
            return sign;
        } catch (Exception e) {
            throw new ScfException(e.getMessage());
        }
    }

    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        PublicKey pubk = getPublicKey(publicKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubk);
        signature.update(data.getBytes());
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    public static boolean verify(File file, String publicKey, String sign) throws Exception {
        PublicKey pubk = getPublicKey(publicKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubk);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[1024 * 1024];
        int len = 0;

        while ((len = bis.read(buf)) > 0) {
            signature.update(buf, 0, len);
        }

        bis.close();
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    private static PrivateKey getPrivateKey(String privateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String key = privateKey.replaceAll("\\r|\\n|\\s|\\t", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(pkcs8KeySpec);
    }

    private static PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String key = publicKey.replaceAll("\\r|\\n|\\s|\\t", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }
}