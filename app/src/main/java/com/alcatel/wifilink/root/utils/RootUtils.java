package com.alcatel.wifilink.root.utils;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/*
 * Created by qianli.ma on 2019/8/15 0015.
 */
public class RootUtils {

    /**
     * 是否为［MW］设备
     *
     * @param devName 设备名
     * @return T:是
     */
    public static boolean isMWDEV(String devName) {
        // 先转换成小写
        devName = devName.toLowerCase();
        // 再遍历判断
        for (String dev : RootCons.FREE_SHARING_DEVICE) {
            if (devName.contains(dev)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为71设备
     *
     * @param devName 设备名
     * @return T:是
     */
    public static boolean isHH71(String devName) {
        // 先转换成小写
        devName = devName.toLowerCase();
        // 再遍历判断
        for (String dev : RootCons.HH71_DEVICE) {
            if (devName.contains(dev)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取edittext文本内容并去除空格
     *
     * @param editText 文本域
     * @return 内容
     */
    public static String getEDText(EditText editText) {
        return editText.getText().toString().trim().replace(" ", "");
    }

    /**
     * 获取edittext文本内容并去除空格
     *
     * @param editText 文本域
     * @param isSpace  是否保留空格(只消除两端)
     * @return 内容
     */
    public static String getEDText(EditText editText, boolean isSpace) {
        if (isSpace) {
            return editText.getText().toString().trim();
        } else {
            return editText.getText().toString().trim().replace(" ", "");
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeyBoard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showKeyBoard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInputFromInputMethod(context.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 设置编辑域编辑状态
     */
    public static void setEdEnable(EditText et, boolean enable) {
        et.setFocusable(enable);
        et.setFocusableInTouchMode(enable);
    }

    /**
     * 开启或关闭WIFI
     *
     * @param context 域
     * @param open    T:开
     * @return 是否已经开启
     */
    public static boolean setWifiOn(Context context, boolean open) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifi.setWifiEnabled(open);
    }

    /**
     * 编辑与是否为空
     */
    public static boolean isEdEmpty(EditText... eds) {
        for (EditText editText : eds) {
            if (TextUtils.isEmpty(getEDText(editText))) {
                return true;
            }
        }
        return false;
    }

    /**
     * IP是否符合规则
     * @param ip IP
     */
    private static boolean ipMatch(String ip) {
        String ipRule = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."// 1
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."// 2
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."// 3
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";   // 4
        return Pattern.matches(ipRule, ip);
    }

    /**
     * 是否全部达标
     *
     * @param ipAddress IP地址
     * @return T:达标
     */
    public static boolean isAllMatch(String ipAddress) {
        if (!ipMatch(ipAddress)) {
            return false;
        }
        Logs.t("ma_ip").vv("ip_phone address: " + ipAddress);
        if (TextUtils.isEmpty(ipAddress)) {// 空值
            return false;
        }
        if (!ipAddress.contains(".")) {// 不含点
            return false;
        }
        String[] address = ipAddress.split("\\.");
        for (String s : address) {// 全部是点没有数字
            if (TextUtils.isEmpty(s)) {
                return false;
            }
        }
        List<Integer> ips = new ArrayList<>();
        for (String s : address) {
            ips.add(Integer.valueOf(s));
        }

        int num0;
        try {// 避免位数不够
            num0 = ips.get(0);
        } catch (Exception e) {
            num0 = 0;
        }

        int num1;
        try {// 避免位数不够
            num1 = ips.get(1);
        } catch (Exception e) {
            num1 = 0;
        }

        int num2;
        try {// 避免位数不够
            num2 = ips.get(2);
        } catch (Exception e) {
            num2 = 0;
        }

        int num3;
        try {// 避免位数不够
            num3 = ips.get(3);
        } catch (Exception e) {
            num3 = 0;
        }

        return (num0 > 0 && num0 != 127 && num0 <= 223) && // num0
                       (num1 >= 0 && num1 <= 255) && // num1
                       (num2 >= 0 && num2 <= 255) && // num2
                       (num3 > 0 && num3 < 255);
    }
}
