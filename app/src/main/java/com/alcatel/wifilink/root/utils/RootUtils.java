package com.alcatel.wifilink.root.utils;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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
}
