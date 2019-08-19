package com.alcatel.wifilink.root.ue.root_activity;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.app.SmartLinkV3App;
import com.alcatel.wifilink.root.bean.Other_PinPukBean;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.helper.WpsHelper;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.SP;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.HeartBeatHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// TOGO 2019/8/17 0017 WizardFrag
@Deprecated
public class WizardRxActivity extends BaseActivityWithBack {


    @BindView(R.id.iv_wizardrx_banner_back)
    ImageView ivWizardrxBannerBack;
    @BindView(R.id.tv_wizardrx_banner_skip)
    TextView tvWizardrxBannerSkip;

    @BindView(R.id.rl_sim_rx)
    RelativeLayout rlSimRx;
    @BindView(R.id.iv_sim_rx)
    ImageView ivSimRx;
    @BindView(R.id.tv_sim_rx)
    TextView tvSimRx;

    @BindView(R.id.v_rx_split_wizard)
    View vRxSplitWizard;// 分割线
    @BindView(R.id.tv_rx_split_wizard)
    TextView tvRxSplitWizard;// 分割文字

    @BindView(R.id.rl_wan_rx)
    RelativeLayout rlWanRx;
    @BindView(R.id.iv_wan_rx)
    ImageView ivWanRx;
    @BindView(R.id.tv_wan_rx)
    TextView tvWanRx;


