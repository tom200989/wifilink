package com.alcatel.wifilink.root.ue.frag;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.WpsHelper;
import com.alcatel.wifilink.root.ue.activity.HomeActivity;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.hiber.tools.ShareUtils;
import com.hiber.tools.layout.PercentRelativeLayout;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetWanSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetWanSettingsHelper;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/16 0016.
 */
public class WanInitFrag extends BaseFrag {

    // 导航栏
    @BindView(R.id.iv_wanmode_rx_back)
    ImageView ivBack;
    @BindView(R.id.tv_wanmode_rx_skip)
    TextView tvSkip;
    @BindView(R.id.tv_wanmode_rx_title)
    TextView tvWanmodeRxTitle;

    // 选项板
    @BindView(R.id.rl_wanmode_check_dhcp)
    PercentRelativeLayout rlCheckDhcp;
    @BindView(R.id.iv_wanmode_rx_dhcp_check)
    ImageView ivDhcpCheck;// DHCP
    @BindView(R.id.rl_wanmode_check_pppoe)
    PercentRelativeLayout rlCheckPppoe;
    @BindView(R.id.iv_wanmode_rx_pppoe_check)
    ImageView ivPppoeCheck;// pppoe
    @BindView(R.id.rl_wanmode_check_static)
    PercentRelativeLayout rlCheckStatic;
    @BindView(R.id.iv_wanmode_rx_static_check)
    ImageView ivStaticCheck;// static

    // PPPOE详情卡
    @BindView(R.id.rl_wanmode_rx_pppoe_detail)
    PercentRelativeLayout rlPppoeDetail;
    @BindView(R.id.et_wanmode_rx_pppoe_account)
    EditText etPppoeAccount;
    @BindView(R.id.et_wanmode_rx_pppoe_psd)
    EditText etPppoePsd;
    @BindView(R.id.et_wanmode_rx_pppoe_mtu)
    EditText etPppoeMtu;

    // STATIC详情卡
    @BindView(R.id.rl_wanmode_rx_static_detail)
    PercentRelativeLayout rlStaticDetail;
    @BindView(R.id.et_wanmode_rx_static_detail_ipaddress4)
    EditText etStaticIpaddress;
    @BindView(R.id.et_wanmode_rx_static_detail_subnet)
    EditText etStaticSubnet;

    // 提交按钮
    @BindView(R.id.tv_wanmode_rx_connect)
    TextView tvConnect;

    // 成功界面
    @BindView(R.id.rl_wanmode_rx_successful)
    PercentRelativeLayout rlSuccessful;

    // 失败界面
    @BindView(R.id.rl_wanmode_rx_failed)
    PercentRelativeLayout rlFailed;
    @BindView(R.id.tv_wanmode_rx_tryagain)
    TextView tvTryagain;
    @BindView(R.id.tv_wanmode_rx_tohome)
    TextView tvTohome;

    @BindView(R.id.wd_wan_init_load)
    HH70_LoadWidget wdLoad;

