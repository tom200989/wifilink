package com.xsmart.tokenjar.des;


import com.xsmart.tokenjar.encoder.BASE64Decoder;
import com.xsmart.tokenjar.encoder.BASE64Encoder;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Des {

    private final static String DES = "DES";

    public static void main(String[] args) throws Exception {
        String data = "这是测试数据";
        String key = "ma_encrypt_test";
        System.out.println(encrypt(data, key));
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data 原始数据
     * @param key  加密键byte数组
     * @return 密文
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        return new BASE64Encoder().encode(bt);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data 密文
     * @param key  加密键byte数组
     * @return 明文
     */
    public static String decrypt(String data, String key) throws Exception {
        if (data != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] buf = decoder.decodeBuffer(data);
            byte[] bt = decrypt(buf, key.getBytes());
            return new String(bt);
        }
        return null;
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data 原始数据
     * @param key  加密键byte数组
     * @return 密文
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 1.生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 2.从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 3.创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // 4.Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 5.用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data 密文
     * @param key  加密键byte数组
     * @return 明文
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 1.生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 2.从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 3.创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // 4.Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 5.用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
}
