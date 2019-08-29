package com.alcatel.wifilink.root.helper;

import android.content.Context;

import com.alcatel.wifilink.root.bean.ConnectedListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectedDeviceListHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianli.ma on 2018/4/11 0011.
 */

public class DeviceHelper {
    private Context context;

    public DeviceHelper(Context context) {
        this.context = context;
    }

    public void getDeviecs() {
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
            devicesNext(tempConnectedListBean);
        });
        xGetConnectedDeviceListHelper.setOnGetDeviceListFailListener(this::getDeviceListFailedNext);
        xGetConnectedDeviceListHelper.setOnAPPErrorListener(this::AppErrorNext);
        xGetConnectedDeviceListHelper.setOnFwErrorListener(this::FwErrorNext);
        xGetConnectedDeviceListHelper.getConnectDeviceList();
    }

    private OnAppErrorListener onAppErrorListener;

    // Inteerface--> 接口OnAppErrorListener
    public interface OnAppErrorListener {
        void AppError();
    }

    // 对外方式setOnAppErrorListener
    public void setOnAppErrorListener(OnAppErrorListener onAppErrorListener) {
        this.onAppErrorListener = onAppErrorListener;
    }

    // 封装方法AppErrorNext
    private void AppErrorNext() {
        if (onAppErrorListener != null) {
            onAppErrorListener.AppError();
        }
    }

    private OnFwErrorListener onFwErrorListener;

    // Inteerface--> 接口OnFwErrorListener
    public interface OnFwErrorListener {
        void FwError();
    }

    // 对外方式setOnFwErrorListener
    public void setOnFwErrorListener(OnFwErrorListener onFwErrorListener) {
        this.onFwErrorListener = onFwErrorListener;
    }

    // 封装方法FwErrorNext
    private void FwErrorNext() {
        if (onFwErrorListener != null) {
            onFwErrorListener.FwError();
        }
    }

    private OnGetDeviceListFailedListener onGetDeviceListFailedListener;

    // Inteerface--> 接口OnGetDeviceListFailedListener
    public interface OnGetDeviceListFailedListener {
        void getDeviceListFailed();
    }

    // 对外方式setOnGetDeviceListFailedListener
    public void setOnGetDeviceListFailedListener(OnGetDeviceListFailedListener onGetDeviceListFailedListener) {
        this.onGetDeviceListFailedListener = onGetDeviceListFailedListener;
    }

    // 封装方法getDeviceListFailedNext
    private void getDeviceListFailedNext() {
        if (onGetDeviceListFailedListener != null) {
            onGetDeviceListFailedListener.getDeviceListFailed();
        }
    }


    private OnGetDevicesSuccessListener onGetDevicesSuccessListener;

    // 接口OnGetDevicesSuccessListener
    public interface OnGetDevicesSuccessListener {
        void devices(ConnectedListBean connectedListBean);
    }

    // 对外方式setOnGetDevicesSuccessListener
    public void setOnGetDevicesSuccessListener(OnGetDevicesSuccessListener onGetDevicesSuccessListener) {
        this.onGetDevicesSuccessListener = onGetDevicesSuccessListener;
    }

    // 封装方法devicesNext
    private void devicesNext(ConnectedListBean connectedListBean) {
        if (onGetDevicesSuccessListener != null) {
            onGetDevicesSuccessListener.devices(connectedListBean);
        }
    }
}
