package com.alcatel.wifilink.root.helper;

import android.content.Context;

import com.alcatel.wifilink.root.bean.ConnectedList;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.utils.Logs;

/**
 * Created by qianli.ma on 2018/4/11 0011.
 */

public class DeviceHelper {
    private Context context;

    public DeviceHelper(Context context) {
        this.context = context;
    }

    public void getDeviecs() {
        RX.getInstant().getConnectedDeviceList(new ResponseObject<ConnectedList>() {
            @Override
            protected void onSuccess(ConnectedList connectedList) {
                Logs.t("ma_device").ii("devices size: " + connectedList.getConnectedList().size());
                devicesNext(connectedList);
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                Logs.t("ma_device").ee("ma devices resultError: " + error.getMessage());
                resuletErrorNext(error);
            }

            @Override
            public void onError(Throwable e) {
                Logs.t("ma_device").ee("ma devices error: " + e.getMessage());
                errorNext(e);
            }
        });
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
