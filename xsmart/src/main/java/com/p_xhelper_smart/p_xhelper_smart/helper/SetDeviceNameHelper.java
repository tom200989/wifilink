package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetDeviceNameParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
@SuppressWarnings("unchecked")
public class SetDeviceNameHelper extends BaseHelper {

    public void setDeviceName(SetDeviceNameParam param) {
        prepareHelperNext();
        XSmart xSmart = new XSmart();
        xSmart.xMethod(XCons.METHOD_SET_DEVICE_NAME).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                setDeviceNameSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                setDeviceNameFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                setDeviceNameFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }


    /*----------------------------------设置设备名成功的回调------------------------------*/
    public interface OnSetDeviceNameSuccessListener {
        void setDeviceNameSuccess();
    }

    private OnSetDeviceNameSuccessListener onSetDeviceNameSuccessListener;

    //对外方式setOnSetDeviceNameSuccessListener
    public void setOnSetDeviceNameSuccessListener(OnSetDeviceNameSuccessListener onSetDeviceNameSuccessListener) {
        this.onSetDeviceNameSuccessListener = onSetDeviceNameSuccessListener;
    }

    //封装方法setDeviceNameSuccessNext
    private void setDeviceNameSuccessNext() {
        if (onSetDeviceNameSuccessListener != null) {
            onSetDeviceNameSuccessListener.setDeviceNameSuccess();
        }
    }

    /*----------------------------------设置设备名失败的回调------------------------------*/
    public interface OnSetDeviceNameFailListener {
        void setDeviceNameFail();
    }

    private OnSetDeviceNameFailListener onSetDeviceNameFailListener;

    //对外方式setOnSetDeviceNameFailListener
    public void setOnSetDeviceNameFailListener(OnSetDeviceNameFailListener onSetDeviceNameFailListener) {
        this.onSetDeviceNameFailListener = onSetDeviceNameFailListener;
    }

    //封装方法setDeviceNameFailNext
    private void setDeviceNameFailNext() {
        if (onSetDeviceNameFailListener != null) {
            onSetDeviceNameFailListener.setDeviceNameFail();
        }
    }

}
