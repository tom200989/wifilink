package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetDeviceNewVersionBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectionStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceStartUpdateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetFOTAStartDownloadHelper;

/**
 * Created by qianli.ma on 2017/12/15 0015.
 */

public class FirmUpgradeHelper {
    private Activity activity;
    private ProgressDialog pgd;

    public FirmUpgradeHelper(Activity activity, boolean isDialog) {
        this.activity = activity;
        if (isDialog) {
            pgd = OtherUtils.showProgressPop(activity);
        }
    }

    /**
     * 开始升级
     */
    public void startUpgrade() {
        SetDeviceStartUpdateHelper startUpdateHelper = new SetDeviceStartUpdateHelper();
        startUpdateHelper.setOnSetDeviceStartUpdateSuccessListener(() -> {
            startUpgradeNext();
        });
        startUpdateHelper.setOnSetDeviceStartUpdateFailedListener(() -> {
            errorNext();
        });
        startUpdateHelper.setDeviceStartUpdate();
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
        SetFOTAStartDownloadHelper setFOTAStartDownloadHelper = new SetFOTAStartDownloadHelper();
        setFOTAStartDownloadHelper.setOnSetFOTAStartDownloadSuccessListener(() -> setFOTADownSuccessNext());
        setFOTAStartDownloadHelper.setOnSetFOTAStartDownloadFailedListener(() -> errorNext());
        setFOTAStartDownloadHelper.setFOTAStartDownload();
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
    public void checkNewVersion() {
        checkWan();// 检测是否连接了WAN口或者SIM卡(WAN口优先)
    }

    /**
     * 检查wan口
     */
    private void checkWan() {
        BoardWanHelper wh = new BoardWanHelper(activity);
        wh.setOnError(e -> checkSim());
        wh.setOnResultError(error -> checkSim());
        wh.setOnConnetedNextListener(wanResult -> checkNw());// 检查新版本
        wh.setOnConnetingNextListener(wanResult -> checkSim());
        wh.setOnDisConnetedNextListener(wanResult -> checkSim());
        wh.setOnDisconnetingNextListener(wanResult -> checkSim());
        wh.boardTimer();
    }

    /**
     * 检查sim card
     */
    private void checkSim() {
        hideDialog();
        BoardSimHelper bsh = new BoardSimHelper(activity);
        bsh.setOnRollRequestOnError(e -> toast(R.string.qs_pin_unlock_can_not_connect_des));
        bsh.setOnRollRequestOnResultError(e -> toast(R.string.qs_pin_unlock_can_not_connect_des));
        bsh.setOnNormalSimstatusListener(simStatus -> {
            int simState = simStatus.getSIMState();
            if (simState != Cons.READY) {
                toast(R.string.qs_pin_unlock_can_not_connect_des);
            } else {
                // 加入拨号连接的判断, 如果没有拨号连接则提示没有拨号
                GetConnectionStateHelper xGetConnectionStateHelper = new GetConnectionStateHelper();
                xGetConnectionStateHelper.setOnGetConnectionStateFailedListener(() -> toast(R.string.qs_pin_unlock_can_not_connect_des));
                xGetConnectionStateHelper.setOnDisConnectingListener(() -> toast(R.string.setting_upgrade_no_connection));
                xGetConnectionStateHelper.setOnDisconnectedListener(() -> toast(R.string.setting_upgrade_no_connection));
                xGetConnectionStateHelper.setOnConnectingListener(() ->toast(R.string.setting_upgrade_no_connection) );
                xGetConnectionStateHelper.setOnConnectedListener(this::checkNw);
                xGetConnectionStateHelper.getConnectionState();
            }
        });
        bsh.boardTimer();
    }

    /**
     * 检查新版本
     */
    private void checkNw() {
        hideDialog();// 停止先前的等待条
        Drawable pop_bg = activity.getResources().getDrawable(R.drawable.bg_pop_conner);
        UpgradeHelper uh = new UpgradeHelper(activity, true);
        uh.setOnResultErrorListener(() -> toast(R.string.setting_upgrade_check_firmware_failed));
        uh.setOnErrorListener(() -> toast(R.string.setting_upgrade_check_firmware_failed));
        uh.setOnCheckErrorListener(this::getCurrentVersion);

        uh.setOnServiceNotAvailableListener(attr -> toast(R.string.setting_upgrade_not_available));
        uh.setOnNoConnectListener(attr -> toast(R.string.setting_upgrade_no_connection));
        uh.setOnNoNewVersionListener(this::getCurrentVersion);
        uh.setOnNewVersionListener(this::newVersionNext);
        uh.checkVersion();
    }

    /**
     * 获取当前版本号
     */
    private void getCurrentVersion(GetDeviceNewVersionBean updateDeviceNewVersion) {
        GetSystemInfoHelper getSystemInfoHelper = new GetSystemInfoHelper();
        getSystemInfoHelper.setOnGetSystemInfoSuccessListener(result -> {
            noNewVersionNext(updateDeviceNewVersion, result);
        });
        getSystemInfoHelper.setOnAppErrorListener(() -> toast(R.string.setting_upgrade_check_firmware_failed));
        getSystemInfoHelper.setOnFwErrorListener(() -> toast(R.string.setting_upgrade_check_firmware_failed));
        getSystemInfoHelper.getSystemInfo();
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

    private void hideDialog() {
        if (pgd != null) {
            pgd.dismiss();
        }
    }

    private void toast(int resId) {
        ToastUtil_m.show(activity, resId);
    }

    private void toastLong(int resId) {
        ToastUtil_m.showLong(activity, resId);
    }

    private void toast(String content) {
        ToastUtil_m.show(activity, content);
    }

    private void to(Class ac, boolean isFinish) {
        CA.toActivity(activity, ac, false, isFinish, false, 0);
    }
}
