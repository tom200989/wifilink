package com.alcatel.wifilink.root.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alcatel.wifilink.root.utils.Constants;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.PreferenceUtil;
import com.alcatel.wifilink.root.utils.RootCons;
import com.hiber.hiber.language.LangHelper;
import com.hiber.hiber.language.RootApp;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.utils.HostnameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.alcatel.wifilink.root.utils.C_Constants.Language.LANGUAGE;


public class SmartLinkV3App extends RootApp {

    private static SmartLinkV3App m_instance = null;
    private static List<Activity> contexts;

    public static SmartLinkV3App getInstance() {
        return m_instance;
    }

    public static List<Activity> getContextInstance() {
        return contexts;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // TOGO 2019/8/22 0022 
        PreferenceUtil.init(this);
        String languageFromPhone = OtherUtils.getLanguageFromPhone();
        String languageFromApp = PreferenceUtil.getString(LANGUAGE, "");
        PreferenceUtil.commitString(LANGUAGE, TextUtils.isEmpty(languageFromApp) ? languageFromPhone : languageFromApp);
        contexts = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences(Constants.SP_GLOBAL_INFO, Context.MODE_PRIVATE);
        sp.edit().clear().apply();

        /* 新框架 */
        XSmart.init(this);
        ShareUtils.init(this);
        ShareUtils.spName = "ROOT_WIFI_LINK";
        cacheLanguage();
        HostnameUtils.setVerifyHostName(this);
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
