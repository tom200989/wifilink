package com.alcatel.wifilink.helper;

import com.p_xhelper_smart.p_xhelper_smart.helper.DisConnectHotspotHelper;

/**
 * Created by qianli.ma on 2018/5/24 0024.
 */

public class Extender_DisConnectHotspotHelper {
    public void disconnect() {
        DisConnectHotspotHelper xDisConnectHotspotHelper = new DisConnectHotspotHelper();
        xDisConnectHotspotHelper.setOnDisConnectHotSpotSuccessListener(() -> successNext(null));
        xDisConnectHotspotHelper.setOnDisConnectHotSpotFailListener(this::disconnectFailedNext);
        xDisConnectHotspotHelper.disConnectHotspot();
    }

    private OnDisconnectFailedListener onDisconnectFailedListener;

    // Inteerface--> 接口OnDisconnectFailedListener
    public interface OnDisconnectFailedListener {
        void disconnectFailed();
    }

    // 对外方式setOnDisconnectFailedListener
    public void setOnDisconnectFailedListener(OnDisconnectFailedListener onDisconnectFailedListener) {
        this.onDisconnectFailedListener = onDisconnectFailedListener;
    }

    // 封装方法disconnectFailedNext
    private void disconnectFailedNext() {
        if (onDisconnectFailedListener != null) {
            onDisconnectFailedListener.disconnectFailed();
        }
    }

    private OnSuccessListener onSuccessListener;

    // 接口OnSuccessListener
    public interface OnSuccessListener {
        void success(Object object);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(Object result) {
        if (onSuccessListener != null) {
            onSuccessListener.success(result);
        }
    }
}
