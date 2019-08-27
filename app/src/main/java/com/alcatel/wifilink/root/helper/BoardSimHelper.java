package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.ToastTool;
import com.hiber.bean.SkipBean;
import com.hiber.hiber.RootMAActivity;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;

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
        // 1.连接硬件
        new CheckBoard() {
            @Override
            public void successful() {
                // 2.登陆状态
                GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
                xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
                    if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGOUT) {
                        to(RootCons.ACTIVITYS.SPLASH_AC, RootCons.FRAG.LOGIN_FR);
                        return;
                    }
                    // 3.sim卡状态(仅一次)
                    obtainSimStatusOne();
                });

                xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> {
                    OtherUtils.hideProgressPop(pgd);
                    toast(R.string.connect_failed);
                    to(RootCons.ACTIVITYS.SPLASH_AC, RootCons.FRAG.REFRESH_FR);
                });
                xGetLoginStateHelper.getLoginState();
            }
        }.checkBoard(activity);
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
                    if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGOUT) {
                        to(RootCons.ACTIVITYS.SPLASH_AC, RootCons.FRAG.LOGIN_FR);
                        return;
                    }
                    // 3.sim卡状态(循环)
                    obtainSimStatusRoll();
                });
                xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> rollRequestOnResultErrorNext(null));
                xGetLoginStateHelper.getLoginState();
            }
        }.checkBoard(activity);
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
                case GetSimStatusBean.CONS_NOWN:
                    nownStatusNext(result);
                    toast(R.string.Home_no_sim);
                    break;
                case GetSimStatusBean.CONS_SIM_CARD_DETECTED:
                    delayRepeatGetSimstatu();
                    break;
                case GetSimStatusBean.CONS_PIN_REQUIRED:
                    pinRequireNext(result);
                    break;
                case GetSimStatusBean.CONS_PUK_REQUIRED:
                    pukRequireNext(result);
                    break;
                case GetSimStatusBean.CONS_SIM_LOCK_REQUIRED:
                    simlockNext(result);
                    toast(R.string.home_sim_loched);
                    break;
                case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                    pukTimeoutNext(result);
                    break;
                case GetSimStatusBean.CONS_SIM_CARD_ILLEGAL:
                    toast(R.string.Home_sim_invalid);
                    break;
                case GetSimStatusBean.CONS_SIM_CARD_READY:
                    simReadyNext(result);
                    break;
                case GetSimStatusBean.CONS_SIM_CARD_IS_INITING:
                    delayRepeatGetSimstatu();
                    break;
            }
            if (simState != GetSimStatusBean.CONS_SIM_CARD_DETECTED & simState != GetSimStatusBean.CONS_SIM_CARD_IS_INITING) {
                OtherUtils.hideProgressPop(pgd);
            }
        });

        xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
            OtherUtils.hideProgressPop(pgd);
            toast(R.string.home_sim_not_accessible);
            to(RootCons.ACTIVITYS.SPLASH_AC, RootCons.FRAG.REFRESH_FR);
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
                case GetSimStatusBean.CONS_NOWN:
                    nownStatusNext(result);
                    break;
                case GetSimStatusBean.CONS_SIM_CARD_DETECTED:
                    detectedNext(result);
                    break;
                case GetSimStatusBean.CONS_PIN_REQUIRED:
                    pinRequireNext(result);
                    break;
                case GetSimStatusBean.CONS_PUK_REQUIRED:
                    pukRequireNext(result);
                    break;
                case GetSimStatusBean.CONS_SIM_LOCK_REQUIRED:
                    simlockNext(result);
                    break;
                case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                    pukTimeoutNext(result);
                    break;
                case GetSimStatusBean.CONS_SIM_CARD_ILLEGAL:
                    break;
                case GetSimStatusBean.CONS_SIM_CARD_READY:
                    simReadyNext(result);
                    break;
                case GetSimStatusBean.CONS_SIM_CARD_IS_INITING:
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
        ToastTool.show(activity, resId);
    }


    /**
     * 跳转activity
     *
     * @param targetAc
     * @param targetFr
     */
    private void to(String targetAc, String targetFr) {
        Intent intent = new Intent();
        intent.setAction(targetAc);
        SkipBean skipBean = RootMAActivity.getPendingIntentValue(targetAc, targetFr, null);
        skipBean.setCurrentACFinish(true);
        intent.putExtra(RootMAActivity.getPendingIntentKey(), skipBean);
        activity.startActivity(intent);
    }



    /* -------------------------------------------- INTERFACE -------------------------------------------- */

    public interface OnRollRequestOnResultError {
        void onResultError(FwError error);
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

    private void rollRequestOnResultErrorNext(FwError error) {
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
