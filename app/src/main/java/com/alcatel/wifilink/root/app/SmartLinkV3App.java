package com.alcatel.wifilink.root.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.alcatel.wifilink.root.utils.Constants;
import com.alcatel.wifilink.root.utils.HostnameUtils;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.PreferenceUtil;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.alcatel.wifilink.root.utils.C_Constants.Language.LANGUAGE;


public class SmartLinkV3App extends MultiDexApplication {

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
        PreferenceUtil.init(this);
        String languageFromPhone = OtherUtils.getLanguageFromPhone();
        String languageFromApp = PreferenceUtil.getString(LANGUAGE, "");
        PreferenceUtil.commitString(LANGUAGE, TextUtils.isEmpty(languageFromApp) ? languageFromPhone : languageFromApp);
        x.Ext.init(this);
        x.Ext.setDebug(true);
        // 2.初始化其他参数
        m_instance = this;
        contexts = new ArrayList<>();
        /* checked hostNameVerify  */
        HostnameUtils.setVerifyHostName();
        // 4.清空share
        SharedPreferences sp = SmartLinkV3App.getInstance().getSharedPreferences(Constants.SP_GLOBAL_INFO, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
