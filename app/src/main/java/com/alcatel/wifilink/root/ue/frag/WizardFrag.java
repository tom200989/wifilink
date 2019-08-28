package com.alcatel.wifilink.root.ue.frag;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.WpsHelper;
import com.alcatel.wifilink.root.ue.activity.HomeActivity;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.hiber.cons.TimerState;
import com.hiber.hiber.RootFrag;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/16 0016.
 */
public class WizardFrag extends RootFrag {

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

    @BindView(R.id.wd_wizard_load)
    HH70_LoadWidget wdLoad;

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

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_wizard;
    }

    @Override
    public void initViewFinish(View inflateView) {
        sim_unchecked_pic = getResources().getDrawable(R.drawable.results_sim_dis);
        sim_checked_pic = getResources().getDrawable(R.drawable.results_sim_nor);
        wan_unchecked_pic = getResources().getDrawable(R.drawable.results_wan_dis);
        wan_checked_pic = getResources().getDrawable(R.drawable.results_wan_nor);
        sim_checked_str = getResources().getString(R.string.hh70_use_broadband);
        sim_unchecked_str = getResources().getString(R.string.hh70_insert_sim_first);
        wan_checked_str = getResources().getString(R.string.hh70_always_ethernet_connect);
        wan_unchecked_str = getResources().getString(R.string.hh70_insert_inter_wan);
        red_color = getResources().getColor(R.color.color_red);
        blue_color = getResources().getColor(R.color.mg_blue);
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        timerState = TimerState.OFF_ALL_BUT_KEEP_CURRENT_OFF_WHEN_PAUSE;
        initClick();
    }

    /**
     * 点击事件
     */
    private void initClick() {
        // 回退
        ivWizardrxBannerBack.setOnClickListener(v -> onBackPresss());
        // 跳过
        tvWizardrxBannerSkip.setOnClickListener(v -> {
            ShareUtils.set(RootCons.SP_WIZARD, true);
            toFrag(getClass(), WifiInitFrag.class, null, true);
        });
        // 点击SIM面板
        clickSim();
        // 点击WAN面板
        clickWan();
    }

    /**
     * 点击WAN面板
     */
    private void clickWan() {
        GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
        xGetWanSettingsHelper.setOnPrepareHelperListener(() -> wdLoad.setVisibles());
        xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(result -> {
            int wanStatus = result.getStatus();
            switch (wanStatus) {
                case GetWanSettingsBean.CONS_CONNECTED:// 已连接
                    wdLoad.setGone();
                    boolean isWanInit = ShareUtils.get(RootCons.SP_WAN_INIT, false);
                    boolean isWifiInit = ShareUtils.get(RootCons.SP_WIFI_INIT, false);
                    if (isWanInit) {
                        if (isWifiInit) {
                            toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
                        } else {
                            checWPS();
                        }
                    } else {
                        lastFrag = getClass();
                        toFrag(getClass(), WanInitFrag.class, null, true);
                    }
                    break;

                case GetWanSettingsBean.CONS_CONNECTING:// 连接中
                    clickWan();
                    break;

                default:// 其他
                    wdLoad.setGone();
                    toast(R.string.hh70_insert_inter_wan, 5000);
                    break;
            }
        });
        xGetWanSettingsHelper.setOnGetWanSettingFailedListener(() -> {
            toast(R.string.hh70_connect_failed, 5000);
            wdLoad.setGone();
            toFrag(getClass(), RefreshFrag.class, null, true);
        });
        xGetWanSettingsHelper.getWanSettings();
    }

    /**
     * 点击SIM面板
     */
    private void clickSim() {
        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnPrepareHelperListener(() -> wdLoad.setVisibles());
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
            int simState = result.getSIMState();

            switch (simState) {
                case GetSimStatusBean.CONS_SIM_CARD_READY:
                    wdLoad.setGone();
                    // 检测是否进入过向导设置
                    boolean isDataPlanInit = ShareUtils.get(RootCons.SP_DATA_PLAN_INIT, false);
                    boolean isWifiInit = ShareUtils.get(RootCons.SP_WIFI_INIT, false);
                    if (isDataPlanInit) {
                        if (isWifiInit) {
                            toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
                        } else {
                            // 检测是否为WPS模式
                            checWPS();
                        }
                    } else {
                        lastFrag = getClass();// 提交临时标记 -- 被dataplan使用
                        toFrag(getClass(), DataPlanInitFrag.class, null, true);
                    }
                    break;

                case GetSimStatusBean.CONS_NOWN:
                    wdLoad.setGone();
                    toast(R.string.hh70_insert_sim_first, 5000);
                    break;

                case GetSimStatusBean.CONS_SIM_CARD_DETECTED:
                case GetSimStatusBean.CONS_SIM_CARD_IS_INITING:
                    clickSim();
                    break;

                case GetSimStatusBean.CONS_SIM_LOCK_REQUIRED:
                    wdLoad.setGone();
                    toast(R.string.hh70_sim_lock, 5000);
                    break;

                case GetSimStatusBean.CONS_SIM_CARD_ILLEGAL:
                    wdLoad.setGone();
                    toast(R.string.hh70_invalid_sim, 5000);
                    break;

                case GetSimStatusBean.CONS_PIN_REQUIRED:
                    wdLoad.setGone();
                    lastFrag = getClass();// 提交临时标记 -- 被dataplan使用
                    toFrag(getClass(), PinInitFrag.class, null, true);
                    break;

                case GetSimStatusBean.CONS_PUK_REQUIRED:
                    wdLoad.setGone();
                    lastFrag = getClass();// 提交临时标记 -- 被dataplan使用
                    toFrag(getClass(), PukInitFrag.class, null, true);
                    break;

                case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                    wdLoad.setGone();
                    toast(R.string.hh70_you_have_incorrect, 5000);
                    break;

            }
        });
        xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
            toast(R.string.hh70_connect_failed, 5000);
            wdLoad.setGone();
            toFrag(getClass(), RefreshFrag.class, null, true);
        });
        xGetSimStatusHelper.getSimStatus();

    }

    /**
     * 检测是否为WPS模式
     */
    private void checWPS() {
        WpsHelper wpsHelper = new WpsHelper();
        wpsHelper.setOnGetWPSSuccessListener(isWPS -> {
            if (isWPS)
                toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
            else
                toFrag(getClass(), WifiInitFrag.class, null, true);
        });
        wpsHelper.setOnGetWPSFailedListener(() -> toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true));
        wpsHelper.getWpsStatus();
    }

    @Override
    public void setTimerTask() {
        switchPanel();// 切换面板
    }

    /**
     * 获取系统各类状态
     */
    private void switchPanel() {

        // 设备是否为MW设备 -- 显示对应的面板
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(getSystemInfoBean -> {
            boolean isMWDev = RootUtils.isMWDEV(getSystemInfoBean.getDeviceName());
            vRxSplitWizard.setVisibility(isMWDev ? View.GONE : View.VISIBLE);
            tvRxSplitWizard.setVisibility(isMWDev ? View.GONE : View.VISIBLE);
            rlWanRx.setVisibility(isMWDev ? View.GONE : View.VISIBLE);
        });
        xGetSystemInfoHelper.getSystemInfo();

        // 检测WAN口连接 -- 显示WAN面板
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

        // 检测SIM卡连接 -- 显示SIM面板
        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
            int simState = result.getSIMState();
            boolean isPin = simState == GetSimStatusBean.CONS_PIN_REQUIRED;
            boolean isPuk = simState == GetSimStatusBean.CONS_PUK_REQUIRED;
            boolean isReady = simState == GetSimStatusBean.CONS_SIM_CARD_READY;
            boolean isSimEnable = isPin | isPuk | isReady;

            ivSimRx.setImageDrawable(isSimEnable ? sim_checked_pic : sim_unchecked_pic);
            tvSimRx.setText(isSimEnable ? sim_checked_str : sim_unchecked_str);
            tvSimRx.setTextColor(isSimEnable ? blue_color : red_color);
        });
        xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
            ivSimRx.setImageDrawable(sim_unchecked_pic);
            tvSimRx.setText(sim_unchecked_str);
            tvSimRx.setTextColor(red_color);
        });
        xGetSimStatusHelper.getSimStatus();

    }

    @Override
    public boolean onBackPresss() {
        logout();
        return false;
    }

    /**
     * 登出
     */
    private void logout() {
        LogoutHelper xLogouthelper = new LogoutHelper();
        xLogouthelper.setOnLogoutSuccessListener(() -> toFrag(getClass(), LoginFrag.class, null, true));
        xLogouthelper.setOnLogOutFailedListener(() -> toast(R.string.hh70_cant_logout, 5000));
        xLogouthelper.logout();
    }
}
