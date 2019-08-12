package com.alcatel.wifilink.root.utils;

import com.p_encrypt.p_encrypt.core.aesPKCS7.AES_PCKS7;
import com.p_encrypt.p_encrypt.core.encoder.BASE64Encoder;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;


/**
 * Created by tao.j on 2017/6/22.
 */

public class EncryptionUtil {

    public static String encrypt(String info) {

        // char[] key = AUTHORIZATION_KEY.toCharArray();
        char[] key = XSmart.KEY.toCharArray();
        char str1[] = new char[info.length() * 2];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < info.length(); i++) {
            char char_i = info.charAt(i);
            str1[2 * i] = (char) ((key[i % key.length] & 0xf0) | ((char_i & 0xf) ^ (key[i % key.length] & 0xf)));
            str1[2 * i + 1] = (char) ((key[i % key.length] & 0xf0) | ((char_i >> 4) ^ (key[i % key.length] & 0xf)));
        }

        for (char aStr1 : str1) {
            builder.append(aStr1);
        }

        return builder.toString();
    }

    /**
     * 针对MW120的加密(需要经过两次加密,HH70只需要一次)
     *
     * @param info      token原文
     * @param token_key token需要的KEY
     * @param token_iv  token需要的IV
     * @return
     */
    public static String encryptForMW120(String info, String token_key, String token_iv) {

        // 加密1
        char[] key = XSmart.KEY.toCharArray();
        char str1[] = new char[info.length() * 2];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < info.length(); i++) {
            char char_i = info.charAt(i);
            str1[2 * i] = (char) ((key[i % key.length] & 0xf0) | ((char_i & 0xf) ^ (key[i % key.length] & 0xf)));
            str1[2 * i + 1] = (char) ((key[i % key.length] & 0xf0) | ((char_i >> 4) ^ (key[i % key.length] & 0xf)));
        }

        for (char aStr1 : str1) {
            builder.append(aStr1);
        }

        String firstToken = builder.toString();

        // 加密2
        AES_PCKS7 aes = new AES_PCKS7();
        byte[] firstToken_b = aes.encrypt(firstToken.getBytes(), token_key.getBytes(), token_iv.getBytes());
        BASE64Encoder encoder = new BASE64Encoder();
        String secondToken = encoder.encode(firstToken_b);// 2.B64封装

        return secondToken;
    }

    public static String encryptUser(String info) {

        char[] key = XSmart.KEY.toCharArray();
        char str1[] = new char[info.length() * 2];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < info.length(); i++) {
            char char_i = info.charAt(i);
            str1[2 * i] = (char) ((key[i % key.length] & 0xf0) | ((char_i & 0xf) ^ (key[i % key.length] & 0xf)));
            str1[2 * i + 1] = (char) ((key[i % key.length] & 0xf0) | ((char_i >> 4) ^ (key[i % key.length] & 0xf)));
        }

        for (char aStr1 : str1) {
            builder.append(aStr1);
        }

        return builder.toString();
    }
}
