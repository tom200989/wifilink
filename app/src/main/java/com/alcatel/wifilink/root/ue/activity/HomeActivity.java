package com.alcatel.wifilink.root.ue.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.alcatel.wifilink.BuildConfig;
import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.TabBean;
import com.alcatel.wifilink.root.helper.GetSMSUnreadHelper;
import com.alcatel.wifilink.root.ue.frag.AboutFrag;
import com.alcatel.wifilink.root.ue.frag.DeviceBlockFrag;
import com.alcatel.wifilink.root.ue.frag.DeviceConnectFrag;
import com.alcatel.wifilink.root.ue.frag.EtherWANFrag;
import com.alcatel.wifilink.root.ue.frag.FeedbackFrag;
import com.alcatel.wifilink.root.ue.frag.InternetStatusFrag;
import com.alcatel.wifilink.root.ue.frag.LanguageFrag;
import com.alcatel.wifilink.root.ue.frag.LoginFrag;
import com.alcatel.wifilink.root.ue.frag.MobileNetworkFrag;
import com.alcatel.wifilink.root.ue.frag.PinRxFrag;
import com.alcatel.wifilink.root.ue.frag.PukRxFrag;
import com.alcatel.wifilink.root.ue.frag.SetDataPlanFrag;
import com.alcatel.wifilink.root.ue.frag.SettingAccountFrag;
import com.alcatel.wifilink.root.ue.frag.SettingFrag;
import com.alcatel.wifilink.root.ue.frag.SettingNetworkFrag;
import com.alcatel.wifilink.root.ue.frag.SettingShareFrag;
import com.alcatel.wifilink.root.ue.frag.SmsDetailFrag;
import com.alcatel.wifilink.root.ue.frag.SmsFrag;
import com.alcatel.wifilink.root.ue.frag.SmsNewFrag;
import com.alcatel.wifilink.root.ue.frag.UsageRxFrag;
import com.alcatel.wifilink.root.ue.frag.WifiExtenderRxFrag;
import com.alcatel.wifilink.root.ue.frag.WifiFrag;
import com.alcatel.wifilink.root.ue.frag.WlanFrag;
import com.alcatel.wifilink.root.ue.frag.mainFrag;
import com.alcatel.wifilink.root.widget.HH70_HomeTabWidget;
import com.hiber.bean.RootProperty;
import com.hiber.hiber.RootMAActivity;
import com.hiber.impl.RootEventListener;
import com.hiber.tools.TimerHelper;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.HeartBeatHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;

import java.lang.reflect.Field;
import java.util.HashMap;

public class HomeActivity extends RootMAActivity {

    private TimerHelper heartTimer;
    private TimerHelper touchTimer;

    Class[] frags = {// fragmengs
            mainFrag.class, // 主页
            WifiFrag.class, // WIFI
            SmsFrag.class, // SMS
            SettingFrag.class, // 设置
            InternetStatusFrag.class, // 网络连接状态页
            EtherWANFrag.class, // 网络类型页
            AboutFrag.class, // 说明页
            SettingAccountFrag.class, // 设置用户页
            SettingShareFrag.class, // 共享设备页
            WlanFrag.class, // 共享设备页
            LanguageFrag.class, // 选择语言页
            SettingNetworkFrag.class, // SIM设置页
            FeedbackFrag.class,//反馈页面
            MobileNetworkFrag.class,// 网络
            SetDataPlanFrag.class,//设置流量计划页面
            SmsNewFrag.class,//新建短信页面
            SmsDetailFrag.class,//sms的详情页InternetStatusFrag
            DeviceBlockFrag.class, // DeviceBlock
            DeviceConnectFrag.class, // DeviceConnect
            PinRxFrag.class,// Pin
            PukRxFrag.class,// Puk
            UsageRxFrag.class,// Usage
            WifiExtenderRxFrag.class,// WifiExtender
            InternetStatusFrag.class,//InternetStatus
    };

