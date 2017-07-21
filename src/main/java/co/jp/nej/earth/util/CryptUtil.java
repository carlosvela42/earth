package co.jp.nej.earth.util;

import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.model.constant.Constant.EnCryption;

public class CryptUtil {
    /**
     * @param passwordToHash password to hash
     * @return hashed string
     */
    public static String encryptOneWay(String passwordToHash) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance(EnCryption.SHA_512);
            md.update(Constant.SALT.getBytes(Constant.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(Constant.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & EnCryption._0XFF) + EnCryption._0X100, EnCryption.RADIUS)
                        .substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new EarthException(e);
        }
    }

    public static String encryptData(String plainText) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(EnCryption.PKCS5PADDING);
            SecretKeySpec secretKey = new SecretKeySpec(EnCryption.KEY, EnCryption.AES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(Constant.UTF_8));
            String encryptedString = new String(Base64.getEncoder().encode(cipherText), Constant.UTF_8);
            return encryptedString;
        } catch (Exception e) {
            throw new EarthException(e);
        }
    }

    public static String decryptData(String encryptedText) throws EarthException {
        try {
            Cipher cipher = Cipher.getInstance(EnCryption.PKCS5PADDING);
            SecretKeySpec secretKey = new SecretKeySpec(EnCryption.KEY, EnCryption.AES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherText = Base64.getDecoder().decode(encryptedText.getBytes(Constant.UTF_8));
            String decryptedString = new String(cipher.doFinal(cipherText), Constant.UTF_8);
            return decryptedString;
        } catch (Exception e) {
            throw new EarthException(e);
        }
    }
}
