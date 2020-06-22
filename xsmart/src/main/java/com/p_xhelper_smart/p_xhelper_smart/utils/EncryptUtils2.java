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

    private static String uid; // 用户uid
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
     * @param uid_k 用户UID(外部传入)
     * @return 加密后token
     */
    public static String getRefreshToken(String uid_k) {
        // 准备加密要素
        uid = uid_k;
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
    public static String getAuthorzation() {
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
    private static byte[] getKey() {
        byte[] timeStampBts = getTimeStampBts();
        byte[] keyBytes = getPreBytes(new byte[16], timeStampBts);
        for (int i = 0; i < 4; i++) {
            keyBytes[i + 8] = timeStampBts[4 - i];
        }
        byte[] signBts = getSignBts();
        for (int i = 0; i < 4; i++) {
            keyBytes[i + 12] = signBts[4 - i];
        }
        return keyBytes;
    }

    /**
     * 获取IV
     *
     * @return 返回IV
     */
    private static byte[] getIv() {
        byte[] signBts = getSignBts();
        byte[] ivBytes = getPreBytes(new byte[16], signBts);

        for (int i = 0; i < 4; i++) {
            ivBytes[i + 8] = signBts[8 - i];
        }

        byte[] timeStampBts = getTimeStampBts();
        for (int i = 0; i < 4; i++) {
            ivBytes[i + 12] = timeStampBts[8 - i];
        }
        return ivBytes;
    }


    /**
     * 封装前8个字节, 规则如下:
     * <p>
     * key 输入参数包括timestamp，sign，各参数定义如下：
     * 1. timestamp: 时间戳, 8字节整型
     * 2. sign: 随机数，8字节整型
     * <p>
     * 结构如下：
     * +---8bytes---+--4bytes--+--4bytes--+
     * |    n       |   t_H4   | signH4   |
     * <p>
     * 1. signH4: sign 高四位（小端字节序）。
     * 2. t_H4: timestamp 高四位（小端字节序）。
     * 3. n: timestamp 按字节序依次做与运算
     * byte1(n) = byte1(timestamp) & byte2(timestamp)
     * byte2(n) = byte2(timestamp) & byte3(timestamp)
     * ...
     * byte8(n) = byte8(timestamp) & byte1(timestamp)
     *
     * @param desBytes 目标字节数组
     * @param srcBytes 原始字节数组
     * @return 前8个字节
     */
    private static byte[] getPreBytes(byte[] desBytes, byte[] srcBytes) {
        for (int i = 0; i < srcBytes.length; i++) {
            if (i == srcBytes.length - 1) {
                desBytes[i] = (byte) (srcBytes[i] & srcBytes[0]);
            } else {
                desBytes[i] = (byte) (srcBytes[i] & srcBytes[i + 1]);
            }
        }
        return desBytes;
    }


    /**
     * 获取时间戳 timestamp(秒) - bytes[8]
     *
     * @return 时间戳(8字节数组)
     */
    private static byte[] getTimeStampBts() {
        return getbts(timeStamp_l);
    }

    /**
     * 获取时间戳 timestamp(秒) - String
     *
     * @return 时间戳(字符)
     */
    public static String getTimeStampStr() {
        return new String(getTimeStampBts());
    }

    /**
     * 获取随机数 sign(秒) - bytes[8]
     *
     * @return 随机数(8字节数组)
     */
    private static byte[] getSignBts() {
        // 获取随机数(long)
        return getbts(sign_l);
    }

    /**
     * 获取随机数 sign(秒) - String
     *
     * @return 随机数(字符)
     */
    public static String getSignStr() {
        return new String(getSignBts());
    }

    /**
     * 转换long - byte
     *
     * @param num 原始数字
     * @return 转换后的byte数组
     */
    private static byte[] getbts(long num) {
        // 获取时间戳(long)
        long tempNum = num;
        // 创建8位字节数组
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[b.length - 1 - i] = Long.valueOf(tempNum & 0xFF).byteValue();
            // 向右移8位 
            tempNum = tempNum >> 8;
        }
        return b;
    }
}
