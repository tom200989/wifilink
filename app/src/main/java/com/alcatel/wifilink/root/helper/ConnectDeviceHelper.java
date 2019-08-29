package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.ConnectedListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectedDeviceListHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianli.ma on 2018/5/18 0018.
 */

public class ConnectDeviceHelper {

    public void get() {
        GetConnectedDeviceListHelper xGetConnectedDeviceListHelper = new GetConnectedDeviceListHelper();
        xGetConnectedDeviceListHelper.setOnGetDeviceListSuccessListener(bean -> {
            ConnectedListBean tempConnectedListBean = new ConnectedListBean();
            List<GetConnectDeviceListBean.ConnectedDeviceBean> connectedList = bean.getConnectedList();
            if(connectedList != null && connectedList.size() > 0){
                List<ConnectedListBean.Device> tempDeviceArrayList = new ArrayList<>();
                for(GetConnectDeviceListBean.ConnectedDeviceBean connectedDeviceBean : connectedList){
                    ConnectedListBean.Device device = new ConnectedListBean.Device();
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
                tempConnectedListBean.setConnectedList(tempDeviceArrayList);
            }
            devicesSuccessNext(tempConnectedListBean);
        });
        xGetConnectedDeviceListHelper.setOnGetDeviceListFailListener(this::devicesFailedNext);
        xGetConnectedDeviceListHelper.getConnectDeviceList();
    }

    private OnDevicesSuccessListener onDevicesSuccessListener;

    // 接口OnDevicesSuccessListener
    public interface OnDevicesSuccessListener {
        void devicesSuccess(ConnectedListBean connectedListBean);
    }

    // 对外方式setOnDevicesSuccessListener
    public void setOnDevicesSuccessListener(OnDevicesSuccessListener onDevicesSuccessListener) {
        this.onDevicesSuccessListener = onDevicesSuccessListener;
    }

    // 封装方法devicesSuccessNext
    private void devicesSuccessNext(ConnectedListBean connectedListBean) {
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
