package com.p_xhelper_smart.p_xhelper_smart.utils;

import com.p_encrypt.p_encrypt.core.aesPKCS7.AES_PCKS7;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.tcl.token.javaToken.BASE64_src.sun.misc.BASE64Encoder;

/*
 * Created by qianli.ma on 2019/7/26 0026.
 */
public class EncryptUtils {

    /**
     * 兼容TOKEN加密
     *
     * @param token      token
     * @param tokenKey   token key(仅仅使用于新设备)
     * @param tokenIv    token iv(仅仅使用于新设备)
     * @param deviceName 当前设备名
     * @return 加密后的token
     */
    public static String encryptToken(String token, String tokenKey, String tokenIv, String deviceName) {
        // 1.对固有的KEY进行处理
        char[] key = XSmart.KEY.toCharArray();
        char chrs[] = new char[token.length() * 2];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < token.length(); i++) {
            char char_i = token.charAt(i);
            chrs[2 * i] = (char) ((key[i % key.length] & 0xf0) | ((char_i & 0xf) ^ (key[i % key.length] & 0xf)));
            chrs[2 * i + 1] = (char) ((key[i % key.length] & 0xf0) | ((char_i >> 4) ^ (key[i % key.length] & 0xf)));
        }
        // 2.根据算法拼接
        for (char chr : chrs) {
            builder.append(chr);
        }
        // 3.判断设备
        if (SmartUtils.getDEVType(deviceName) == XCons.ENCRYPT_DEV_2017) {// 老设备加密方式
            return builder.toString();

        } else if (SmartUtils.getDEVType(deviceName) == XCons.ENCRYPT_DEV_2019) {// 新设备加密方式
            AES_PCKS7 aes = new AES_PCKS7();
            byte[] tokenByte = aes.encrypt(builder.toString().getBytes(), tokenKey.getBytes(), tokenIv.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(tokenByte);

        } else {
            return "0";
        }
    }

    /**
     * 对帐号或密码进行加密
     *
     * @param admin 帐号或密码
     * @return 密文
     */
    public static String encryptAdmin(String admin) {
        // 1.对固有的KEY进行处理
        char[] key = XSmart.KEY.toCharArray();
        char chrs[] = new char[admin.length() * 2];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < admin.length(); i++) {
            char char_i = admin.charAt(i);
            chrs[2 * i] = (char) ((key[i % key.length] & 0xf0) | ((char_i & 0xf) ^ (key[i % key.length] & 0xf)));
            chrs[2 * i + 1] = (char) ((key[i % key.length] & 0xf0) | ((char_i >> 4) ^ (key[i % key.length] & 0xf)));
        }
        // 2.根据算法拼接
        for (char chr : chrs) {
            builder.append(chr);
        }
        return builder.toString();
    }
}
