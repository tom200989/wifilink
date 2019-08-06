package com.alcatel.wifilink.root.ue.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.WanSettingsParams;
import com.alcatel.wifilink.root.bean.WanSettingsResult;
import com.alcatel.wifilink.root.helper.BoardWanHelper;
import com.alcatel.wifilink.root.helper.CheckBoardLogin;
import com.alcatel.wifilink.root.helper.GetWanSettingHelper;
import com.alcatel.wifilink.root.helper.WpsHelper;
import com.alcatel.wifilink.root.app.SmartLinkV3App;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.SP;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WanModeRxActivity extends AppCompatActivity {

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
    


    private int PPPOE = 0;// 标记2
    private int DHCP = 1;// 标记1
    private int STATIC = 2;// 标记3
    private int MODE = DHCP;// 缓存点击后的标记
    private TimerHelper heartTimer;
    private ImageView[] iv_wanmodes;
    private ProgressDialog pgd;
    private WanSettingsResult result;
    private BoardWanHelper boardWanHelper;
    private Activity activity;
    private String flag = "";
    private boolean isRussia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        activity = this;
        setContentView(R.layout.activity_wan_mode_rx);
        SmartLinkV3App.getContextInstance().add(this);
        ButterKnife.bind(this);
        isRussia = OtherUtils.getCurrentLanguage().equalsIgnoreCase("ru");
        initRes();
        initUi();
        startHeartTimer();
        initData();
    }

    private void initUi() {
        if (isRussia) {
            tvWanmodeRxTitle.setScaleX(0.65f);
            tvSkip.setScaleX(0.65f);
        }
    }

    private void initRes() {
        iv_wanmodes = new ImageView[]{ivPppoeCheck, ivDhcpCheck, ivStaticCheck};
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getFlag(String flag) {// 判断是否从向导页跳转过来的
        this.flag = flag;
    }

    private void initData() {
        if (pgd == null) {
            pgd = OtherUtils.showProgressPop(this);
        }

        GetWanSettingHelper wan = new GetWanSettingHelper();
        wan.setOnGetwansettingsErrorListener(e -> {
            toast(R.string.check_your_wan_cabling);
            to(RefreshWifiRxActivity.class);
            OtherUtils.hideProgressPop(pgd);
        });
        wan.setOnGetWanSettingsFailedListener(() -> {
        });
        wan.setOnGetWanSettingsResultErrorListener(error -> {
            toast(R.string.check_your_wan_cabling);
            OtherUtils.hideProgressPop(pgd);
        });
        wan.setOnGetWanSettingsSuccessListener(result -> {
            WanModeRxActivity.this.result = result;
            int wanStatus = result.getStatus();
            if (wanStatus == Cons.CONNECTED) {
                OtherUtils.hideProgressPop(pgd);
                MODE = result.getConnectType();
                showCheck(MODE);// 切换到对应的选项板
                showDetail(MODE, result);// 显示对应的参数
            } else if (wanStatus == Cons.CONNECTING) {
                initData();
            } else {
                toast(R.string.check_your_wan_cabling);
                OtherUtils.hideProgressPop(pgd);
            }
        });
        wan.get();
    }

    /**
     * 显示对应的参数
     *
     * @param mode
     * @param result
     */
    private void showDetail(int mode, WanSettingsResult result) {
        switch (mode) {
            case Cons.PPPOE:
                etPppoeAccount.setText(result.getAccount());
                etPppoePsd.setText(result.getPassword());
                etPppoeMtu.setText(String.valueOf(result.getPppoeMtu()));
                break;
            case Cons.STATIC:
                Logs.t("ma_etherwan").vv("static ip_phone: " + result.getStaticIpAddress());
                etStaticIpaddress.setText(result.getStaticIpAddress());
                etStaticSubnet.setText(result.getSubNetMask());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        OtherUtils.stopHeartBeat(heartTimer);
    }

    private void startHeartTimer() {
        heartTimer = OtherUtils.startHeartBeat(this, RefreshWifiRxActivity.class, LoginRxActivity.class);
    }

    @Override
    public void onBackPressed() {
        clickBackButton();
    }

    /**
     * 点击返回登陆界面
     */
    private void clickBackButton() {
        if (Cons.WIZARD_RX.equalsIgnoreCase(flag)) {
            to(WizardRxActivity.class);
        } else {
            LogoutHelper xLogouthelper = new LogoutHelper();
            xLogouthelper.setOnLogoutSuccessListener(() -> to(LoginRxActivity.class));
            xLogouthelper.setOnLogOutFailedListener(() -> ToastUtil_m.show(this, getString(R.string.login_logout_failed)));
            xLogouthelper.logout();
        }
    }

    @OnClick({R.id.iv_wanmode_rx_back,// 回退
            R.id.tv_wanmode_rx_skip,// 跳过
            R.id.rl_wanmode_check_dhcp,// DHCP
            R.id.iv_wanmode_rx_dhcp_check,// DHCP
            R.id.rl_wanmode_check_pppoe,// pppoe
            R.id.iv_wanmode_rx_pppoe_check,// pppoe
            R.id.rl_wanmode_check_static,// static
            R.id.iv_wanmode_rx_static_check,// static
            R.id.tv_wanmode_rx_connect,// 连接按钮
            R.id.tv_wanmode_rx_tryagain,// 重试按钮
            R.id.tv_wanmode_rx_tohome// 前往主页
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wanmode_rx_back:// 回退
                clickBackButton();
                break;
            case R.id.tv_wanmode_rx_skip:// 跳过
                toAc();
                break;
            case R.id.rl_wanmode_check_dhcp:// DHCP
            case R.id.iv_wanmode_rx_dhcp_check:
                showCheck(DHCP);
                break;
            case R.id.rl_wanmode_check_pppoe:// PPPOE
            case R.id.iv_wanmode_rx_pppoe_check:
                showCheck(PPPOE);
                break;
            case R.id.rl_wanmode_check_static:// STATIC
            case R.id.iv_wanmode_rx_static_check:
                showCheck(STATIC);
                break;
            case R.id.tv_wanmode_rx_connect:// 连接
                connectClick(MODE);
                break;
            case R.id.tv_wanmode_rx_tryagain:// 重试
                rlFailed.setVisibility(View.GONE);
                rlSuccessful.setVisibility(View.GONE);
                break;
            case R.id.tv_wanmode_rx_tohome:// 前往主页
                toHome();
                break;
        }
        // 隐藏软键盘
        OtherUtils.hideKeyBoard(this);
    }

    /**
     * 点击了connect按钮逻辑
     */
    private void connectClick(int connectType) {
        // wifi是否连接
        if (OtherUtils.isWifiConnect(this)) {

            switch (connectType) {
                case Cons.PPPOE:
                    // 空值判断
                    if (OtherUtils.isEmptys(etPppoeAccount, etPppoePsd, etPppoeMtu)) {
                        toast(R.string.not_empty);
                        return;
                    }
                    // MTU匹配判断
                    String mtu = OtherUtils.getEdContent(etPppoeMtu);
                    if (mtu.startsWith("0")) {// 是否为0开头
                        toast(R.string.mtu_not_match);
                        return;
                    }
                    // MTU取值范围判断
                    int mtuValue = Integer.valueOf(mtu);
                    if (mtuValue < 576 | mtuValue > 1492) {
                        toast(R.string.mtu_not_match);
                        return;
                    }
                    break;
                case Cons.STATIC:
                    // 空值判断
                    String ipaddress = OtherUtils.getEdContent(etStaticIpaddress);
                    String subnetMask = OtherUtils.getEdContent(etStaticSubnet);
                    if (OtherUtils.isEmptys(ipaddress, subnetMask)) {
                        toast(R.string.not_empty);
                        return;
                    }

                    // IP高级规则匹配判断
                    boolean ip_super_match = OtherUtils.ipSuperMatch(ipaddress);
                    boolean subnet_super_match = OtherUtils.ipSuperMatch(subnetMask);

                    if (!ip_super_match) {
                        Logs.t("ma_ip").vv("ip_phone: " + ip_super_match + ";");
                        String ipValid = getString(R.string.ip_address) + "\n" + getString(R.string.connect_failed);
                        toast(ipValid);
                        return;
                    }
                    if (!subnet_super_match) {
                        Logs.t("ma_ip").vv("subnet: " + subnet_super_match + ";");
                        String subnetValid = getString(R.string.subnet_mask) + "\n" + getString(R.string.connect_failed);
                        toast(subnetValid);
                        return;
                    }
                    break;
            }

            // 封装并提交
            hibernateAndRequest(connectType);
        } else {
            toast(R.string.connect_failed);
            to(RefreshWifiRxActivity.class);
        }
    }

    /**
     * 封装并提交
     *
     * @param connectType
     */
    private void hibernateAndRequest(int connectType) {
        //  初始化--> 封装数据
        WanSettingsParams wsp = new WanSettingsParams();
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
            case Cons.PPPOE:
                String account = OtherUtils.getEdContent(etPppoeAccount);
                String password = OtherUtils.getEdContent(etPppoePsd);
                String mtu = OtherUtils.getEdContent(etPppoeMtu);
                wsp.setAccount(account);
                wsp.setPassword(password);
                wsp.setPppoeMtu(Integer.valueOf(mtu));
                break;
            case Cons.STATIC:
                String ipAddress = OtherUtils.getEdContent(etStaticIpaddress);
                String subnet = OtherUtils.getEdContent(etStaticSubnet);
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
    private void checkWanStatuAndReadyToRequest(WanSettingsParams wsp) {
        if (pgd == null) {
            pgd = OtherUtils.showProgressPop(this);
        }
        if (boardWanHelper == null) {
            boardWanHelper = new BoardWanHelper(this);
        }
        boardWanHelper.setOnConnetedNextListener(wanResult -> sendRequest(wsp));
        boardWanHelper.setOnDisConnetedNextListener(wanResult -> showFailed());
        boardWanHelper.setOnDisConnetedNextListener(wanResult -> showFailed());
        boardWanHelper.boardNormal();
    }


    /**
     * 发送WAN设置请求
     */
    private void sendRequest(WanSettingsParams wsp) {
        if (boardWanHelper == null) {
            boardWanHelper = new BoardWanHelper(this);
        }
        boardWanHelper.setOnSendRequestSuccess(this::showSuccess);
        boardWanHelper.setOnSendRequestFailed(this::showFailed);
        boardWanHelper.sendWanRequest(wsp);
    }

    /**
     * 显示成功状态页, 延迟2秒跳转
     */
    private void showSuccess() {
        OtherUtils.hideProgressPop(pgd);
        // 显示成功状态
        rlSuccessful.setVisibility(View.VISIBLE);
        rlFailed.setVisibility(View.GONE);
        // 延迟跳转
        Handler handler = new Handler();
        handler.postDelayed(WanModeRxActivity.this::toAc, 1500);
    }

    /**
     * 显示失败状态页
     */
    private void showFailed() {
        OtherUtils.hideProgressPop(pgd);
        rlSuccessful.setVisibility(View.GONE);
        rlFailed.setVisibility(View.VISIBLE);// 显示失败状态
    }

    /**
     * 直接跳转到主页
     */
    private void toHome() {
        new CheckBoardLogin(this) {
            @Override
            public void afterCheckSuccess(ProgressDialog pgd) {
                // 提交向导标记
                SP.getInstance(activity).putBoolean(Cons.WIZARD_RX, true);
                SP.getInstance(activity).putBoolean(Cons.WANMODE_RX, true);
                SP.getInstance(activity).putBoolean(Cons.WIFIINIT_RX, true);
                // --> 主页
                to(HomeRxActivity.class);
            }
        };

    }

    private void toAc() {
        // 状态页隐藏
        rlSuccessful.setVisibility(View.GONE);
        rlFailed.setVisibility(View.GONE);
        // 提交向导标记
        SP.getInstance(this).putBoolean(Cons.WIZARD_RX, true);
        SP.getInstance(this).putBoolean(Cons.WANMODE_RX, true);
        // 是否进入过wifi初始向导页
        if (SP.getInstance(this).getBoolean(Cons.WIFIINIT_RX, false)) {
            // --> 主页
            to(HomeRxActivity.class);
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
        wpsHelper.setOnWpsListener(attr -> to(attr ? HomeRxActivity.class : WifiInitRxActivity.class));
        wpsHelper.setOnErrorListener(attr -> to(HomeRxActivity.class));
        wpsHelper.setOnResultErrorListener(attr -> to(HomeRxActivity.class));
        wpsHelper.getWpsStatus();
    }

    /**
     * 显示选中的视图
     *
     * @param position
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

    public void toast(int resId) {
        ToastUtil_m.show(this, resId);
    }

    public void toast(String content) {
        ToastUtil_m.show(this, content);
    }

    public void to(Class ac) {
        CA.toActivity(this, ac, false, true, false, 0);
    }
}
