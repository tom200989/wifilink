package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.view.View;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.ToastTool;
import com.alcatel.wifilink.root.widget.HH70_CountDownWidget;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.hiber.tools.TimerHelper;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetDeviceNewVersionBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetDeviceUpgradeStateBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetDeviceNewVersionHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetDeviceUpgradeStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetCheckNewVersionHelper;

/**
 * Created by qianli.ma on 2017/12/14 0014.
 */

public class UpgradeHelper {
    private Activity activity;
    private boolean isShowWaiting;
    private TimerHelper countDownTimer;
    private boolean isContinueChecking = true;// 是否允许在checking状态下继续获取状态
    private HH70_CountDownWidget wd_countdown;
    private HH70_LoadWidget wdLoad;

    public UpgradeHelper(Activity activity, boolean isShowWaiting) {
        isContinueChecking = true;
        this.activity = activity;
        this.isShowWaiting = isShowWaiting;
    }

    /**
     * 获取下载进度
     */
    public void getDownState() {
        GetDeviceUpgradeStateHelper xGetDeviceUpgradeStateHelper = new GetDeviceUpgradeStateHelper();
        xGetDeviceUpgradeStateHelper.setOnGetDeviceUpgradeStateSuccessListener(result -> {
            upgradeStateNormalNext(result);
            int status = result.getStatus();
            switch (status) {
                case GetDeviceUpgradeStateBean.CONS_NO_START_UPDATE:
                    noStartUpdateNext(result);
                    break;
                case GetDeviceUpgradeStateBean.CONS_UPDATING:
                    updatingNext(result);
                    break;
                case GetDeviceUpgradeStateBean.CONS_COMPLETE:
                    completeNext(result);
                    break;
            }
        });
        xGetDeviceUpgradeStateHelper.setOnGetDeviceUpgradeStateFailedListener(this::upgradeFailedNext);
        xGetDeviceUpgradeStateHelper.getDeviceUpgradeState();
    }

    private OnCompleteListener onCompleteListener;

    // 接口OnCompleteListener
    public interface OnCompleteListener {
        void complete(GetDeviceUpgradeStateBean attr);
    }

    // 对外方式setOnCompleteListener
    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    // 封装方法completeNext
    private void completeNext(GetDeviceUpgradeStateBean attr) {
        if (onCompleteListener != null) {
            onCompleteListener.complete(attr);
        }
    }

    private OnUpdatingListener onUpdatingListener;

    // 接口OnUpdatingListener
    public interface OnUpdatingListener {
        void updating(GetDeviceUpgradeStateBean attr);
    }

    // 对外方式setOnUpdatingListener
    public void setOnUpdatingListener(OnUpdatingListener onUpdatingListener) {
        this.onUpdatingListener = onUpdatingListener;
    }

    // 封装方法updatingNext
    private void updatingNext(GetDeviceUpgradeStateBean attr) {
        if (onUpdatingListener != null) {
            onUpdatingListener.updating(attr);
        }
    }

    private OnNoStartUpdateListener onNoStartUpdateListener;

    // 接口OnNoStartUpdateListener
    public interface OnNoStartUpdateListener {
        void noStartUpdate(GetDeviceUpgradeStateBean attr);
    }

    // 对外方式setOnNoStartUpdateListener
    public void setOnNoStartUpdateListener(OnNoStartUpdateListener onNoStartUpdateListener) {
        this.onNoStartUpdateListener = onNoStartUpdateListener;
    }

    // 封装方法noStartUpdateNext
    private void noStartUpdateNext(GetDeviceUpgradeStateBean attr) {
        if (onNoStartUpdateListener != null) {
            onNoStartUpdateListener.noStartUpdate(attr);
        }
    }

    private OnUpgradeStateNormalListener onUpgradeStateListener;

    // 接口OnUpgradeStateListener
    public interface OnUpgradeStateNormalListener {
        void upgradeState(GetDeviceUpgradeStateBean attr);
    }

    // 对外方式setOnUpgradeStateListener
    public void setOnUpgradeStateNormalListener(OnUpgradeStateNormalListener onUpgradeStateListener) {
        this.onUpgradeStateListener = onUpgradeStateListener;
    }

