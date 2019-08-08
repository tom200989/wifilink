package com.alcatel.wifilink.root.ue.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.adapter.LoginDevicesAdapter;
import com.alcatel.wifilink.root.bean.ConnectedList;
import com.alcatel.wifilink.root.bean.Other_DeviceBean;
import com.alcatel.wifilink.root.bean.Other_PinPukBean;
import com.alcatel.wifilink.root.helper.CheckBoard;
import com.alcatel.wifilink.root.helper.ConnectDeviceHelper;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.DeviceHelper;
import com.alcatel.wifilink.root.helper.PreHelper;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.helper.WpsHelper;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Lgg;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.SP;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.widget.DialogOkWidget;
import com.p_freesharing.p_freesharing.bean.InteractiveRequestBean;
import com.p_freesharing.p_freesharing.bean.InteractiveResponceBean;
import com.p_freesharing.p_freesharing.ui.SharingFileActivity;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LoginHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginRxActivity extends BaseActivityWithBack {

    @BindView(R.id.rl_login)
    RelativeLayout rl_login;
    @BindView(R.id.tv_login_linkzone)
    TextView tvLoginLinkzone;
    @BindView(R.id.et_loginRx)
    EditText etLoginRx;
    @BindView(R.id.iv_loginrx_logo)
    ImageView ivLoginrxLogo;
    @BindView(R.id.iv_loginRx_checkbox)
    ImageView ivLoginRxCheckbox;
    @BindView(R.id.tv_remember_psd)
    TextView tvRememberPsd;
    @BindView(R.id.rl_login_remenberPsd)
    RelativeLayout rlLoginRemenberPsd;
    @BindView(R.id.bt_loginRx)
    Button btLoginRx;
    @BindView(R.id.tv_loginRx_forgot)
    TextView tvLoginRxForgot;

    @BindView(R.id.rl_login_pre)
    RelativeLayout rl_loginPre;// 预登录总布局
    @BindView(R.id.iv_login_backToPre)
    ImageView ivLoginBackToPre;// 返回图标
    @BindView(R.id.tv_login_backToPre)
    TextView tvLoginBackToPre;// 返回文本
    @BindView(R.id.tv_login_pre_netName)
    TextView tvLoginPreNetName;// 网络名称
    @BindView(R.id.btn_login_pre_toLogin)
    Button btnLoginPreToLogin;// 点击按钮切换到登陆界面
    @BindView(R.id.tv_login_pre_battery_percent)
    TextView tvLoginPreBatteryPercent;// 电池电量
    @BindView(R.id.pg_login_pre_battery)
    ProgressBar pgLoginPreBattery;// 电量进度条
    @BindView(R.id.iv_login_pre_signal)
    ImageView ivLoginPreSignal;// 信号强度
    @BindView(R.id.tv_login_pre_mobileType)
    TextView tvLoginPreMobileType;// 网络类型(4G|3G...)
    @BindView(R.id.tv_login_pre_connected_count)
    TextView tvLoginPreConnectedCount;// 连接设备数
    @BindView(R.id.rl_login_pre_connected)
    RelativeLayout rlLoginPreConnected;// 点击后切换到登陆界面
    @BindView(R.id.rl_login_pre_freesharing)
    RelativeLayout rlLoginPreFreesharing;// 点击后跳转到free sharing界面

    @BindView(R.id.rl_login_devices)
    LinearLayout rlLoginDevices;// 显示设备总布局
    @BindView(R.id.iv_pre_devices_back)
    ImageView ivPreDevicesBack;// 返回
    @BindView(R.id.rcv_pre_devices)
    RecyclerView rcvPreDevices;// 设备列表

    @BindView(R.id.rl_loginrx_reset_factory)
    DialogOkWidget rlLoginrxResetFactory;//  恢复出厂设置视图
    // @BindView(R.id.tv_loginrx_reset_factory_des)
    // TextView tvLoginrxResetFactoryDes;// 描述
    // @BindView(R.id.bt_loginrx_reset_factory_ok)
    // TextView btLoginrxResetFactoryOk;// OK按钮

    private boolean isRemem;
    private ProgressDialog pgd;
    private String deviceName;
    private TimerHelper preTimer;
    private PreHelper phe;
    private ConnectDeviceHelper cdh;
    private List<Other_DeviceBean> dbs = new ArrayList<>();// 设备列表集合
    private LoginDevicesAdapter devicesAdapter;// 设备列表适配器
    private Drawable cpe_logo;
    private Drawable mw120_logo;
    private Drawable hh71_logo;
    private Drawable default_logo;
    private String TAG = "LoginRxActivity";
    private GetSystemStatusHelper getSystemStatusHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_rx);
        ButterKnife.bind(this);
        initRes();
        initAdapter();
        initHelper();
    }

    private void initAdapter() {
        // ** 初始化设备列表适配器
        LinearLayoutManager devicesLlm = new LinearLayoutManager(this, 1, false);
        devicesAdapter = new LoginDevicesAdapter(this, null);
        rcvPreDevices.setLayoutManager(devicesLlm);
        rcvPreDevices.setAdapter(devicesAdapter);
    }

    private void initHelper() {
        getSignalAbout();// 获取信号相关的信息
        getDevicesInfo();// 获取在线设备的信息
        getSystemStatusInfo();// 获取网络名CMCC等
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        OtherUtils.transferLanguage(this);
        OtherUtils.clearAllTimer();
        OtherUtils.stopHomeTimer();
        OtherUtils.clearContexts(getClass().getSimpleName());
        stopHomeHeart();
        startPreTimer();// 启动预载页定时器
    }

    /**
     * 启动预载页定时器
     */
    private void startPreTimer() {
        preTimer = new TimerHelper(this) {
            @Override
            public void doSomething() {
                getSystemStatusHelper.getSystemStatus();// 获取与网络相关(信号强度,网络类型)
                cdh.get();// 获取设备数详细信息(IP地址,MAC地址,设备名)
            }
        };
        preTimer.start(3000);
    }

    /**
     * 获取在线设备的信息
     */
    private void getDevicesInfo() {
        // 1.初始化设备列表对象
        cdh = new ConnectDeviceHelper();
        GetSystemInfoHelper getSystemInfoHelper = new GetSystemInfoHelper();
        getSystemInfoHelper.setOnGetSystemInfoSuccessListener(attr -> {
            // 2.如果是MW120设备, 则显示LINKZONE,否则显示LINKHUB
            String deviceName = attr.getDeviceName().toLowerCase();
            boolean isMwSerial = deviceName.startsWith(Cons.MW_SERIAL);// 是否为MW系列
            boolean isHH71 = deviceName.equals(Cons.HH71);// 是否为71系列
            tvLoginLinkzone.setText(isMwSerial ? "LINKZONE" : "LINKHUB");

            // 2.判断显示何种LOGO
            Drawable logoDrawable = isMwSerial ? mw120_logo : isHH71 ? hh71_logo : cpe_logo;
            // 2.如果是MW120设备--> 显示对应的LOGO
            ivLoginrxLogo.setImageDrawable(logoDrawable);

            // 2.如果是MW120新设备, 则获取设备列表信息
            if (attr.getDeviceName().toLowerCase().startsWith(Cons.MW_SERIAL)) {
                cdh.setOnDevicesSuccessListener(connectedList -> {
                    int size = connectedList.getConnectedList().size();
                    // 3.更新适配器
                    List<Other_DeviceBean> ddbs = OtherUtils.transferDevicesbean(connectedList);
                    devicesAdapter.notifys(ddbs);

                });
                cdh.setOnDevicesErrorListener(error -> {
                });
                cdh.setOnDevicesFailedListener(o -> {
                });
            }
        });
        getSystemInfoHelper.setOnFwErrorListener(() -> ivLoginrxLogo.setImageDrawable(default_logo));
        getSystemInfoHelper.setOnAppErrorListener(() -> ivLoginrxLogo.setImageDrawable(default_logo));
        getSystemInfoHelper.getSystemInfo();

    }


    /**
     * 获取cmcc名称
     */
    private void getSystemStatusInfo() {
        GetSystemStatusHelper helper = new GetSystemStatusHelper();
        helper.setOnGetSystemStatusSuccessListener(getSystemStatusBean -> {
            String networkName = getSystemStatusBean.getNetworkName();
            tvLoginPreNetName.setText(networkName);
        });
        helper.setOnGetSystemStatusFailedListener(() -> tvLoginPreNetName.setText(""));
        helper.getSystemStatus();
    }

    /**
     * 获取与网络相关(信号强度,网络类型)
     */
    private void getSignalAbout() {
        /* 电池+强度+网络类型+连接数 */
        phe = new PreHelper(this);
        getSystemStatusHelper = new GetSystemStatusHelper();
        getSystemStatusHelper.setOnGetSystemStatusSuccessListener(result -> {
            // 设置电池电量
            Drawable batFlash = getResources().getDrawable(R.drawable.battery_01_flash);
            Drawable batUse = getResources().getDrawable(R.drawable.battery_01);
            boolean isCharing = result.getChg_state() == GetSystemStatusBean.CONS_CHARGE_CHARGING;// 电池状态
            int cap = result.getBat_cap();// 电池电量
            pgLoginPreBattery.setProgress(isCharing ? 0 : cap <= 12 ? 12 : cap);
            pgLoginPreBattery.setProgressDrawable(isCharing ? batFlash : batUse);
            tvLoginPreBatteryPercent.setText(String.valueOf(cap + "%"));
            // 设置强度
            Drawable strenght = phe.getStrenght(result.getSignalStrength());
            ivLoginPreSignal.setImageDrawable(strenght);
            // 设置网络类型
            String mobileType = phe.getMobileType(result.getNetworkType());
            tvLoginPreMobileType.setText(mobileType);
            // 设置连接数
            int c2g = result.getCurr_num_2g();
            int c5g = result.getCurr_num_5g();
            tvLoginPreConnectedCount.setText(String.valueOf(c2g + c5g));
        });
        getSystemStatusHelper.setOnGetSystemStatusFailedListener(() -> {
            preError();
        });
    }


    /**
     * 接收loadingRxActivity Line:105行 传递过来的设备名
     *
     * @param deviceName
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getDeviceName(String deviceName) {
        this.deviceName = deviceName.toLowerCase();
        showLoginPreOrNot(OtherUtils.isMw120(deviceName));
    }

    @Override
    protected void onPause() {
        super.onPause();
        preTimer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void stopHomeHeart() {
        if (HomeRxActivity.heartTimer != null) {
            HomeRxActivity.heartTimer.stop();
        }
    }

    @Override
    public void onBackPressed() {

        /* 设备界面显示--> 隐藏     (优先级0) */
        if (rlLoginDevices.getVisibility() == View.VISIBLE) {
            rlLoginDevices.setVisibility(View.GONE);

            /* 恢复出厂提示--> 隐藏 */
        } else if (rlLoginrxResetFactory.getVisibility() == View.VISIBLE) {
            rlLoginrxResetFactory.setVisibility(View.GONE);

            /* 预登陆界面显示--> 退出    (优先级1) */
        } else if (rl_loginPre.getVisibility() == View.VISIBLE) {
            exit();

            /* 正常登陆界面显示--> 新设备: 显示预登陆; 老设备: 退出   (优先级2) */
        } else {
            GetSystemInfoHelper getSystemInfoHelper = new GetSystemInfoHelper();
            getSystemInfoHelper.setOnGetSystemInfoSuccessListener(attr -> {
                // 如果是MW120新设备--> 则显示预登陆界面; 反之退出
                if (attr.getDeviceName().toLowerCase().startsWith(Cons.MW_SERIAL)) {
                    showLoginPreOrNot(true);
                } else {
                    exit();
                }
            });
            getSystemInfoHelper.setOnFwErrorListener(() -> exit());
            getSystemInfoHelper.setOnAppErrorListener(() -> exit());
            getSystemInfoHelper.getSystemInfo();
        }
    }

    /**
     * 退出
     */
    private void exit() {
        OtherUtils.clearAllTimer();
        OtherUtils.clearContexts(getClass().getSimpleName());
        finish();
        OtherUtils.kill();
        super.onBackPressed();
    }

    private void initRes() {
        cpe_logo = getResources().getDrawable(R.drawable.cpe_login_logo);
        mw120_logo = getResources().getDrawable(R.drawable.mw120_login_logo);
        hh71_logo = getResources().getDrawable(R.drawable.hh71_login_logo);
        default_logo = getResources().getDrawable(R.drawable.ic_launcher);
        isRemem = SP.getInstance(this).getBoolean(Cons.LOGIN_REMEM, false);
        String password = SP.getInstance(this).getString(Cons.LOGIN_RXPSD, "");
        ivLoginRxCheckbox.setImageResource(isRemem ? R.drawable.general_btn_remember_pre : R.drawable.general_btn_remember_nor);
        etLoginRx.setText(isRemem ? password : "");
        etLoginRx.setSelection(OtherUtils.getEdContent(etLoginRx).length());
        pgLoginPreBattery.setProgressDrawable(getResources().getDrawable(R.drawable.battery_01));
        pgLoginPreBattery.setProgress(12);
    }

    @OnClick({R.id.rl_login_remenberPsd,// 记住密码
            R.id.bt_loginRx,// 登陆按钮
            R.id.tv_loginRx_forgot,// 忘记密码
            R.id.iv_login_backToPre,// 返回
            R.id.tv_login_backToPre,// 返回
            R.id.btn_login_pre_toLogin,// 切换至登陆
            R.id.rl_login_pre_connected,// 切换至登陆
            R.id.rl_login_pre_freesharing,// free sharing
            R.id.iv_pre_devices_back// 设备列表返回键
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_login_remenberPsd:
                isRemem = !isRemem;
                ivLoginRxCheckbox.setImageResource(isRemem ? R.drawable.general_btn_remember_pre : R.drawable.general_btn_remember_nor);
                break;
            case R.id.bt_loginRx:
                loginButtonClick();// 点击登陆按钮后的逻辑
                break;
            case R.id.tv_loginRx_forgot:
                showAlertDialog();// 显示对话框
                break;

            case R.id.iv_login_backToPre:// 返回
            case R.id.tv_login_backToPre:// 返回
                showLoginPreOrNot(true);
                break;
            case R.id.btn_login_pre_toLogin:// 切换至登陆
                showLoginPreOrNot(false);
                break;
            case R.id.rl_login_pre_connected:// 切换至设备数界面
                if (Integer.valueOf(tvLoginPreConnectedCount.getText().toString()) > 0) {
                    rlLoginDevices.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_login_pre_freesharing:// 切换至free sharing
                // TODO: 2019/7/24 0024 MA_FREESHARING 
                // to(FreeSharingActivity.class, false); // 上线时把这句打开
                to(SharingFileActivity.class, false);
                break;

            case R.id.iv_pre_devices_back:// 设备界面返回键
                rlLoginDevices.setVisibility(View.GONE);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiverFreesharingAAR(InteractiveRequestBean requestBean) {
        if (requestBean.getRecevie() == InteractiveRequestBean.REQ) {
            getDevicesList();
        }
    }

    private void getDevicesList() {
        if (BaseActivityWithBack.isFreeSharingLock) {
            synchronized (BaseActivityWithBack.class) {
                BaseActivityWithBack.isFreeSharingLock = false;
                DeviceHelper deviceHelper = new DeviceHelper(this);
                deviceHelper.setOnGetDevicesSuccessListener(connectedList -> {
                    // 转换
                    com.p_freesharing.p_freesharing.bean.ConnectedList aarConnectList = transferConnectList(connectedList);
                    InteractiveResponceBean responceBean = new InteractiveResponceBean();
                    responceBean.setConnectedList(aarConnectList);
                    responceBean.setErrorMsg("no error msg");
                    responceBean.setType(InteractiveResponceBean.DEVICEHELPER_SUCCESS);
                    EventBus.getDefault().post(responceBean);
                    BaseActivityWithBack.isFreeSharingLock = true;
                });
                deviceHelper.setOnGetDevicesResultErrorListener(error -> {
                    InteractiveResponceBean responceBean = new InteractiveResponceBean();
                    responceBean.setConnectedList(null);
                    responceBean.setErrorMsg("no error msg");
                    responceBean.setType(InteractiveResponceBean.DEVICEHELPER_FW_ERROR);
                    EventBus.getDefault().post(responceBean);
                    BaseActivityWithBack.isFreeSharingLock = true;
                });
                deviceHelper.setOnGetDevicesErrorListener(throwable -> {
                    InteractiveResponceBean responceBean = new InteractiveResponceBean();
                    responceBean.setConnectedList(null);
                    responceBean.setErrorMsg("no error msg");
                    responceBean.setType(InteractiveResponceBean.DEVICEHELPER_APP_ERROR);
                    EventBus.getDefault().post(responceBean);
                    BaseActivityWithBack.isFreeSharingLock = true;
                });
                deviceHelper.getDeviecs();
            }
        }
    }

    /**
     * 将本地的connectlist转换成aar接收的bean
     */
    private com.p_freesharing.p_freesharing.bean.ConnectedList transferConnectList(ConnectedList connectedList) {
        // 本工程的connectlist
        List<ConnectedList.Device> wifiConnectList = connectedList.getConnectedList();
        // 创建aar包里的connectlist
        com.p_freesharing.p_freesharing.bean.ConnectedList aarConnectList = new com.p_freesharing.p_freesharing.bean.ConnectedList();
        List<com.p_freesharing.p_freesharing.bean.ConnectedList.Device> aarDevices = new ArrayList<>();
        int size = wifiConnectList.size();
        for (int i = 0; i < size; i++) {
            com.p_freesharing.p_freesharing.bean.ConnectedList.Device aarDevice = new com.p_freesharing.p_freesharing.bean.ConnectedList().new Device();
            aarDevice.setAssociationTime(wifiConnectList.get(i).getAssociationTime());
            aarDevice.setConnectMode(wifiConnectList.get(i).getConnectMode());
            aarDevice.setDeviceName(wifiConnectList.get(i).getDeviceName());
            aarDevice.setDeviceType(wifiConnectList.get(i).getDeviceType());
            aarDevice.setId(wifiConnectList.get(i).getId());
            aarDevice.setInternetRight(wifiConnectList.get(i).getInternetRight());
            aarDevice.setIPAddress(wifiConnectList.get(i).getIPAddress());
            aarDevice.setMacAddress(wifiConnectList.get(i).getMacAddress());
            aarDevice.setStorageRight(wifiConnectList.get(i).getStorageRight());
            aarDevices.add(aarDevice);
        }
        aarConnectList.setConnectedList(aarDevices);
        return aarConnectList;
    }

    /**
     * 保存密码
     */
    private void loginButtonClick() {
        // 1. 检测空值
        String loginText = OtherUtils.getEdContent(etLoginRx);
        Lgg.t(TAG).ii("password: " + loginText);
        if (TextUtils.isEmpty(loginText)) {
            ToastUtil_m.show(this, getString(R.string.setting_password_note));
            return;
        }
        // 2.检测密码位数
        int length = loginText.length();
        if (length < 4 | length > 16) {
            ToastUtil_m.show(this, getString(R.string.setting_password_note));
            return;
        }
        // 4.启动登陆
        startLogin();
    }

    /**
     * 保存密码
     */
    private void putRemberPassword() {
        // 3.保存密码
        SP.getInstance(this).putBoolean(Cons.LOGIN_REMEM, isRemem);
        if (isRemem) {
            SP.getInstance(this).putString(Cons.LOGIN_RXPSD, OtherUtils.getEdContent(etLoginRx));
        } else {
            SP.getInstance(this).putString(Cons.LOGIN_RXPSD, "");
        }
    }

    /**
     * 启动登陆
     */
    private void startLogin() {
        // 检测硬件是否连接正常
        new CheckBoard() {
            @Override
            public void successful() {
                pgd = OtherUtils.showProgressPop(LoginRxActivity.this);
                String password = etLoginRx.getText().toString();
                LoginHelper loginHelper = new LoginHelper();
                loginHelper.setOnLoginSuccesListener(() -> getConnectMode());// 登录成功
                loginHelper.setOnPsdNotCorrectListener(() -> {// 密码错误 -- 提示次数限制
                    OtherUtils.hideProgressPop(pgd);
                    showRemainTimes();
                });// 密码不正确
                loginHelper.setOnLoginFailedListener(() -> {// 其他原因导致的无法登陆
                    OtherUtils.hideProgressPop(pgd);
                    ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_failed));
                });
                loginHelper.setOnDeviceRebootListener(() -> {// 超出次数需要重启
                    OtherUtils.hideProgressPop(pgd);
                    ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_login_time_used_out_msg));
                });
                loginHelper.setOnGuestWebUiListener(() -> {// WEB端被登陆
                    OtherUtils.hideProgressPop(pgd);
                    ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_login_app_or_webui));
                });
                loginHelper.setOnOtherUserLoginListener(() -> {// 其他用户登陆
                    OtherUtils.hideProgressPop(pgd);
                    ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_other_user_logined_error_msg));
                });
                loginHelper.setOnLoginOutTimeListener(() -> {// 登陆出错超限
                    OtherUtils.hideProgressPop(pgd);
                    ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_login_time_used_out_msg));
                });
                loginHelper.login("admin", password);
            }
        }.checkBoard(this, RefreshWifiRxActivity.class, RefreshWifiRxActivity.class);
    }

    /**
     * 检测连接模式
     */
    private void getConnectMode() {

        GetWanSettingsHelper helper = new GetWanSettingsHelper();
        helper.setOnFwErrorListener(() -> {
            OtherUtils.hideProgressPop(pgd);
            whenGetWanFailed();
            Lgg.t(TAG).ee("Getwansettings: resultError--> ");
        });
        helper.setOnAppErrorListener(() -> {
            OtherUtils.hideProgressPop(pgd);
            whenGetWanFailed();
            Lgg.t(TAG).ee("Getwansettings: resultError--> ");
        });
        helper.setOnGetWanSettingsSuccessListener(result -> {
            Lgg.t(TAG).ii("GetWanSettings: Success");
            /* 获取WAN口状态 */
            int wanStatus = result.getStatus();

            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(bean -> {
                /* 获取SIM卡状态 */
                int simState = bean.getSIMState();
                boolean simflag =
                        simState == GetSimStatusBean.CONS_SIM_CARD_READY || simState == GetSimStatusBean.CONS_PIN_REQUIRED || simState == GetSimStatusBean.CONS_PUK_REQUIRED;
                OtherUtils.hideProgressPop(pgd);
                    if (wanStatus == GetWanSettingsBean.CONS_CONNECTED & simflag) {// 都有
                    isToWizard();
                    return;
                }
                    if (wanStatus != GetWanSettingsBean.CONS_CONNECTED && simflag) {// 只有SIM卡
                        if (simState == Cons.READY) {
                            simHadReady(false);
                        } else if (simState == Cons.PIN_REQUIRED) {
                        EventBus.getDefault().postSticky(new Other_PinPukBean(Cons.PIN_FLAG));
                            to(PinPukIndexRxActivity.class);
                    } else if (simState == GetSimStatusBean.CONS_PUK_REQUIRED) {
                        EventBus.getDefault().postSticky(new Other_PinPukBean(Cons.PUK_FLAG));
                        to(PinPukIndexRxActivity.class);
                    } else {
                        to(HomeRxActivity.class);
                    }
                    return;
                }
                    if (wanStatus == GetWanSettingsBean.CONS_CONNECTED & !simflag) {// 只有WAN口
                    wanReady();
                    return;
                }
                    if (wanStatus != GetWanSettingsBean.CONS_CONNECTED & !simflag) {// 都没有
                    isToWizard();
                    return;
                }

            });
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(this::isToWizard);
            xGetSimStatusHelper.getSimStatus();
        });
        helper.getWanSettings();
    }

    /**
     * WAN口连接完成
     */
    private void wanReady() {
        // 是否进入过wan口设置向导页
        if (SP.getInstance(LoginRxActivity.this).getBoolean(Cons.WANMODE_RX, false)) {
            // 是否进入过wifi向导页
            if (SP.getInstance(LoginRxActivity.this).getBoolean(Cons.WIFIINIT_RX, false)) {
                to(HomeRxActivity.class);
            } else {
                // 检测是否开启了WPS模式
                checkWps();
            }
        } else {
            to(WanModeRxActivity.class);
        }
    }


    /**
     * 当获取wan口失败做出设备类型判断
     */
    private void whenGetWanFailed() {
        GetSystemInfoHelper getSystemInfoHelper = new GetSystemInfoHelper();
        getSystemInfoHelper.setOnGetSystemInfoSuccessListener(systemInfo -> {
            String deviceName = systemInfo.getDeviceName().toLowerCase();
            if (OtherUtils.isMw120(deviceName)) {// MW120
                Lgg.t(TAG).ii(":whenGetWanFailed() is mw120 again");

                GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
                xGetSimStatusHelper.setOnGetSimStatusSuccessListener(attr -> {
                    int simState = attr.getSIMState();
                    if (simState == GetSimStatusBean.CONS_SIM_CARD_READY) {
                        simHadReady(true);
                    } else if (simState == GetSimStatusBean.CONS_PIN_REQUIRED ) {
                        EventBus.getDefault().postSticky(new Other_PinPukBean(Cons.PIN_FLAG));
                        to(PinPukIndexRxActivity.class);
                    } else if (simState == GetSimStatusBean.CONS_PUK_REQUIRED) {
                        EventBus.getDefault().postSticky(new Other_PinPukBean(Cons.PUK_FLAG));
                        to(PinPukIndexRxActivity.class);
                    } else {
                        to(HomeRxActivity.class);
                    }
                });
                xGetSimStatusHelper.setOnGetSimStatusFailedListener(this::isToWizard);
                xGetSimStatusHelper.getSimStatus();

            } else {// HH70
                Lgg.t(TAG).ee(":whenGetWanFailed() is not mw120 again");
                ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_failed));
            }
        });
        getSystemInfoHelper.setOnFwErrorListener(() -> {
            Lgg.t(TAG).ee("HH70: whenGetWanFailed error--> ");
            ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_failed));
        });
        getSystemInfoHelper.setOnAppErrorListener(() -> {
            Lgg.t(TAG).ee("HH70: whenGetWanFailed error--> ");
            ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_failed));
        });
        getSystemInfoHelper.getSystemInfo();
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
     * SIM卡能正常使用后
     * (包含解PIN完毕后 | SIM本身已经ready)
     */
    private void simHadReady(boolean isMW120) {
        // 1.是否进入过流量向导界面
        if (SP.getInstance(LoginRxActivity.this).getBoolean(Cons.DATAPLAN_RX, false)) {
            // 1.1.是否进入过wifi初始化向导界面
            if (SP.getInstance(LoginRxActivity.this).getBoolean(Cons.WIFIINIT_RX, false)) {
                to(HomeRxActivity.class);// --> 主页
            } else {
                // 是否为新设备？
                if (!isMW120) {
                    checkWps();// 检测wps状态:如果是wps, 则不允许修改wifi属性
                } else {
                    to(WifiInitRxActivity.class);
                }

            }
        } else {
            to(DataPlanRxActivity.class);
        }
    }


    /**
     * 是否进入连接设置向导页
     */
    private void isToWizard() {
        if (SP.getInstance(LoginRxActivity.this).getBoolean(Cons.WIZARD_RX, false)) {
            to(HomeRxActivity.class);
        } else {
            to(WizardRxActivity.class);
        }
    }

    /**
     * 跳转
     *
     * @param clazz
     */
    private void to(Class clazz) {
        CA.toActivity(this, clazz, false, true, false, 0);
    }

    /**
     * 按情况跳转
     *
     * @param clazz
     */
    private void to(Class clazz, boolean isFinish) {
        CA.toActivity(this, clazz, false, isFinish, false, 0);
    }

    /**
     * 显示剩余次数
     */
    public void showRemainTimes() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            OtherUtils.hideProgressPop(pgd);
            String content = "";
            int remainingTimes = getLoginStateBean.getLoginRemainingTimes();
            String noRemain = getString(R.string.login_login_time_used_out_msg);
            String remain = getString(R.string.login_psd_error_msg);
            String tips = remain;
            String remainTips = String.format(tips, remainingTimes);
            content = remainingTimes <= 0 ? noRemain : remainTips;
            ToastUtil_m.show(LoginRxActivity.this, content);
        });
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 显示对话框
     */
    private void showAlertDialog() {
        // 重启设备
        rlLoginrxResetFactory.setVisibility(View.VISIBLE);
        rlLoginrxResetFactory.setTitle(R.string.reset);
        rlLoginrxResetFactory.setDes(R.string.reset_tip);
        rlLoginrxResetFactory.setOnBgClickListener(() -> Lgg.t(TAG).ii("click not in the area"));
        rlLoginrxResetFactory.setCancelVisible(false);
        rlLoginrxResetFactory.setOnOkClickListener(() -> rlLoginrxResetFactory.setVisibility(View.GONE));
    }

    /**
     * 是否显示预登录页
     *
     * @param isShowPre
     */
    public void showLoginPreOrNot(boolean isShowPre) {
        rl_loginPre.setVisibility(isShowPre ? View.VISIBLE : View.GONE);
        rl_login.setVisibility(isShowPre ? View.GONE : View.VISIBLE);
        ivLoginBackToPre.setVisibility(OtherUtils.isMw120(deviceName) ? View.VISIBLE : View.GONE);
        tvLoginBackToPre.setVisibility(OtherUtils.isMw120(deviceName) ? View.VISIBLE : View.GONE);
    }


    /**
     * 获取信号、网络、连接数错误
     */
    private void preError() {
        tvLoginPreBatteryPercent.setText("0%");
        pgLoginPreBattery.setProgress(0);
        ivLoginPreSignal.setImageDrawable(getResources().getDrawable(R.drawable.mw_signal_0));
        tvLoginPreMobileType.setText(getString(R.string.home_no_service));
        tvLoginPreConnectedCount.setText(String.valueOf(0));
    }
}
