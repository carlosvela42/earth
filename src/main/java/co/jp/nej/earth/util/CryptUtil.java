package co.jp.nej.earth.util;

import static co.jp.nej.earth.model.constant.Constant.*;

import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtil {

    private static final String SHA_512 = "SHA-512";
    private static final String PKCS5PADDING = "AES/ECB/PKCS5Padding";
    private static final String AES = "AES";
    private static byte[] KEY = { 0x2d, 0x2a, 0x2d, 0x42, 0x55, 0x49, 0x4c, 0x44, 0x41, 0x43, 0x4f, 0x44, 0x45, 0x2d,
            0x2a, 0x2d };

    /**
     * 
     * @param passwordToHash
     *            password to hash
     * @return hashed string
     */
    public static String encryptOneWay(String passwordToHash) throws Exception {
        MessageDigest md = MessageDigest.getInstance(SHA_512);
        md.update(SALT.getBytes(UTF_8));
        byte[] bytes = md.digest(passwordToHash.getBytes(UTF_8));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String encryptData(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(PKCS5PADDING);
        SecretKeySpec secretKey = new SecretKeySpec(KEY, AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherText = cipher.doFinal(plainText.getBytes(UTF_8));
        String encryptedString = new String(Base64.getEncoder().encode(cipherText), UTF_8);
        return encryptedString;
    }

    public static String decryptData(String encryptedText) throws Exception {
        Cipher cipher = Cipher.getInstance(PKCS5PADDING);
        SecretKeySpec secretKey = new SecretKeySpec(KEY, AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] cipherText = Base64.getDecoder().decode(encryptedText.getBytes(UTF_8));
        String decryptedString = new String(cipher.doFinal(cipherText), UTF_8);
        return decryptedString;
    }
}