    private int PPPOE = 0;// 标记2
    private int DHCP = 1;// 标记1
    private int STATIC = 2;// 标记3
    private int MODE = DHCP;// 缓存点击后的标记
    private ImageView[] iv_wanmodes;
    private GetWanSettingsBean result;
    private boolean isRussia;// 是否为俄语

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_wan_init;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        getLanguage();
        initRes();
        initUi();
        initData();
        initClick();
    }

    /**
     * 初始化资源
     */
    private void initRes() {
        iv_wanmodes = new ImageView[]{ivPppoeCheck, ivDhcpCheck, ivStaticCheck};
    }

    /**
     * 初始化UI
     */
    private void initUi() {
        tvWanmodeRxTitle.setScaleX(isRussia ? 0.65f : 1f);
        tvSkip.setScaleX(isRussia ? 0.65f : 1f);
    }

    /**
     * 获取语言环境
     */
    private void getLanguage() {
        String language = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY, "");
        isRussia = language.contains(RootCons.LANGUAGES.RUSSIAN);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        wdLoad.setVisibles();
        GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
        xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(getWanSettingsBean -> {
            result = getWanSettingsBean;
            int wanStatus = result.getStatus();
            if (wanStatus == GetWanSettingsBean.CONS_CONNECTED) {
                wdLoad.setGone();
                MODE = result.getConnectType();
                showCheck(MODE);// 切换到对应的选项板
                showDetail(MODE, result);// 显示对应的参数
            } else if (wanStatus == GetWanSettingsBean.CONS_CONNECTING) {
                initData();
            } else {
                toast(R.string.check_your_wan_cabling, 5000);
                wdLoad.setGone();
            }
        });
        xGetWanSettingsHelper.setOnGetWanSettingFailedListener(() -> {
            toast(R.string.check_your_wan_cabling, 5000);
            toFrag(getClass(), RefreshFrag.class, null, true);
            wdLoad.setGone();
        });
        xGetWanSettingsHelper.getWanSettings();
    }

    /**
     * 点击
     */
    private void initClick() {
        // 回退
        ivBack.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            onBackPresss();
        });
        // 跳过
        tvSkip.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            skip();
        });
        // DHCP
        rlCheckDhcp.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            showCheck(DHCP);
        });
        ivDhcpCheck.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            showCheck(DHCP);
        });
        // PPPOE
        rlCheckPppoe.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            showCheck(PPPOE);
        });
        ivPppoeCheck.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            showCheck(PPPOE);
        });
        // static
        rlCheckStatic.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            showCheck(STATIC);
        });
        ivStaticCheck.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            showCheck(STATIC);
        });
        // 连接
        tvConnect.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            connectClick(MODE);
        });
        // 重试
        tvTryagain.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            rlFailed.setVisibility(View.GONE);
            rlSuccessful.setVisibility(View.GONE);
        });
        // 前往主页
        tvTohome.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            toHome();
        });
    }

    /**
     * 直接跳转到主页
     */
    private void toHome() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            int state = getLoginStateBean.getState();
            if (state == GetLoginStateBean.CONS_LOGIN) {
                // 提交向导标记
                ShareUtils.set(RootCons.SP_WIZARD, true);
                ShareUtils.set(RootCons.SP_WAN_INIT, true);
                ShareUtils.set(RootCons.SP_WIFI_INIT, true);
                // --> 主页
                toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
            }
        });
        xGetLoginStateHelper.getLoginState();

    }

    /**
     * 点击了connect按钮逻辑
     */
    private void connectClick(int connectType) {
        switch (connectType) {
            case GetWanSettingsBean.CONS_PPPOE:
                // 空值判断
                if (RootUtils.isEdEmpty(etPppoeAccount, etPppoePsd, etPppoeMtu)) {
                    toast(R.string.not_empty, 5000);
                    return;
                }
                // MTU匹配判断
                String mtu = RootUtils.getEDText(etPppoeMtu);
                if (mtu.startsWith("0")) {// 是否为0开头
                    toast(R.string.mtu_not_match, 5000);
                    return;
                }
                // MTU取值范围判断
                int mtuValue = Integer.valueOf(mtu);
                if (mtuValue < 576 | mtuValue > 1492) {
                    toast(R.string.mtu_not_match, 5000);
                    return;
                }
                break;
            case GetWanSettingsBean.CONS_STATIC:
                // 空值判断
                String ipaddress = RootUtils.getEDText(etStaticIpaddress);
                String subnetMask = RootUtils.getEDText(etStaticSubnet);
                if (RootUtils.isEmptys(ipaddress, subnetMask)) {
                    toast(R.string.not_empty, 5000);
                    return;
                }

                // IP高级规则匹配判断
                boolean ip_super_match = RootUtils.isAllMatch(ipaddress);
                boolean subnet_super_match = RootUtils.isAllMatch(subnetMask);

                if (!ip_super_match) {
                    String ipValid = getString(R.string.ip_address) + "\n" + getString(R.string.connect_failed);
                    toast(ipValid, 5000);
                    return;
                }
                if (!subnet_super_match) {
                    String subnetValid = getString(R.string.subnet_mask) + "\n" + getString(R.string.connect_failed);
                    toast(subnetValid, 5000);
                    return;
                }
                break;
        }

        // 封装并提交
        hibernateAndRequest(connectType);
    }

    /**
     * 封装并提交
     *
     * @param connectType
     */
    private void hibernateAndRequest(int connectType) {
        //  初始化--> 封装数据
        SetWanSettingsParam wsp = new SetWanSettingsParam();
        wsp.setSubNetMask(result.getSubNetMask());
        wsp.setGateway(result.getGateway());
        wsp.setIpAddress(result.getIpAddress());
        wsp.setMtu(result.getMtu());
        wsp.setConnectType(connectType);
        wsp.setPrimaryDNS(result.getPrimaryDNS());
        wsp.setSecondaryDNS(result.getSecondaryDNS());
        wsp.setAccount(result.getAccount());
        wsp.setPassword(result.getPassword());
        wsp.setStatus(result.getStatus());
        wsp.setStaticIpAddress(result.getStaticIpAddress());
        wsp.setPppoeMtu(result.getPppoeMtu());

        // 根据类型重新给对应的字段赋值
        switch (connectType) {
            case GetWanSettingsBean.CONS_PPPOE:
                String account = RootUtils.getEDText(etPppoeAccount);
                String password = RootUtils.getEDText(etPppoePsd);
                String mtu = RootUtils.getEDText(etPppoeMtu);
                wsp.setAccount(account);
                wsp.setPassword(password);
                wsp.setPppoeMtu(Integer.valueOf(mtu));
                break;
            case GetWanSettingsBean.CONS_STATIC:
                String ipAddress = RootUtils.getEDText(etStaticIpaddress);
                String subnet = RootUtils.getEDText(etStaticSubnet);
                wsp.setIpAddress(ipAddress);
                wsp.setSubNetMask(subnet);
                break;
        }
        // 检查WAN口状态并准备提交
        checkWanStatuAndReadyToRequest(wsp);
    }

    /**
     * 检查WAN口状态并准备提交
     */
    private void checkWanStatuAndReadyToRequest(SetWanSettingsParam wsp) {
        wdLoad.setVisibles();
        // 获取登陆状态
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                // 登陆 -- 则获取WAN口状态
                GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
                xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(wanSettingsBean -> {
                    int status = wanSettingsBean.getStatus();
                    switch (status) {
                        case GetWanSettingsBean.CONS_CONNECTED:
                            sendRequest(wsp);
                            break;
                        case GetWanSettingsBean.CONS_DISCONNECTED:
                        case GetWanSettingsBean.CONS_DISCONNECTING:
                            showFailed();
                            break;
                    }
                });
                xGetWanSettingsHelper.setOnGetWanSettingFailedListener(this::showFailed);
                xGetWanSettingsHelper.getWanSettings();

            } else {
                wdLoad.setGone();
                toast(R.string.log_out, 5000);
                toFrag(getClass(), LoginFrag.class, null, true);
            }
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(this::showFailed);
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 出错
     */
    private void showFailed() {
        wdLoad.setGone();
        rlSuccessful.setVisibility(View.GONE);
        rlFailed.setVisibility(View.VISIBLE);// 显示失败状态
        toast(R.string.connect_failed, 5000);
    }

    /**
     * 发送设置请求
     */
    private void sendRequest(SetWanSettingsParam wsp) {
        SetWanSettingsHelper xSetWanSettingsHelper = new SetWanSettingsHelper();
        xSetWanSettingsHelper.setOnSetWanSettingsSuccessListener(this::reGet);
        xSetWanSettingsHelper.setOnSetWanSettingsFailedListener(this::showFailed);
        xSetWanSettingsHelper.setWanSettings(wsp);
    }

    /**
     * 重新获取WAN口状态
     */
    private void reGet() {
        GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
        xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(wanSettingsBean -> {
            int status = wanSettingsBean.getStatus();
            switch (status) {
                case GetWanSettingsBean.CONS_CONNECTED:
                    showSuccess();
                    break;
                case GetWanSettingsBean.CONS_CONNECTING:
                    reGet();
                    break;
                case GetWanSettingsBean.CONS_DISCONNECTED:
                case GetWanSettingsBean.CONS_DISCONNECTING:
                    showFailed();
                    break;
            }
        });
        xGetWanSettingsHelper.setOnGetWanSettingFailedListener(this::showFailed);
        xGetWanSettingsHelper.getWanSettings();
    }

    /**
     * 显示成功状态页, 延迟2秒跳转
     */
    private void showSuccess() {
        wdLoad.setGone();
        // 显示成功状态
        rlSuccessful.setVisibility(View.VISIBLE);
        rlFailed.setVisibility(View.GONE);
        // 延迟跳转
        Handler handler = new Handler();
        handler.postDelayed(this::skip, 1000);
    }

    /**
     * 跳过
     */
    private void skip() {
        // 状态页隐藏
        rlSuccessful.setVisibility(View.GONE);
        rlFailed.setVisibility(View.GONE);
        // 提交向导标记
        ShareUtils.set(RootCons.SP_WIZARD, true);
        ShareUtils.set(RootCons.SP_WAN_INIT, true);
        // 是否进入过wifi初始向导页
        boolean isWifiInit = ShareUtils.get(RootCons.SP_WIFI_INIT, false);
        if (isWifiInit) {
            // --> 主页
            toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
        } else {
            // 检测是否开启了WPS模式
            checkWps();
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
        wpsHelper.setOnGetWPSFailedListener(() -> toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true));
        wpsHelper.getWpsStatus();
    }

    /**
     * 显示选中的视图
     */
    public void showCheck(int position) {
        // 缓存标记
        MODE = position;
        // 选中checkbox
        for (int i = 0; i < iv_wanmodes.length; i++) {
            iv_wanmodes[i].setVisibility(i == position ? View.VISIBLE : View.GONE);
        }
        // 详情卡
        if (position == DHCP) {
            rlPppoeDetail.setVisibility(View.GONE);
            rlStaticDetail.setVisibility(View.GONE);
        } else if (position == PPPOE) {
            rlPppoeDetail.setVisibility(View.VISIBLE);
            rlStaticDetail.setVisibility(View.GONE);
        } else if (position == STATIC) {
            rlPppoeDetail.setVisibility(View.GONE);
            rlStaticDetail.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示对应的参数
     */
    private void showDetail(int mode, GetWanSettingsBean result) {
        switch (mode) {
            case GetWanSettingsBean.CONS_PPPOE:
                etPppoeAccount.setText(result.getAccount());
                etPppoePsd.setText(result.getPassword());
                etPppoeMtu.setText(String.valueOf(result.getPppoeMtu()));
                break;
            case GetWanSettingsBean.CONS_STATIC:
                etStaticIpaddress.setText(result.getStaticIpAddress());
                etStaticSubnet.setText(result.getSubNetMask());
                break;
        }
    }

    @Override
    public boolean onBackPresss() {
        if (lastFrag == WizardFrag.class) {
            toFrag(getClass(), WizardFrag.class, null, true);
        } else {
            LogoutHelper xLogouthelper = new LogoutHelper();
            xLogouthelper.setOnLogoutSuccessListener(() -> toFrag(getClass(), LoginFrag.class, null, true));
            xLogouthelper.setOnLogOutFailedListener(() -> toast(R.string.login_logout_failed, 5000));
            xLogouthelper.logout();
        }
        // 隐藏软键盘
        RootUtils.hideKeyBoard(activity);
        return true;
    }
}
