package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.UnlockPukParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
@SuppressWarnings("unchecked")
public class UnlockPukHelper extends BaseHelper {

    /**
     * 解PUK
     *
     * @param puk PUK码
     * @param pin PIN码
     */
    public void unlockPuk(String puk, String pin) {
        prepareHelperNext();
        XSmart xPuk = new XSmart();
        xPuk.xMethod(XCons.METHOD_UNLOCK_PUK);
        xPuk.xParam(new UnlockPukParam(puk, pin));
        xPuk.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                unlockPukSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                unlockPukFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                unlockPukFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnUnlockPukSuccessListener onUnlockPukSuccessListener;

    // Inteerface--> 接口OnUnlockPukSuccessListener
    public interface OnUnlockPukSuccessListener {
        void unlockPukSuccess();
    }

    // 对外方式setOnUnlockPukSuccessListener
    public void setOnUnlockPukSuccessListener(OnUnlockPukSuccessListener onUnlockPukSuccessListener) {
        this.onUnlockPukSuccessListener = onUnlockPukSuccessListener;
    }

    // 封装方法unlockPukSuccessNext
    private void unlockPukSuccessNext() {
        if (onUnlockPukSuccessListener != null) {
            onUnlockPukSuccessListener.unlockPukSuccess();
        }
    }

    private OnUnlockPukFailedListener onUnlockPukFailedListener;

    // Inteerface--> 接口OnUnlockPukFailedListener
    public interface OnUnlockPukFailedListener {
        void unlockPukFailed();
    }

    // 对外方式setOnUnlockPukFailedListener
    public void setOnUnlockPukFailedListener(OnUnlockPukFailedListener onUnlockPukFailedListener) {
        this.onUnlockPukFailedListener = onUnlockPukFailedListener;
    }

    // 封装方法unlockPukFailedNext
    private void unlockPukFailedNext() {
        if (onUnlockPukFailedListener != null) {
            onUnlockPukFailedListener.unlockPukFailed();
        }
    }
}