    private HH70_HomeTabWidget wdTab;
    public static HashMap<Long, Integer> smsUnreadMap = new HashMap<>();// 未读消息缓冲集合
    private int tmp;// 临时索标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        modifyLint();// 暂时屏蔽检查
        super.onCreate(savedInstanceState);
    }

    @Override
    public RootProperty initProperty() {
        startHeartTimer();// 启动心跳检测定时器
        initEvent();
        return getProperty();
    }

    private void initEvent() {
        // 底部栏显隐监听
        setEventListener(TabBean.class, new RootEventListener<TabBean>() {
            @Override
            public void getData(TabBean tabBean) {
                if (tabBean != null) {
                    wdTab.setVisibility(tabBean.isShowTab() ? View.VISIBLE : View.GONE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTouchTimer();// 启动触摸检测定时器(息屏, 后台回来等需要重置定时器)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopHeartTimer();
        stopTouchTimer();
    }

    private RootProperty getProperty() {
        RootProperty rootProperty = new RootProperty();
        rootProperty.setColorStatusBar(R.color.color_009aff);
        rootProperty.setContainId(R.id.hh70_home_fl);
        rootProperty.setFragmentClazzs(frags);
        rootProperty.setFullScreen(true);
        rootProperty.setLayoutId(R.layout.hh70_activity_home);
        rootProperty.setSaveInstanceState(false);
        rootProperty.setPackageName(BuildConfig.APPLICATION_ID);
        return rootProperty;
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

    /**
     * 设置TAB
     */
    private void setTab() {
        wdTab = findViewById(R.id.wd_home_tab);
        wdTab.setCheck(0);
        tmp = 0;
        wdTab.setOnClickTabListener(position -> {
            switch (position) {
                case 0:// 主页
                    if (tmp != 0) {
                        toFrag(getClass(), mainFrag.class, null, false, 0);
                        tmp = 0;
                    }
                    break;
                case 1:// WIFI
                    if (tmp != 1) {
                        toFrag(getClass(), WifiFrag.class, null, false, 0);
                        tmp = 1;
                    }
                    break;
                case 2:// SMS
                    if (tmp != 2) {
                        checkSIMCard();
                        tmp = 2;
                    }
                    break;
                case 3:// SETTING
                    if (tmp != 3) {
                        toFrag(getClass(), SettingFrag.class, null, false, 0);
                        tmp = 3;
                    }
                    break;
            }
        });
    }

    /**
     * 检查SIM状态
     */
    private void checkSIMCard() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
                xGetSimStatusHelper.setOnGetSimStatusSuccessListener(getSimStatusBean -> {
                    switch (getSimStatusBean.getSIMState()) {
                        case GetSimStatusBean.CONS_SIM_CARD_READY:
                            toFrag(getClass(), SmsFrag.class, null, false, 0);
                            break;
                        case GetSimStatusBean.CONS_NOWN:
                            toast(R.string.hh70_insert_sim_first, 5000);
                            break;
                    }
                });
                xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> toast(R.string.hh70_check_sim_cost, 5000));
                xGetSimStatusHelper.getSimStatus();
            } else {
                toast(R.string.hh70_cant_login, 5000);
            }
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> toast(R.string.hh70_check_sim_cost, 5000));
        xGetLoginStateHelper.getLoginState();
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
                // 触发心跳
                HeartBeatHelper xHeartBeatHelper = new HeartBeatHelper();
                xHeartBeatHelper.heartbeat();
                // 获取短信未读数
                getSMSUnread();
            } else {
                heartTimer.stop();
                touchTimer.stop();
                if (!LoginFrag.currentIsLoginPage) {
                    toast(R.string.hh70_log_out, 3000);
                    toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true, true, 0);
                }
            }
        });
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 获取短信未读数
     */
    private void getSMSUnread() {
        GetSMSUnreadHelper unreadHelper = new GetSMSUnreadHelper();
        unreadHelper.setOnGetSMSUnreadSuccessListener(count -> wdTab.setSMSDot(count > 0));
        unreadHelper.setOnGetSMSUnreadFailedListener(() -> wdTab.setSMSDot(false));
        unreadHelper.getSMSUnread();
    }

    /**
     * 启动触摸检测定时器
     */
    private void startTouchTimer() {
        stopTouchTimer();
        touchTimer = new TimerHelper(this) {
            @Override
            public void doSomething() {
                logOut();// 如果该定时器的业务触发 -- 即说明超时, 需登出
            }
        };
        touchTimer.startDelay(5 * 60 * 1000);// 延迟5分钟启动
    }

    /**
     * 登出
     */
    private void logOut() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                xGetLoginStateHelper.getLoginState();
                LogoutHelper xLogoutHelper = new LogoutHelper();
                xLogoutHelper.setOnLogoutSuccessListener(() -> {
                    toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true);
                    if (touchTimer != null) {
                        touchTimer.stop();
                    }
                });
                xLogoutHelper.setOnLogOutFailedListener(() -> {
                    if (!LoginFrag.currentIsLoginPage) {
                        toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true);
                    }
                    if (touchTimer != null) {
                        touchTimer.stop();
                    }
                });
                xLogoutHelper.logout();
            } else {
                if (!LoginFrag.currentIsLoginPage) {
                    toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true);
                }
                if (touchTimer != null) {
                    touchTimer.stop();
                }
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 按下或者移动过程 -- 停止触摸定时器
                stopTouchTimer();
                break;
            case MotionEvent.ACTION_UP:
                // 抬起过程 -- 重启触摸定时器
                startTouchTimer();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void initViewFinish(int layoutId) {
        super.initViewFinish(layoutId);
        setTab();// 设置TAB
    }

    @Override
    public void onNexts() {

    }

    @Override
    public boolean onBackClick() {
        return false;
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

    /**
     * 停止触摸检测定时器
     */
    private void stopTouchTimer() {
        // 停止定时器
        if (touchTimer != null) {
            touchTimer.stop();
            touchTimer = null;
        }
    }

}
