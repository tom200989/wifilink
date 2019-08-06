package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.UnlockPinParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
@SuppressWarnings("unchecked")
public class UnlockPinHelper extends BaseHelper {

    /**
     * 解PIN
     *
     * @param pin pin码
     */
    public void unlockPin(String pin) {
        prepareHelperNext();
        XSmart xPin = new XSmart();
        xPin.xMethod(XCons.METHOD_UNLOCK_PIN);
        xPin.xParam(new UnlockPinParam(pin));
        xPin.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                unlockPinSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                unlockPinFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                unlockPinFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnUnlockPinSuccessListener onUnlockPinSuccessListener;

    // Inteerface--> 接口OnUnlockPinSuccessListener
    public interface OnUnlockPinSuccessListener {
        void unlockPinSuccess();
    }

    // 对外方式setOnUnlockPinSuccessListener
    public void setOnUnlockPinSuccessListener(OnUnlockPinSuccessListener onUnlockPinSuccessListener) {
        this.onUnlockPinSuccessListener = onUnlockPinSuccessListener;
    }

    // 封装方法unlockPinSuccessNext
    private void unlockPinSuccessNext() {
        if (onUnlockPinSuccessListener != null) {
            onUnlockPinSuccessListener.unlockPinSuccess();
        }
    }

    private OnUnlockPinFailedListener onUnlockPinFailedListener;

    // Inteerface--> 接口OnUnlockPinFailedListener
    public interface OnUnlockPinFailedListener {
        void unlockPinFailed();
    }

    // 对外方式setOnUnlockPinFailedListener
    public void setOnUnlockPinFailedListener(OnUnlockPinFailedListener onUnlockPinFailedListener) {
        this.onUnlockPinFailedListener = onUnlockPinFailedListener;
    }

    // 封装方法unlockPinFailedNext
    private void unlockPinFailedNext() {
        if (onUnlockPinFailedListener != null) {
            onUnlockPinFailedListener.unlockPinFailed();
        }
    }
}
