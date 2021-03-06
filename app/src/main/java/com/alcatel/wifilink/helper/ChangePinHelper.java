package com.alcatel.wifilink.helper;

import android.app.Activity;
import android.text.TextUtils;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.utils.ToastTool;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.ChangePinCodeHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.UnlockPinHelper;

/**
 * Created by qianli.ma on 2017/12/12 0012.
 */

public class ChangePinHelper {

    private Activity activity;

    public ChangePinHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * 修改
     *
     * @param currentCode 当前PIN码
     * @param refreshCode 新的PIN码
     */
    public void change(String currentCode, String refreshCode, String confirmCode) {
        PinStatuHelper pinStatuHelper = new PinStatuHelper(activity);
        pinStatuHelper.setOnNormalPinStatesListener(attr -> {
            if (attr.getPinState() != GetSimStatusBean.CONS_FOR_PIN_PIN_ENABLE_VERIFIED) {// PIN码没有开启状态下
                toast(R.string.hh70_enable_sim_pin);
            } else {// PIN码已经开启且有效
                changePin(currentCode, refreshCode, confirmCode);
            }

        });
        pinStatuHelper.getOne();
    }

    private void changePin(String currentCode, String refreshCode, String confirmCode) {
        // 条件判断
        boolean currentCodeEmpty = TextUtils.isEmpty(currentCode);
        boolean refreshCodeEmpty = TextUtils.isEmpty(refreshCode);
        boolean confirmCodeEmpty = TextUtils.isEmpty(confirmCode);
        if (currentCodeEmpty || refreshCodeEmpty || confirmCodeEmpty) {
            toast(R.string.hh70_pin_empty);
            return;
        }

        boolean currentLengthNotMatch = currentCode.length() < 4 || currentCode.length() > 8;
        boolean refreshLengthNotMatch = refreshCode.length() < 4 || refreshCode.length() > 8;
        boolean confirmLengthNotMatch = confirmCode.length() < 4 || confirmCode.length() > 8;
        if (currentLengthNotMatch || refreshLengthNotMatch || confirmLengthNotMatch) {
            toast(R.string.hh70_the_pin_code_should_4);
            return;
        }

        if (!refreshCode.equals(confirmCode)) {
            toast(R.string.hh70_confirm_pin_same);
            return;
        }

        // 提交
        UnlockPinHelper xUnlockPinHelper = new UnlockPinHelper();
        xUnlockPinHelper.setOnUnlockPinSuccessListener(() -> {
            ChangePinCodeHelper xChangePinCodeHelper = new ChangePinCodeHelper();
            xChangePinCodeHelper.setOnChangePinCodeSuccessListener(() -> {
                toast(R.string.hh70_success);
                changePinSuccessNext(null);
            });
            xChangePinCodeHelper.setOnChangePinCodeFailedListener(() -> toast(R.string.hh70_cant_unlock_pin));
            xChangePinCodeHelper.changePinCode(refreshCode, currentCode);
        });
        xUnlockPinHelper.setOnUnlockPinFailedListener(() -> {
            toast(R.string.hh70_pin_code_wrong);
            getPinRemaingTime();
        });
        xUnlockPinHelper.setOnUnlockPinRemainTimeFailedListener(() -> {
            toast(R.string.hh70_pin_code_wrong);
            getPinRemaingTime();
        });
        xUnlockPinHelper.unlockPin(currentCode);
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
                    String tip = pinRemainingTimes + " " + activity.getString(R.string.hh70_attempts_remaing);
                    toast(tip);
                } else {
                    toast(R.string.hh70_pin_timeout);
                }
            } else if (result.getSIMState() == GetSimStatusBean.CONS_PUK_REQUIRED) {
                pinTimeoutNext(result);
            }
        });
        xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> toast(R.string.hh70_failed));
        xGetSimStatusHelper.getSimStatus();
    }

    private OnChangePinSuccessListener onChangePinSuccessListener;

    // 接口OnChangePinSuccessListener
    public interface OnChangePinSuccessListener {
        void changePinSuccess(Object attr);
    }

    // 对外方式setOnChangePinSuccessListener
    public void setOnChangePinSuccessListener(OnChangePinSuccessListener onChangePinSuccessListener) {
        this.onChangePinSuccessListener = onChangePinSuccessListener;
    }

    // 封装方法changePinSuccessNext
    private void changePinSuccessNext(Object attr) {
        if (onChangePinSuccessListener != null) {
            onChangePinSuccessListener.changePinSuccess(attr);
        }
    }

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

    private void toast(int resId) {
        ToastTool.show(activity, resId);
    }

    private void toastLong(int resId) {
        ToastTool.showLong(activity, resId);
    }

    private void toast(String content) {
        ToastTool.show(activity, content);
    }


}
