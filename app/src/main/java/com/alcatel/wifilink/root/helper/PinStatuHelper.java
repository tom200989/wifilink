package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.content.Intent;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.ToastTool;
import com.hiber.bean.SkipBean;
import com.hiber.hiber.RootMAActivity;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;

/**
 * Created by qianli.ma on 2017/12/12 0012.
 */

public class PinStatuHelper {
    private Activity activity;

    public PinStatuHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * 定时器使用该方法请求pins state
     */
    public void getRoll() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGOUT) {
                to(RootCons.ACTIVITYS.SPLASH_AC,RootCons.FRAG.LOGIN_FR);
                return;
            }
            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
                int pinState = result.getPinState();
                switch (pinState) {
                    case GetSimStatusBean.CONS_FOR_PIN_UNKNOWN:
                        unknownNext(result);
                        break;
                    case GetSimStatusBean.CONS_FOR_PIN_ENABLE_BUT_NOT_VERIFIED:
                        pinEnableButNotVerifyNext(result);
                        break;
                    case GetSimStatusBean.CONS_FOR_PIN_PIN_ENABLE_VERIFIED:
                        pinEnableVerifyNext(result);
                        break;
                    case GetSimStatusBean.CONS_FOR_PIN_PIN_DISABLE:
                        pinDisableNext(result);
                        break;
                    case GetSimStatusBean.CONS_FOR_PIN_PUK_REQUIRED:
                        pukRequiredNext(result);
                        break;
                    case GetSimStatusBean.CONS_FOR_PIN_PUK_TIMES_USED_OUT:
                        pukTimeoutNext(result);
                        break;
                }
            });
            xGetSimStatusHelper.getSimStatus();
        });
        xGetLoginStateHelper.getLoginState();
    }


    /**
     * 点击事件使用该方法请求pins state
     */
    public void getOne() {
        // 1.先获取SIM卡的状态
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGOUT) {
                to(RootCons.ACTIVITYS.SPLASH_AC, RootCons.FRAG.LOGIN_FR);
                return;
            }
            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
                int simState = result.getSIMState();
                switch (simState) {
                    case GetSimStatusBean.CONS_SIM_CARD_IS_INITING:
                        toast(R.string.hh70_initializing);
                        break;
                    case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                        toast(R.string.hh70_puk_timeout);
                        break;
                    case GetSimStatusBean.CONS_SIM_CARD_DETECTED:
                        toast(R.string.hh70_sim_card_deleted);
                        break;
                    case GetSimStatusBean.CONS_NOWN:
                        toast(R.string.hh70_not_inserted);
                        break;
                    case GetSimStatusBean.CONS_SIM_CARD_ILLEGAL:
                        toast(R.string.hh70_invalid_sim);
                        break;
                    case GetSimStatusBean.CONS_SIM_LOCK_REQUIRED:
                        toast(R.string.hh70_sim_locked);
                        break;
                    default:
                        // 2.获取PIN status
                        getPinStatus(result);
                        break;
                }
            });
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
                toast(R.string.hh70_sim_not_accessible);
                to(RootCons.ACTIVITYS.SPLASH_AC, RootCons.FRAG.REFRESH_FR);
            });
            xGetSimStatusHelper.getSimStatus();
        });

        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> {
            toast(R.string.hh70_connect_failed);
            to(RootCons.ACTIVITYS.SPLASH_AC, RootCons.FRAG.REFRESH_FR);
        });
        xGetLoginStateHelper.getLoginState();
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

    /**
     * 获取PIN status
     *
     * @param result
     */
    private void getPinStatus(GetSimStatusBean result) {
        normalPinStatesNext(result);
        int pinState = result.getPinState();
        switch (pinState) {
            case GetSimStatusBean.CONS_FOR_PIN_UNKNOWN:
                unknownNext(result);
                break;
            case GetSimStatusBean.CONS_FOR_PIN_ENABLE_BUT_NOT_VERIFIED:
                pinEnableButNotVerifyNext(result);
                break;
            case GetSimStatusBean.CONS_FOR_PIN_PIN_ENABLE_VERIFIED:
                pinEnableVerifyNext(result);
                break;
            case GetSimStatusBean.CONS_FOR_PIN_PIN_DISABLE:
                pinDisableNext(result);
                break;
            case GetSimStatusBean.CONS_FOR_PIN_PUK_REQUIRED:
                pukRequiredNext(result);
                break;
            case GetSimStatusBean.CONS_FOR_PIN_PUK_TIMES_USED_OUT:
                pukTimeoutNext(result);
                break;
        }
    }

    /* -------------------------------------------- interface -------------------------------------------- */

    private OnPukTimeoutListener onPukTimeoutListener;

    // 接口OnPukTimeoutListener
    public interface OnPukTimeoutListener {
        void pukTimeout(GetSimStatusBean attr);
    }

    // 对外方式setOnPukTimeoutListener
    public void setOnPukTimeoutListener(OnPukTimeoutListener onPukTimeoutListener) {
        this.onPukTimeoutListener = onPukTimeoutListener;
    }

    // 封装方法pukTimeoutNext
    private void pukTimeoutNext(GetSimStatusBean attr) {
        if (onPukTimeoutListener != null) {
            onPukTimeoutListener.pukTimeout(attr);
        }
    }

    private OnPukRequiredListener onPukRequiredListener;

    // 接口OnPukRequiredListener
    public interface OnPukRequiredListener {
        void pukRequired(GetSimStatusBean attr);
    }

    // 对外方式setOnPukRequiredListener
    public void setOnPukRequiredListener(OnPukRequiredListener onPukRequiredListener) {
        this.onPukRequiredListener = onPukRequiredListener;
    }

    // 封装方法pukRequiredNext
    private void pukRequiredNext(GetSimStatusBean attr) {
        if (onPukRequiredListener != null) {
            onPukRequiredListener.pukRequired(attr);
        }
    }

    private OnPinDisableListener onPinDisableListener;

    // 接口OnPinDisableListener
    public interface OnPinDisableListener {
        void pinDisable(GetSimStatusBean attr);
    }

    // 对外方式setOnPinDisableListener
    public void setOnPinDisableListener(OnPinDisableListener onPinDisableListener) {
        this.onPinDisableListener = onPinDisableListener;
    }

    // 封装方法pinDisableNext
    private void pinDisableNext(GetSimStatusBean attr) {
        if (onPinDisableListener != null) {
            onPinDisableListener.pinDisable(attr);
        }
    }

    private OnPinEnableVerifyListener onPinEnableVerifyListener;

    // 接口OnPinEnableVerifyListener
    public interface OnPinEnableVerifyListener {
        void pinEnableVerify(GetSimStatusBean attr);
    }

    // 对外方式setOnPinEnableVerifyListener
    public void setOnPinEnableVerifyListener(OnPinEnableVerifyListener onPinEnableVerifyListener) {
        this.onPinEnableVerifyListener = onPinEnableVerifyListener;
    }

    // 封装方法pinEnableVerifyNext
    private void pinEnableVerifyNext(GetSimStatusBean attr) {
        if (onPinEnableVerifyListener != null) {
            onPinEnableVerifyListener.pinEnableVerify(attr);
        }
    }

    private OnPinEnableButNotVerifyListener onPinEnableButNotVerifyListener;

    // 接口OnPinEnableButNotVerifyListener
    public interface OnPinEnableButNotVerifyListener {
        void pinEnableButNotVerify(GetSimStatusBean attr);
    }

    // 对外方式setOnPinEnableButNotVerifyListener
    public void setOnPinEnableButNotVerifyListener(OnPinEnableButNotVerifyListener onPinEnableButNotVerifyListener) {
        this.onPinEnableButNotVerifyListener = onPinEnableButNotVerifyListener;
    }

    // 封装方法pinEnableButNotVerifyNext
    private void pinEnableButNotVerifyNext(GetSimStatusBean attr) {
        if (onPinEnableButNotVerifyListener != null) {
            onPinEnableButNotVerifyListener.pinEnableButNotVerify(attr);
        }
    }

    private OnUnkonwnListener onUnkonwnListener;

    // 接口OnUnkonwnListener
    public interface OnUnkonwnListener {
        void unknown(GetSimStatusBean attr);
    }

    // 对外方式setOnUnkonwnListener
    public void setOnUnkonwnListener(OnUnkonwnListener onUnkonwnListener) {
        this.onUnkonwnListener = onUnkonwnListener;
    }

    // 封装方法unknownNext
    private void unknownNext(GetSimStatusBean attr) {
        if (onUnkonwnListener != null) {
            onUnkonwnListener.unknown(attr);
        }
    }

    private OnNormalPinStatesListener onNormalPinStatesListener;

    // 接口OnNormalPinStatesListener
    public interface OnNormalPinStatesListener {
        void normalPinStates(GetSimStatusBean attr);
    }

    // 对外方式setOnNormalPinStatesListener
    public void setOnNormalPinStatesListener(OnNormalPinStatesListener onNormalPinStatesListener) {
        this.onNormalPinStatesListener = onNormalPinStatesListener;
    }

    // 封装方法normalPinStatesNext
    private void normalPinStatesNext(GetSimStatusBean attr) {
        if (onNormalPinStatesListener != null) {
            onNormalPinStatesListener.normalPinStates(attr);
        }
    }

    /* -------------------------------------------- helper -------------------------------------------- */
    public void toast(int resId) {
        ToastTool.show(activity, resId);
    }


}
