package com.alcatel.wifilink.ue.activity;

import android.os.Bundle;

import com.alcatel.wifilink.BuildConfig;
import com.alcatel.wifilink.R;
import com.alcatel.wifilink.ue.frag.DataPlanInitFrag;
import com.alcatel.wifilink.ue.frag.GuideFrag;
import com.alcatel.wifilink.ue.frag.LoginFrag;
import com.alcatel.wifilink.ue.frag.Login_mw_Frag;
import com.alcatel.wifilink.ue.frag.Login_mw_dev_Frag;
import com.alcatel.wifilink.ue.frag.PinInitFrag;
import com.alcatel.wifilink.ue.frag.PukInitFrag;
import com.alcatel.wifilink.ue.frag.RefreshFrag;
import com.alcatel.wifilink.ue.frag.SplashFrag;
import com.alcatel.wifilink.ue.frag.WanInitFrag;
import com.alcatel.wifilink.ue.frag.WifiInitFrag;
import com.alcatel.wifilink.ue.frag.WizardFrag;
import com.hiber.bean.RootProperty;
import com.hiber.hiber.RootMAActivity;
import com.hiber.tools.TimerHelper;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.HeartBeatHelper;

import java.lang.reflect.Field;

import static com.hiber.hiber.RootFrag.lastFrag;

public class SplashActivity extends RootMAActivity {

    Class[] frags = {// fragments
            SplashFrag.class,// 启动页 -1
            RefreshFrag.class,// 断连页 -2
            LoginFrag.class,// 登陆页 -3
            Login_mw_Frag.class,// 登陆页(MW系列) -4
            Login_mw_dev_Frag.class,// 登陆设备页(MW系列) -5
            GuideFrag.class,// 向导页 -6
            DataPlanInitFrag.class,// 流量设置页 -7
            WanInitFrag.class,// WAN初始设置页 -8
            WifiInitFrag.class,// WIFI初始设置页 -9
            PinInitFrag.class,// PIN页 -10
            PukInitFrag.class,// PUK页 -11
            WizardFrag.class,// 引导页 -12
    };

    public static boolean freeSharingLock = true;// 默认FreeSharing锁止
    private TimerHelper heartTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        modifyLint();// 暂时屏蔽lint检查
        super.onCreate(savedInstanceState);
    }

    @Override
    public RootProperty initProperty() {
        startHeartTimer();// 启动心跳检测定时器
        return getProperty();
    }

    /**
     * 启动心跳检测定时器
     */
    private void startHeartTimer() {
        stopHeartTimer();
        heartTimer = new TimerHelper(this) {
            @Override
            public void doSomething() {
                checkHeart();// 检查心跳
            }
        };
        heartTimer.start(3000);
    }

    /**
     * 检查心跳
     */
    private void checkHeart() {
        // 获取登陆状态
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            // 如果处于登陆状态 -- 维持心跳
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                // TODO: 2020/5/27  以下这些页面不发心跳 - 为了适配HH42 - 可能会有bug
                // todo: 如果有bug则把这些页面的lastfrag=getClass()的赋值语句删除
                if (lastFrag == SplashFrag.class // 
                            | lastFrag == RefreshFrag.class //
                            | lastFrag == LoginFrag.class //
                            | lastFrag == Login_mw_Frag.class //
                            | lastFrag == Login_mw_dev_Frag.class //
                            | lastFrag == GuideFrag.class //
                ) {
                    return;
                }
                HeartBeatHelper xHeartBeatHelper = new HeartBeatHelper();
                xHeartBeatHelper.heartbeat();
            }
        });
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 停止心跳检测定时器
     */
    private void stopHeartTimer() {
        // 停止定时器
        if (heartTimer != null) {
            heartTimer.stop();
            heartTimer = null;
        }
    }

    @Override
    public void onNexts() {

    }

    @Override
    public boolean onBackClick() {
        return false;
    }

    private RootProperty getProperty() {
        RootProperty rootProperty = new RootProperty();
        rootProperty.setColorStatusBar(R.color.color_009aff);
        rootProperty.setContainId(R.id.hh70_splash_fl);
        rootProperty.setFragmentClazzs(frags);
        rootProperty.setFullScreen(true);
        rootProperty.setLayoutId(R.layout.hh70_activity_splash);
        rootProperty.setSaveInstanceState(false);
        rootProperty.setPackageName(BuildConfig.APPLICATION_ID);
        return rootProperty;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopHeartTimer();
    }

    /**
     * 暂时屏蔽lint检查
     */
    public void modifyLint() {
        try {
            // 得到私有字段
            Field privateStringField = RootMAActivity.class.getDeclaredField("isLintCheck");
            // 设置私有变量可以访问
            privateStringField.setAccessible(true);
            privateStringField.set(this, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
