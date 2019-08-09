package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.Extender_GetConnectHotspotStateResult;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectHotspotStateBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectHotspotStateHelper;

/**
 * Created by qianli.ma on 2018/5/24 0024.
 */

public class Extender_GetConnectHotspotStateHelper {

    // 0: none
    // 1: connecting
    // 2: success
    // 3: password error
    // 4: need password
    // 5: fail

    public void get() {
        GetConnectHotspotStateHelper xGetConnectHotspotStateHelper = new GetConnectHotspotStateHelper();
        xGetConnectHotspotStateHelper.setOnGetConnectHotSpotStateSuccessListener(bean -> {
            Extender_GetConnectHotspotStateResult extenderGetConnectHotspotStateResult = new Extender_GetConnectHotspotStateResult();
            extenderGetConnectHotspotStateResult.setState(bean.getState());
            successNext(extenderGetConnectHotspotStateResult);
        });
        xGetConnectHotspotStateHelper.setOnGetConnectHotSpotFailListener(() -> {
            failedNext(null);
            resultErrorNext(null);
        });
        xGetConnectHotspotStateHelper.getConnectHotspotState();
    }

    private OnResultErrorListener onResultErrorListener;

    // 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError(ResponseBody.Error error);
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
        void success(Extender_GetConnectHotspotStateResult result);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(Extender_GetConnectHotspotStateResult attr) {
        if (onSuccessListener != null) {
            onSuccessListener.success(attr);
        }
    }
}
