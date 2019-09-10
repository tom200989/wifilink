package com.alcatel.wifilink.root.ue.frag;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.widget.HH70_CheckWifiWidget;
import com.hiber.cons.TimerState;
import com.hiber.hiber.RootFrag;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.utils.SmartUtils;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/12 0012.
 */
public class RefreshFrag extends RootFrag {

    @BindView(R.id.bt_refresh)
    Button btRefresh;
    @BindView(R.id.wd_refresh_connect_wifi)
    HH70_CheckWifiWidget wdCheckWifi;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_refresh;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        init();
        setClickEvent();
        timerState = TimerState.ON_BUT_OFF_WHEN_HIDE_AND_PAUSE;
    }

    @Override
    public void setTimerTask() {
        if (SmartUtils.isWifiOn(activity)) {
            GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
            xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> toFrag(getClass(), LoginFrag.class, null, true, getClass()));
            xGetLoginStateHelper.getLoginState();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        wdCheckWifi.setVisibility(View.GONE);
    }

    /**
     * 设置点击事件
     */
    private void setClickEvent() {
        // 点击OK
        wdCheckWifi.setOnRefreshClickOkListener(() -> {
            // 隐藏提示会话框
            wdCheckWifi.setVisibility(View.GONE);
            // 跳转到setting
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        });
        // 点击refresh
        btRefresh.setOnClickListener(v -> checkNet());
    }

    private void checkNet() {
        if (SmartUtils.isWifiOn(activity)) {
            GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
            xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> toFrag(getClass(), LoginFrag.class, null, true, getClass()));
            xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> wdCheckWifi.setVisibility(View.VISIBLE));
            xGetLoginStateHelper.getLoginState();
        } else {
            wdCheckWifi.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onBackPresss() {
        killAllActivitys();
        kill();
        return true;
    }
}
