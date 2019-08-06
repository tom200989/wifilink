package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetConnectedDeviceBlockParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class SetConnectedDeviceBlockHelper extends BaseHelper {


    public void setConnectedDeviceBlock(String DeviceName, String MacAddress) {
        prepareHelperNext();
        SetConnectedDeviceBlockParam param = new SetConnectedDeviceBlockParam();
        param.setDeviceName(DeviceName);
        param.setMacAddress(MacAddress);
        XSmart xSmart = new XSmart();
        xSmart.xMethod(XCons.METHOD_SET_CONNECTED_DEVICE_BLOCK).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                getSetConnectDeviceBlockSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                getSetConnectDeviceBlockFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getSetConnectDeviceBlockFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------设置黑名单成功的回调------------------------------*/
    public interface OnSetConnectDeviceBlockSuccessListener {
        void setConnectDeviceBlockSuccess();
    }

    private OnSetConnectDeviceBlockSuccessListener onSetConnectDeviceBlockSuccessListener;

    //对外方式setOnSetConnectDeviceBlockSuccessListener
    public void setOnSetConnectDeviceBlockSuccessListener(OnSetConnectDeviceBlockSuccessListener onSetConnectDeviceBlockSuccessListener) {
        this.onSetConnectDeviceBlockSuccessListener = onSetConnectDeviceBlockSuccessListener;
    }

    //封装方法getSetConnectDeviceBlockSuccessNext
    private void getSetConnectDeviceBlockSuccessNext() {
        if (onSetConnectDeviceBlockSuccessListener != null) {
            onSetConnectDeviceBlockSuccessListener.setConnectDeviceBlockSuccess();
        }
    }

    /*----------------------------------设置黑名单失败的回调------------------------------*/
    public interface OnSetConnectDeviceBlockFailListener {
        void setConnectDeviceBlockFail();
    }

    private OnSetConnectDeviceBlockFailListener onSetConnectDeviceBlockFailListener;

    //对外方式setOnSetConnectDeviceBlockFailListener
    public void setOnSetConnectDeviceBlockFailListener(OnSetConnectDeviceBlockFailListener onSetConnectDeviceBlockFailListener) {
        this.onSetConnectDeviceBlockFailListener = onSetConnectDeviceBlockFailListener;
    }

    //封装方法getSetConnectDeviceBlockFailNext
    private void getSetConnectDeviceBlockFailNext() {
        if (onSetConnectDeviceBlockFailListener != null) {
            onSetConnectDeviceBlockFailListener.setConnectDeviceBlockFail();
        }
    }



}
