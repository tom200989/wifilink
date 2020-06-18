package com.p_xhelper_smart.p_xhelper_smart.utils;

import com.p_encrypt.p_encrypt.core.aesPKCS7.AES_PCKS7;
import com.tcl.token.javaToken.BASE64_src.sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/*
 * Created by Administrator on 2020/6/18.
 * 该加密类使用于all in one的CPE产品
 */
public class EncryptUtils2 {

    private String uid; // 用户uid
    private static long sign_l;// 随机数
    private static long timeStamp_l;// 时间

    static {
        sign_l = new Random().nextLong();
        timeStamp_l = System.currentTimeMillis() / 1000;
    }

    /* -------------------------------------------- public -------------------------------------------- */

    /**
     * 获取加密后的token
     * 格式: token = base64(ase128(uid, key, iv))
     *
     * @param uid 用户UID(外部传入)
     * @return 加密后token
     */
    public String getRefreshToken(String uid) {
        // 准备加密要素
        this.uid = uid;
        byte[] data = uid.getBytes(StandardCharsets.UTF_8);
        byte[] key = getKey();
        byte[] iv = getIv();

        // 开始加密
        return new BASE64Encoder().encode(new AES_PCKS7().encrypt(data, key, iv));
    }

    /**
     * 获取Authorization(基本封装, 如有扩展, 外部执行实现)
     *
     * @return Authorization
     */
    public String getAuthorzation() {
        StringBuilder sb = new StringBuilder();
        sb.append("Authorization:");
        sb.append("sign=").append(getSignStr()).append(";");
        sb.append("timestamp=").append(getTimeStampStr()).append(";");
        sb.append("token=").append(getRefreshToken(uid)).append(";");
        sb.append("uid=").append(uid);
        return sb.toString();
    }


    /* -------------------------------------------- private -------------------------------------------- */

    /**
     * 获取KEY
     *
     * @return 返回key
     */
    private byte[] getKey() {
        // TODO: 2020/6/18  
        return new byte[8];
    }

    /**
     * 获取IV
     *
     * @return 返回IV
     */
    private byte[] getIv() {
        // TODO: 2020/6/18  
        return new byte[8];
    }

    /**
     * 获取时间戳 timestamp(秒) - bytes[8]
     *
     * @return 时间戳(8字节数组)
     */
    private byte[] getTimeStampBts() {
        // 获取时间戳(long)
        long tempTimeStamp = timeStamp_l;
        // 创建8位字节数组
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[b.length - 1 - i] = Long.valueOf(tempTimeStamp & 0xFF).byteValue();
            // 向右移8位 
            tempTimeStamp = tempTimeStamp >> 8;
        }
        return b;
    }

    /**
     * 获取时间戳 timestamp(秒) - String
     *
     * @return 时间戳(字符)
     */
    private String getTimeStampStr() {
        return new String(getTimeStampBts());
    }


    /**
     * 获取随机数 sign(秒) - bytes[8]
     *
     * @return 随机数(8字节数组)
     */
    private byte[] getSignBts() {
        // 获取随机数(long)
        long tempSign = sign_l;
        // 创建8位字节数组
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[b.length - 1 - i] = Long.valueOf(tempSign & 0xFF).byteValue();
            // 向右移8位 
            tempSign = tempSign >> 8;
        }
        return b;
    }

    /**
     * 获取随机数 sign(秒) - String
     *
     * @return 随机数(字符)
     */
    private String getSignStr() {
        return new String(getSignBts());
    }
}
