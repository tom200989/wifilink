package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.network.ResponseBody;
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
            GetWlanSettingsBean.AP2GBean ap2G = settingsBean.getAP2G();
            GetWlanSettingsBean.AP2GGuestBean ap2G_guest = settingsBean.getAP2G_guest();
            GetWlanSettingsBean.AP5GBean ap5G = settingsBean.getAP5G();
            GetWlanSettingsBean.AP5GGuestBean ap5G_guest = settingsBean.getAP5G_guest();
            boolean isWpsWork = false;

            // 检测2.4G下的WPS状态
            if (ap2G != null) {
                int apStatus = ap2G.getApStatus();
                if (apStatus == GetWlanSettingsBean.CONS_AP_STATUS_WPS) {
                    isWpsWork = true;
                }
            }

            // 检测2.4G-guest下的WPS状态
            if (ap2G_guest != null) {
                int apStatus = ap2G_guest.getApStatus();
                if (apStatus == GetWlanSettingsBean.CONS_AP_STATUS_WPS) {
                    isWpsWork = true;
                }
            }

            // 检测5G下的WPS状态
            if (ap5G != null) {
                int apStatus = ap5G.getApStatus();
                if (apStatus ==GetWlanSettingsBean.CONS_AP_STATUS_WPS) {
                    isWpsWork = true;
                }
            }

            // 检测5G-guest下的WPS状态
            if (ap5G_guest != null) {
                int apStatus = ap5G_guest.getApStatus();
                if (apStatus == GetWlanSettingsBean.CONS_AP_STATUS_WPS) {
                    isWpsWork = true;
                }
            }

            // 对接
            getWpsNext(isWpsWork);
        });
        xGetWlanSettingsHelper.setOnGetWlanSettingsFailedListener(() -> {
            resultErrorNext(null);
            errorNext(null);
        });
        xGetWlanSettingsHelper.getWlanSettings();
    }

    /* -------------------------------------------- interface -------------------------------------------- */

    private OnWpsListener onWpsListener;

    // 接口OnWlansettingListener
    public interface OnWpsListener {
        void getWlansetting(boolean attr);
    }

    // 对外方式setOnWlansettingListener
    public void setOnWpsListener(OnWpsListener onWpsListener) {
        this.onWpsListener = onWpsListener;
    }

    // 封装方法getWlansettingNext
    private void getWpsNext(boolean attr) {
        if (onWpsListener != null) {
            onWpsListener.getWlansetting(attr);
        }
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

    private OnErrorListener onErrorListener;

    // 接口OnErrorListener
    public interface OnErrorListener {
        void error(Throwable attr);
    }

    // 对外方式setOnErrorListener
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    // 封装方法errorNext
    private void errorNext(Throwable attr) {
        if (onErrorListener != null) {
            onErrorListener.error(attr);
        }
    }
}
