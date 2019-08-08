package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.Update_DeviceNewVersion;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ScreenSize;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.widget.CountDownTextView;
import com.alcatel.wifilink.root.widget.PopupWindows;
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
    private ProgressDialog pgd;
    private Handler handler;
    private TimerHelper countDownHelper;
    private PopupWindows countDown_pop;// 倒计时弹框
    private CountDownTextView ctv;// 倒计时文本
    private TimerHelper countDownTimer;
    private boolean isContinueChecking = true;// 是否允许在checking状态下继续获取状态

    public UpgradeHelper(Activity activity, boolean isShowWaiting) {
        isContinueChecking = true;
        this.activity = activity;
        this.isShowWaiting = isShowWaiting;
        handler = new Handler();
    }

    /**
     * 获取下载进度
     */
    public void getDownState() {
        GetDeviceUpgradeStateHelper getDeviceUpgradeStateHelper = new GetDeviceUpgradeStateHelper();
        getDeviceUpgradeStateHelper.setOnGetDeviceUpgradeStateSuccessListener(result -> {
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
        getDeviceUpgradeStateHelper.setOnGetDeviceUpgradeStateFailedListener(() -> {
            errorNext();
        });
        getDeviceUpgradeStateHelper.getDeviceUpgradeState();
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
    public void checkVersion() {
        if (isShowWaiting) {
            try {
                isContinueChecking = true;
                startCountDownView();// 1.启动倒计时view
                startCountDownTimer();// 2.启动延时器(延迟180秒)
            } catch (Exception e) {
                pgd = OtherUtils.showProgressPop(activity);
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
                    hideDialog();
                    isContinueChecking = false;
                    countDownTimer.stop();
                    ToastUtil_m.show(activity, R.string.could_not_update_try_again);
                });

            }
        };
        countDownTimer.startDelay(180 * 1000);
    }

    /**
     * 启动倒计时view
     */
    private void startCountDownView() {
        View inflate = View.inflate(activity, R.layout.pop_countdown_upgrade, null);
        ctv = inflate.findViewById(R.id.ctv_pop_upgrade);
        ctv.setCount(180);
        ctv.setTopColor(Color.parseColor("#009AFF"));
        ctv.setBottomColor(Color.parseColor("#009AFF"));
        ctv.run();
        int width = (int) (ScreenSize.getSize(activity).width * 0.85f);
        int height = (int) (ScreenSize.getSize(activity).height * 0.12f);
        countDown_pop = new PopupWindows(activity, inflate, width, height, false, new ColorDrawable(Color.TRANSPARENT));
    }

    private void setCheck() {
        // 1.先触发检查new version
        SetCheckNewVersionHelper helper = new SetCheckNewVersionHelper();
        helper.setOnSetCheckNewVersionSuccessListener(() -> {
            // 2.在获取查询new version
            getNewVersionDo();
        });
        helper.setOnSetCheckNewVersionFailedListener(() -> {
            hideDialog();
            errorNext();
        });
        helper.setCheckNewVersion();
    }

    /**
     * 获取查询new version
     */
    private void getNewVersionDo() {
        if (isContinueChecking) {// 是否允许获取状态(该标记位是在180秒后如果状态还是checking则认为失败)
            GetDeviceNewVersionHelper getDeviceNewVersionHelper = new GetDeviceNewVersionHelper();
            getDeviceNewVersionHelper.setOnGetDeviceNewVersionSuccessListener(result -> {
                switch (result.getState()) {
                    case GetDeviceNewVersionBean.CONS_CHECKING:
                        checkingNext(result);
                        getNewVersionDo();
                        break;
                    case GetDeviceNewVersionBean.CONS_NEW_VERSION:
                        hideDialog();
                        newVersionNext(result);
                        break;
                    case GetDeviceNewVersionBean.CONS_NO_NEW_VERSION:
                        hideDialog();
                        noNewVersionNext(result);
                        break;
                    case GetDeviceNewVersionBean.CONS_NO_CONNECT:
                        hideDialog();
                        noConnectNext(result);
                        break;
                    case GetDeviceNewVersionBean.CONS_SERVICE_NOT_AVAILABLE:
                        hideDialog();
                        serviceNotAvailableNext(result);
                        break;
                    case GetDeviceNewVersionBean.CONS_CHECK_ERROR:
                        hideDialog();
                        checkErrorNext(result);
                        break;
                }
            });
            getDeviceNewVersionHelper.setOnGetDeviceNewVersionFailedListener(() -> {
                hideDialog();
                errorNext();
            });
            getDeviceNewVersionHelper.getDeviceNewVersion();
        } else {
            hideDialog();
            ToastUtil_m.show(activity, R.string.could_not_update_try_again);
        }

    }

    private void hideDialog() {
        if (countDownTimer != null) {
            countDownTimer.stop();// 停止倒数计时器
        }
        if (pgd != null) {
            pgd.dismiss();// 隐藏等待条
        }
        if (countDown_pop != null) {
            countDown_pop.dismiss();// 隐藏倒数条
        }
        if (ctv != null) {
            ctv.setCount(180);// 停止倒数
            ctv.pause();
        }
    }

    private OnNormalGetNewVersionDoListener onNormalGetNewVersionDoListener;

    // 接口OnNormalGetNewVersionDoListener
    public interface OnNormalGetNewVersionDoListener {
        void normalGetNewVersionDo(Update_DeviceNewVersion attr);
    }

    // 对外方式setOnNormalGetNewVersionDoListener
    public void setOnNormalGetNewVersionDoListener(OnNormalGetNewVersionDoListener onNormalGetNewVersionDoListener) {
        this.onNormalGetNewVersionDoListener = onNormalGetNewVersionDoListener;
    }

    // 封装方法normalGetNewVersionDoNext
    private void normalGetNewVersionDoNext(Update_DeviceNewVersion attr) {
        if (onNormalGetNewVersionDoListener != null) {
            onNormalGetNewVersionDoListener.normalGetNewVersionDo(attr);
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

    private OnResultErrorListener onResultErrorListener;

    // 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError();
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNext
    private void resultErrorNext() {
        if (onResultErrorListener != null) {
            onResultErrorListener.resultError();
        }
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
}
