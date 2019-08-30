package com.alcatel.wifilink.root.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;

@SuppressLint({"WifiManagerPotentialLeak", "HardwareIds"})
public class MacHelper {

    /**
     * 是否为本机地址
     */
    public static boolean isHost(Context context, String mac) {
        String localMac;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            localMac = getMACHigh();
        } else {
            localMac = getMACLow(context);
        }
        return mac.equalsIgnoreCase(localMac);
    }

    /**
     * 获取 6.0 以下的手机MAC
     */
    private static String getMACLow(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }


    /**
     * 获取安卓6.0手机的MAC地址
     */
    private static String getMACHigh() {
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格  
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (TextUtils.isEmpty(macSerial)) {
            try {
                return loadFileAsString().toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return macSerial;
    }

    private static String loadFileAsString() throws Exception {
        FileReader reader = new FileReader("/sys/class/net/eth0/address");
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }
}
