package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.p_xhelper_smart.p_xhelper_smart.bean.ConnectHotspotParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.ConnectHotspotHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.DisConnectHotspotHelper;

/**
 * Created by qianli.ma on 2018/5/24 0024.
 */

public class Extender_ConnectHotspotHelper {
    
    public void connect(String HotspotId, String SSID, String Key, int SecurityMode, int Hidden) {

        ConnectHotspotParam xConnectHotspotParam = new ConnectHotspotParam();
        xConnectHotspotParam.setHidden(Hidden);
        xConnectHotspotParam.setHotspotId(HotspotId);
        xConnectHotspotParam.setKey(Key);
        xConnectHotspotParam.setSecurityMode(SecurityMode);
        xConnectHotspotParam.setSSID(SSID);

        ConnectHotspotHelper xConnectHotspotHelper = new ConnectHotspotHelper();
        xConnectHotspotHelper.setOnConnectHotSpotFailListener(() -> {
            failedNext(null);
            resultErrorNext(null);
        });
        xConnectHotspotHelper.setOnConnectHotSpotSuccessListener(this::successNext);
        xConnectHotspotHelper.connectHotSpot(xConnectHotspotParam);
    }

    private OnResultErrorListener onResultErrorListener;

    // 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError(ResponseBody.Error attr);
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNext
    private void resultErrorNext(ResponseBody.Error attr) {
        if (onResultErrorListener != null) {
            onResultErrorListener.resultError(attr);
        }
    }

    private OnFailedListener onFailedListener;

    // 接口OnFailedListener
    public interface OnFailedListener {
        void failed(Object attr);
    }

    // 对外方式setOnFailedListener
    public void setOnFailedListener(OnFailedListener onFailedListener) {
        this.onFailedListener = onFailedListener;
    }

    // 封装方法failedNext
    private void failedNext(Object attr) {
        if (onFailedListener != null) {
            onFailedListener.failed(attr);
        }
    }

    private OnSuccessListener onSuccessListener;

    // 接口OnSuccessListener
    public interface OnSuccessListener {
        void success(Object attr);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(Object attr) {
        if (onSuccessListener != null) {
            onSuccessListener.success(attr);
        }
    }
}
