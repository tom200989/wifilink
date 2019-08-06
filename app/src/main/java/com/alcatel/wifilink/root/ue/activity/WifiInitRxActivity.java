package com.alcatel.wifilink.root.ue.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.app.SmartLinkV3App;
import com.alcatel.wifilink.root.helper.CheckBoardLogin;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.SystemInfoHelper;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Lgg;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.SP;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.widget.DialogOkWidget;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWlanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWlanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetWlanSettingsHelper;
import com.zhy.android.percent.support.PercentRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * WIFI设置向导
 */
public class WifiInitRxActivity extends BaseActivityWithBack {


    @BindView(R.id.iv_wifiInit_back)
    ImageView ivBack;// 回退
    @BindView(R.id.tv_wifiInit_skip)
    TextView tvWifiInitSkip;// 跳过

    // 2.4G面板
    @BindView(R.id.rl_wifiInit_2p4G)
    RelativeLayout rl2p4GPanel;
    @BindView(R.id.rl_wifiInit_wlanStatus_2p4G)
    RelativeLayout rlWifiInitWlanStatus2p4G;// 开关面板
    @BindView(R.id.iv_wifiInit_wlanStatus_socket_2p4G)
    ImageView ivSocket2p4G;// 状态开关
    @BindView(R.id.rl_wifiInit_account_2p4G)
    RelativeLayout rlWifiInitAccount2p4G;// 用户名面板
    @BindView(R.id.et_wifiInit_wlanStatus_account_2p4G)
    EditText etAccount2p4G;// 用户名
    @BindView(R.id.rl_wifiInit_password_2p4G)
    RelativeLayout rlWifiInitPassword2p4G;// 密码面板
    @BindView(R.id.iv_wifiInit_password_eyes_2p4G)
    ImageView ivPasswordEyes2p4G;// 可见开关
    @BindView(R.id.et_wifiInit_password_2p4G)
    EditText etPassword2p4G;// 密码

    // 5G面板
    @BindView(R.id.rl_wifiInit_5G)
    PercentRelativeLayout rl5GPanel;
    @BindView(R.id.rl_wifiInit_wlanStatus_5G)
    RelativeLayout rlWifiInitWlanStatus5G;// 开关面板
    @BindView(R.id.iv_wifiInit_wlanStatus_socket_5G)
    ImageView ivSocket5G;// 状态开关
    @BindView(R.id.rl_wifiInit_account_5G)
    RelativeLayout rlWifiInitAccount5G;// 用户名面板
    @BindView(R.id.et_wifiInit_wlanStatus_account_5G)
    EditText etAccount5G;// 用户名
    @BindView(R.id.rl_wifiInit_password_5G)
    RelativeLayout rlWifiInitPassword5G;// 密码面板
    @BindView(R.id.iv_wifiInit_password_eyes_5G)
    ImageView ivPasswordEyes5G;// 可见开关
    @BindView(R.id.et_wifiInit_password_5G)
    EditText etPassword5G;// 密码

    // 点击done按钮
    @BindView(R.id.tv_wifiInit_done)
    TextView btNext;

    // 失败界面
    @BindView(R.id.rl_wifiInit_rx_failed)
    RelativeLayout rlFailedPanel;
    @BindView(R.id.tv_wifiInit_rx_tryagain)
    TextView tvTryagain;// 重试按钮
    @BindView(R.id.tv_wifiInit_rx_tohome)
    TextView tvTohome;// 前往主页

    @BindView(R.id.dg_wifiInit_ok)
    DialogOkWidget dgWifiInitOk;// ok & cancel

