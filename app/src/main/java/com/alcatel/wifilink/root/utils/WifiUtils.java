package com.alcatel.wifilink.root.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

/**
 * Created by qianli.ma on 2017/8/2.
 */

public class WifiUtils {

    /**
     * 获取wifi网关
     *
     * @param context
     * @return
     */
    public static String getWifiGateWay(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        String gateWay = transferNetMask(dhcp.gateway);
        return gateWay;
    }

    /**
     * 转换16进制的WIFI网关
     *
     * @param gateWayHex 16进制的网关
     * @return
     */
    private static String transferNetMask(long gateWayHex) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf((int) (gateWayHex & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((gateWayHex >> 8) & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((gateWayHex >> 16) & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((gateWayHex >> 24) & 0xff)));
        return sb.toString();
    }

}
