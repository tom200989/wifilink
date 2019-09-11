package com.alcatel.wifilink.root.ue.frag;

import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.ue.activity.HomeActivity;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.alcatel.wifilink.root.widget.HH70_NormalWidget;
import com.hiber.tools.ShareUtils;
import com.hiber.tools.layout.PercentRelativeLayout;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWlanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWlanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetWlanSettingsHelper;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/16 0016.
 */
public class WifiInitFrag extends BaseFrag {

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
    TextView tvDone;

    // 失败界面
    @BindView(R.id.rl_wifiInit_rx_failed)
    RelativeLayout rlFailedPanel;
    @BindView(R.id.tv_wifiInit_rx_tryagain)
    TextView tvTryagain;// 重试按钮
    @BindView(R.id.tv_wifiInit_rx_tohome)
    TextView tvTohome;// 前往主页

    @BindView(R.id.dg_wifiInit_ok)
    HH70_NormalWidget wdNormal;// ok & cancel

    @BindView(R.id.wd_wifi_init_load)
    HH70_LoadWidget wdLoad;// 等待

    private int FLAG_2P4G = 0;
    private int FLAG_5G = 1;
    private Drawable switch_on;
    private Drawable switch_off;
    private Drawable eyes_on;
    private Drawable eyes_off;
    private int showPsd;
    private int hidePsd;
    private int blue_color;
    private int gray_color;
    private GetWlanSettingsBean result;
    private GetWlanSettingsBean resultCache;// 备份缓存
    private View[] childPanelViewFor2G;// 2.4G子面板集合
    private View[] childPanelViewFor5G;// 5G子面板集合
    private boolean isMWDev;// 是否为MW设备

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_wifi_init;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        initRes();
        initData();
        initClick();
    }

    /**
     * 初始化资源
     */
    private void initRes() {
        switch_on = getRootDrawable(R.drawable.pwd_switcher_on);
        switch_off = getRootDrawable(R.drawable.pwd_switcher_off);
        eyes_on = getRootDrawable(R.drawable.general_password_show);
        eyes_off = getRootDrawable(R.drawable.general_password_hidden);
        showPsd = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        hidePsd = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        blue_color = getRootColor(R.color.mg_blue);
        gray_color = getRootColor(R.color.gray);

        childPanelViewFor2G = new View[]{rlWifiInitAccount2p4G, rlWifiInitPassword2p4G};
        childPanelViewFor5G = new View[]{rlWifiInitAccount5G, rlWifiInitPassword5G};
    }

    private void initData() {
        // 获取设备类型
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(systemInfo -> {
            isMWDev = RootUtils.isMWDEV(systemInfo.getDeviceName());
            // 获取支持的模式(2.4|5)以及当前模式的状态
            getSupportModeAndCurrentStatus(isMWDev);
        });
        xGetSystemInfoHelper.setOnFwErrorListener(() -> getSupportModeAndCurrentStatus(false));
        xGetSystemInfoHelper.setOnAppErrorListener(() -> getSupportModeAndCurrentStatus(false));
        xGetSystemInfoHelper.getSystemInfo();
    }

    /**
     * 获取支持的模式(2.4|5)以及当前模式的状态
     */
    private void getSupportModeAndCurrentStatus(boolean isMWDev) {
        // 1.检测是否连接上硬件以及是否处于登陆状态
        wdLoad.setVisibles();
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            int state = getLoginStateBean.getState();
            if (state == GetLoginStateBean.CONS_LOGIN) {
                // 2.获取wlan信息
                GetWlanSettingsHelper xGetWlanSettingsHelper = new GetWlanSettingsHelper();
                xGetWlanSettingsHelper.setOnGetWlanSettingsSuccessListener(settingsBean -> {
                    wdLoad.setGone();
                    result = settingsBean;
                    resultCache = result.deepClone();// 备份缓存
                    GetWlanSettingsBean.AP2GBean ap2G = result.getAP2G();
                    boolean is2P4GEnable = false;
                    if (ap2G != null) {
                        is2P4GEnable = ap2G.getApStatus() == GetWlanSettingsBean.CONS_AP_STATUS_ON;
                    }
                    GetWlanSettingsBean.AP5GBean ap5G = result.getAP5G();
                    boolean is5GEnable = false;
                    if (ap5G != null) {
                        is5GEnable = ap5G.getApStatus() == GetWlanSettingsBean.CONS_AP_STATUS_ON;
                    }

                    if (!isMWDev) {
                        // 面板
                        rl2p4GPanel.setVisibility(is2P4GEnable ? View.VISIBLE : View.GONE);
                        rl5GPanel.setVisibility(is5GEnable ? View.VISIBLE : View.GONE);
                    } else {
                        turnMWPanel(is2P4GEnable);
                        rl2p4GPanel.setVisibility(View.VISIBLE);
                        rl5GPanel.setVisibility(View.VISIBLE);
                    }

                    // 开关UI
                    ivSocket2p4G.setImageDrawable(is2P4GEnable ? switch_on : switch_off);
                    ivSocket5G.setImageDrawable(is5GEnable ? switch_on : switch_off);
                    // 编辑可操作状态
                    RootUtils.setEdEnable(etAccount2p4G, is2P4GEnable);
                    RootUtils.setEdEnable(etPassword2p4G, is2P4GEnable);
                    RootUtils.setEdEnable(etAccount5G, is5GEnable);
                    RootUtils.setEdEnable(etPassword5G, is5GEnable);
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
                xGetWlanSettingsHelper.setOnGetWlanSettingsFailedListener(() -> {
                    wdLoad.setGone();
                    rlFailedPanel.setVisibility(View.VISIBLE);
                });
                xGetWlanSettingsHelper.getWlanSettings();
            } else {
                wdLoad.setGone();
                toast(R.string.hh70_log_out, 5000);
                toFrag(getClass(), LoginFrag.class, null, true,getClass());
            }
        });
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 切换MW120设备下的WIFI单开模式
     *
     * @param isCurrent2P4 当前是否为2.4G
     */
    public void turnMWPanel(boolean isCurrent2P4) {
        for (View view : childPanelViewFor2G) {
            view.setVisibility(isCurrent2P4 ? View.VISIBLE : View.GONE);
        }
        for (View view : childPanelViewFor5G) {
            view.setVisibility(isCurrent2P4 ? View.GONE : View.VISIBLE);
        }
    }

    private void initClick() {
        // 回退
        ivBack.setOnClickListener(v -> onBackPresss());
        // 跳过
        tvWifiInitSkip.setOnClickListener(v -> toHome());
        // 2.4G开关
        ivSocket2p4G.setOnClickListener(v -> checkWlanStatusSocket(FLAG_2P4G));
        // 2.4可视
        ivPasswordEyes2p4G.setOnClickListener(v -> checkVisibleEyes(FLAG_2P4G));
        // 5G开关
        ivSocket5G.setOnClickListener(v -> checkWlanStatusSocket(FLAG_5G));
        // 5G可视
        ivPasswordEyes5G.setOnClickListener(v -> checkVisibleEyes(FLAG_5G));
        // done
        tvDone.setOnClickListener(v -> clickDone());
        // 重试
        tvTryagain.setOnClickListener(v -> toLogin());
        // 主页
        tvTohome.setOnClickListener(v -> toHome());
    }


    /**
     * 点击done按钮
     */
    private void clickDone() {
        // 0.获取开关状态
        boolean is2P4GEnable = ivSocket2p4G.getDrawable() == switch_on;
        boolean is5GEnable = ivSocket5G.getDrawable() == switch_on;
        // 1.空值判断
        if (is2P4GEnable && TextUtils.isEmpty(RootUtils.getEDText(etAccount2p4G, true))) {
            toast(R.string.hh70_ssid_can_contain, 5000);
            return;
        }
        if (is2P4GEnable && TextUtils.isEmpty(RootUtils.getEDText(etPassword2p4G, true))) {
            toast(R.string.hh70_the_password, 5000);
            return;
        }
        if (is5GEnable && TextUtils.isEmpty(RootUtils.getEDText(etAccount5G, true))) {
            toast(R.string.hh70_ssid_can_contain, 5000);
            return;
        }
        if (is5GEnable && TextUtils.isEmpty(RootUtils.getEDText(etPassword5G, true))) {
            toast(R.string.hh70_the_password, 5000);
            return;
        }
        // 2.位数判断
        int digits_2P4G = RootUtils.getEDText(etPassword2p4G, true).length();
        int digits_5G = RootUtils.getEDText(etPassword5G, true).length();
        if (is2P4GEnable && (digits_2P4G < 8 | digits_2P4G > 63)) {
            toast(R.string.hh70_the_password, 5000);
            return;
        }
        if (is5GEnable && (digits_5G < 8 | digits_5G > 63)) {
            toast(R.string.hh70_the_password, 5000);
            return;
        }
        // 3.封装
        if (result.getAP2G() != null) {
            if (is2P4GEnable) {
                result.getAP2G().setApStatus(GetWlanSettingsBean.CONS_AP_STATUS_ON);
                result.getAP2G().setSecurityMode(GetWlanSettingsBean.CONS_SECURITY_MODE_WPA_WPA2);// 强制为WPA\WPA2模式
                result.getAP2G().setSsid(RootUtils.getEDText(etAccount2p4G, true));
                result.getAP2G().setWpaKey(RootUtils.getEDText(etPassword2p4G, true));
            } else {
                result.getAP2G().setApStatus(GetWlanSettingsBean.CONS_AP_STATUS_OFF);
            }
        }


        if (result.getAP5G() != null) {
            if (is5GEnable) {
                result.getAP5G().setApStatus(GetWlanSettingsBean.CONS_AP_STATUS_ON);
                result.getAP5G().setSecurityMode(GetWlanSettingsBean.CONS_SECURITY_MODE_WPA_WPA2);// 强制为WPA\WPA2模式
                result.getAP5G().setSsid(RootUtils.getEDText(etAccount5G, true));
                result.getAP5G().setWpaKey(RootUtils.getEDText(etPassword5G, true));
            } else {
                result.getAP5G().setApStatus(GetWlanSettingsBean.CONS_AP_STATUS_OFF);
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
        wdLoad.setVisibles();
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                // 2.判断是否有修改过
                checkChange();
            } else {
                wdLoad.setGone();
                toast(R.string.hh70_log_out, 5000);
                toFrag(getClass(), LoginFrag.class, null, true,getClass());
            }
        });
        xGetLoginStateHelper.getLoginState();
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
            toast(R.string.hh70_setup_success, 5000);
            skip();
        } else {
            // 4.显示重启提示对话框
            wdLoad.setGone();
            showResetUi();
        }

    }

    private void skip() {
        wdLoad.setGone();
        ShareUtils.set(RootCons.SP_WIFI_INIT, true);
        toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
    }

    /**
     * 显示重启提示对话框
     */
    private void showResetUi() {
        String des1 = getString(R.string.hh70_change_wifi);
        String des2 = getString(R.string.hh70_list_will_restart);
        String des = des1 + "\n" + des2;
        wdNormal.setVisibility(View.VISIBLE);
        wdNormal.setTitle(R.string.hh70_restart);
        wdNormal.setDes(des);
        wdNormal.setOnCancelClickListener(() -> wdNormal.setVisibility(View.GONE));
        wdNormal.setOnOkClickListener(this::pullSetting);
    }

    /**
     * 提交设置
     */
    private void pullSetting() {
        wdLoad.setVisibles();
        // 1.提交设置
        SetWlanSettingsHelper xSetWlanSettingsHelper = new SetWlanSettingsHelper();
        xSetWlanSettingsHelper.setOnSetWlanSettingsSuccessListener(() -> {
            // 提交标记位
            ShareUtils.set(RootCons.SP_WIFI_INIT, true);
            toast(R.string.hh70_update_wifi, 5000);
            wdLoad.setGone();
        });
        xSetWlanSettingsHelper.setOnSetWlanSettingsFailedListener(() -> {
            toast(R.string.hh70_cant_change_wifi, 5000);
            wdLoad.setGone();
        });
        xSetWlanSettingsHelper.setWlanSettings(result);
    }


    /**
     * 前往主页
     */
    private void toHome() {
        // 1.检查是否连接上硬件以及处于登陆状态
        wdLoad.setVisibles();
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                skip();
            } else {
                wdLoad.setGone();
                toast(R.string.hh70_log_out, 5000);
                toFrag(getClass(), LoginFrag.class, null, true,getClass());
            }
        });
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 切换密码可视化开关
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
     */
    private void checkWlanStatusSocket(int flag) {
        if (flag == FLAG_2P4G) {
            // 切换视图
            Drawable switch_2p4g = ivSocket2p4G.getDrawable() == switch_on ? switch_off : switch_on;
            ivSocket2p4G.setImageDrawable(switch_2p4g);
            // 设置编辑域编辑权限
            boolean isAfterSwitchOn = ivSocket2p4G.getDrawable() == switch_on;
            RootUtils.setEdEnable(etAccount2p4G, isAfterSwitchOn);
            RootUtils.setEdEnable(etPassword2p4G, isAfterSwitchOn);
            etAccount2p4G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
            etPassword2p4G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
            ivPasswordEyes2p4G.setClickable(isAfterSwitchOn);
        } else {
            Drawable switch_5g = ivSocket5G.getDrawable() == switch_on ? switch_off : switch_on;
            ivSocket5G.setImageDrawable(switch_5g);
            boolean isAfterSwitchOn = ivSocket5G.getDrawable() == switch_on;
            RootUtils.setEdEnable(etAccount5G, isAfterSwitchOn);
            RootUtils.setEdEnable(etPassword5G, isAfterSwitchOn);
            etAccount5G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
            etPassword5G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
            ivPasswordEyes5G.setClickable(isAfterSwitchOn);
        }

        // 对于mw120型号的处理
        if (isMWDev) {
            // UI切换
            if (flag == FLAG_2P4G) {
                ivSocket5G.setImageDrawable(ivSocket2p4G.getDrawable() == switch_on ? switch_off : switch_on);
                boolean isAfterSwitchOn = ivSocket5G.getDrawable() == switch_on;
                RootUtils.setEdEnable(etAccount5G, isAfterSwitchOn);
                RootUtils.setEdEnable(etPassword5G, isAfterSwitchOn);
                etAccount5G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
                etPassword5G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
                ivPasswordEyes5G.setClickable(isAfterSwitchOn);
                // 恢复这2.4G部份的信息
                etAccount2p4G.setText(resultCache.getAP2G().getSsid());
                etPassword2p4G.setText(resultCache.getAP2G().getWpaKey());
            } else {
                ivSocket2p4G.setImageDrawable(ivSocket5G.getDrawable() == switch_on ? switch_off : switch_on);
                boolean isAfterSwitchOn = ivSocket2p4G.getDrawable() == switch_on;
                RootUtils.setEdEnable(etAccount2p4G, isAfterSwitchOn);
                RootUtils.setEdEnable(etPassword2p4G, isAfterSwitchOn);
                etAccount2p4G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
                etPassword2p4G.setTextColor(isAfterSwitchOn ? blue_color : gray_color);
                ivPasswordEyes2p4G.setClickable(isAfterSwitchOn);
                // 恢复这5G部份的信息
                etAccount5G.setText(resultCache.getAP5G().getSsid());
                etPassword5G.setText(resultCache.getAP5G().getWpaKey());
            }
            // 面板切换
            turnMWPanel(ivSocket2p4G.getDrawable() == switch_on);
        }
    }

    /**
     * 退出登陆--> 前往登陆界面
     */
    private void toLogin() {
        // 1.退出登陆
        LogoutHelper xLogouthelper = new LogoutHelper();
        xLogouthelper.setOnLogoutSuccessListener(() -> toFrag(getClass(), LoginFrag.class, null, true));
        xLogouthelper.setOnLogOutFailedListener(() -> toast(R.string.hh70_cant_connect, 5000));
        xLogouthelper.logout();
    }

    @Override
    public boolean onBackPresss() {
        if (wdNormal.getVisibility() == View.VISIBLE) {
            wdNormal.setVisibility(View.GONE);
        } else if (wdLoad.getVisibility() == View.VISIBLE) {
            wdLoad.setGone();
        } else {
            toLogin();// 退出登陆--> 前往登陆界面
        }
        return true;
    }
}
