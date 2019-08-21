package com.alcatel.wifilink.root.ue.activity;

import android.os.Bundle;

import com.alcatel.wifilink.BuildConfig;
import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.GetSMSUnreadHelper;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.ue.frag.AboutFrag;
import com.alcatel.wifilink.root.ue.frag.EtherWANFrag;
import com.alcatel.wifilink.root.ue.frag.InternetStatusFrag;
import com.alcatel.wifilink.root.ue.frag.LanguageFrag;
import com.alcatel.wifilink.root.ue.frag.SettingAccountFrag;
import com.alcatel.wifilink.root.ue.frag.SettingFrag;
import com.alcatel.wifilink.root.ue.frag.SettingShareFrag;
import com.alcatel.wifilink.root.ue.frag.SmsFrag;
import com.alcatel.wifilink.root.ue.frag.WifiFrag;
import com.alcatel.wifilink.root.ue.frag.WlanFrag;
import com.alcatel.wifilink.root.ue.frag.mainFrag;
import com.alcatel.wifilink.root.widget.HH70_HomeTabWidget;
import com.hiber.bean.RootProperty;
import com.hiber.hiber.RootMAActivity;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.HeartBeatHelper;

import java.lang.reflect.Field;

public class HomeActivity extends RootMAActivity {

    private TimerHelper heartTimer;

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
    };

    private HH70_HomeTabWidget wdTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        modifyLint();// 暂时屏蔽检查
        super.onCreate(savedInstanceState);
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
        wdTab.setOnClickTabListener(position -> {
            switch (position) {
                case 0:// 主页
                    toFrag(getClass(), mainFrag.class, null, false, 0);
                    break;
                case 1:// WIFI
                    toFrag(getClass(), WifiFrag.class, null, false, 0);
                    break;
                case 2:// SMS
                    checkSIMCard();
                    break;
                case 3:// SETTING
                    toFrag(getClass(), SettingFrag.class, null, false, 0);
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
                            toast(R.string.not_inserted, 5000);
                            break;
                    }
                });
                xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> toast(R.string.check_sim_normal_or_no_cost, 5000));
                xGetSimStatusHelper.getSimStatus();
            } else {
                toast(R.string.login_failed, 5000);
            }
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> toast(R.string.check_sim_normal_or_no_cost, 5000));
        xGetLoginStateHelper.getLoginState();
    }

    @Override
    public RootProperty initProperty() {
        startHeartTimer();// 启动心跳检测定时器
        return getProperty();
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
     * 停止心跳检测定时器
     */
    private void stopHeartTimer() {
        // 停止定时器
        if (heartTimer != null) {
            heartTimer.stop();
            heartTimer = null;
        }
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

}