    // 封装方法upgradeStateNext
    private void upgradeStateNormalNext(GetDeviceUpgradeStateBean attr) {
        if (onUpgradeStateListener != null) {
            onUpgradeStateListener.upgradeState(attr);
        }
    }

    /* -------------------------------------------- method1 -------------------------------------------- */

    /**
     * 检查版本
     */
    public void checkVersion(HH70_CountDownWidget wd_countdown, HH70_LoadWidget wdLoad) {
        this.wdLoad = wdLoad;
        if (isShowWaiting) {
            try {
                isContinueChecking = true;
                startCountDownView(wd_countdown);// 1.启动倒计时view
                startCountDownTimer();// 2.启动倒计时(延迟180秒)
            } catch (Exception e) {
                wdLoad.setVisibles();
            }
        }
        setCheck();
    }

    /**
     * 启动延时器(延迟180秒)
     */
    private void startCountDownTimer() {
        countDownTimer = new TimerHelper(activity) {
            @Override
            public void doSomething() {
                isContinueChecking = false;
                activity.runOnUiThread(() -> {
                    hideAllWidget();
                    isContinueChecking = false;
                    countDownTimer.stop();
                    ToastTool.show(activity, R.string.hh70_update_try_again);
                });

            }
        };
        countDownTimer.startDelay(180 * 1000);
    }

    /**
     * 启动倒计时view
     */
    private void startCountDownView(HH70_CountDownWidget wd_countdown) {
        this.wd_countdown = wd_countdown;
        this.wd_countdown.setVisibility(View.VISIBLE);
        wd_countdown.getCountDownText().reset();
    }

    private void setCheck() {
        // 1.先触发检查new version
        SetCheckNewVersionHelper xSetCheckNewVersionHelper = new SetCheckNewVersionHelper();
        // 2.在获取查询new version
        xSetCheckNewVersionHelper.setOnSetCheckNewVersionSuccessListener(this::getNewVersionDo);
        xSetCheckNewVersionHelper.setOnSetCheckNewVersionFailedListener(() -> {
            hideAllWidget();
            upgradeFailedNext();
        });
        xSetCheckNewVersionHelper.setCheckNewVersion();
    }

    /**
     * 获取查询new version
     */
    private void getNewVersionDo() {
        if (isContinueChecking) {// 是否允许获取状态(该标记位是在180秒后如果状态还是checking则认为失败)
            GetDeviceNewVersionHelper xGetDeviceNewVersionHelper = new GetDeviceNewVersionHelper();
            xGetDeviceNewVersionHelper.setOnGetDeviceNewVersionSuccessListener(result -> {
                switch (result.getState()) {
                    case GetDeviceNewVersionBean.CONS_CHECKING:
                        checkingNext(result);
                        getNewVersionDo();
                        break;
                    case GetDeviceNewVersionBean.CONS_NEW_VERSION:
                        hideAllWidget();
                        newVersionNext(result);
                        break;
                    case GetDeviceNewVersionBean.CONS_NO_NEW_VERSION:
                        hideAllWidget();
                        noNewVersionNext(result);
                        break;
                    case GetDeviceNewVersionBean.CONS_NO_CONNECT:
                        hideAllWidget();
                        noConnectNext(result);
                        break;
                    case GetDeviceNewVersionBean.CONS_SERVICE_NOT_AVAILABLE:
                        hideAllWidget();
                        serviceNotAvailableNext(result);
                        break;
                    case GetDeviceNewVersionBean.CONS_CHECK_ERROR:
                        hideAllWidget();
                        checkErrorNext(result);
                        break;
                }
            });
            xGetDeviceNewVersionHelper.setOnGetDeviceNewVersionFailedListener(() -> {
                hideAllWidget();
                upgradeFailedNext();
            });
            xGetDeviceNewVersionHelper.getDeviceNewVersion();
        } else {
            hideAllWidget();
            ToastTool.show(activity, R.string.hh70_update_try_again);
        }

    }