    private int FLAG_2P4G = 0;
    private int FLAG_5G = 1;
    private TimerHelper heartTimer;
    private Activity activity;
    private Drawable switch_on;
    private Drawable switch_off;
    private Drawable eyes_on;
    private Drawable eyes_off;
    private Drawable unClick;
    private int showPsd;
    private int hidePsd;
    private int blue_color;
    private int gray_color;
    private int showPsd_hex;
    private int hidePsd_hex;
    private GetWlanSettingsBean result;
    private GetWlanSettingsBean resultCache;// 备份缓存
    private ProgressDialog pgd;
    private View[] childPanelViewFor2G;// 2.4G子面板集合
    private View[] childPanelViewFor5G;// 5G子面板集合
    private boolean isMw120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        SmartLinkV3App.getContextInstance().add(this);
        setContentView(R.layout.activity_wifi_init_rx);
        ButterKnife.bind(this);
        startHeartTimer();
        initRes();
        initData();
    }

    /**
     * 初始化资源
     */
    private void initRes() {
        switch_on = getResources().getDrawable(R.drawable.pwd_switcher_on);
        switch_off = getResources().getDrawable(R.drawable.pwd_switcher_off);
        eyes_on = getResources().getDrawable(R.drawable.general_password_show);
        eyes_off = getResources().getDrawable(R.drawable.general_password_hidden);
        unClick = getResources().getDrawable(R.drawable.bg_unclick);
        showPsd = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        hidePsd = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        showPsd_hex = 0x90;
        hidePsd_hex = 0x80;
        blue_color = getResources().getColor(R.color.mg_blue);
        gray_color = getResources().getColor(R.color.gray);

        childPanelViewFor2G = new View[]{rlWifiInitAccount2p4G, rlWifiInitPassword2p4G};
        childPanelViewFor5G = new View[]{rlWifiInitAccount5G, rlWifiInitPassword5G};
    }

    private void initData() {
        // 获取设备类型
        SystemInfoHelper sih = new SystemInfoHelper();
        sih.setOnGetSystemInfoFailedListener(() -> getSupportModeAndCurrentStatus(false));
        sih.setOnErrorListener(attr -> getSupportModeAndCurrentStatus(false));
        sih.setOnResultErrorListener(attr -> getSupportModeAndCurrentStatus(false));
        sih.setOnGetSystemInfoSuccessListener(systemInfo -> {
            String deviceName = systemInfo.getDeviceName();
            isMw120 = OtherUtils.isMw120(deviceName);
            // 获取支持的模式(2.4|5)以及当前模式的状态
            getSupportModeAndCurrentStatus(isMw120);
        });
        sih.get();
    }

    /**
     * 获取支持的模式(2.4|5)以及当前模式的状态
     */
    private void getSupportModeAndCurrentStatus(boolean ismw120) {
        // 1.检测是否连接上硬件以及是否处于登陆状态
        new CheckBoardLogin(this) {
            @Override
            public void afterCheckSuccess(ProgressDialog pgd) {
                // 2.获取wlan信息

                GetWlanSettingsHelper xGetWlanSettingsHelper = new GetWlanSettingsHelper();
                xGetWlanSettingsHelper.setOnGetWlanSettingsSuccessListener(settingsBean -> {
                    WifiInitRxActivity.this.result = settingsBean;
                    WifiInitRxActivity.this.resultCache = result.deepClone();// 备份缓存
                    GetWlanSettingsBean.AP2GBean ap2G = result.getAP2G();
                    boolean is2P4GEnable = false;
                    if (ap2G != null) {
                        is2P4GEnable = ap2G.getApStatus() == Cons.ENABLE;
                    }
                    GetWlanSettingsBean.AP5GBean ap5G = result.getAP5G();
                    boolean is5GEnable = false;
                    if (ap5G != null) {
                        is5GEnable = ap5G.getApStatus() == Cons.ENABLE;
                    }

                    if (!ismw120) {
                        // 面板
                        rl2p4GPanel.setVisibility(is2P4GEnable ? View.VISIBLE : View.GONE);
                        rl5GPanel.setVisibility(is5GEnable ? View.VISIBLE : View.GONE);
                    } else {
                        switchMw120WifiPanel(is2P4GEnable);
                        rl2p4GPanel.setVisibility(View.VISIBLE);
                        rl5GPanel.setVisibility(View.VISIBLE);
                    }

                    // 开关UI
                    ivSocket2p4G.setImageDrawable(is2P4GEnable ? switch_on : switch_off);
                    ivSocket5G.setImageDrawable(is5GEnable ? switch_on : switch_off);
                    // 编辑可操作状态
                    OtherUtils.setEdittextEditable(etAccount2p4G, is2P4GEnable);
                    OtherUtils.setEdittextEditable(etPassword2p4G, is2P4GEnable);
                    OtherUtils.setEdittextEditable(etAccount5G, is5GEnable);
                    OtherUtils.setEdittextEditable(etPassword5G, is5GEnable);
                    // 编辑可视状态
                    etPassword2p4G.setInputType(hidePsd);
                    etPassword5G.setInputType(hidePsd);
                    // 编辑文本颜色
                    etAccount2p4G.setTextColor(is2P4GEnable ? blue_color : gray_color);
                    etPassword2p4G.setTextColor(is2P4GEnable ? blue_color : gray_color);
                    etAccount5G.setTextColor(is5GEnable ? blue_color : gray_color);
                    etPassword5G.setTextColor(is5GEnable ? blue_color : gray_color);
                    // 编辑SSID
                    etAccount2p4G.setText(ap2G != null ? ap2G.getSsid() : "");
                    etAccount5G.setText(ap5G != null ? ap5G.getSsid() : "");
                    // 编辑密码
                    etPassword2p4G.setText(ap2G != null ? ap2G.getWpaKey() : "");
                    etPassword5G.setText(ap5G != null ? ap5G.getWpaKey() : "");
                });
                xGetWlanSettingsHelper.setOnGetWlanSettingsFailedListener(() -> rlFailedPanel.setVisibility(View.VISIBLE));
                xGetWlanSettingsHelper.getWlanSettings();
            }
        };
    }

    /**
     * 切换MW120设备下的WIFI单开模式
     *
     * @param isCurrent2P4 当前是否为2.4G
     */
    public void switchMw120WifiPanel(boolean isCurrent2P4) {
        for (View view : childPanelViewFor2G) {
            view.setVisibility(isCurrent2P4 ? View.VISIBLE : View.GONE);
        }
        for (View view : childPanelViewFor5G) {
            view.setVisibility(isCurrent2P4 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        OtherUtils.stopHeartBeat(heartTimer);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        clickBack();
    }

    @OnClick({R.id.iv_wifiInit_back,// 回退
            R.id.tv_wifiInit_skip,// 跳过
            R.id.iv_wifiInit_wlanStatus_socket_2p4G,// 2.4G状态开关
            R.id.iv_wifiInit_password_eyes_2p4G,// 2.4G可视按钮
            R.id.iv_wifiInit_wlanStatus_socket_5G,// 5G状态开关
            R.id.iv_wifiInit_password_eyes_5G,// 5G可视按钮
            R.id.tv_wifiInit_done,// 点击提交按钮
            R.id.tv_wifiInit_rx_tryagain,// 重试按钮
            R.id.tv_wifiInit_rx_tohome// 前往主页
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wifiInit_back:// 回退
                clickBack();
                break;
            case R.id.tv_wifiInit_skip:// 跳过
                toHome();
                break;
            case R.id.iv_wifiInit_wlanStatus_socket_2p4G:// 2.4G状态开关
                checkWlanStatusSocket(FLAG_2P4G);
                break;
            case R.id.iv_wifiInit_password_eyes_2p4G:// 2.4G可视
                checkVisibleEyes(FLAG_2P4G);
                break;
            case R.id.iv_wifiInit_wlanStatus_socket_5G:// 5G状态开关
                checkWlanStatusSocket(FLAG_5G);
                break;
            case R.id.iv_wifiInit_password_eyes_5G:// 5G可视
                checkVisibleEyes(FLAG_5G);
                break;
            case R.id.tv_wifiInit_done:// done按钮
                clickDone();
                break;
            case R.id.tv_wifiInit_rx_tryagain:// 重试按钮
                // 退出登陆--> 前往登陆界面
                toLoginRx();
                break;
            case R.id.tv_wifiInit_rx_tohome:
                toHome();// 前往主页
                break;
        }
    }

    /**
     * 点击done按钮
     */
    private void clickDone() {
        // 0.获取开关状态
        boolean is2P4GEnable = ivSocket2p4G.getDrawable() == switch_on;
        boolean is5GEnable = ivSocket5G.getDrawable() == switch_on;
        // 1.空值判断
        if (is2P4GEnable && TextUtils.isEmpty(OtherUtils.getEdContentIncludeSpace(etAccount2p4G))) {
            toast(R.string.setting_ssid_invalid);
            return;
        }
        if (is2P4GEnable && TextUtils.isEmpty(OtherUtils.getEdContentIncludeSpace(etPassword2p4G))) {
            toast(R.string.qs_wifi_passwd_prompt);
            return;
        }
        if (is5GEnable && TextUtils.isEmpty(OtherUtils.getEdContentIncludeSpace(etAccount5G))) {
            toast(R.string.setting_ssid_invalid);
            return;
        }
        if (is5GEnable && TextUtils.isEmpty(OtherUtils.getEdContentIncludeSpace(etPassword5G))) {
            toast(R.string.qs_wifi_passwd_prompt);
            return;
        }
        // 2.位数判断
        int digits_2P4G = OtherUtils.getEdContentIncludeSpace(etPassword2p4G).length();
        int digits_5G = OtherUtils.getEdContentIncludeSpace(etPassword5G).length();
        if (is2P4GEnable && (digits_2P4G < 8 | digits_2P4G > 63)) {
            toast(R.string.qs_wifi_passwd_prompt);
            return;
        }
        if (is5GEnable && (digits_5G < 8 | digits_5G > 63)) {
            toast(R.string.qs_wifi_passwd_prompt);
            return;
        }
        // 3.封装
        if (result.getAP2G() != null) {
            if (is2P4GEnable) {
                result.getAP2G().setApStatus(Cons.ENABLE);
                result.getAP2G().setSecurityMode(Cons.SECURITY_WPA_WPA2);// 强制为WPA\WPA2模式
                result.getAP2G().setSsid(OtherUtils.getEdContentIncludeSpace(etAccount2p4G));
                result.getAP2G().setWpaKey(OtherUtils.getEdContentIncludeSpace(etPassword2p4G));
            } else {
                result.getAP2G().setApStatus(Cons.DISABLE);
            }
        }


        if (result.getAP5G() != null) {
            if (is5GEnable) {
                result.getAP5G().setApStatus(Cons.ENABLE);
                result.getAP5G().setSecurityMode(Cons.SECURITY_WPA_WPA2);// 强制为WPA\WPA2模式
                result.getAP5G().setSsid(OtherUtils.getEdContentIncludeSpace(etAccount5G));
                result.getAP5G().setWpaKey(OtherUtils.getEdContentIncludeSpace(etPassword5G));
            } else {
                result.getAP5G().setApStatus(Cons.DISABLE);
            }
        }

        // 4.检测是否连接上硬件以及内容是否发生改变
        checkBoardAndContent();
    }

    /**
     * 检测是否连接上硬件以及内容是否发生改变
     */
    private void checkBoardAndContent() {
        // 1.检查硬件是否连接上
        new CheckBoardLogin(this) {
            @Override
            public void afterCheckSuccess(ProgressDialog pop) {
                // 2.判断是否有修改过
                checkChange();
            }
        };
    }

    /**
     * 判断是否有修改过
     */
    private void checkChange() {
        GetWlanSettingsBean.AP2GBean ap2G = result.getAP2G();
        GetWlanSettingsBean.AP5GBean ap5G = result.getAP5G();

        // 默认为true
        boolean is2P4GAPStatusSame = true;
        boolean is5GAPStatusSame = true;
        boolean is2P4GSSIDSame = true;
        boolean is5GSSIDSame = true;
        boolean is2P4GWpaSame = true;
        boolean is5GWpaSame = true;

        if (ap2G != null) {
            // 1.状态是否相同
            is2P4GAPStatusSame = ap2G.getApStatus() == resultCache.getAP2G().getApStatus();
            // 2.SSID是否相同
            is2P4GSSIDSame = ap2G.getSsid().equals(resultCache.getAP2G().getSsid());
            // 3.password是否相同
            is2P4GWpaSame = ap2G.getWpaKey().equals(resultCache.getAP2G().getWpaKey());
        }
        if (ap5G != null) {
            is5GAPStatusSame = ap5G.getApStatus() == resultCache.getAP5G().getApStatus();
            is5GSSIDSame = ap5G.getSsid().equals(resultCache.getAP5G().getSsid());
            is5GWpaSame = ap5G.getWpaKey().equals(resultCache.getAP5G().getWpaKey());
        }
        if (is2P4GAPStatusSame & is5GAPStatusSame & is2P4GSSIDSame & is5GSSIDSame & is2P4GWpaSame & is5GWpaSame) {
            // 4.直接跳转到主页
            toast(R.string.ergo_20181010_setup_success);
            toAc();
        } else {
            // 4.显示重启提示对话框
            showReStartDeviceDialog();
        }

    }

    private void toAc() {
        SP.getInstance(activity).putBoolean(Cons.WIFIINIT_RX, true);
        to(HomeRxActivity.class);
    }

    /**
     * 显示重启提示对话框
     */
    private void showReStartDeviceDialog() {
        String des1 = getString(R.string.setting_wifi_set_success);
        String des2 = getString(R.string.connectedlist_will_be_restarted_to_apply_new_settings);
        String des = des1 + "\n" + des2;
        dgWifiInitOk.setVisibility(View.VISIBLE);
        dgWifiInitOk.setTitle(R.string.restart);
        dgWifiInitOk.setDes(des);
        dgWifiInitOk.setOnBgClickListener(() -> Lgg.t("mainrx").ii("click not area"));
        dgWifiInitOk.setOnCancelClickListener(() -> dgWifiInitOk.setVisibility(View.GONE));
        dgWifiInitOk.setOnOkClickListener(this::pullSetting);
    }

    /**
     * 提交设置
     */
    private void pullSetting() {
        pgd = OtherUtils.showProgressPop(this);
        // 1.提交设置
        SetWlanSettingsHelper xSetWlanSettingsHelper = new SetWlanSettingsHelper();
        xSetWlanSettingsHelper.setOnSetWlanSettingsSuccessListener(() -> {
            // 2.切断wifi
            OtherUtils.setWifiActive(activity, false);
            // 提交标记位
            SP.getInstance(activity).putBoolean(Cons.WIFIINIT_RX, true);
            toast(R.string.ergo_20181010_update_wifi);
            OtherUtils.hideProgressPop(pgd);
        });
        xSetWlanSettingsHelper.setOnSetWlanSettingsFailedListener(() -> {
            toast(R.string.setting_wifi_set_failed);
            OtherUtils.hideProgressPop(pgd);
        });
        xSetWlanSettingsHelper.setWlanSettings(result);
    }


    /**
     * 前往主页
     */

    private void toHome() {
        // 1.检查是否连接上硬件以及处于登陆状态
        new CheckBoardLogin(this) {
            @Override
            public void afterCheckSuccess(ProgressDialog pgd) {
                toAc();
            }
        };
    }

    /**
     * 切换密码可视化开关
     *
     * @param flag
     */
    private void checkVisibleEyes(int flag) {
        if (flag == FLAG_2P4G) {
            // 切换UI
            Drawable eye_2p4g = ivPasswordEyes2p4G.getDrawable() == eyes_on ? eyes_off : eyes_on;
            ivPasswordEyes2p4G.setImageDrawable(eye_2p4g);
            // 切换编辑域可视状态
            etPassword2p4G.setInputType(etPassword2p4G.getInputType() == showPsd ? hidePsd : showPsd);
        } else {
            Drawable eye_5g = ivPasswordEyes5G.getDrawable() == eyes_on ? eyes_off : eyes_on;
            ivPasswordEyes5G.setImageDrawable(eye_5g);
            etPassword5G.setInputType(etPassword5G.getInputType() == showPsd ? hidePsd : showPsd);
        }
    }

    /**
     * 切换状态开关
     *
     * @param flag
     */
    private void checkWlanStatusSocket(int flag) {
        if (flag == FLAG_2P4G) {
            // 切换视图
            Drawable switch_2p4g = ivSocket2p4G.getDrawable() == switch_on ? switch_off : switch_on;
            ivSocket2p4G.setImageDrawable(switch_2p4g);
            // 设置编辑域编辑权限
            boolean isAfterSwitchOn = ivSocket2p4G.getDrawable() == switch_on;
            OtherUtils.setEdittextEditable(etAccount2p4G, isAfterSwitchOn);
            OtherUtils.setEdittextEditable(etPassword2p4G, isAfterSwitchOn);
            etAccount2p4G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
            etPassword2p4G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
            ivPasswordEyes2p4G.setClickable(isAfterSwitchOn);
        } else {
            Drawable switch_5g = ivSocket5G.getDrawable() == switch_on ? switch_off : switch_on;
            ivSocket5G.setImageDrawable(switch_5g);
            boolean isAfterSwitchOn = ivSocket5G.getDrawable() == switch_on;
            OtherUtils.setEdittextEditable(etAccount5G, isAfterSwitchOn);
            OtherUtils.setEdittextEditable(etPassword5G, isAfterSwitchOn);
            etAccount5G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
            etPassword5G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
            ivPasswordEyes5G.setClickable(isAfterSwitchOn);
        }

        // 对于mw120型号的处理
        if (isMw120) {
            // UI切换
            if (flag == FLAG_2P4G) {
                ivSocket5G.setImageDrawable(ivSocket2p4G.getDrawable() == switch_on ? switch_off : switch_on);
                boolean isAfterSwitchOn = ivSocket5G.getDrawable() == switch_on;
                OtherUtils.setEdittextEditable(etAccount5G, isAfterSwitchOn);
                OtherUtils.setEdittextEditable(etPassword5G, isAfterSwitchOn);
                etAccount5G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
                etPassword5G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
                ivPasswordEyes5G.setClickable(isAfterSwitchOn);
                // 恢复这2.4G部份的信息
                etAccount2p4G.setText(resultCache.getAP2G().getSsid());
                etPassword2p4G.setText(resultCache.getAP2G().getWpaKey());
            } else {
                ivSocket2p4G.setImageDrawable(ivSocket5G.getDrawable() == switch_on ? switch_off : switch_on);
                boolean isAfterSwitchOn = ivSocket2p4G.getDrawable() == switch_on;
                OtherUtils.setEdittextEditable(etAccount2p4G, isAfterSwitchOn);
                OtherUtils.setEdittextEditable(etPassword2p4G, isAfterSwitchOn);
                etAccount2p4G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
                etPassword2p4G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
                ivPasswordEyes2p4G.setClickable(isAfterSwitchOn);
                // 恢复这5G部份的信息
                etAccount5G.setText(resultCache.getAP5G().getSsid());
                etPassword5G.setText(resultCache.getAP5G().getWpaKey());
            }
            // 面板切换
            switchMw120WifiPanel(ivSocket2p4G.getDrawable() == switch_on);
        }
    }

    private void startHeartTimer() {
        heartTimer = OtherUtils.startHeartBeat(this, RefreshWifiRxActivity.class, LoginRxActivity.class);
    }

    /**
     * 点击了回退按钮
     */
    private void clickBack() {
        if (dgWifiInitOk.getVisibility() == View.VISIBLE) {
            dgWifiInitOk.setVisibility(View.GONE);
        } else {
            toLoginRx();// 退出登陆--> 前往登陆界面
        }
    }

    /**
     * 退出登陆--> 前往登陆界面
     */
    private void toLoginRx() {
        // 1.退出登陆
        LogoutHelper xLogouthelper = new LogoutHelper();
        xLogouthelper.setOnLogoutSuccessListener(() -> to(LoginRxActivity.class));
        xLogouthelper.setOnLogOutFailedListener(() -> ToastUtil_m.show(this, getString(R.string.login_logout_failed)));
        xLogouthelper.logout();
    }

    public void toast(int resId) {
        ToastUtil_m.show(this, resId);
    }

    public void toast(String content) {
        ToastUtil_m.show(this, content);
    }

    private void to(Class clazz) {
        CA.toActivity(this, clazz, false, true, false, 0);
    }
}
