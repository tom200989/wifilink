package com.xsmart.tokenjar.aesPKCS7;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Key;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author ngh
 * AES128 算法
 * <p>
 * CBC 模式
 * <p>
 * PKCS7Padding 填充模式
 * <p>
 * CBC模式需要添加一个参数iv
 * <p>
 * 介于java 不支持PKCS7Padding，只支持PKCS5Padding 但是PKCS7Padding 和 PKCS5Padding 没有什么区别
 * 要实现在java端用PKCS7Padding填充，需要用到bouncycastle组件来实现
 */
public class AES_PCKS7 {

    // 算法名称
    private final String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    private final String algorithmStr = "AES/CBC/PKCS7Padding";
    private Key key;
    private Cipher cipher;
    boolean isInited = false;

    //byte[] iv = {0x30, 0x31, 0x30, 0x32, 0x30, 0x33, 0x30, 0x34, 0x30, 0x35, 0x30, 0x36, 0x30, 0x37, 0x30, 0x38};


    public void init(byte[] key) {
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        int base = 16;
        if (key.length % base != 0) {
            int groups = key.length / base + (key.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        this.key = new SecretKeySpec(key, KEY_ALGORITHM);
        try {
            // 初始化cipher
            cipher = Cipher.getInstance(algorithmStr, "BC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密方法
     *
     * @param data 要加密的字符串
     * @param key  加密密钥
     * @param iv   向量
     * @return 密文
     */
    public byte[] encrypt(byte[] data, byte[] key, byte[] iv) {
        byte[] encryptedText = null;
        init(key);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, this.key, new IvParameterSpec(iv));
            encryptedText = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedText;
    }

    /**
     * 解密方法
     *
     * @param unData 要解密的字符串
     * @param key    解密密钥
     * @param iv     向量
     * @return 明文
     */
    public byte[] decrypt(byte[] unData, byte[] key, byte[] iv) {
        byte[] encryptedText = null;
        init(key);
        try {
            cipher.init(Cipher.DECRYPT_MODE, this.key, new IvParameterSpec(iv));
            encryptedText = cipher.doFinal(unData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedText;
    }
}
