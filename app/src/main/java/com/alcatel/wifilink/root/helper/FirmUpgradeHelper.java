package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.ToastTool;
import com.alcatel.wifilink.root.widget.HH70_CountDownWidget;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.hiber.bean.SkipBean;
import com.hiber.hiber.RootMAActivity;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetDeviceNewVersionBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectionStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceStartUpdateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetFOTAStartDownloadHelper;

/**
 * Created by qianli.ma on 2017/12/15 0015.
 */

public class FirmUpgradeHelper {
    private Activity activity;

    public FirmUpgradeHelper(Activity activity, HH70_LoadWidget wdLoad, boolean isDialog) {
        this.activity = activity;
        wdLoad.setVisibility(isDialog? View.VISIBLE:View.GONE);
    }

    /**
     * 开始升级
     */
    public void startUpgrade() {
        SetDeviceStartUpdateHelper xSetDeviceStartUpdateHelper = new SetDeviceStartUpdateHelper();
        xSetDeviceStartUpdateHelper.setOnSetDeviceStartUpdateSuccessListener(this::startUpgradeNext);
        xSetDeviceStartUpdateHelper.setOnSetDeviceStartUpdateFailedListener(this::errorNext);
        xSetDeviceStartUpdateHelper.setDeviceStartUpdate();
    }

    private OnStartUpgradeListener onStartUpgradeListener;

    // 接口OnStartUpgradeListener
    public interface OnStartUpgradeListener {
        void startUpgrade();
    }

    // 对外方式setOnStartUpgradeListener
    public void setOnStartUpgradeListener(OnStartUpgradeListener onStartUpgradeListener) {
        this.onStartUpgradeListener = onStartUpgradeListener;
    }

    // 封装方法startUpgradeNext
    private void startUpgradeNext() {
        if (onStartUpgradeListener != null) {
            onStartUpgradeListener.startUpgrade();
        }
    }


    /* -------------------------------------------- method2 -------------------------------------------- */

    /**
     * 触发FOTA下载
     */
    public void triggerFOTA() {
        SetFOTAStartDownloadHelper xSetFOTAStartDownloadHelper = new SetFOTAStartDownloadHelper();
        xSetFOTAStartDownloadHelper.setOnSetFOTAStartDownloadSuccessListener(this::setFOTADownSuccessNext);
        xSetFOTAStartDownloadHelper.setOnSetFOTAStartDownloadFailedListener(this::errorNext);
        xSetFOTAStartDownloadHelper.setFOTAStartDownload();
    }

    private OnSetFOTADownSuccessListener onSetFOTADownSuccessListener;

    // 接口OnSetFOTADownSuccessListener
    public interface OnSetFOTADownSuccessListener {
        void setFOTADownSuccess();
    }

    // 对外方式setOnSetFOTADownSuccessListener
    public void setOnSetFOTADownSuccessListener(OnSetFOTADownSuccessListener onSetFOTADownSuccessListener) {
        this.onSetFOTADownSuccessListener = onSetFOTADownSuccessListener;
    }

    // 封装方法setFOTADownSuccessNext
    private void setFOTADownSuccessNext() {
        if (onSetFOTADownSuccessListener != null) {
            onSetFOTADownSuccessListener.setFOTADownSuccess();
        }
    }

    /* -------------------------------------------- method1 -------------------------------------------- */

    /**
     * 检查新版本
     */
    public void checkNewVersion(HH70_CountDownWidget wd_countdown, HH70_LoadWidget wdLoad) {
        checkWan(wd_countdown, wdLoad);// 检测是否连接了WAN口或者SIM卡(WAN口优先)
    }

