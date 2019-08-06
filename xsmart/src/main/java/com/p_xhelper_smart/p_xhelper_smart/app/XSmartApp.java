package com.p_xhelper_smart.p_xhelper_smart.app;

import android.app.Application;

import org.xutils.x;

/*
 * Created by qianli.ma on 2019/7/27 0027.
 */
public class XSmartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
