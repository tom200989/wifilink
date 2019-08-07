package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.text.TextUtils;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.SimStatus;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.ChangePinStateParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.ChangePinStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.UnlockPinHelper;

/**
 * Created by qianli.ma on 2017/12/11 0011.
 */

public class SimPinHelper {
    private Activity activity;

    public SimPinHelper(Activity activity) {
        this.activity = activity;
    }


    public void transfer(String pincode) {

        // 条件判断
        if (TextUtils.isEmpty(pincode)) {
            toast(R.string.not_empty);
            return;
        }
        if (pincode.length() < 4 || pincode.length() > 8) {
            toast(R.string.the_pin_code_should_be_4_8_characters);
            return;
        }

        PinStatuHelper pinStatuHelper = new PinStatuHelper(activity);
        pinStatuHelper.setOnPukTimeoutListener(attr -> toast(R.string.Home_PukTimes_UsedOut));
        pinStatuHelper.setOnUnkonwnListener(attr -> toast(R.string.not_inserted));
        pinStatuHelper.setOnPukTimeoutListener(attr -> toast(R.string.Home_PukTimes_UsedOut));
        pinStatuHelper.setOnPukRequiredListener(attr -> {
            toast(R.string.IDS_PUK_LOCKED);
            pukLockNext(attr);
        });
        pinStatuHelper.setOnPinDisableListener(attr -> enablePin(attr, pincode));// 原先为disable状态--> 设置为有效
        pinStatuHelper.setOnPinEnableButNotVerifyListener(attr -> enableUnlockFirst(attr, pincode));// 原先为PIN码设置但未开启--> 设置为有效
        pinStatuHelper.setOnPinEnableVerifyListener(attr -> disablePin(attr, pincode));// 原先为PIN码有效--> 设置为无效
        pinStatuHelper.getOne();
    }

    /**
     * 使PIN码失效
     *
     * @param attr
     * @param pincode
     */
    private void disablePin(GetSimStatusBean attr, String pincode) {

        // 1.先解PIN
        UnlockPinHelper xUnlockPinHelper = new UnlockPinHelper();
        xUnlockPinHelper.setOnUnlockPinSuccessListener(() -> {
            // 2.再改变PIN码状态
            ChangePinStateHelper xChangePinStateHelper = new ChangePinStateHelper();
            xChangePinStateHelper.setOnChangePinStateSuccessListener(() -> pinDisableNext(null));
            xChangePinStateHelper.setOnChangePinStateFailedListener(() -> toast(R.string.setting_failed));
            xChangePinStateHelper.changePinState(pincode, ChangePinStateParam.CONS_DISABLED_PIN);
        });
        xUnlockPinHelper.setOnUnlockPinRemainTimeFailedListener(this::getPinRemaingTime);
        xUnlockPinHelper.setOnUnlockPinFailedListener(() -> toast(R.string.setting_failed));
        xUnlockPinHelper.unlockPin(pincode);
    }

    /**
     * 使PIN码有效(但要先解PIN)
     *
     * @param attr
     * @param pincode
     */
    private void enableUnlockFirst(GetSimStatusBean attr, String pincode) {
        // 1.先解PIN

        UnlockPinHelper xUnlockPinHelper = new UnlockPinHelper();
        xUnlockPinHelper.setOnUnlockPinSuccessListener(() -> {
            // 2.再改变PIN码状态
            ChangePinStateHelper xChangePinStateHelper = new ChangePinStateHelper();
            xChangePinStateHelper.setOnChangePinStateSuccessListener(() -> pinEnableNext(null));
            xChangePinStateHelper.setOnChangePinStateFailedListener(() -> toast(R.string.setting_failed));
            xChangePinStateHelper.changePinState(pincode, ChangePinStateParam.CONS_ENABLED_PIN);
        });
        xUnlockPinHelper.setOnUnlockPinRemainTimeFailedListener(this::getPinRemaingTime);
        xUnlockPinHelper.setOnUnlockPinFailedListener(() -> toast(R.string.setting_failed));
        xUnlockPinHelper.unlockPin(pincode);
    }

