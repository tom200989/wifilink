package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
@SuppressWarnings("unchecked")
public class SetDeviceRebootHelper extends BaseHelper {

    /**
     * 重启设备
     */
    public void SetDeviceReboot() {
        prepareHelperNext();
        XSmart xDeviceReboot = new XSmart();
        xDeviceReboot.xMethod(XCons.METHOD_SET_DEVICE_REBOOT);
        xDeviceReboot.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                SetDeviceRebootSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                SetDeviceRebootFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                SetDeviceRebootFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnSetDeviceRebootSuccessListener onSetDeviceRebootSuccessListener;

    // Inteerface--> 接口OnSetDeviceRebootSuccessListener
    public interface OnSetDeviceRebootSuccessListener {
        void SetDeviceRebootSuccess();
    }

    // 对外方式setOnSetDeviceRebootSuccessListener
    public void setOnSetDeviceRebootSuccessListener(OnSetDeviceRebootSuccessListener onSetDeviceRebootSuccessListener) {
        this.onSetDeviceRebootSuccessListener = onSetDeviceRebootSuccessListener;
    }

    // 封装方法SetDeviceRebootSuccessNext
    private void SetDeviceRebootSuccessNext() {
        if (onSetDeviceRebootSuccessListener != null) {
            onSetDeviceRebootSuccessListener.SetDeviceRebootSuccess();
        }
    }

    private OnSetDeviceRebootFailedListener onSetDeviceRebootFailedListener;

    // Inteerface--> 接口OnSetDeviceRebootFailedListener
    public interface OnSetDeviceRebootFailedListener {
        void SetDeviceRebootFailed();
    }

    // 对外方式setOnSetDeviceRebootFailedListener
    public void setOnSetDeviceRebootFailedListener(OnSetDeviceRebootFailedListener onSetDeviceRebootFailedListener) {
        this.onSetDeviceRebootFailedListener = onSetDeviceRebootFailedListener;
    }

    // 封装方法SetDeviceRebootFailedNext
    private void SetDeviceRebootFailedNext() {
        if (onSetDeviceRebootFailedListener != null) {
            onSetDeviceRebootFailedListener.SetDeviceRebootFailed();
        }
    }
}
