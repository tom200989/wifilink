package com.alcatel.wifilink.root.utils;

import android.text.TextUtils;

import com.alcatel.wifilink.root.app.SmartLinkV3App;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


/**
 * Created by qianli.ma on 2017/8/5.
 */

public class HostnameUtils {

    private static HostNameImpl hostName;// hostname接口

    /* 设置SSL安全性验证 */
    public static void setVerifyHostName() {
        HttpsURLConnection.setDefaultHostnameVerifier(getVerify());
    }

    /* 获取SSL验证签名 */
    public static HostnameVerifier getVerify() {
        if (hostName == null) {
            hostName = new HostNameImpl();
        }
        return hostName;
    }

    /* 验证SSL类 */
    private static class HostNameImpl implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            String feedback_url = "www.alcatel-move.com";
            String wifiGateWay = WifiUtils.getWifiGateWay(SmartLinkV3App.getInstance());
            String ip = TextUtils.isEmpty(wifiGateWay) ? "192.168.1.1" : wifiGateWay;
            String http_ip = "http://" + ip;
            if (hostname.contains(feedback_url) || hostname.contains(ip) || hostname.equalsIgnoreCase(http_ip) || hostname.equalsIgnoreCase(ip)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
