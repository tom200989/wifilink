package com.alcatel.wifilink.root.ue.root_activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.LoginStateHelper;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.SP;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

// TOGO 2019/8/17 0017 SplashFrag
@Deprecated
public class LoadingRxActivity extends BaseActivityWithBack {

    private static String[] PERMISSIONS_NEED = {// 
            Manifest.permission.READ_EXTERNAL_STORAGE,// 读取外部存储
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入外部存储
            Manifest.permission.CHANGE_WIFI_STATE,// 切换WIFI
            Manifest.permission.ACCESS_WIFI_STATE// 访问WIFI
    };
    private static final int REQUEST_NEED = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_rx);
        // OtherUtils.transferLanguage(this);
        verifyPermission();// 认证权限
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Integer> ints = new ArrayList<>();
        for (int grantResult : grantResults) {
            ints.add(grantResult);
        }

        // 如果有任意一个权限未通过, 则杀死进程
        if (ints.contains(PackageManager.PERMISSION_DENIED)) {
            killself();
            return;
        } else {
            doOperate();
        }
    }

    /**
     * 杀死自身并退出
     */
    private void killself() {
        finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void verifyPermission() {
        // 6.0 以上申请权限
        if (Build.VERSION.SDK_INT < 23) {
            doOperate();
        } else {
            // 开启外部存储权限
            int exstorePer = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // 开启WIFI权限
            int wifiPer = ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE);
            if (exstorePer == PackageManager.PERMISSION_DENIED | wifiPer == PackageManager.PERMISSION_DENIED) {// 权限本身不允许--> 请求
                ActivityCompat.requestPermissions(this, PERMISSIONS_NEED, REQUEST_NEED);
            } else {// 权限本身允许--> 执行业务逻辑
                doOperate();
            }
        }
    }

    /**
     * 整体业务逻辑
     */
    private void doOperate() {
        stopAll();// 停止所有的定时器和context残余
        LoginStateHelper lsh = new LoginStateHelper(this);
        lsh.setOnErrorListener(attr -> to(RefreshWifiRxActivity.class, true, 2000));
        lsh.setOnResultErrorListener(attr -> to(RefreshWifiRxActivity.class, true, 2000));
        lsh.setOnNoWifiListener(attr -> to(RefreshWifiRxActivity.class, true, 2000));
        lsh.setOnYesWifiListener(attr -> {});
        lsh.setOnLoginstateListener(attr -> nextSkip());
        lsh.get();
        getSystemAbout();
    }

    /**
     * 获取系统相关信息(主要用于设备类型)
     */
    private void getSystemAbout() {
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(info -> {
            String deviceName = info.getDeviceName();
            EventBus.getDefault().postSticky(deviceName);// 发送--> LoginRxActivity
        });
        xGetSystemInfoHelper.setOnFwErrorListener(() -> to(RefreshWifiRxActivity.class, true));
        xGetSystemInfoHelper.setOnAppErrorListener(() -> to(RefreshWifiRxActivity.class, true));
        xGetSystemInfoHelper.getSystemInfo();
    }

    /**
     * 停止所有的定时器和context残余
     */
    private void stopAll() {
        OtherUtils.clearAllTimer();
        OtherUtils.stopHomeTimer();
        OtherUtils.clearContexts(getClass().getSimpleName());
    }

    /**
     * 下一步跳转
     */
    private void nextSkip() {
        boolean guideRx_flag = SP.getInstance(this).getBoolean(Cons.GUIDE_RX, false);
        if (guideRx_flag) {
            CA.toActivity(this, LoginRxActivity.class, false, true, false, 2000);
        } else {
            CA.toActivity(this, GuideRxActivity.class, false, true, false, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void toast(int resId) {
        ToastUtil_m.show(this, resId);
    }

    private void toastLong(int resId) {
        ToastUtil_m.showLong(this, resId);
    }

    private void toast(String content) {
        ToastUtil_m.show(this, content);
    }

    private void to(Class ac, boolean isFinish) {
        CA.toActivity(this, ac, false, isFinish, false, 0);
    }

    private void to(Class ac, boolean isFinish, int delay) {
        CA.toActivity(this, ac, false, isFinish, false, delay);
    }
}
