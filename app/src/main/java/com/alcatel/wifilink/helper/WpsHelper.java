package com.alcatel.wifilink.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetWlanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWlanSettingsHelper;

/**
 * Created by qianli.ma on 2017/11/27 0027.
 */

public class WpsHelper {

    /**
     * 获取WPS状态
     */
    public void getWpsStatus() {

        GetWlanSettingsHelper xGetWlanSettingsHelper = new GetWlanSettingsHelper();
        xGetWlanSettingsHelper.setOnGetWlanSettingsSuccessListener(settingsBean -> {
            
            boolean isWPS = false;// 默认WPS不启动
            int WPS_STATUS = GetWlanSettingsBean.CONS_AP_STATUS_WPS;// WPS状态(2)
            
            // 获取各个类型的WPS状态
            GetWlanSettingsBean.AP2GBean ap2G = settingsBean.getAP2G();
            GetWlanSettingsBean.AP2GGuestBean ap2G_guest = settingsBean.getAP2G_guest();
            GetWlanSettingsBean.AP5GBean ap5G = settingsBean.getAP5G();
            GetWlanSettingsBean.AP5GGuestBean ap5G_guest = settingsBean.getAP5G_guest();
            
            // 检测2.4G下的WPS状态
            if (ap2G != null) {
                isWPS = ap2G.getApStatus() == WPS_STATUS;
            }

            // 检测2.4G-guest下的WPS状态
            if (ap2G_guest != null) {
                isWPS = ap2G_guest.getApStatus() == WPS_STATUS;
            }

            // 检测5G下的WPS状态
            if (ap5G != null) {
                isWPS = ap5G.getApStatus() == WPS_STATUS;
            }

            // 检测5G-guest下的WPS状态
            if (ap5G_guest != null) {
                isWPS = ap5G_guest.getApStatus() == WPS_STATUS;
            }

            // 回调
            getWPSSuccessNext(isWPS);
        });
        
        xGetWlanSettingsHelper.setOnGetWlanSettingsFailedListener(this::getWPSFailedNext);
        xGetWlanSettingsHelper.getWlanSettings();
    }

    /* -------------------------------------------- interface -------------------------------------------- */

    private OnGetWPSSuccessListener onGetWPSSuccessListener;

    // 接口OnWlansettingListener
    public interface OnGetWPSSuccessListener {
        void getWPSSuccess(boolean isWPS);
    }

    // 对外方式setOnWlansettingListener
    public void setOnGetWPSSuccessListener(OnGetWPSSuccessListener onGetWPSSuccessListener) {
        this.onGetWPSSuccessListener = onGetWPSSuccessListener;
    }

    // 封装方法getWlansettingNext
    private void getWPSSuccessNext(boolean result) {
        if (onGetWPSSuccessListener != null) {
            onGetWPSSuccessListener.getWPSSuccess(result);
        }
    }

    private OnGetWPSFailedListener onGetWPSFailedListener;

    // Inteerface--> 接口OnGetWPSFailedListener
    public interface OnGetWPSFailedListener {
        void getWPSFailed();
    }

    // 对外方式setOnGetWPSFailedListener
    public void setOnGetWPSFailedListener(OnGetWPSFailedListener onGetWPSFailedListener) {
        this.onGetWPSFailedListener = onGetWPSFailedListener;
    }

    // 封装方法getWPSFailedNext
    private void getWPSFailedNext() {
        if (onGetWPSFailedListener != null) {
            onGetWPSFailedListener.getWPSFailed();
        }
    }
}