    private TimerHelper heartTimerHelper;// 心跳定时器
    private TimerHelper connectTimer;// 连接定时器
    private Drawable sim_unchecked_pic;
    private Drawable sim_checked_pic;
    private Drawable wan_unchecked_pic;
    private Drawable wan_checked_pic;
    private String sim_checked_str;
    private String sim_unchecked_str;
    private String wan_checked_str;
    private String wan_unchecked_str;
    private int red_color;
    private int blue_color;
    private ProgressDialog pgd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmartLinkV3App.getContextInstance().add(this);
        setContentView(R.layout.activity_wizard_rx);
        ButterKnife.bind(this);
        Logs.t("ma_login").vv("wizard onCreate");
        initRes();
    }

    // TOGO 2019/8/17 0017 roothiber timer
    @Override
    protected void onResume() {
        super.onResume();
        Logs.t("ma_login").vv("wizard onResume");
        // 启动心跳防止登出
        heartTimer();
        // 启动定时器
        connectTimer();
    }

    // TOGO 2019/8/17 0017 
    private void initRes() {
        sim_unchecked_pic = getResources().getDrawable(R.drawable.results_sim_dis);
        sim_checked_pic = getResources().getDrawable(R.drawable.results_sim_nor);
        wan_unchecked_pic = getResources().getDrawable(R.drawable.results_wan_dis);
        wan_checked_pic = getResources().getDrawable(R.drawable.results_wan_nor);
        sim_checked_str = getResources().getString(R.string.connect_type_select_sim_card_enable);
        sim_unchecked_str = getResources().getString(R.string.connect_type_select_sim_card_disable);
        wan_checked_str = getResources().getString(R.string.connect_type_select_wan_port_enable);
        wan_unchecked_str = getResources().getString(R.string.connect_type_select_wan_port_disable);
        red_color = getResources().getColor(R.color.color_red);
        blue_color = getResources().getColor(R.color.mg_blue);
    }

    // TOGO 2019/8/17 0017 
    @Override
    public void onBackPressed() {
        logout();
    }

    // TOGO 2019/8/17 0017 
    private void logout() {
        LogoutHelper xLogouthelper = new LogoutHelper();
        xLogouthelper.setOnLogoutSuccessListener(() -> to(LoginRxActivity.class));
        xLogouthelper.setOnLogOutFailedListener(() -> ToastUtil_m.show(this, getString(R.string.login_logout_failed)));
        xLogouthelper.logout();
    }

    // TOGO 2019/8/17 0017 roothiber-hiber
    /**
     * 检测连接信号
     */
    private void connectTimer() {
        connectTimer = new TimerHelper(this) {
            @Override
            public void doSomething() {
                // 检测设备是否为MW120新型设备
                GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
                xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(attr -> {
                    boolean isMw120 = attr.getDeviceName().toLowerCase().startsWith(Cons.MW_SERIAL);
                    vRxSplitWizard.setVisibility(isMw120 ? View.GONE : View.VISIBLE);
                    tvRxSplitWizard.setVisibility(isMw120 ? View.GONE : View.VISIBLE);
                    rlWanRx.setVisibility(isMw120 ? View.GONE : View.VISIBLE);
                });
                xGetSystemInfoHelper.getSystemInfo();


                // wifi是否生效
                boolean iswifi = OtherUtils.isWifiConnect(WizardRxActivity.this);
                if (iswifi) {
                    // 检测WAN口连接
                    GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
                    xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(result -> {
                        boolean isWanConnect = result.getStatus() == GetWanSettingsBean.CONS_CONNECTED;
                        ivWanRx.setImageDrawable(isWanConnect ? wan_checked_pic : wan_unchecked_pic);
                        tvWanRx.setText(isWanConnect ? wan_checked_str : wan_unchecked_str);
                        tvWanRx.setTextColor(isWanConnect ? blue_color : red_color);
                    });
                    xGetWanSettingsHelper.setOnGetWanSettingFailedListener(() -> {
                        ivWanRx.setImageDrawable(wan_unchecked_pic);
                        tvWanRx.setText(wan_unchecked_str);
                        tvWanRx.setTextColor(red_color);
                    });
                    xGetWanSettingsHelper.getWanSettings();

                    // 检测SIM卡连接

                    GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
                    xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
                        int simState = result.getSIMState();
                        boolean isSimEffect = simState == GetSimStatusBean.CONS_PIN_REQUIRED | simState == GetSimStatusBean.CONS_PUK_REQUIRED | simState == GetSimStatusBean.CONS_SIM_CARD_READY;
                        ivSimRx.setImageDrawable(isSimEffect ? sim_checked_pic : sim_unchecked_pic);
                        tvSimRx.setText(isSimEffect ? sim_checked_str : sim_unchecked_str);
                        tvSimRx.setTextColor(isSimEffect ? blue_color : red_color);
                    });
                    xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
                        ivSimRx.setImageDrawable(sim_unchecked_pic);
                        tvSimRx.setText(sim_unchecked_str);
                        tvSimRx.setTextColor(red_color);
                    });
                    xGetSimStatusHelper.getSimStatus();

                } else {
                    wifiDisconnect();// wifi失效
                }
            }
        };
        connectTimer.start(3000);
        OtherUtils.timerList.add(connectTimer);
    }

    // TOGO 2019/8/17 0017 basefrag
    /**
     * wifi失效
     */
    private void wifiDisconnect() {
        Log.v("ma_couldn_connect", "WizardRx wifiDisconnect error");
        toast(R.string.connect_failed);
        to(RefreshWifiRxActivity.class);
    }

    // TOGO 2019/8/17 0017 mainActivity
    private void heartTimer() {
        heartTimerHelper = new TimerHelper(this) {
            @Override
            public void doSomething() {
                HeartBeatHelper xHeartBeatHelper = new HeartBeatHelper();
                xHeartBeatHelper.setOnHeartbeanFailedListener(() -> to(RefreshWifiRxActivity.class));
                xHeartBeatHelper.heartbeat();
            }
        };
        heartTimerHelper.start(3000);
    }

    @OnClick({R.id.tv_wizardrx_banner_skip,// skip
            R.id.iv_wizardrx_banner_back,// back
            R.id.rl_sim_rx,// sim
            R.id.rl_wan_rx})// wan
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wizardrx_banner_back:
                logout();
                break;
            case R.id.tv_wizardrx_banner_skip:
                Logs.i("ma_login", "put wizard true");
                SP.getInstance(WizardRxActivity.this).putBoolean(Cons.WIZARD_RX, true);
                to(WifiInitRxActivity.class);
                break;
            case R.id.rl_sim_rx:
                clickSimRl();
                break;
            case R.id.rl_wan_rx:
                clickWanRl();
                break;
        }
    }

    // TOGO 2019/8/17 0017 clickwan
    /**
     * 点击了WAN口模式
     */
    private void clickWanRl() {
        if (OtherUtils.isWifiConnect(this)) {
            if (pgd == null) {
                pgd = OtherUtils.showProgressPop(this);
            }

            GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
            xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(result -> {
                int status = result.getStatus();
                if (status == GetWanSettingsBean.CONS_CONNECTED) {
                    OtherUtils.hideProgressPop(pgd);
                    if (SP.getInstance(WizardRxActivity.this).getBoolean(Cons.WANMODE_RX, false)) {
                        if (SP.getInstance(WizardRxActivity.this).getBoolean(Cons.WIFIINIT_RX, false)) {
                            to(HomeRxActivity.class);
                        } else {
                            checkWps();
                        }
                    } else {
                        to(WanModeRxActivity.class);
                    }
                } else if (status == GetWanSettingsBean.CONS_CONNECTING) {
                    clickWanRl();
                } else {
                    OtherUtils.hideProgressPop(pgd);
                    toast(R.string.connect_type_select_wan_port_disable);
                }
            });
            xGetWanSettingsHelper.setOnGetWanSettingFailedListener(() -> {
                toast(R.string.connect_failed);
                OtherUtils.hideProgressPop(pgd);
                to(RefreshWifiRxActivity.class);
            });
            xGetWanSettingsHelper.getWanSettings();

        } else {
            wifiDisconnect();
        }
    }

    // TOGO: 2019/8/17 0017  clicksim
    /**
     * 检测是否开启了WPS模式
     */
    private void checkWps() {
        WpsHelper wpsHelper = new WpsHelper();
        wpsHelper.setOnGetWPSSuccessListener(attr -> to(attr ? HomeRxActivity.class : WifiInitRxActivity.class));
        wpsHelper.setOnGetWPSFailedListener(() -> to(HomeRxActivity.class));
        wpsHelper.getWpsStatus();
    }

    // TOGO: 2019/8/17 0017  clicksim
    /**
     * 点击了SIM卡模式
     */
    private void clickSimRl() {
        if (OtherUtils.isWifiConnect(this)) {

            if (pgd == null) {
                pgd = OtherUtils.showProgressPop(this);
            }

            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
                int simState = result.getSIMState();
                if (simState == GetSimStatusBean.CONS_SIM_CARD_READY) {
                    OtherUtils.hideProgressPop(pgd);
                    // 检测是否进入过向导设置
                    if (SP.getInstance(WizardRxActivity.this).getBoolean(Cons.DATAPLAN_RX, false)) {
                        if (SP.getInstance(WizardRxActivity.this).getBoolean(Cons.WIFIINIT_RX, false)) {
                            to(HomeRxActivity.class);
                        } else {
                            checkWps();// 检测是否为WPS,true:则不允许进入wifi修改界面
                        }
                    } else {
                        EventBus.getDefault().postSticky(Cons.WIZARD_RX);
                        to(DataPlanRxActivity.class);
                    }
                    return;
                }
                if (simState == GetSimStatusBean.CONS_NOWN) {
                    OtherUtils.hideProgressPop(pgd);
                    toast(R.string.connect_type_select_sim_card_disable);
                    return;
                }
                if (simState == GetSimStatusBean.CONS_SIM_CARD_DETECTED || simState == GetSimStatusBean.CONS_SIM_CARD_IS_INITING) {
                    clickSimRl();
                    return;
                }
                if (simState == GetSimStatusBean.CONS_SIM_LOCK_REQUIRED) {
                    OtherUtils.hideProgressPop(pgd);
                    toast(R.string.Home_SimLock_Required);
                    return;
                }
                if (simState == GetSimStatusBean.CONS_SIM_CARD_ILLEGAL) {
                    OtherUtils.hideProgressPop(pgd);
                    toast(R.string.Home_sim_invalid);
                    return;
                }
                if (simState == GetSimStatusBean.CONS_PIN_REQUIRED) {
                    OtherUtils.hideProgressPop(pgd);
                    EventBus.getDefault().postSticky(new Other_PinPukBean(Cons.PIN_FLAG));
                    EventBus.getDefault().postSticky(Cons.WIZARD_RX);
                    to(PinPukIndexRxActivity.class);
                    return;
                }
                if (simState == GetSimStatusBean.CONS_PUK_REQUIRED) {
                    OtherUtils.hideProgressPop(pgd);
                    EventBus.getDefault().postSticky(new Other_PinPukBean(Cons.PUK_FLAG));
                    EventBus.getDefault().postSticky(Cons.WIZARD_RX);
                    to(PinPukIndexRxActivity.class);
                    return;
                }
                if (simState == GetSimStatusBean.CONS_PUK_TIMES_USED_OUT) {
                    OtherUtils.hideProgressPop(pgd);
                    toast(R.string.puk_alarm_des1);
                    return;
                }
            });
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
                toast(R.string.connect_failed);
                OtherUtils.hideProgressPop(pgd);
                to(RefreshWifiRxActivity.class);
            });
            xGetSimStatusHelper.getSimStatus();
        } else {
            wifiDisconnect();
        }
    }

    // TOGO 2019/8/17 0017 roothiber
    /**
     * 跳转ACTIVITY
     *
     * @param target
     */
    public void to(Class target) {
        CA.toActivity(this, target, false, true, false, 0);
    }

    // TOGO 2019/8/17 0017 roothiber
    /**
     * 显示吐司
     *
     * @param resId
     */
    public void toast(int resId) {
        ToastUtil_m.show(WizardRxActivity.this, getString(resId));
    }

    // TOGO 2019/8/17 0017 roothiber + mainactivity
    @Override
    protected void onPause() {
        super.onPause();
        // 停止心跳定时器
        OtherUtils.stopHeartBeat(heartTimerHelper);
        // 停止检测连接定时器
        connectTimer.stop();
    }
}
