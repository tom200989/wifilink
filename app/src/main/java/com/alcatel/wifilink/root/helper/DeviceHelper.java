package com.alcatel.wifilink.root.helper;

import android.content.Context;

import com.alcatel.wifilink.root.bean.ConnectedList;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.utils.Logs;
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
            devicesNext(tempConnectedList);
        });
        xGetConnectedDeviceListHelper.setOnGetDeviceListFailListener(() -> {
            resuletErrorNext(null);
            errorNext(null);
        });
        xGetConnectedDeviceListHelper.getConnectDeviceList();
    }

    private OnGetDevicesErrorListener onGetDevicesErrorListener;

    // 接口OnGetDevicesErrorListener
    public interface OnGetDevicesErrorListener {
        void error(Throwable throwable);
    }

    // 对外方式setOnGetDevicesErrorListener
    public void setOnGetDevicesErrorListener(OnGetDevicesErrorListener onGetDevicesErrorListener) {
        this.onGetDevicesErrorListener = onGetDevicesErrorListener;
    }

    // 封装方法errorNext
    private void errorNext(Throwable throwable) {
        if (onGetDevicesErrorListener != null) {
            onGetDevicesErrorListener.error(throwable);
        }
    }

    private OnGetDevicesResultErrorListener onGetDevicesResultErrorListener;

    // 接口OnGetDevicesResultErrorListener
    public interface OnGetDevicesResultErrorListener {
        void resuletError(ResponseBody.Error error);
    }

    // 对外方式setOnGetDevicesResultErrorListener
    public void setOnGetDevicesResultErrorListener(OnGetDevicesResultErrorListener onGetDevicesResultErrorListener) {
        this.onGetDevicesResultErrorListener = onGetDevicesResultErrorListener;
    }

    // 封装方法resuletErrorNext
    private void resuletErrorNext(ResponseBody.Error error) {
        if (onGetDevicesResultErrorListener != null) {
            onGetDevicesResultErrorListener.resuletError(error);
        }
    }

    private OnGetDevicesSuccessListener onGetDevicesSuccessListener;

    // 接口OnGetDevicesSuccessListener
    public interface OnGetDevicesSuccessListener {
        void devices(ConnectedList connectedList);
    }

    // 对外方式setOnGetDevicesSuccessListener
    public void setOnGetDevicesSuccessListener(OnGetDevicesSuccessListener onGetDevicesSuccessListener) {
        this.onGetDevicesSuccessListener = onGetDevicesSuccessListener;
    }

    // 封装方法devicesNext
    private void devicesNext(ConnectedList connectedList) {
        if (onGetDevicesSuccessListener != null) {
            onGetDevicesSuccessListener.devices(connectedList);
        }
    }
}
