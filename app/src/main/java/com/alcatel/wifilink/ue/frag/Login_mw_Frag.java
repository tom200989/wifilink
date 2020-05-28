package com.alcatel.wifilink.ue.frag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.helper.PreLoginHelper;
import com.alcatel.wifilink.helper.DeviceHelper;
import com.alcatel.wifilink.ue.activity.SplashActivity;
import com.alcatel.wifilink.utils.RootCons;
import com.alcatel.wifilink.utils.RootUtils;
import com.hiber.cons.TimerState;
import com.hiber.hiber.RootFrag;
import com.hiber.impl.RootEventListener;
import com.hiber.tools.ShareUtils;
import com.p_freesharing.p_freesharing.bean.InteractiveRequestBean;
import com.p_freesharing.p_freesharing.bean.InteractiveResponceBean;
import com.p_freesharing.p_freesharing.ui.SharingFileActivity;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemStatusHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/15 0015.
 */
public class Login_mw_Frag extends RootFrag {

    @BindView(R.id.rl_login_pre)
    RelativeLayout rl_loginPre;// 预登录总布局
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
    @BindView(R.id.rl_login_pre_connected)
    RelativeLayout rlLoginPreConnected;// 点击后切换到连接设备列表
    @BindView(R.id.tv_login_pre_connected_count)
    TextView tvLoginPreConnectedCount;// 连接设备数
    @BindView(R.id.rl_login_pre_freesharing)
    RelativeLayout rlLoginPreFreesharing;// 点击后跳转到free sharing界面

