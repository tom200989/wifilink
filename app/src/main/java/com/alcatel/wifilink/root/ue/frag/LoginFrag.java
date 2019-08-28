package com.alcatel.wifilink.root.ue.frag;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.WpsHelper;
import com.alcatel.wifilink.root.ue.activity.HomeActivity;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.alcatel.wifilink.root.widget.NormalWidget;
import com.hiber.cons.TimerState;
import com.hiber.hiber.RootFrag;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LoginHelper;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/14 0014.
 */
public class LoginFrag extends RootFrag {

    @BindView(R.id.rl_login)
    RelativeLayout rl_login;// 总布局

    @BindView(R.id.iv_login_backToPre)
    ImageView ivLoginBackToPre;// 返回图标
    @BindView(R.id.tv_login_backToPre)
    TextView tvLoginBackToPre;// 返回文本
    @BindView(R.id.tv_login_linkzone)
    TextView tvLoginLinkzone;// linkzone标题

    @BindView(R.id.iv_loginrx_logo)
    ImageView ivLoginrxLogo;// LOGO

    @BindView(R.id.et_loginRx)
    EditText etLoginRx;// 密码框

    @BindView(R.id.rl_login_remenberPsd)
    RelativeLayout rlLoginRemenberPsd;// 记住密码布局
    @BindView(R.id.iv_loginRx_checkbox)
    ImageView ivLoginRxCheckbox;// 勾选框
    @BindView(R.id.tv_remember_psd)
    TextView tvRememberPsd;// 记住密码提示

    @BindView(R.id.bt_loginRx)
    Button btLoginRx;// 登陆按钮

    @BindView(R.id.tv_loginRx_forgot)
    TextView tvLoginRxForgot;// 忘记密码提示

    @BindView(R.id.wd_reset_factory)
    NormalWidget wdResetFactory;// 恢复出厂设置视图

    @BindView(R.id.wd_login_load)
    HH70_LoadWidget wdLoginLoad;// 等待

