package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
@SuppressWarnings("unchecked")
public class SetDeviceResetHelper extends BaseHelper {

    /**
     * 设备重置
     */
    public void SetDeviceReset() {
        prepareHelperNext();
        XSmart xDeviceReset = new XSmart();
        xDeviceReset.xMethod(XCons.METHOD_SET_DEVICE_RESET);
        xDeviceReset.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                SetDeviceResetSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                SetDeviceResetFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                SetDeviceResetFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnSetDeviceResetSuccessListener onSetDeviceResetSuccessListener;

    // Inteerface--> 接口OnSetDeviceResetSuccessListener
    public interface OnSetDeviceResetSuccessListener {
        void SetDeviceResetSuccess();
    }

    // 对外方式setOnSetDeviceResetSuccessListener
    public void setOnSetDeviceResetSuccessListener(OnSetDeviceResetSuccessListener onSetDeviceResetSuccessListener) {
        this.onSetDeviceResetSuccessListener = onSetDeviceResetSuccessListener;
    }

    // 封装方法SetDeviceResetSuccessNext
    private void SetDeviceResetSuccessNext() {
        if (onSetDeviceResetSuccessListener != null) {
            onSetDeviceResetSuccessListener.SetDeviceResetSuccess();
        }
    }

    private OnSetDeviceResetFailedListener onSetDeviceResetFailedListener;

    // Inteerface--> 接口OnSetDeviceResetFailedListener
    public interface OnSetDeviceResetFailedListener {
        void SetDeviceResetFailed();
    }

    // 对外方式setOnSetDeviceResetFailedListener
    public void setOnSetDeviceResetFailedListener(OnSetDeviceResetFailedListener onSetDeviceResetFailedListener) {
        this.onSetDeviceResetFailedListener = onSetDeviceResetFailedListener;
    }

    // 封装方法SetDeviceResetFailedNext
    private void SetDeviceResetFailedNext() {
        if (onSetDeviceResetFailedListener != null) {
            onSetDeviceResetFailedListener.SetDeviceResetFailed();
        }
    }
}
