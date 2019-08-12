package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.ConnectedList;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectedDeviceListHelper;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianli.ma on 2018/5/18 0018.
 */

public class ConnectDeviceHelper {

    public void get() {
        GetConnectedDeviceListHelper xGetConnectedDeviceListHelper = new GetConnectedDeviceListHelper();
        xGetConnectedDeviceListHelper.setOnGetDeviceListSuccessListener(bean -> {
            ConnectedList tempConnectedList = new ConnectedList();
            List<GetConnectDeviceListBean.ConnectedDeviceBean> connectedList = bean.getConnectedList();
            if(connectedList != null && connectedList.size() > 0){
                List<ConnectedList.Device> tempDeviceArrayList = new ArrayList<>();
                for(GetConnectDeviceListBean.ConnectedDeviceBean connectedDeviceBean : connectedList){
                    ConnectedList.Device device = new ConnectedList.Device();
                    device.setAssociationTime(connectedDeviceBean.getAssociationTime());
                    device.setConnectMode(connectedDeviceBean.getConnectMode());
                    device.setDeviceName(connectedDeviceBean.getDeviceName());
                    device.setDeviceType(connectedDeviceBean.getDeviceType());
                    device.setId(connectedDeviceBean.getId());
                    device.setInternetRight(connectedDeviceBean.getInternetRight());
                    device.setIPAddress(connectedDeviceBean.getIPAddress());
                    device.setMacAddress(connectedDeviceBean.getMacAddress());
                    device.setStorageRight(connectedDeviceBean.getStorageRight());
                    tempDeviceArrayList.add(device);
                }
                tempConnectedList.setConnectedList(tempDeviceArrayList);
            }
            devicesSuccessNext(tempConnectedList);
        });
        xGetConnectedDeviceListHelper.setOnGetDeviceListFailListener(() -> {
            devicesFailedNext(null);
            devicesErrorNext(null);
        });
        xGetConnectedDeviceListHelper.getConnectDeviceList();
    }

    private OnDevicesSuccessListener onDevicesSuccessListener;

    // 接口OnDevicesSuccessListener
    public interface OnDevicesSuccessListener {
        void devicesSuccess(ConnectedList connectedList);
    }

    // 对外方式setOnDevicesSuccessListener
    public void setOnDevicesSuccessListener(OnDevicesSuccessListener onDevicesSuccessListener) {
        this.onDevicesSuccessListener = onDevicesSuccessListener;
    }

    // 封装方法devicesSuccessNext
    private void devicesSuccessNext(ConnectedList connectedList) {
        if (onDevicesSuccessListener != null) {
            onDevicesSuccessListener.devicesSuccess(connectedList);
        }
    }

    private OnDevicesFailedListener onDevicesFailedListener;

    // 接口OnDevicesFailedListener
    public interface OnDevicesFailedListener {
        void devicesFailed(Object o);
    }

    // 对外方式setOnDevicesFailedListener
    public void setOnDevicesFailedListener(OnDevicesFailedListener onDevicesFailedListener) {
        this.onDevicesFailedListener = onDevicesFailedListener;
    }

    // 封装方法devicesFailedNext
    private void devicesFailedNext(Object attr) {
        if (onDevicesFailedListener != null) {
            onDevicesFailedListener.devicesFailed(attr);
        }
    }

    private OnDevicesErrorListener onDevicesErrorListener;

    // 接口OnDevicesErrorListener
    public interface OnDevicesErrorListener {
        void devicesError(FwError error);
    }

    // 对外方式setOnDevicesErrorListener
    public void setOnDevicesErrorListener(OnDevicesErrorListener onDevicesErrorListener) {
        this.onDevicesErrorListener = onDevicesErrorListener;
    }

    // 封装方法devicesErrorNext
    private void devicesErrorNext(FwError error) {
        if (onDevicesErrorListener != null) {
            onDevicesErrorListener.devicesError(error);
        }
    }

}
