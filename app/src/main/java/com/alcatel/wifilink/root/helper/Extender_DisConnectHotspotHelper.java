package com.alcatel.wifilink.root.helper;

import com.p_xhelper_smart.p_xhelper_smart.helper.DisConnectHotspotHelper;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;

/**
 * Created by qianli.ma on 2018/5/24 0024.
 */

public class Extender_DisConnectHotspotHelper {
    public void disconnect() {
        DisConnectHotspotHelper xDisConnectHotspotHelper = new DisConnectHotspotHelper();
        xDisConnectHotspotHelper.setOnDisConnectHotSpotSuccessListener(() -> successNext(null));
        xDisConnectHotspotHelper.setOnDisConnectHotSpotFailListener(() -> {
            failedNext(null);
            resultErrorNext(null);
        });
        xDisConnectHotspotHelper.disConnectHotspot();
    }

    private OnResultErrorListener onResultErrorListener;

    // 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError(FwError error);
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNext
    private void resultErrorNext(FwError error) {
        if (onResultErrorListener != null) {
            onResultErrorListener.resultError(error);
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
