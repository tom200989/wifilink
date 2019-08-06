package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.root.ue.activity.LoginRxActivity;
import com.alcatel.wifilink.root.ue.activity.RefreshWifiRxActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;

/**
 * Created by qianli.ma on 2017/11/16 0016.
 */

public class BoardSimHelper {


    private Activity activity;
    private ProgressDialog pgd;
    private Handler handler;
    private OnNownListener onNownListener;
    private OnSimReadyListener onSimReadyListener;
    private OnPinRequireListener onPinRequireListener;
    private OnpukRequireListener onpukRequireListener;
    private OnpukTimeoutListener onpukTimeoutListener;
    private OnNormalSimstatusListener onNormalSimstatusListener;
    private OnRollRequestOnResultError onRollRequestOnResultError;
    private OnRollRequestOnError onRollRequestOnError;
    private OnDetectedListener onDetectedListener;
    private OnSimLockListener onSimLockListener;
    private OnInitingListener onInitingListener;

    public BoardSimHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * 点击事件调用此方法
     */
    public void boardNormal() {
        // if (pgd == null) {
        //     pgd = OtherUtils.showProgressPop(activity);
        // }
        // if (!pgd.isShowing()) {
        //     pgd.show();
        // }
        // 1.连接硬件
        new CheckBoard() {
            @Override
            public void successful() {
                // 2.登陆状态
                GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
                xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
                    if (getLoginStateBean.getState() == Cons.LOGOUT) {
                        to(LoginRxActivity.class);
                        return;
                    }
                    // 3.sim卡状态(仅一次)
                    obtainSimStatusOne();
                });
                
                xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> {
                    OtherUtils.hideProgressPop(pgd);
                    toast(R.string.connect_failed);
                    to(RefreshWifiRxActivity.class);
                });
                xGetLoginStateHelper.getLoginState();
            }
        }.checkBoard(activity, LoginRxActivity.class, RefreshWifiRxActivity.class);
    }

    /**
     * 定时器调用此方法
     */
    public void boardTimer() {
        // 1.连接硬件
        new CheckBoard() {
            @Override
            public void successful() {

                GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
                xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
                    if (getLoginStateBean.getState() == Cons.LOGOUT) {
                        to(LoginRxActivity.class);
                        return;
                    }
                    // 3.sim卡状态(循环)
                    obtainSimStatusRoll();
                });
                xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> rollRequestOnResultErrorNext(null));
                xGetLoginStateHelper.getLoginState();
            }
        }.checkBoard(activity, LoginRxActivity.class, RefreshWifiRxActivity.class);
    }

    /**
     * 获取sim状态(只获取一次的时候使用)
     */
    private void obtainSimStatusOne() {

        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
            normalSimStatusNext(result);
            int simState = result.getSIMState();
            switch (simState) {
                case Cons.NONE:
                    nownStatusNext(result);
                    toast(R.string.Home_no_sim);
                    break;
                case Cons.DETECTED:
                    delayRepeatGetSimstatu();
                    break;
                case Cons.PIN_REQUIRED:
                    pinRequireNext(result);
                    break;
                case Cons.PUK_REQUIRED:
                    pukRequireNext(result);
                    break;
                case Cons.SIMLOCK:
                    simlockNext(result);
                    toast(R.string.home_sim_loched);
                    break;
                case Cons.PUK_TIMESOUT:
                    pukTimeoutNext(result);
                    break;
                case Cons.ILLEGAL:
                    toast(R.string.Home_sim_invalid);
                    break;
                case Cons.READY:
                    simReadyNext(result);
                    break;
                case Cons.INITING:
                    delayRepeatGetSimstatu();
                    break;
            }
            if (simState != Cons.DETECTED & simState != Cons.INITING) {
                OtherUtils.hideProgressPop(pgd);
            }
        });
        
        xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
            OtherUtils.hideProgressPop(pgd);
            toast(R.string.home_sim_not_accessible);
            to(RefreshWifiRxActivity.class);
        });
        xGetSimStatusHelper.getSimStatus();
    }

    /**
     * 获取sim状态(循环获取的时候使用)
     */
    private void obtainSimStatusRoll() {

        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
            normalSimStatusNext(result);
            int simState = result.getSIMState();
            switch (simState) {
                case Cons.NONE:
                    nownStatusNext(result);
                    break;
                case Cons.DETECTED:
                    detectedNext(result);
                    break;
                case Cons.PIN_REQUIRED:
                    pinRequireNext(result);
                    break;
                case Cons.PUK_REQUIRED:
                    pukRequireNext(result);
                    break;
                case Cons.SIMLOCK:
                    simlockNext(result);
                    break;
                case Cons.PUK_TIMESOUT:
                    pukTimeoutNext(result);
                    break;
                case Cons.ILLEGAL:
                    break;
                case Cons.READY:
                    simReadyNext(result);
                    break;
                case Cons.INITING:
                    initingNext(result);
                    break;
            }
        });
        
        xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
            rollRequestOnResultErrorNext(null);
            rollRequestOnErrorNext(null);
        });
        xGetSimStatusHelper.getSimStatus();
    }

    /**
     * 延迟迭代请求
     */
    private void delayRepeatGetSimstatu() {
        handler = new Handler();
        handler.postDelayed(this::obtainSimStatusOne, 2500);
    }

    private void toast(int resId) {
        Log.v("ma_counld", getClass().getSimpleName());
        ToastUtil_m.show(activity, resId);
    }

    private void to(Class ac) {
        CA.toActivity(activity, ac, false, true, false, 0);
    }
    
    /* -------------------------------------------- INTERFACE -------------------------------------------- */

    public interface OnRollRequestOnResultError {
        void onResultError(ResponseBody.Error error);
    }

    public interface OnRollRequestOnError {
        void onError(Throwable e);
    }

    public interface OnDetectedListener {
        void onDetected(GetSimStatusBean simStatus);
    }

    public interface OnSimLockListener {
        void onSimlock(GetSimStatusBean simStatus);
    }

    public interface OnInitingListener {
        void onIniting(GetSimStatusBean simStatus);
    }


    public interface OnNormalSimstatusListener {
        void normalSimStatu(GetSimStatusBean simStatus);
    }

    public interface OnNownListener {
        void nownStatu(GetSimStatusBean simStatus);
    }

    public interface OnPinRequireListener {
        void pinRequire(GetSimStatusBean result);
    }

    public interface OnpukRequireListener {
        void pukRequire(GetSimStatusBean result);
    }

    public interface OnpukTimeoutListener {
        void pukTimeout(GetSimStatusBean result);
    }

    public interface OnSimReadyListener {
        void simReady(GetSimStatusBean result);
    }
    
    /* -------------------------------------------- LISTENER -------------------------------------------- */

    public void setOnRollRequestOnResultError(OnRollRequestOnResultError onRollRequestOnResultError) {
        this.onRollRequestOnResultError = onRollRequestOnResultError;
    }

    public void setOnRollRequestOnError(OnRollRequestOnError onRollRequestOnError) {
        this.onRollRequestOnError = onRollRequestOnError;
    }

    public void setOnDetectedListener(OnDetectedListener onDetectedListener) {
        this.onDetectedListener = onDetectedListener;
    }

    public void setOnSimLockListener(OnSimLockListener onSimLockListener) {
        this.onSimLockListener = onSimLockListener;
    }

    public void setOnInitingListener(OnInitingListener onInitingListener) {
        this.onInitingListener = onInitingListener;
    }

    public void setOnNormalSimstatusListener(OnNormalSimstatusListener onNormalSimstatusListener) {
        this.onNormalSimstatusListener = onNormalSimstatusListener;
    }

    public void setOnNownListener(OnNownListener onNownListener) {
        this.onNownListener = onNownListener;
    }

    public void setOnPinRequireListener(OnPinRequireListener onPinRequireListener) {
        this.onPinRequireListener = onPinRequireListener;
    }

    public void setOnpukRequireListener(OnpukRequireListener onpukRequireListener) {
        this.onpukRequireListener = onpukRequireListener;
    }

    public void setOnpukTimeoutListener(OnpukTimeoutListener onpukTimeoutListener) {
        this.onpukTimeoutListener = onpukTimeoutListener;
    }

    public void setOnSimReadyListener(OnSimReadyListener onSimReadyListener) {
        this.onSimReadyListener = onSimReadyListener;
    }

    /* -------------------------------------------- METHOD -------------------------------------------- */

    private void rollRequestOnResultErrorNext(ResponseBody.Error error) {
        if (onRollRequestOnResultError != null) {
            onRollRequestOnResultError.onResultError(error);
        }
    }

    private void rollRequestOnErrorNext(Throwable e) {
        if (onRollRequestOnError != null) {
            onRollRequestOnError.onError(e);
        }
    }

    private void detectedNext(GetSimStatusBean simStatus) {
        if (onDetectedListener != null) {
            onDetectedListener.onDetected(simStatus);
        }
    }

    private void simlockNext(GetSimStatusBean simStatus) {
        if (onSimLockListener != null) {
            onSimLockListener.onSimlock(simStatus);
        }
    }

    private void initingNext(GetSimStatusBean simStatus) {
        if (onInitingListener != null) {
            onInitingListener.onIniting(simStatus);
        }
    }

    private void normalSimStatusNext(GetSimStatusBean result) {
        if (onNormalSimstatusListener != null) {
            onNormalSimstatusListener.normalSimStatu(result);
        }
    }

    private void nownStatusNext(GetSimStatusBean result) {
        if (onNownListener != null) {
            onNownListener.nownStatu(result);
        }
    }

    private void pinRequireNext(GetSimStatusBean result) {
        if (onPinRequireListener != null) {
            onPinRequireListener.pinRequire(result);
        }
    }

    private void pukRequireNext(GetSimStatusBean result) {
        if (onpukRequireListener != null) {
            onpukRequireListener.pukRequire(result);
        }
    }

    private void pukTimeoutNext(GetSimStatusBean result) {
        if (onpukTimeoutListener != null) {
            onpukTimeoutListener.pukTimeout(result);
        }
    }

    private void simReadyNext(GetSimStatusBean result) {
        if (onSimReadyListener != null) {
            onSimReadyListener.simReady(result);
        }
    }

}