    private void hideAllWidget() {
        isContinueChecking = false;
        if (countDownTimer != null) {
            countDownTimer.stop();// 停止倒数计时器
        }
        if (wdLoad != null && wdLoad.getVisibility() == View.VISIBLE) {
            wdLoad.setGone();
        }
        if (wd_countdown != null && wd_countdown.getVisibility() == View.VISIBLE) {
            wd_countdown.setVisibility(View.GONE);// 隐藏倒数条
        }
        if (wd_countdown != null && wd_countdown.getCountDownText() != null) {
            wd_countdown.getCountDownText().setCount(180);// 停止倒数
            wd_countdown.getCountDownText().pause();
        }
    }

    private OnCheckErrorListener onCheckErrorListener;

    // 接口OnCheckErrorListener
    public interface OnCheckErrorListener {
        void checkError(GetDeviceNewVersionBean attr);
    }

    // 对外方式setOnCheckErrorListener
    public void setOnCheckErrorListener(OnCheckErrorListener onCheckErrorListener) {
        this.onCheckErrorListener = onCheckErrorListener;
    }

    // 封装方法checkErrorNext
    private void checkErrorNext(GetDeviceNewVersionBean attr) {
        if (onCheckErrorListener != null) {
            onCheckErrorListener.checkError(attr);
        }
    }

    private OnServiceNotAvailableListener onServiceNotAvailableListener;

    // 接口OnServiceNotAvailableListener
    public interface OnServiceNotAvailableListener {
        void serviceNotAvailable(GetDeviceNewVersionBean attr);
    }

    // 对外方式setOnServiceNotAvailableListener
    public void setOnServiceNotAvailableListener(OnServiceNotAvailableListener onServiceNotAvailableListener) {
        this.onServiceNotAvailableListener = onServiceNotAvailableListener;
    }

    // 封装方法serviceNotAvailableNext
    private void serviceNotAvailableNext(GetDeviceNewVersionBean attr) {
        if (onServiceNotAvailableListener != null) {
            onServiceNotAvailableListener.serviceNotAvailable(attr);
        }
    }

    private OnNoConnectListener onNoConnectListener;

    // 接口OnNoConnectListener
    public interface OnNoConnectListener {
        void noConnect(GetDeviceNewVersionBean attr);
    }

    // 对外方式setOnNoConnectListener
    public void setOnNoConnectListener(OnNoConnectListener onNoConnectListener) {
        this.onNoConnectListener = onNoConnectListener;
    }

    // 封装方法noConnectNext
    private void noConnectNext(GetDeviceNewVersionBean attr) {
        if (onNoConnectListener != null) {
            onNoConnectListener.noConnect(attr);
        }
    }

    private OnNoNewVersionListener onNoNewVersionListener;

    // 接口OnNoNewVersionListener
    public interface OnNoNewVersionListener {
        void noNewVersion(GetDeviceNewVersionBean attr);
    }

    // 对外方式setOnNoNewVersionListener
    public void setOnNoNewVersionListener(OnNoNewVersionListener onNoNewVersionListener) {
        this.onNoNewVersionListener = onNoNewVersionListener;
    }

    // 封装方法noNewVersionNext
    private void noNewVersionNext(GetDeviceNewVersionBean attr) {
        if (onNoNewVersionListener != null) {
            onNoNewVersionListener.noNewVersion(attr);
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

    private OnCheckingListener onCheckingListener;

    // 接口OnCheckingListener
    public interface OnCheckingListener {
        void checking(GetDeviceNewVersionBean attr);
    }

    // 对外方式setOnCheckingListener
    public void setOnCheckingListener(OnCheckingListener onCheckingListener) {
        this.onCheckingListener = onCheckingListener;
    }

    // 封装方法checkingNext
    private void checkingNext(GetDeviceNewVersionBean attr) {
        if (onCheckingListener != null) {
            onCheckingListener.checking(attr);
        }
    }

    private OnUpgradeFailedListener onUpgradeFailedListener;

    // Inteerface--> 接口OnUpgradeFailedListener
    public interface OnUpgradeFailedListener {
        void upgradeFailed();
    }

    // 对外方式setOnUpgradeFailedListener
    public void setOnUpgradeFailedListener(OnUpgradeFailedListener onUpgradeFailedListener) {
        this.onUpgradeFailedListener = onUpgradeFailedListener;
    }

    // 封装方法upgradeFailedNext
    private void upgradeFailedNext() {
        if (onUpgradeFailedListener != null) {
            onUpgradeFailedListener.upgradeFailed();
        }
    }

}
