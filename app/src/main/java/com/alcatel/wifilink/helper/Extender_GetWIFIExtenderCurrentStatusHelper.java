package com.alcatel.wifilink.helper;

import com.alcatel.wifilink.bean.Extender_GetWIFICurrentStatuBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWIFIExtenderCurrentStatusHelper;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_GetWIFIExtenderCurrentStatusHelper {
    public void get() {
        GetWIFIExtenderCurrentStatusHelper xGetWIFIExtenderCurrentStatusHelper = new GetWIFIExtenderCurrentStatusHelper();
        xGetWIFIExtenderCurrentStatusHelper.setOnGetWifiExCurStatusSuccessListener(bean -> {
            Extender_GetWIFICurrentStatuBean extenderGetWIFIExtenderCurrentStatusResult = new Extender_GetWIFICurrentStatuBean();
            extenderGetWIFIExtenderCurrentStatusResult.setHotspotConnectStatus(bean.getHotspotConnectStatus());
            extenderGetWIFIExtenderCurrentStatusResult.setHotspotSSID(bean.getHotspotSSID());
            extenderGetWIFIExtenderCurrentStatusResult.setIPV4Addr(bean.getIPV4Addr());
            extenderGetWIFIExtenderCurrentStatusResult.setIPV6Addr(bean.getIPV6Addr());
            extenderGetWIFIExtenderCurrentStatusResult.setSignal(bean.getSignal());
            extenderGetWIFIExtenderCurrentStatusSuccessNext(extenderGetWIFIExtenderCurrentStatusResult);
        });
        xGetWIFIExtenderCurrentStatusHelper.setOnGetWifiExCurStatusFailListener(this::extenderGetWIFIExtenderCurrentStatusFailedNext);
        xGetWIFIExtenderCurrentStatusHelper.getWIFIExtenderCurrentStatus();
    }

    /* ************************** Success ***************************** */

    private OnExtenderGetWIFIExtenderCurrentStatusSuccessListener onExtenderGetWIFIExtenderCurrentStatusSuccessListener;

    // 接口OnExtenderGetWIFIExtenderCurrentStatusSuccessListener
    public interface OnExtenderGetWIFIExtenderCurrentStatusSuccessListener {
        void extenderGetWIFIExtenderCurrentStatusSuccess(Extender_GetWIFICurrentStatuBean result);
    }

    // 对外方式setOnExtenderGetWIFIExtenderCurrentStatusSuccessListener
    public void setOnExtenderGetWIFIExtenderCurrentStatusSuccessListener(OnExtenderGetWIFIExtenderCurrentStatusSuccessListener onExtenderGetWIFIExtenderCurrentStatusSuccessListener) {
        this.onExtenderGetWIFIExtenderCurrentStatusSuccessListener = onExtenderGetWIFIExtenderCurrentStatusSuccessListener;
    }

    // 封装方法ExtenderGetWIFIExtenderCurrentStatusSuccessNext
    private void extenderGetWIFIExtenderCurrentStatusSuccessNext(Extender_GetWIFICurrentStatuBean result) {
        if (onExtenderGetWIFIExtenderCurrentStatusSuccessListener != null) {
            onExtenderGetWIFIExtenderCurrentStatusSuccessListener.extenderGetWIFIExtenderCurrentStatusSuccess(result);
        }
    }

    /* ************************** Failed ***************************** */

    private OnExtenderGetWIFIExtenderCurrentStatusFailedListener onExtenderGetWIFIExtenderCurrentStatusFailedListener;

    // 接口OnExtenderGetWIFIExtenderCurrentStatusFailedListener
    public interface OnExtenderGetWIFIExtenderCurrentStatusFailedListener {
        void extenderGetWIFIExtenderCurrentStatusFailed();
    }

    // 对外方式setOnExtenderGetWIFIExtenderCurrentStatusFailedListener
    public void setOnExtenderGetWIFIExtenderCurrentStatusFailedListener(OnExtenderGetWIFIExtenderCurrentStatusFailedListener onExtenderGetWIFIExtenderCurrentStatusFailedListener) {
        this.onExtenderGetWIFIExtenderCurrentStatusFailedListener = onExtenderGetWIFIExtenderCurrentStatusFailedListener;
    }

    // 封装方法ExtenderGetWIFIExtenderCurrentStatusFailedNext
    private void extenderGetWIFIExtenderCurrentStatusFailedNext() {
        if (onExtenderGetWIFIExtenderCurrentStatusFailedListener != null) {
            onExtenderGetWIFIExtenderCurrentStatusFailedListener.extenderGetWIFIExtenderCurrentStatusFailed();
        }
    }
}
