package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetDeviceNameParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class SetDeviceNameHelper extends BaseHelper {

    public void setDeviceName(SetDeviceNameParam param) {
        prepareHelperNext();
        XSmart xSmart = new XSmart();
        xSmart.xMethod(XCons.METHOD_SET_DEVICE_NAME).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                setDeviceUnBlockSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                setDeviceUnBlockFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                setDeviceUnBlockFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }


    /*----------------------------------设置设备名成功的回调------------------------------*/
    public interface OnSetDeviceUnblockSuccessListener {
        void setDeviceUnBlockSuccess();
    }

    private OnSetDeviceUnblockSuccessListener onSetDeviceUnblockSuccessListener;

    //对外方式setOnSetDeviceUnblockSuccessListener
    public void setOnSetDeviceUnblockSuccessListener(OnSetDeviceUnblockSuccessListener onSetDeviceUnblockSuccessListener) {
        this.onSetDeviceUnblockSuccessListener = onSetDeviceUnblockSuccessListener;
    }

    //封装方法setDeviceUnBlockSuccessNext
    private void setDeviceUnBlockSuccessNext() {
        if (onSetDeviceUnblockSuccessListener != null) {
            onSetDeviceUnblockSuccessListener.setDeviceUnBlockSuccess();
        }
    }

    /*----------------------------------设置设备名失败的回调------------------------------*/
    public interface OnSetDeviceUnblockFailListener {
        void setDeviceUnBlockFail();
    }

    private OnSetDeviceUnblockFailListener onSetDeviceUnblockFailListener;

    //对外方式setOnSetDeviceUnblockFailListener
    public void setOnSetDeviceUnblockFailListener(OnSetDeviceUnblockFailListener onSetDeviceUnblockFailListener) {
        this.onSetDeviceUnblockFailListener = onSetDeviceUnblockFailListener;
    }

    //封装方法setDeviceUnBlockFailNext
    private void setDeviceUnBlockFailNext() {
        if (onSetDeviceUnblockFailListener != null) {
            onSetDeviceUnblockFailListener.setDeviceUnBlockFail();
        }
    }
}
