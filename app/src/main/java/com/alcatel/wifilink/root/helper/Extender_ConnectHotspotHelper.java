package com.alcatel.wifilink.root.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.ConnectHotspotParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.ConnectHotspotHelper;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;

/**
 * Created by qianli.ma on 2018/5/24 0024.
 */

public class Extender_ConnectHotspotHelper {
    
    public void connect(String HotspotId, String SSID, String Key, int SecurityMode, int Hidden) {
        //参数
        ConnectHotspotParam xConnectHotspotParam = new ConnectHotspotParam();
        xConnectHotspotParam.setHidden(Hidden);
        xConnectHotspotParam.setHotspotId(HotspotId);
        xConnectHotspotParam.setKey(Key);
        xConnectHotspotParam.setSecurityMode(SecurityMode);
        xConnectHotspotParam.setSSID(SSID);
        //请求
        ConnectHotspotHelper xConnectHotspotHelper = new ConnectHotspotHelper();
        xConnectHotspotHelper.setOnConnectHotSpotFailListener(this::extenderConnectHotspotFailedNext);
        xConnectHotspotHelper.setOnConnectHotSpotSuccessListener(this::extenderConnectHotspotSuccessNext);
        xConnectHotspotHelper.connectHotSpot(xConnectHotspotParam);
    }

    /* ************************** Success ***************************** */

    private OnExtenderConnectHotspotSuccessListener onExtenderConnectHotspotSuccessListener;

    // 接口OnExtenderConnectHotspotSuccessListener
    public interface OnExtenderConnectHotspotSuccessListener {
        void extenderConnectHotspotSuccess(Object attr);
    }

    // 对外方式setOnExtenderConnectHotspotSuccessListener
    public void setOnExtenderConnectHotspotSuccessListener(OnExtenderConnectHotspotSuccessListener onExtenderConnectHotspotSuccessListener) {
        this.onExtenderConnectHotspotSuccessListener = onExtenderConnectHotspotSuccessListener;
    }

    // 封装方法ExtenderConnectHotspotSuccessNext
    private void extenderConnectHotspotSuccessNext(Object attr) {
        if (onExtenderConnectHotspotSuccessListener != null) {
            onExtenderConnectHotspotSuccessListener.extenderConnectHotspotSuccess(attr);
        }
    }

    /* ************************** Failed ***************************** */

    private OnExtenderConnectHotspotFailedListener onExtenderConnectHotspotFailedListener;

    // 接口OnExtenderConnectHotspotFailedListener
    public interface OnExtenderConnectHotspotFailedListener {
        void extenderConnectHotspotFailed();
    }

    // 对外方式setOnExtenderConnectHotspotFailedListener
    public void setOnExtenderConnectHotspotFailedListener(OnExtenderConnectHotspotFailedListener onExtenderConnectHotspotFailedListener) {
        this.onExtenderConnectHotspotFailedListener = onExtenderConnectHotspotFailedListener;
    }

    // 封装方法ExtenderConnectHotspotFailedNext
    private void extenderConnectHotspotFailedNext() {
        if (onExtenderConnectHotspotFailedListener != null) {
            onExtenderConnectHotspotFailedListener.extenderConnectHotspotFailed();
        }
    }
}
