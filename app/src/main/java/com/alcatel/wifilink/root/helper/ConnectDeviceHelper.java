package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.ConnectedList;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;

/**
 * Created by qianli.ma on 2018/5/18 0018.
 */

public class ConnectDeviceHelper {

    public void get() {
        RX.getInstant().getConnectedDeviceList(new ResponseObject<ConnectedList>() {
            @Override
            protected void onSuccess(ConnectedList result) {
                devicesSuccessNext(result);
            }

            @Override
            protected void onFailure() {
                devicesFailedNext(null);
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                devicesErrorNext(error);
            }
        });
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
        void devicesError(ResponseBody.Error error);
    }

    // 对外方式setOnDevicesErrorListener
    public void setOnDevicesErrorListener(OnDevicesErrorListener onDevicesErrorListener) {
        this.onDevicesErrorListener = onDevicesErrorListener;
    }

    // 封装方法devicesErrorNext
    private void devicesErrorNext(ResponseBody.Error error) {
        if (onDevicesErrorListener != null) {
            onDevicesErrorListener.devicesError(error);
        }
    }

}
