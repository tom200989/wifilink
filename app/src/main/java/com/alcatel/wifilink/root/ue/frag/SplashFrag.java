package com.alcatel.wifilink.root.ue.frag;

import android.Manifest;
import android.view.View;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.hiber.bean.PermissBean;
import com.hiber.bean.StringBean;
import com.hiber.cons.TimerState;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;

/*
 * Created by qianli.ma on 2019/8/12 0012.
 */
public class SplashFrag extends BaseFrag {

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_splash;
    }

    @Override
    public String[] initPermissed() {
        // 设置权限监听
        setPermissedListener((isAllPermissionPass, notPassPermissions) -> {
            if (!isAllPermissionPass) {// 权限没有全部通过 -- 结束APP
                killAllActivitys();
                kill();
            }
        });
        // 创建自定义权限视图对象
        PermissBean permissBean = new PermissBean();
        StringBean stringBean = new StringBean();
        stringBean.setCancel(String.valueOf(R.string.hh70_cancel));
        stringBean.setOk(String.valueOf(R.string.hh70_ok));
        stringBean.setTitle(String.valueOf(R.string.warning));
        permissBean.setStringBean(stringBean);
        permissBean.setPermissView(null);
        // 传递给框架内部
        setPermissBean(permissBean);
        return new String[]{// permission
                Manifest.permission.READ_EXTERNAL_STORAGE,// 读取SD
                Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入SD
                Manifest.permission.CHANGE_WIFI_STATE,// 切换WIFI
                Manifest.permission.ACCESS_WIFI_STATE};// 获取WIFI
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        timerState = TimerState.OFF_ALL;// 初始关停全部定时器
        checkFW();// 检测FW是否连接正常
    }

    /**
     * 检查FW是否正常
     */
    private void checkFW() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> toFrag(getClass(), RefreshFrag.class, null, true));
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> checkDeviceType());
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 获取设备类型
     */
    private void checkDeviceType() {
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(getSystemInfobean -> {
            // 保存设备名到缓存
            ShareUtils.set(RootCons.DEVICE_NAME, getSystemInfobean.getDeviceName());
            // 跳转
            toNextFrag(getSystemInfobean.getDeviceName());
        });
        xGetSystemInfoHelper.setOnFwErrorListener(() -> toFrag(getClass(), RefreshFrag.class, null, true));
        xGetSystemInfoHelper.setOnAppErrorListener(() -> toFrag(getClass(), RefreshFrag.class, null, true));
        xGetSystemInfoHelper.getSystemInfo();
    }

    /**
     * 前往下一个界面
     */
    private void toNextFrag(String deviceName) {
        boolean isGuide = ShareUtils.get(RootCons.SP_GUIDE, false);
        boolean isMWDev = RootUtils.isMWDEV(deviceName);
        toFrag(getClass(), isGuide ? (isMWDev ? LoginFrag_mw.class : LoginFrag.class) : GuideFrag.class, null, true);
    }

    @Override
    public boolean onBackPresss() {
        return true;
    }
}