    /**
     * 获取剩余次数
     */
    private void getPinRemaingTime() {
        // 获取剩余次数
        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
            if (result.getSIMState() == GetSimStatusBean.CONS_PIN_REQUIRED || result.getSIMState() == GetSimStatusBean.CONS_SIM_CARD_READY) {
                int pinRemainingTimes = result.getPinRemainingTimes();
                if (pinRemainingTimes >= 1) {
                    String tip = pinRemainingTimes + " " + activity.getString(R.string.sim_unlocked_attempts);
                    toast(tip);
                } else {
                    toast(R.string.Home_PinTimes_UsedOut);
                }
            } else if (result.getSIMState() == GetSimStatusBean.CONS_PUK_REQUIRED) {
                pinTimeoutNext(result);
            }
        });
        xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> toast(R.string.setting_failed));
        xGetSimStatusHelper.getSimStatus();
    }

    /**
     * 使PIN码有效
     *
     * @param attr
     * @param pincode
     */
    private void enablePin(GetSimStatusBean attr, String pincode) {
        ChangePinStateHelper xChangePinStateHelper = new ChangePinStateHelper();
        xChangePinStateHelper.setOnChangePinStateSuccessListener(() -> pinEnableNext(null));
        xChangePinStateHelper.setOnChangePinStateFailedListener(() -> toast(R.string.setting_failed));
        xChangePinStateHelper.changePinState(pincode, ChangePinStateParam.CONS_ENABLED_PIN);
    }

    /* -------------------------------------------- interface -------------------------------------------- */

    private OnPinTimeoutListener onPinTimeoutListener;

    // 接口OnPinTimeoutListener
    public interface OnPinTimeoutListener {
        void pinTimeout(Object attr);
    }

    // 对外方式setOnPinTimeoutListener
    public void setOnPinTimeoutListener(OnPinTimeoutListener onPinTimeoutListener) {
        this.onPinTimeoutListener = onPinTimeoutListener;
    }

    // 封装方法pinTimeoutNext
    private void pinTimeoutNext(Object attr) {
        if (onPinTimeoutListener != null) {
            onPinTimeoutListener.pinTimeout(attr);
        }
    }

    private OnPinDisableListener onPinDisableListener;

    // 接口OnPinDisableListener
    public interface OnPinDisableListener {
        void pinDisable(SimStatus attr);
    }

    // 对外方式setOnPinDisableListener
    public void setOnPinDisableListener(OnPinDisableListener onPinDisableListener) {
        this.onPinDisableListener = onPinDisableListener;
    }

    // 封装方法pinDisableNext
    private void pinDisableNext(SimStatus attr) {
        if (onPinDisableListener != null) {
            onPinDisableListener.pinDisable(attr);
        }
    }

    private OnPinEnableListener onPinEnableListener;

    // 接口OnPinEnableListener
    public interface OnPinEnableListener {
        void pinEnable(SimStatus attr);
    }

    // 对外方式setOnPinEnableListener
    public void setOnPinEnableListener(OnPinEnableListener onPinEnableListener) {
        this.onPinEnableListener = onPinEnableListener;
    }

    // 封装方法pinEnableNext
    private void pinEnableNext(SimStatus attr) {
        if (onPinEnableListener != null) {
            onPinEnableListener.pinEnable(attr);
        }
    }

    private OnPukLockedListener onPukLockedListener;

    // 接口OnPukLockedListener
    public interface OnPukLockedListener {
        void pukLock(GetSimStatusBean attr);
    }

    // 对外方式setOnPukLockedListener
    public void setOnPukLockedListener(OnPukLockedListener onPukLockedListener) {
        this.onPukLockedListener = onPukLockedListener;
    }

    // 封装方法pukLockNext
    private void pukLockNext(GetSimStatusBean attr) {
        if (onPukLockedListener != null) {
            onPukLockedListener.pukLock(attr);
        }
    }

    /* -------------------------------------------- helper -------------------------------------------- */
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
