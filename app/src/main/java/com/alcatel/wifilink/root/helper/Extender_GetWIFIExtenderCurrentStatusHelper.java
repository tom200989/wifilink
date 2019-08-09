package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.Extender_GetWIFIExtenderCurrentStatusResult;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWIFIExtenderCurrentStatusHelper;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_GetWIFIExtenderCurrentStatusHelper {
    public void get() {
        GetWIFIExtenderCurrentStatusHelper xGetWIFIExtenderCurrentStatusHelper = new GetWIFIExtenderCurrentStatusHelper();
        xGetWIFIExtenderCurrentStatusHelper.setOnGetWifiExCurStatusSuccessListener(bean -> {
            Extender_GetWIFIExtenderCurrentStatusResult extenderGetWIFIExtenderCurrentStatusResult = new Extender_GetWIFIExtenderCurrentStatusResult();
            extenderGetWIFIExtenderCurrentStatusResult.setHotspotConnectStatus(bean.getHotspotConnectStatus());
            extenderGetWIFIExtenderCurrentStatusResult.setHotspotSSID(bean.getHotspotSSID());
            extenderGetWIFIExtenderCurrentStatusResult.setIPV4Addr(bean.getIPV4Addr());
            extenderGetWIFIExtenderCurrentStatusResult.setIPV6Addr(bean.getIPV6Addr());
            extenderGetWIFIExtenderCurrentStatusResult.setSignal(bean.getSignal());
            successNext(extenderGetWIFIExtenderCurrentStatusResult);
        });
        xGetWIFIExtenderCurrentStatusHelper.setOnGetWifiExCurStatusFailListener(() -> {
            failedNext(null);
            resultErrorNext(null);
        });
        xGetWIFIExtenderCurrentStatusHelper.getWIFIExtenderCurrentStatus();
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
    private void resultErrorNext(ResponseBody.Error error) {
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
        void success(Extender_GetWIFIExtenderCurrentStatusResult result);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(Extender_GetWIFIExtenderCurrentStatusResult result) {
        if (onSuccessListener != null) {
            onSuccessListener.success(result);
        }
    }
}
