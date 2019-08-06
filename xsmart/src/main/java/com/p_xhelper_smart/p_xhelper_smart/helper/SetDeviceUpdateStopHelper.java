package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
@SuppressWarnings("unchecked")
public class SetDeviceUpdateStopHelper extends BaseHelper {

    /**
     * 触发FW停止升级
     */
    public void setDeviceUpdateStop() {
        prepareHelperNext();
        XSmart xUpdateStop = new XSmart();
        xUpdateStop.xMethod(XCons.METHOD_SET_DEVICE_UPDATE_STOP);
        xUpdateStop.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                SetDeviceUpdateStopSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                SetDeviceUpdateStopFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                SetDeviceUpdateStopFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnSetDeviceUpdateStopSuccessListener onSetDeviceUpdateStopSuccessListener;

    // Inteerface--> 接口OnSetDeviceUpdateStopSuccessListener
    public interface OnSetDeviceUpdateStopSuccessListener {
        void SetDeviceUpdateStopSuccess();
    }

    // 对外方式setOnSetDeviceUpdateStopSuccessListener
    public void setOnSetDeviceUpdateStopSuccessListener(OnSetDeviceUpdateStopSuccessListener onSetDeviceUpdateStopSuccessListener) {
        this.onSetDeviceUpdateStopSuccessListener = onSetDeviceUpdateStopSuccessListener;
    }

    // 封装方法SetDeviceUpdateStopSuccessNext
    private void SetDeviceUpdateStopSuccessNext() {
        if (onSetDeviceUpdateStopSuccessListener != null) {
            onSetDeviceUpdateStopSuccessListener.SetDeviceUpdateStopSuccess();
        }
    }

    private OnSetDeviceUpdateStopFailedListener onSetDeviceUpdateStopFailedListener;

    // Inteerface--> 接口OnSetDeviceUpdateStopFailedListener
    public interface OnSetDeviceUpdateStopFailedListener {
        void SetDeviceUpdateStopFailed();
    }

    // 对外方式setOnSetDeviceUpdateStopFailedListener
    public void setOnSetDeviceUpdateStopFailedListener(OnSetDeviceUpdateStopFailedListener onSetDeviceUpdateStopFailedListener) {
        this.onSetDeviceUpdateStopFailedListener = onSetDeviceUpdateStopFailedListener;
    }

    // 封装方法SetDeviceUpdateStopFailedNext
    private void SetDeviceUpdateStopFailedNext() {
        if (onSetDeviceUpdateStopFailedListener != null) {
            onSetDeviceUpdateStopFailedListener.SetDeviceUpdateStopFailed();
        }
    }
}
