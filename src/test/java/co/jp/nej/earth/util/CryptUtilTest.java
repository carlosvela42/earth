package co.jp.nej.earth.util;

import co.jp.nej.earth.BaseTest;
import org.junit.Assert;
import org.junit.Test;

public class CryptUtilTest extends BaseTest {

    @Test
    public void testEncryptOneWay() throws Exception {
        String encryptedStr = CryptUtil.encryptOneWay("admin123");
        Assert.assertTrue(encryptedStr != null && !encryptedStr.equals(""));
    }

    @Test
    public void testEncryptAndDecryptTwoWay() throws Exception {
        String plainText = "This is message test";
        String ecryptedText = CryptUtil.encryptData(plainText);
        Assert.assertEquals(plainText, CryptUtil.decryptData(ecryptedText));
    }
}
