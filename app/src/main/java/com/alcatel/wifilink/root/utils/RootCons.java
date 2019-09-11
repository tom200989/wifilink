package com.alcatel.wifilink.root.utils;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by qianli.ma on 2019/8/15 0015.
 */
public class RootCons {

    public static String SP_GUIDE = "SP_GUIDE";// 是否进入过向导页
    public static String SP_WIZARD = "SP_WIZARD";// 是否进入过引导页
    public static String SP_DATA_PLAN_INIT = "SP_DATA_PLAN_INIT";// 是否进入过流量计划页
    public static String SP_WIFI_INIT = "SP_WIFI_INIT";// 是否进入过WIFI初始设置页
    public static String SP_WAN_INIT = "SP_WAN_INIT";// 是否进入过WAN口初始设置页
    public static String DEVICE_NAME = "DEVICE_NAME";// 设备名 (在splash时候存到缓存)
    public static String DEVICE_NAME_DEFAULT = "hh70";// 默认设备名 (HH70)
    public static String LOGIN_IS_REMEM_PSD = "LOGIN_IS_REMEM_PSD";// 登陆界面记住密码标记
    public static String LOGIN_REMEM_PSD = "LOGIN_REMEM_PSD";// 登陆界面缓存的密码
    public static String PIN_INIT_IS_REMEM_PSD = "PIN_INIT_IS_REMEM_PSD";// PIN码初始化界面记住密码标记
    public static String PIN_INIT_REMEM_PSD = "PIN_INIT_REMEM_PSD";// PIN码初始化界面缓存的密码
    public static String PIN_RX_IS_REMEM_PSD = "PIN_RX_IS_REMEM_PSD";// PIN码设置界面记住密码标记
    public static String PIN_RX_REMEM_PSD = "PIN_RX_REMEM_PSD";// PIN码设置界面缓存的密码
    public static String USAGE_LIMIT_DEFAULT = "USAGE_LIMIT";// 流量默认
    public static String LOCALE_LANGUAGE_COUNTRY = "LOCALE_LANGUAGE_COUNTRY";// 当前需要显示的语言和国家, 如:es-MX

    public static class ACTIVITYS {// Activity
        public static final String SPLASH_AC = "com.alcatel.wifilink.root.ue.activity.SplashActivity";
        public static final String HOME_AC = "com.alcatel.wifilink.root.ue.activity.HomeActivity";
    }

    public static class FRAG {
        public static final String LOGIN_FR = "com.alcatel.wifilink.root.ue.frag.LoginFrag";
        public static final String REFRESH_FR = "com.alcatel.wifilink.root.ue.frag.RefreshFrag";
    }

    public static class LANGUAGES {// 支持的语言
        public static final String ENGLISH = "en";
        public static final String ARABIC = "ar";
        public static final String ESPANYOL = "es";
        public static final String GERMENIC = "de";
        public static final String ITALIAN = "it";
        public static final String FRENCH = "fr";
        public static final String SERBIAN = "sr";
        public static final String CROATIAN = "hr";
        public static final String SLOVENIAN = "sl";
        public static final String POLAND = "pl";
        public static final String RUSSIAN = "ru";
        public static final String CHINA = "zh";
    }

    public static List<String> LANGUAGE_COUNTRY_LIST = new ArrayList<>();// 语言集合

    static {
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.ENGLISH + "-US");
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.ARABIC + "-AR");
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.ESPANYOL + "-ES");
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.ESPANYOL + "-MX");
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.GERMENIC + "-DE");
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.ITALIAN + "-IT");
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.FRENCH + "-FR");
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.SERBIAN + "-RS");
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.CROATIAN + "-HR");
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.SLOVENIAN + "-SI");
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.POLAND + "-PL");
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.RUSSIAN+ "-RU");
        LANGUAGE_COUNTRY_LIST.add(LANGUAGES.CHINA + "-TW");
    }

    public static List<String> MW_DEVICE = new ArrayList<>();// 需要free-sharing的设备
    public static List<String> HH71_DEVICE = new ArrayList<>();// HH71设备
    public static List<String> HH4X_DEVICE = new ArrayList<>();// HH4X设备

    static {
        MW_DEVICE.add("mw12");
        MW_DEVICE.add("mw70");
        HH71_DEVICE.add("hh71");
        HH71_DEVICE.add("hub71");
        HH4X_DEVICE.add("hh4");
        HH4X_DEVICE.add("hub4");
    }
}
