package com.p_xhelper_smart.p_xhelper_smart.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


/**
 * Created by qianli.ma on 2017/8/5.
 */

public class HostnameUtils {

    @SuppressLint("StaticFieldLeak")
    private static HostNameImpl hostName;// hostname接口

    /* 设置SSL安全性验证 */
    public static void setVerifyHostName(Context context) {
        HttpsURLConnection.setDefaultHostnameVerifier(getVerify(context));
    }

    /* 获取SSL验证签名 */
    public static HostnameVerifier getVerify(Context context) {
        if (hostName == null) {
            hostName = new HostNameImpl(context);
        }
        return hostName;
    }

    /* 验证SSL类 */
    private static class HostNameImpl implements HostnameVerifier {

        private Context context;

        HostNameImpl(Context context) {
            this.context = context;
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            // feedback地址
            String feedback_url = "www.alcatel-move.com";
            // gateway like 192.168.1.1
            String gateWay = TextUtils.isEmpty(SmartUtils.getWIFIGateWay(context)) ? "192.168.1.1" : SmartUtils.getWIFIGateWay(context);
            boolean isContainFeedbackUrl = hostname.contains(feedback_url);
            boolean isContainGateWay = hostname.contains(gateWay);
            return isContainFeedbackUrl || isContainGateWay;
        }
    }
}