    private PreLoginHelper preLoginHelper;// 转换辅助工具
    private int currentConnCount;// 当前连接总数

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_login_mw;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        initRes();
        initClick();
        initEvent();
        lastFrag = getClass();
        timerState = TimerState.OFF_ALL_BUT_KEEP_CURRENT_OFF_WHEN_PAUSE;
    }

    private void initRes() {
        pgLoginPreBattery.setProgressDrawable(getResources().getDrawable(R.drawable.battery_01));
        pgLoginPreBattery.setProgress(12);
    }

    private void initEvent() {
        // 监听FREE-SHARING
        setEventListener(InteractiveRequestBean.class, new RootEventListener<InteractiveRequestBean>() {
            @Override
            public void getData(InteractiveRequestBean requestBean) {
                if (requestBean.getRecevie() == InteractiveRequestBean.REQ) {
                    getDevicesList();
                }
            }
        });
    }

    /**
     * 获取设备列表
     */
    private void getDevicesList() {
        if (SplashActivity.freeSharingLock) {
            synchronized (SplashActivity.class) {
                SplashActivity.freeSharingLock = false;
                DeviceHelper deviceHelper = new DeviceHelper();
                deviceHelper.setOnGetDevicesSuccessListener(connectedList -> {
                    // 转换
                    com.p_freesharing.p_freesharing.bean.ConnectedList aarConnectList = transferConnectList(connectedList);
                    InteractiveResponceBean responceBean = new InteractiveResponceBean();
                    responceBean.setConnectedList(aarConnectList);
                    responceBean.setErrorMsg("no error msg");
                    responceBean.setType(InteractiveResponceBean.DEVICEHELPER_SUCCESS);
                    sendEvent(responceBean, false);
                    SplashActivity.freeSharingLock = true;
                });
                deviceHelper.setOnFwErrorListener(() -> {
                    InteractiveResponceBean responceBean = new InteractiveResponceBean();
                    responceBean.setConnectedList(null);
                    responceBean.setErrorMsg("no error msg");
                    responceBean.setType(InteractiveResponceBean.DEVICEHELPER_FW_ERROR);
                    sendEvent(responceBean, false);
                    SplashActivity.freeSharingLock = true;
                });
                deviceHelper.setOnAppErrorListener(() -> {
                    InteractiveResponceBean responceBean = new InteractiveResponceBean();
                    responceBean.setConnectedList(null);
                    responceBean.setErrorMsg("no error msg");
                    responceBean.setType(InteractiveResponceBean.DEVICEHELPER_APP_ERROR);
                    sendEvent(responceBean, false);
                    SplashActivity.freeSharingLock = true;
                });
                deviceHelper.getDeviecs();
            }
        }
    }

    /**
     * 将本地的connectlist转换成aar接收的bean
     */
    private com.p_freesharing.p_freesharing.bean.ConnectedList transferConnectList(GetConnectDeviceListBean connectedListBean) {
        // 本工程的connectlist
        List<GetConnectDeviceListBean.ConnectedDeviceBean> wifiConnectList = connectedListBean.getConnectedList();
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
     * 点击
     */
    private void initClick() {
        btnLoginPreToLogin.setOnClickListener(v -> toFrag(getClass(), LoginFrag.class, null, true));
        rlLoginPreConnected.setOnClickListener(v -> {
            if (currentConnCount > 0) {// 当设备连接数大于0才连接
                // 因为FW的错误导致接口没有权限，导致这个操作先屏蔽
                //toFrag(getClass(), Login_mw_dev_Frag.class, null, true);
            } else {// 提示没发现设备
                toast(R.string.hh70_no_device_find, 5000);
            }
        });
        rlLoginPreFreesharing.setOnClickListener(v -> {
            Intent intent = new Intent(activity, SharingFileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void setTimerTask() {
        getSignalAbout();// 获取信号相关的信息
        getSystemStatusInfo();// 获取网络名CMCC等
        getDeviceName();// 获取设备名
    }

    /**
     * 获取设备名
     */
    private void getDeviceName() {
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(getSystemInfobean -> {
            // 如果不是MW设备 -- 切换到普通登陆页
            String deviceName = getSystemInfobean.getDeviceName();
            ShareUtils.set(RootCons.DEVICE_NAME, deviceName);
            boolean isMWDev = RootUtils.isMWDEV(deviceName);
            if (!isMWDev) {
                toFrag(getClass(), LoginFrag.class, null, true);
            }
        });
        xGetSystemInfoHelper.getSystemInfo();
    }

    /**
     * 获取与网络相关(信号强度,网络类型)
     */
    @SuppressLint("SetTextI18n")
    private void getSignalAbout() {
        // 电池 + 强度 + 网络类型 + 连接数
        preLoginHelper = new PreLoginHelper(activity);
        GetSystemStatusHelper xGetSystemStatusHelper = new GetSystemStatusHelper();
        xGetSystemStatusHelper.setOnGetSystemStatusSuccessListener(result -> {
            // 设置电池电量
            Drawable batFlash = getResources().getDrawable(R.drawable.battery_01_flash);
            Drawable batUse = getResources().getDrawable(R.drawable.battery_01);
            boolean isCharing = result.getChg_state() == GetSystemStatusBean.CONS_CHARGE_CHARGING;// 电池状态
            int cap = result.getBat_cap();// 电池电量
            pgLoginPreBattery.setProgress(isCharing ? 0 : Math.max(cap, 12));
            pgLoginPreBattery.setProgressDrawable(isCharing ? batFlash : batUse);
            tvLoginPreBatteryPercent.setText(cap + "%");
            // 设置强度
            Drawable strenght = preLoginHelper.getStrenght(result.getSignalStrength());
            ivLoginPreSignal.setImageDrawable(strenght);
            // 设置网络类型
            String mobileType = preLoginHelper.getMobileType(result.getNetworkType());
            tvLoginPreMobileType.setText(mobileType);
            // 设置连接数
            int c2g = result.getCurr_num_2g();
            int c5g = result.getCurr_num_5g();
            currentConnCount = c2g + c5g;
            tvLoginPreConnectedCount.setText(String.valueOf(currentConnCount));
        });
        xGetSystemStatusHelper.setOnGetSystemStatusFailedListener(this::getSystemStatusError);
        xGetSystemStatusHelper.getSystemStatus();
    }

    /**
     * 获取信号、网络、连接数错误
     */
    private void getSystemStatusError() {
        tvLoginPreBatteryPercent.setText("0%");
        pgLoginPreBattery.setProgress(0);
        ivLoginPreSignal.setImageDrawable(getRootDrawable(R.drawable.mw_signal_0));
        tvLoginPreMobileType.setText(getString(R.string.hh70_no_service));
        tvLoginPreConnectedCount.setText(String.valueOf(0));
    }

    /**
     * 获取cmcc名称
     */
    private void getSystemStatusInfo() {
        GetSystemStatusHelper xGetSystemStatusHelper = new GetSystemStatusHelper();
        xGetSystemStatusHelper.setOnGetSystemStatusSuccessListener(getSystemStatusBean -> tvLoginPreNetName.setText(getSystemStatusBean.getNetworkName()));
        xGetSystemStatusHelper.setOnGetSystemStatusFailedListener(() -> tvLoginPreNetName.setText(""));
        xGetSystemStatusHelper.getSystemStatus();
    }

    @Override
    public boolean onBackPresss() {
        return false;
    }
}
