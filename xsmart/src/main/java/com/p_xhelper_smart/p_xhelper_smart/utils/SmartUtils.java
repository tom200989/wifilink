package com.p_xhelper_smart.p_xhelper_smart.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;

/**
 * The type Smart utils.
 */
/*
 * Created by qianli.ma on 2019/7/25 0025.
 */
@SuppressWarnings({"unchecked", "warn", "all"})
public class SmartUtils {

    /**
     * 获取SD卡目录(兼容android Q)
     *
     * @param context 域
     * @return SD卡目录
     */
    public static String get_android_Q_SD_path(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return context.getExternalFilesDir(null).getAbsolutePath();
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取需要MD5加密密码的设备类型 (用于MD5加密密码类型适配)
     *
     * @param deviceName 当前设备名
     * @return 4: 需要MD5对密码加密
     */
    public static int getDevMd5Type(String deviceName) {
        // 先最小化
        deviceName = deviceName.toLowerCase();
        // 老设备
        for (String dev : XCons.ENCRYPT_LIST_MD5) {
            if (deviceName.contains(dev) || dev.contains(deviceName)) {
                return XCons.ENCRYPT_MD5;
            }
        }
        // 未知设备
        return XCons.ENCRYPT_DEV_UNKNOWN;
    }

    /**
     * 获取设备类型(用于Token加密类型适配)
     *
     * @param deviceName 当前设备名
     * @return -1:未知设备; 0:老设备; 1:新设备
     */
    public static int getDevTokenType(String deviceName) {
        // 先最小化
        deviceName = deviceName.toLowerCase();
        // 老设备
        for (String dev : XCons.ENCRYPT_LIST_DEV_ROUTER) {
            if (deviceName.contains(dev) || dev.contains(deviceName)) {
                return XCons.ENCRYPT_DEV_ROUTER;
            }
        }
        // 新设备
        for (String dev : XCons.ENCRYPT_LIST_DEV_HOTSPOT) {
            if (deviceName.contains(dev) || dev.contains(deviceName)) {
                return XCons.ENCRYPT_DEV_HOTSPOT;
            }
        }
        // 定制设备
        for (String dev : XCons.ENCRYPT_LIST_DEV_TARGET) {
            if (deviceName.contains(dev) || dev.contains(deviceName)) {
                return XCons.ENCRYPT_DEV_TARGET;
            }
        }
        // 5G-CPE设备
        for (String dev : XCons.ENCRYPT_LIST_DEV_5G_CPE) {
            if (deviceName.contains(dev) || dev.contains(deviceName)) {
                return XCons.ENCRYPT_DEV_5G_CPE;
            }
        }

        // 未知设备
        return XCons.ENCRYPT_DEV_UNKNOWN;
    }

    /**
     * 获取WIFI网管
     *
     * @param context 域
     * @return 网关, 如192.168.1.1
     */
    public static String getWIFIGateWay(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf((dhcp.gateway & 0xff))).append(".");
        builder.append(String.valueOf(((dhcp.gateway >> 8) & 0xff))).append(".");
        builder.append(String.valueOf(((dhcp.gateway >> 16) & 0xff))).append(".");
        builder.append(String.valueOf(((dhcp.gateway >> 24) & 0xff)));
        return builder.toString();
    }

    /**
     * WIFI是否连接
     *
     * @param context 域
     * @return T :有
     */
    public static boolean isWifiOn(Context context) {
        // 第一种方式测量WIFI是否打开
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        boolean isWifiOn_1 = wifi.isWifiEnabled() && ipAddress != 0;
        // 第二种方式测量WIFI是否打开
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo.State wifiState = networkInfo.getState();
        boolean isWifiOn_2 = NetworkInfo.State.CONNECTED == wifiState;
        // 只要有一个是TRUE -- 都认为已经连接成功
        return isWifiOn_1 | isWifiOn_2;
    }
}