    private boolean isRemem;// 是否记住密码
    private boolean isMWDev;// 是否为MW系列
    private boolean isHH71;// 是否为71系列
    private Drawable cpe_logo;
    private Drawable mw12_logo;
    private Drawable hh71_logo;
    private Drawable default_logo;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_login;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        timerState = TimerState.OFF_ALL_BUT_KEEP_CURRENT_OFF_WHEN_PAUSE;
        initRes();
        initClick();
    }

    @Override
    public void setTimerTask() {
        // 定时更新设备名
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(getSystemInfobean -> {
            ShareUtils.set(RootCons.DEVICE_NAME, getSystemInfobean.getDeviceName());
            // 根据设备名定时更新UI
            isMWDev = RootUtils.isMWDEV(ShareUtils.get(RootCons.DEVICE_NAME, RootCons.DEVICE_NAME_DEFAULT));
            isHH71 = RootUtils.isHH71(ShareUtils.get(RootCons.DEVICE_NAME, RootCons.DEVICE_NAME_DEFAULT));
            // 设置UI
            ivLoginBackToPre.setVisibility(isMWDev ? View.VISIBLE : View.GONE);// 回退按钮
            tvLoginBackToPre.setVisibility(ivLoginBackToPre.getVisibility());// 回退文本
            tvLoginLinkzone.setText(isMWDev ? "LINKZONE" : "LINKHUB");// 标题
            ivLoginrxLogo.setImageDrawable(isMWDev ? mw12_logo : isHH71 ? hh71_logo : cpe_logo);// 图标
        });
        xGetSystemInfoHelper.setOnAppErrorListener(() -> ivLoginrxLogo.setImageDrawable(default_logo));
        xGetSystemInfoHelper.setOnFwErrorListener(() -> ivLoginrxLogo.setImageDrawable(default_logo));
        xGetSystemInfoHelper.getSystemInfo();
    }

    /**
     * 初始化资源
     */
    private void initRes() {
        cpe_logo = getResources().getDrawable(R.drawable.cpe_login_logo);
        mw12_logo = getResources().getDrawable(R.drawable.mw120_login_logo);
        hh71_logo = getResources().getDrawable(R.drawable.hh71_login_logo);
        default_logo = getResources().getDrawable(R.drawable.ic_launcher);
        isRemem = ShareUtils.get(RootCons.LOGIN_IS_REMEM_PSD, false);
        String password = ShareUtils.get(RootCons.LOGIN_REMEM_PSD, "");
        ivLoginRxCheckbox.setImageResource(isRemem ? R.drawable.general_btn_remember_pre : R.drawable.general_btn_remember_nor);
        etLoginRx.setText(isRemem ? password : "");
        etLoginRx.setSelection(RootUtils.getEDText(etLoginRx).length());
    }

    /**
     * 初始化点击
     */
    private void initClick() {
        // 记住密码
        rlLoginRemenberPsd.setOnClickListener(v -> {
            isRemem = !isRemem;
            ivLoginRxCheckbox.setImageResource(isRemem ? R.drawable.general_btn_remember_pre : R.drawable.general_btn_remember_nor);
        });
        // 登陆按钮
        btLoginRx.setOnClickListener(v -> clickLogin());
        // 忘记密码
        tvLoginRxForgot.setOnClickListener(v -> showResetUI());
        // 返回键
        ivLoginBackToPre.setOnClickListener(v -> onBackPresss());
        tvLoginBackToPre.setOnClickListener(v -> onBackPresss());
    }

    /**
     * 显示重启设备对话框
     */
    private void showResetUI() {
        // 提示重启设备
        wdResetFactory.setVisibility(View.VISIBLE);
        wdResetFactory.setTitle(R.string.reset);
        wdResetFactory.setDes(R.string.reset_tip);
        wdResetFactory.setCancelVisible(false);
        wdResetFactory.setOnOkClickListener(() -> wdResetFactory.setVisibility(View.GONE));
    }

    /**
     * 保存密码
     */
    private void clickLogin() {
        // 1. 检测空值
        String loginText = RootUtils.getEDText(etLoginRx);
        if (TextUtils.isEmpty(loginText)) {
            toast(R.string.hh70_the_psd_should, 5000);
            return;
        }
        // 2.检测密码位数
        int length = loginText.length();
        if (length < 4 | length > 16) {
            toast(R.string.hh70_the_psd_should, 5000);
            return;
        }
        // 3.启动登陆
        toLogin();
    }

    /**
     * 发起登陆请求
     */
    private void toLogin() {
        String password = etLoginRx.getText().toString();
        LoginHelper loginHelper = new LoginHelper();
        loginHelper.setOnPrepareHelperListener(() -> wdLoginLoad.setVisibles());
        // 登录成功
        loginHelper.setOnLoginSuccesListener(() -> {
            savePassword();// 保存密码
            getConnMode();// 获取连接模式
        });
        // 密码错误 -- 提示次数限制
        loginHelper.setOnPsdNotCorrectListener(this::showRemainTimes);
        // 其他原因导致的无法登陆
        loginHelper.setOnLoginFailedListener(() -> {
            wdLoginLoad.setGone();
            toast(R.string.login_failed, 5000);
        });
        // 超出次数需要重启
        loginHelper.setOnDeviceRebootListener(() -> {
            wdLoginLoad.setGone();
            toast(R.string.hh70_login_used_out, 5000);
        });
        // WEB端被登陆
        loginHelper.setOnGuestWebUiListener(() -> {
            wdLoginLoad.setGone();
            toast(R.string.hh70_ap_cant_webui, 5000);
        });
        // 其他用户登陆
        loginHelper.setOnOtherUserLoginListener(() -> {
            wdLoginLoad.setGone();
            toast(R.string.hh70_other_use_login, 5000);
        });
        // 登陆出错超限
        loginHelper.setOnLoginOutTimeListener(() -> {
            wdLoginLoad.setGone();
            toast(R.string.hh70_login_used_out, 5000);
        });
        // 正式请求
        loginHelper.login("admin", password);
    }

    /**
     * 保存密码
     */
    private void savePassword() {
        // 3.保存密码
        ShareUtils.set(RootCons.LOGIN_IS_REMEM_PSD, isRemem);
        ShareUtils.set(RootCons.LOGIN_REMEM_PSD, isRemem ? RootUtils.getEDText(etLoginRx) : "");
    }

    /**
     * 检测连接模式
     */
    private void getConnMode() {
        /* 这一步可能会在MW设备上获取WAN口失败
         * 如果失败 -- 则当前设备是MW设备
         * 如果成功 -- 则是HH设备
         */
        GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
        xGetWanSettingsHelper.setOnGetWanSettingFailedListener(() -> {
            wdLoginLoad.setGone();
            getWanError();
        });
        xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(getWanSettingsBean -> {
            // 获取WAN口状态
            int wanStatus = getWanSettingsBean.getStatus();
            boolean isWan = wanStatus == GetWanSettingsBean.CONS_CONNECTED;

            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(getSimStatusBean -> {
                wdLoginLoad.setGone();

                // 获取SIM卡状态
                int simState = getSimStatusBean.getSIMState();
                boolean isSim = simState == GetSimStatusBean.CONS_SIM_CARD_READY // ready
                                        || simState == GetSimStatusBean.CONS_PIN_REQUIRED // pin
                                        || simState == GetSimStatusBean.CONS_PUK_REQUIRED;// puk

                if ((isWan & isSim) || (!isWan & !isSim)) {
                    // ［sim和wan都连接］或者［sim和wan都不连接］
                    to_Wizard_Home();

                } else if (isSim) {
                    // 仅有sim
                    handleSim(getSimStatusBean, false);

                } else {
                    // 仅有wan
                    wanReady();

                }
            });
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(this::to_Wizard_Home);
            xGetSimStatusHelper.getSimStatus();
        });
        xGetWanSettingsHelper.getWanSettings();
    }

    /**
     * 是否进入连接设置引导页
     */
    private void to_Wizard_Home() {
        boolean isWizard = ShareUtils.get(RootCons.SP_WIZARD, false);
        if (isWizard) {
            toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
        } else {
            toFrag(getClass(), WizardFrag.class, null, true);
        }
    }

    /**
     * SIM卡能正常使用后
     * (包含解PIN完毕后 | SIM本身已经ready)
     */
    private void simReady(boolean isMWDev) {

        // 1.是否进入过[流量向导界面][WIFI初始界面]
        boolean isDataPlanInit = ShareUtils.get(RootCons.SP_DATA_PLAN_INIT, false);
        boolean isWifiInit = ShareUtils.get(RootCons.SP_WIFI_INIT, false);

        // 2.进入过流量向导页
        if (isDataPlanInit) {
            // 3.进入过WIFI初始设置页
            if (isWifiInit) {
                toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
            } else if (!isMWDev) {// 4.是否为HH系列设备
                checkWps();// 5.检测wps状态
            } else {
                toFrag(getClass(), WifiInitFrag.class, null, true);
            }
        } else {
            toFrag(getClass(), DataPlanInitFrag.class, null, true);
        }
    }

    /**
     * WAN口连接完成
     */
    private void wanReady() {
        // 0.是否进入过[WAN设置界面][WIFI初始界面]
        boolean isWanInit = ShareUtils.get(RootCons.SP_WAN_INIT, false);
        boolean isWifiInit = ShareUtils.get(RootCons.SP_WIFI_INIT, false);

        // 1.是否进入过wan口设置向导页
        if (isWanInit) {
            // 2.是否进入过wifi向导页
            if (isWifiInit) {
                toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
            } else {
                // 3.检测是否开启了WPS模式
                checkWps();
            }
        } else {
            // WAN口设置页
            toFrag(getClass(), WanInitFrag.class, null, true);
        }
    }

    /**
     * 检测是否开启了WPS模式
     */
    private void checkWps() {
        WpsHelper wpsHelper = new WpsHelper();
        wpsHelper.setOnGetWPSSuccessListener(isWPS -> {
            if (isWPS)
                toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
            else
                toFrag(getClass(), WifiInitFrag.class, null, true);
        });
        wpsHelper.setOnGetWPSFailedListener(() ->  toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true));
        wpsHelper.getWpsStatus();
    }

    /**
     * 当获取wan口失败做出设备类型判断
     */
    private void getWanError() {
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(systemInfo -> {
            boolean isMWDev = RootUtils.isMWDEV(systemInfo.getDeviceName());
            if (isMWDev) {// MW设备
                GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
                xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> handleSim(result, true));
                xGetSimStatusHelper.setOnGetSimStatusFailedListener(this::to_Wizard_Home);
                xGetSimStatusHelper.getSimStatus();
            } else {// HH设备
                toast(R.string.login_failed, 5000);
            }
        });
        xGetSystemInfoHelper.setOnFwErrorListener(() -> toast(R.string.login_failed, 5000));
        xGetSystemInfoHelper.setOnAppErrorListener(() -> toast(R.string.login_failed, 5000));
        xGetSystemInfoHelper.getSystemInfo();
    }

    /**
     * 处理SIM卡状态
     *
     * @param result  获取到的SIM卡对象
     * @param isMWDev 是否MW设备
     */
    private void handleSim(GetSimStatusBean result, boolean isMWDev) {
        switch (result.getSIMState()) {
            case GetSimStatusBean.CONS_SIM_CARD_READY:
                simReady(isMWDev);
                break;
            case GetSimStatusBean.CONS_PIN_REQUIRED:
                toFrag(getClass(), PinInitFrag.class, null, true);
                break;
            case GetSimStatusBean.CONS_PUK_REQUIRED:
                toFrag(getClass(), PukInitFrag.class, null, true);
                break;
            default:
                toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
                break;
        }
    }

    /**
     * 显示剩余次数
     */
    public void showRemainTimes() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            wdLoginLoad.setGone();
            String content = "";
            int remainingTimes = getLoginStateBean.getLoginRemainingTimes();
            String noRemain = getString(R.string.hh70_login_used_out);
            String tips = getString(R.string.login_psd_error_msg);
            String remainTips = String.format(tips, remainingTimes);
            content = remainingTimes <= 0 ? noRemain : remainTips;
            toast(content, 5000);
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> wdLoginLoad.setVisibles());
        xGetLoginStateHelper.getLoginState();
    }

    @Override
    public boolean onBackPresss() {

        // 恢复出厂窗口
        if (wdResetFactory.getVisibility() == View.VISIBLE) {
            wdResetFactory.setVisibility(View.GONE);
            return true;
        }

        // 根据设备类型进行回退操作
        String cacheDevice = ShareUtils.get(RootCons.DEVICE_NAME, RootCons.DEVICE_NAME_DEFAULT);
        boolean isMWDev = RootUtils.isMWDEV(cacheDevice);
        if (isMWDev) {
            toFrag(getClass(), LoginFrag_mw.class, null, true);
        } else {
            killAllActivitys();
            kill();
        }
        return true;
    }
}
