package com.alcatel.wifilink.root.app;

import android.app.Activity;
import android.text.TextUtils;

import com.alcatel.wifilink.root.utils.RootCons;
import com.hiber.hiber.language.LangHelper;
import com.hiber.hiber.language.RootApp;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.utils.HostnameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class wifiApp extends RootApp {

    private static wifiApp m_instance = null;
    private static List<Activity> contexts;

    public static wifiApp getInstance() {
        return m_instance;
    }

    public static List<Activity> getContextInstance() {
        return contexts;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contexts = new ArrayList<>();

        /* 新框架 */
        XSmart.init(this);// 初始化网络框架
        ShareUtils.init(this);// 初始化缓存框架
        ShareUtils.spName = "ROOT_WIFI_LINK";
        cacheLanguage();// 初始化语言
        HostnameUtils.setVerifyHostName(this);// 设置google请求认证
    }

    /**
     * 把需要展示的语言保存到缓存
     * 在[语言选择页]再取出来显示
     */
    private void cacheLanguage() {
        Locale locale = LangHelper.getLocale(getApplicationContext());
        String language = locale.getLanguage();
        String country = locale.getCountry();
        // 如果没有指定地区, 则格式为: es-default, 否则es-MX
        ShareUtils.set(RootCons.LOCALE_LANGUAGE_COUNTRY, language + "-" + (TextUtils.isEmpty(country) ? "default" : country));
    }
}
