package com.alcatel.wifilink.root.app;

import android.text.TextUtils;

import com.alcatel.wifilink.root.utils.RootCons;
import com.hiber.hiber.language.LangHelper;
import com.hiber.hiber.language.RootApp;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.utils.HostnameUtils;

import java.util.Locale;

public class WifiLinkApp extends RootApp {

    @Override
    public void onCreate() {
        super.onCreate();
        XSmart.init(this);// 初始化网络框架
        XSmart.PRINT_HEAD = false;
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
        // 如果没有指定地区, 则格式为: es-default, 否则es-MX (一般情况下即便是从系统取, 系统会自动带地区, 这里仅仅为防止其他情况)
        ShareUtils.set(RootCons.LOCALE_LANGUAGE_COUNTRY, language + "-" + (TextUtils.isEmpty(country) ? "default" : country));
    }
}