    /**
     * 检查wan口
     */
    private void checkWan(HH70_CountDownWidget wd_countdown, HH70_LoadWidget wdLoad) {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
                xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(wanSettingsBean -> {
                    switch (wanSettingsBean.getStatus()) {
                        case GetWanSettingsBean.CONS_CONNECTED:
                            checkNw(wd_countdown, wdLoad);
                            break;
                        case GetWanSettingsBean.CONS_CONNECTING:
                        case GetWanSettingsBean.CONS_DISCONNECTED:
                        case GetWanSettingsBean.CONS_DISCONNECTING:
                            checkSim(wd_countdown, wdLoad);
                            break;
                    }
                });
                xGetWanSettingsHelper.setOnGetWanSettingFailedListener(() -> checkSim(wd_countdown, wdLoad));
                xGetWanSettingsHelper.getWanSettings();
            } else {
                loginOutNext();
            }
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> checkSim(wd_countdown, wdLoad));
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 检查sim card
     */
    private void checkSim(HH70_CountDownWidget wd_countdown, HH70_LoadWidget wdload) {
        wdload.setGone();
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGOUT) {
                to(RootCons.ACTIVITYS.SPLASH_AC, RootCons.FRAG.LOGIN_FR);
                return;
            }
            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
                int simState = result.getSIMState();
                if (simState != GetSimStatusBean.CONS_SIM_CARD_READY) {
                    toast(R.string.hh70_check_your_wan);
                } else {
                    // 加入拨号连接的判断, 如果没有拨号连接则提示没有拨号
                    GetConnectionStateHelper xGetConnectionStateHelper = new GetConnectionStateHelper();
                    xGetConnectionStateHelper.setOnGetConnectionStateFailedListener(() -> toast(R.string.hh70_check_your_wan));
                    xGetConnectionStateHelper.setOnDisConnectingListener(() -> toast(R.string.hh70_no_connect));
                    xGetConnectionStateHelper.setOnDisconnectedListener(() -> toast(R.string.hh70_no_connect));
                    xGetConnectionStateHelper.setOnConnectingListener(() -> toast(R.string.hh70_no_connect));
                    xGetConnectionStateHelper.setOnConnectedListener(() -> checkNw(wd_countdown, wdload));
                    xGetConnectionStateHelper.getConnectionState();
                }
            });
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> toast(R.string.hh70_check_your_wan));
            xGetSimStatusHelper.getSimStatus();
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> toast(R.string.hh70_check_your_wan));
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 跳转activity
     */
    private void to(String targetAc, String targetFr) {
        Intent intent = new Intent();
        intent.setAction(targetAc);
        SkipBean skipBean = RootMAActivity.getPendingIntentValue(targetAc, targetFr, null);
        skipBean.setCurrentACFinish(true);
        intent.putExtra(RootMAActivity.getPendingIntentKey(), skipBean);
        activity.startActivity(intent);
    }

    /**
     * 检查新版本
     */
    private void checkNw(HH70_CountDownWidget wd_countdown, HH70_LoadWidget wdLoad) {
        wdLoad.setGone();
        UpgradeHelper uh = new UpgradeHelper(activity, true);
        uh.setOnUpgradeFailedListener(() -> toast(R.string.hh70_cant_get_new_version));
        uh.setOnCheckErrorListener(this::getCurrentVersion);
        uh.setOnServiceNotAvailableListener(attr -> toast(R.string.hh70_service_not_available));
        uh.setOnNoConnectListener(attr -> toast(R.string.hh70_no_connect));
        uh.setOnNoNewVersionListener(this::getCurrentVersion);
        uh.setOnNewVersionListener(this::newVersionNext);
        uh.checkVersion(wd_countdown, wdLoad);
    }

    /**
     * 获取当前版本号
     */
    private void getCurrentVersion(GetDeviceNewVersionBean updateDeviceNewVersion) {
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(result -> noNewVersionNext(updateDeviceNewVersion, result));
        xGetSystemInfoHelper.setOnAppErrorListener(() -> toast(R.string.hh70_cant_get_new_version));
        xGetSystemInfoHelper.setOnFwErrorListener(() -> toast(R.string.hh70_cant_get_new_version));
        xGetSystemInfoHelper.getSystemInfo();
    }

    private OnErrorListener onErrorListener;

    // 接口OnErrorListener
    public interface OnErrorListener {
        void error();
    }

    // 对外方式setOnErrorListener
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    // 封装方法errorNext
    private void errorNext() {
        if (onErrorListener != null) {
            onErrorListener.error();
        }
    }

    private OnNewVersionListener onNewVersionListener;

    // 接口OnNewVersionListener
    public interface OnNewVersionListener {
        void newVersion(GetDeviceNewVersionBean attr);
    }

    // 对外方式setOnNewVersionListener
    public void setOnNewVersionListener(OnNewVersionListener onNewVersionListener) {
        this.onNewVersionListener = onNewVersionListener;
    }

    // 封装方法newVersionNext
    private void newVersionNext(GetDeviceNewVersionBean attr) {
        if (onNewVersionListener != null) {
            onNewVersionListener.newVersion(attr);
        }
    }

    private OnNoNewVersionListener onNoNewVersionListener;

    // 接口OnNoNewVersionListener
    public interface OnNoNewVersionListener {
        void noNewVersion(GetDeviceNewVersionBean updateDeviceNewVersion, GetSystemInfoBean attr);
    }

    // 对外方式setOnNoNewVersionListener
    public void setOnNoNewVersionListener(OnNoNewVersionListener onNoNewVersionListener) {
        this.onNoNewVersionListener = onNoNewVersionListener;
    }

    // 封装方法noNewVersionNext
    private void noNewVersionNext(GetDeviceNewVersionBean updateDeviceNewVersion, GetSystemInfoBean attr) {
        if (onNoNewVersionListener != null) {
            onNoNewVersionListener.noNewVersion(updateDeviceNewVersion, attr);
        }
    }

    private OnLoginOutListener onLoginOutListener;

    // Inteerface--> 接口OnLoginOutListener
    public interface OnLoginOutListener {
        void loginOut();
    }

    // 对外方式setOnLoginOutListener
    public void setOnLoginOutListener(OnLoginOutListener onLoginOutListener) {
        this.onLoginOutListener = onLoginOutListener;
    }

    // 封装方法loginOutNext
    private void loginOutNext() {
        if (onLoginOutListener != null) {
            onLoginOutListener.loginOut();
        }
    }

    private void toast(int resId) {
        ToastTool.show(activity, resId);
    }

}
