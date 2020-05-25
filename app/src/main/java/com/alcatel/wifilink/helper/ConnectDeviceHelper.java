package com.alcatel.wifilink.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectedDeviceListHelper;

/**
 * Created by qianli.ma on 2018/5/18 0018.
 */

public class ConnectDeviceHelper {

    public void get() {
        GetConnectedDeviceListHelper xGetConnectedDeviceListHelper = new GetConnectedDeviceListHelper();
        xGetConnectedDeviceListHelper.setOnGetDeviceListSuccessListener(this::devicesSuccessNext);
        xGetConnectedDeviceListHelper.setOnGetDeviceListFailListener(this::devicesFailedNext);
        xGetConnectedDeviceListHelper.getConnectDeviceList();
    }

    private OnDevicesSuccessListener onDevicesSuccessListener;

    // 接口OnDevicesSuccessListener
    public interface OnDevicesSuccessListener {
        void devicesSuccess(GetConnectDeviceListBean connectedListBean);
    }

    // 对外方式setOnDevicesSuccessListener
    public void setOnDevicesSuccessListener(OnDevicesSuccessListener onDevicesSuccessListener) {
        this.onDevicesSuccessListener = onDevicesSuccessListener;
    }

    // 封装方法devicesSuccessNext
    private void devicesSuccessNext(GetConnectDeviceListBean connectedListBean) {
        if (onDevicesSuccessListener != null) {
            onDevicesSuccessListener.devicesSuccess(connectedListBean);
        }
    }

    private OnDevicesFailedListener onDevicesFailedListener;

    // 接口OnDevicesFailedListener
    public interface OnDevicesFailedListener {
        void devicesFailed();
    }

    // 对外方式setOnDevicesFailedListener
    public void setOnDevicesFailedListener(OnDevicesFailedListener onDevicesFailedListener) {
        this.onDevicesFailedListener = onDevicesFailedListener;
    }

    // 封装方法devicesFailedNext
    private void devicesFailedNext() {
        if (onDevicesFailedListener != null) {
            onDevicesFailedListener.devicesFailed();
        }
    }

}
