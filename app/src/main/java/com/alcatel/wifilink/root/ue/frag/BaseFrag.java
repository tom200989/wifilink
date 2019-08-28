package com.alcatel.wifilink.root.ue.frag;

import android.view.View;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.ue.activity.HomeActivity;
import com.alcatel.wifilink.root.ue.activity.SplashActivity;
import com.hiber.hiber.RootFrag;
import com.hiber.impl.RootEventListener;
import com.p_xhelper_smart.p_xhelper_smart.impl.WifiShutDownBean;

/*
 * Created by qianli.ma on 2019/8/15 0015.
 */
public class BaseFrag extends RootFrag {

    @Override
    public int onInflateLayout() {
        return 0;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        // wifi没有连接上的处理
        setEventListener(WifiShutDownBean.class, new RootEventListener<WifiShutDownBean>() {
            @Override
            public void getData(WifiShutDownBean wifiShutDownBean) {
                // 提示连接失败
                toast(R.string.hh70_connect_failed, 3000);
                // 跳转到refresh
                if (activity instanceof SplashActivity) {
                    toFrag(getClass(), RefreshFrag.class, null, true);
                } else if (activity instanceof HomeActivity) {
                    toFragActivity(getClass(), SplashActivity.class, RefreshFrag.class, null, true, true, 0);
                }

            }
        });
    }

    @Override
    public boolean onBackPresss() {
        return false;
    }
}
