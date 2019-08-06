package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetDeviceUnblockParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class SetDeviceUnblockHelper extends BaseHelper {

    public void setDeviceUnblock(String DeviceName, String MacAddress) {
        prepareHelperNext();
        SetDeviceUnblockParam param = new SetDeviceUnblockParam();
        param.setDeviceName(DeviceName);
        param.setMacAddress(MacAddress);
        XSmart xSmart = new XSmart();
        xSmart.xMethod(XCons.METHOD_SET_DEVICE_UNBLOCK).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                setDeviceUnblockSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                setDeviceUnblockFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                setDeviceUnblockFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------设置设备取消黑名单成功的回调------------------------------*/
    public interface OnSetDeviceUnBlockSuccessListener {
        void setDeviceUnBlockSuccess();
    }

    private OnSetDeviceUnBlockSuccessListener onSetDeviceUnBlockSuccessListener;

    //对外方式setOnSetDeviceUnBlockSuccessListener
    public void setOnSetDeviceUnBlockSuccessListener(OnSetDeviceUnBlockSuccessListener onSetDeviceUnBlockSuccessListener) {
        this.onSetDeviceUnBlockSuccessListener = onSetDeviceUnBlockSuccessListener;
    }

    //封装方法setDeviceUnblockSuccessNext
    private void setDeviceUnblockSuccessNext() {
        if (onSetDeviceUnBlockSuccessListener != null) {
            onSetDeviceUnBlockSuccessListener.setDeviceUnBlockSuccess();
        }
    }


    /*----------------------------------设置设备取消黑名单失败的回调------------------------------*/
    public interface OnSetDeviceUnBlockFailListener {
        void setDeviceUnBlockFail();
    }

    private OnSetDeviceUnBlockFailListener onSetDeviceUnBlockFailListener;

    //对外方式setOnSetDeviceUnBlockFailListener
    public void setOnSetDeviceUnBlockFailListener(OnSetDeviceUnBlockFailListener onSetDeviceUnBlockFailListener) {
        this.onSetDeviceUnBlockFailListener = onSetDeviceUnBlockFailListener;
    }

    //封装方法setDeviceUnblockFailNext
    private void setDeviceUnblockFailNext() {
        if (onSetDeviceUnBlockFailListener != null) {
            onSetDeviceUnBlockFailListener.setDeviceUnBlockFail();
        }
    }

}
