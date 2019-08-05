package com.alcatel.wifilink.root.ue.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.adapter.LoginDevicesAdapter;
import com.alcatel.wifilink.root.bean.ConnectedList;
import com.alcatel.wifilink.root.bean.Other_DeviceBean;
import com.alcatel.wifilink.root.bean.Other_PinPukBean;
import com.alcatel.wifilink.root.bean.SimStatus;
import com.alcatel.wifilink.root.bean.System_SystemInfo;
import com.alcatel.wifilink.root.bean.User_LoginResult;
import com.alcatel.wifilink.root.bean.User_LoginState;
import com.alcatel.wifilink.root.helper.CheckBoard;
import com.alcatel.wifilink.root.helper.ConnectDeviceHelper;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.DeviceHelper;
import com.alcatel.wifilink.root.helper.GetWanSettingHelper;
import com.alcatel.wifilink.root.helper.PreHelper;
import com.alcatel.wifilink.root.helper.SystemInfoHelper;
import com.alcatel.wifilink.root.helper.SystemStatuHelper;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.helper.WpsHelper;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Constants;
import com.alcatel.wifilink.root.utils.EncryptionUtil;
import com.alcatel.wifilink.root.utils.Lgg;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.SP;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.widget.DialogOkWidget;
import com.p_encrypt.p_encrypt.core.md5.Md5Code;
import com.p_freesharing.p_freesharing.bean.InteractiveRequestBean;
import com.p_freesharing.p_freesharing.bean.InteractiveResponceBean;
import com.p_freesharing.p_freesharing.ui.SharingFileActivity;

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
    private SystemStatuHelper ssuh;

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
                phe.get();// 获取与网络相关(信号强度,网络类型)
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
        SystemInfoHelper sif = new SystemInfoHelper();
        sif.setOnGetSystemInfoSuccessListener(attr -> {
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
        sif.setOnResultErrorListener(attr -> ivLoginrxLogo.setImageDrawable(default_logo));
        sif.setOnGetSystemInfoFailedListener(() -> ivLoginrxLogo.setImageDrawable(default_logo));
        sif.get();

    }


    /**
     * 获取cmcc名称
     */
    private void getSystemStatusInfo() {
        ssuh = new SystemStatuHelper();
        ssuh.setOnFailedListener(o -> tvLoginPreNetName.setText(""));
        ssuh.setOnResultErrorListener(error -> tvLoginPreNetName.setText(""));
        ssuh.setOnSuccessListener(systemStates -> {
            String networkName = systemStates.getNetworkName();
            tvLoginPreNetName.setText(networkName);
        });
    }

    /**
     * 获取与网络相关(信号强度,网络类型)
     */
    private void getSignalAbout() {
        /* 电池+强度+网络类型+连接数 */
        phe = new PreHelper(this);
        phe.setOnSuccessListener(result -> {
            // 设置电池电量
            Drawable batFlash = getResources().getDrawable(R.drawable.battery_01_flash);
            Drawable batUse = getResources().getDrawable(R.drawable.battery_01);
            boolean isCharing = result.getChg_state() == Cons.BATTERY_CHARING;// 电池状态
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
        phe.setOnErrorListener(attr -> preError());
        phe.setOnResultErrorListener(attr -> preError());
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
            SystemInfoHelper sif = new SystemInfoHelper();
            sif.setOnGetSystemInfoSuccessListener(attr -> {
                // 如果是MW120新设备--> 则显示预登陆界面; 反之退出
                if (attr.getDeviceName().toLowerCase().startsWith(Cons.MW_SERIAL)) {
                    showLoginPreOrNot(true);
                } else {
                    exit();
                }
            });
            sif.setOnErrorListener(attr -> exit());
            sif.setOnResultErrorListener(attr -> exit());
            sif.get();
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
                OtherUtils otherUtils = new OtherUtils();
                otherUtils.setOnSwVersionListener(needToEncrypt -> {
                    // needToEncrypt是2017年的版本--> 需要加密
                    // 2018年新出的MW120版本, 需要根据版本判断--> 进行新型加密
                    SystemInfoHelper sif = new SystemInfoHelper();
                    sif.setOnErrorListener(attr -> {
                        OtherUtils.hideProgressPop(pgd);
                        ToastUtil_m.show(LoginRxActivity.this, R.string.connect_failed);
                    });
                    sif.setOnResultErrorListener(error -> {
                        OtherUtils.hideProgressPop(pgd);
                        ToastUtil_m.show(LoginRxActivity.this, R.string.connect_failed);
                    });
                    sif.setOnGetSystemInfoSuccessListener(systemInfo -> {
                        // 如果是新设备
                        toLogin(needToEncrypt, systemInfo);
                    });
                    sif.get();
                });
                otherUtils.getDeviceSwVersion();
            }
        }.checkBoard(this, RefreshWifiRxActivity.class, RefreshWifiRxActivity.class);
    }

    /**
     * 登陆操作
     *
     * @param needEncrypt 2017年的版本需要加密
     */
    String account = Constants.USER_NAME_ADMIN;
    String passwd = "";

    private void toLogin(boolean needEncrypt, System_SystemInfo systemSystemInfo) {
        boolean isMW120 = systemSystemInfo.getDeviceName().toLowerCase().startsWith(Cons.MW_SERIAL);
        pgd = OtherUtils.showProgressPop(this);
        // 明文密码
        passwd = etLoginRx.getText().toString().trim();
        account = Constants.USER_NAME_ADMIN;
        account = needEncrypt ? EncryptionUtil.encryptUser(account) : account;
        if (!isMW120) {
            // 2017年的版本加密
            passwd = needEncrypt ? EncryptionUtil.encryptUser(passwd) : passwd;
        } else {
            // 2018年版本加密
            // 登陆账户名ADMIN使用原先算法
            // 登陆密码直接在原文基础上采用MD5加密
            // 登陆成功后返回的token和key, token再经过aes(token, key)后, 再经过BASE64包装 
            // passwd = needEncrypt ? EncryptionUtil.encryptUser(passwd) : passwd;

            // MW120的加密模式(必须小写)
            passwd = Md5Code.encryption(passwd).toLowerCase();
        }

        // 正式请求登陆接口
        requestLogin(account, passwd, systemSystemInfo);
    }

    /**
     * 正式请求登陆接口
     *
     * @param account
     * @param passwd
     */
    private void requestLogin(String account, String passwd, System_SystemInfo systemSystemInfo) {
        // 获取设备名称
        String deviceName = systemSystemInfo.getDeviceName();
        boolean isMw120 = OtherUtils.isMw120(deviceName);
        Lgg.t(TAG).ii("ase--> account: " + account);
        Lgg.t(TAG).ii("ase--> passwd: " + passwd);
        RX.getInstant().login(account, passwd, new ResponseObject<User_LoginResult>() {
            @Override
            protected void onSuccess(User_LoginResult userLoginResult) {
                // 保存密码
                putRemberPassword();
                RX.getInstant().getLoginState(new ResponseObject<User_LoginState>() {
                    @Override
                    protected void onSuccess(User_LoginState userLoginState) {
                        try {
                            if (userLoginState.getState() == Cons.LOGIN) {
                                if (!isMw120) {
                                    // 非MW120机型
                                    RX.getInstant().updateToken(userLoginResult.getToken(), deviceName, "", "");
                                } else {
                                    // 登陆成功后返回的token和key, token再经过aes(token, key, iv)后, 再经过BASE64包装 
                                    String token = userLoginResult.getToken();
                                    String key = userLoginResult.getKey();
                                    String iv = userLoginResult.getIv();
                                    RX.getInstant().updateToken(token, deviceName, key, iv);
                                }

                                // 判断连接的模式从而决定是否进入wizard向导页--> (延迟2秒)
                                getConnectMode(isMw120);
                            } else {
                                OtherUtils.hideProgressPop(pgd);
                                ToastUtil_m.show(LoginRxActivity.this, getString(R.string.smsdetail_tryagain_confirm));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        OtherUtils.hideProgressPop(pgd);
                        CA.toActivity(LoginRxActivity.this, RefreshWifiRxActivity.class, false, true, false, 0);
                    }

                    @Override
                    protected void onResultError(ResponseBody.Error error) {
                        OtherUtils.hideProgressPop(pgd);
                        if (error.getCode().equalsIgnoreCase(Cons.GET_LOGIN_STATE_FAILED)) {
                            ToastUtil_m.show(LoginRxActivity.this, getString(R.string.connection_timed_out));
                        } else {
                            ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_failed));
                        }
                        CA.toActivity(LoginRxActivity.this, RefreshWifiRxActivity.class, false, true, false, 0);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                OtherUtils.hideProgressPop(pgd);
                CA.toActivity(LoginRxActivity.this, RefreshWifiRxActivity.class, false, true, false, 0);
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                Log.v("ma_loginrx", "login impl error: " + error.getCode() + ";errormes: " + error.getMessage());
                OtherUtils.hideProgressPop(pgd);
                if (Cons.PASSWORD_IS_NOT_CORRECT.equals(error.getCode())) {
                    showRemainTimes();// 显示剩余次数
                } else if (Cons.OTHER_USER_IS_LOGIN.equals(error.getCode())) {
                    ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_other_user_logined_error_msg));
                } else if (Cons.DEVICE_REBOOT.equals(error.getCode())) {
                    ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_login_time_used_out_msg));
                } else if (Cons.GUEST_AP_OR_WEBUI.equals(error.getCode())) {
                    ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_login_app_or_webui));
                } else {
                    ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_failed));
                }
            }
        });
    }

    /**
     * 检测连接模式
     *
     * @param isMw120
     */
    private void getConnectMode(boolean isMw120) {

        GetWanSettingHelper wan = new GetWanSettingHelper();
        wan.setOnGetwansettingsErrorListener(e -> {
            OtherUtils.hideProgressPop(pgd);
            whenGetWanFailed();
            Lgg.t(TAG).ee("Getwansettings: error--> " + e.getMessage());
        });
        wan.setOnGetWanSettingsFailedListener(() -> {
            OtherUtils.hideProgressPop(pgd);
            whenGetWanFailed();
            Lgg.t(TAG).ee("Getwansettings: failed");
        });
        wan.setOnGetWanSettingsResultErrorListener(error -> {
            OtherUtils.hideProgressPop(pgd);
            whenGetWanFailed();
            Lgg.t(TAG).ee("Getwansettings: resultError--> " + error.getMessage());
        });
        wan.setOnGetWanSettingsSuccessListener(result -> {
            Lgg.t(TAG).ii("GetWanSettings: Success");
            /* 获取WAN口状态 */
            int wanStatus = result.getStatus();
            RX.getInstant().getSimStatus(new ResponseObject<SimStatus>() {
                @Override
                protected void onSuccess(SimStatus result) {
                    /* 获取SIM卡状态 */
                    int simState = result.getSIMState();
                    boolean simflag = simState == Cons.READY || simState == Cons.PIN_REQUIRED || simState == Cons.PUK_REQUIRED;
                    OtherUtils.hideProgressPop(pgd);
                    if (wanStatus == Cons.CONNECTED & simflag) {// 都有
                        isToWizard();
                        return;
                    }
                    if (wanStatus != Cons.CONNECTED && simflag) {// 只有SIM卡
                        if (simState == Cons.READY) {
                            simHadReady(false);
                        } else if (simState == Cons.PIN_REQUIRED) {
                            EventBus.getDefault().postSticky(new Other_PinPukBean(Cons.PIN_FLAG));
                            to(PinPukIndexRxActivity.class);
                        } else if (simState == Cons.PUK_REQUIRED) {
                            EventBus.getDefault().postSticky(new Other_PinPukBean(Cons.PUK_FLAG));
                            to(PinPukIndexRxActivity.class);
                        } else {
                            to(HomeRxActivity.class);
                        }
                        return;
                    }
                    if (wanStatus == Cons.CONNECTED & !simflag) {// 只有WAN口
                        wanReady();
                        return;
                    }
                    if (wanStatus != Cons.CONNECTED & !simflag) {// 都没有
                        isToWizard();
                        return;
                    }

                }

                @Override
                protected void onResultError(ResponseBody.Error error) {
                    isToWizard();
                }

                @Override
                public void onError(Throwable e) {
                    isToWizard();
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

            });
        });
        wan.get();

    }


    /**
     * 当获取wan口失败做出设备类型判断
     */
    private void whenGetWanFailed() {
        SystemInfoHelper sif = new SystemInfoHelper();
        sif.setOnGetSystemInfoSuccessListener(systemInfo -> {
            String deviceName = systemInfo.getDeviceName().toLowerCase();
            if (OtherUtils.isMw120(deviceName)) {// MW120
                Lgg.t(TAG).ii(":whenGetWanFailed() is mw120 again");
                RX.getInstant().getSimStatus(new ResponseObject<SimStatus>() {
                    @Override
                    protected void onSuccess(SimStatus result) {
                        int simState = result.getSIMState();
                        if (simState == Cons.READY) {
                            simHadReady(true);
                        } else if (simState == Cons.PIN_REQUIRED) {
                            EventBus.getDefault().postSticky(new Other_PinPukBean(Cons.PIN_FLAG));
                            to(PinPukIndexRxActivity.class);
                        } else if (simState == Cons.PUK_REQUIRED) {
                            EventBus.getDefault().postSticky(new Other_PinPukBean(Cons.PUK_FLAG));
                            to(PinPukIndexRxActivity.class);
                        } else {
                            to(HomeRxActivity.class);
                        }
                    }

                    @Override
                    protected void onFailure() {
                        isToWizard();
                    }

                    @Override
                    protected void onResultError(ResponseBody.Error error) {
                        isToWizard();
                    }
                });
            } else {// HH70
                Lgg.t(TAG).ee(":whenGetWanFailed() is not mw120 again");
                ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_failed));
            }
        });
        sif.setOnErrorListener(attr -> {
            Lgg.t(TAG).ee("HH70: whenGetWanFailed error--> " + attr.getMessage());
            ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_failed));
        });
        sif.setOnResultErrorListener(attr -> {
            Lgg.t(TAG).ee("HH70: whenGetWanFailed error--> " + attr.getMessage());
            ToastUtil_m.show(LoginRxActivity.this, getString(R.string.login_failed));
        });
        sif.get();
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
        RX.getInstant().getLoginState(new ResponseObject<User_LoginState>() {
            @Override
            protected void onSuccess(User_LoginState result) {
                String content = "";
                int remainingTimes = result.getLoginRemainingTimes();
                String noRemain = getString(R.string.login_login_time_used_out_msg);
                String remain = getString(R.string.login_psd_error_msg);
                String tips = remain;
                String remainTips = String.format(tips, remainingTimes);
                content = remainingTimes <= 0 ? noRemain : remainTips;
                ToastUtil_m.show(LoginRxActivity.this, content);
            }
        });
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
