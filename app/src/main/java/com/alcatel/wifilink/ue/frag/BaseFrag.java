package com.alcatel.wifilink.ue.frag;

import android.view.View;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.bean.TabBean;
import com.alcatel.wifilink.ue.activity.HomeActivity;
import com.alcatel.wifilink.ue.activity.SplashActivity;
import com.hiber.hiber.RootFrag;
import com.hiber.impl.RootEventListener;
import com.hiber.tools.Lgg;
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
    public void initViewFinish(View inflateView) {
        // wifi没有连接上的处理
        setEventListener(WifiShutDownBean.class, new RootEventListener<WifiShutDownBean>() {
            @Override
            public void getData(WifiShutDownBean wifiShutDownBean) {
                // 提示连接失败
                toast(R.string.hh70_cant_connect, 3000);
                Lgg.w("roottrack", "BaseFrag wifi off");
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
    public void onNexts(Object o, View view, String s) {
        // 发送隐藏任务栏的监听
        handleTab();
    }

    @Override
    public boolean onBackPresss() {
        return false;
    }

    /**
     * 处理底部选择器
     */
    public void handleTab() {
        // 以下四个界面需要显示tab -- 其余统统不显示
        boolean needShow = this instanceof mainFrag // main
                | this instanceof WifiFrag // wifi
                | this instanceof SmsFrag // sms
                | this instanceof SettingFrag;// setting
        sendEvent(new TabBean(needShow), false);
    }

}
