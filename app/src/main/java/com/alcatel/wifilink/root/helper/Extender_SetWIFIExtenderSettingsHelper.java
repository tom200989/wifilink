package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.utils.Logs;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetWIFIExtenderSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_SetWIFIExtenderSettingsHelper {
    private String TAG = "Extender_SetWIFIExtenderSettingsHelper";

    public void set(int StationEnable) {
        SetWIFIExtenderSettingsHelper xSetWIFIExtenderSettingsHelper = new SetWIFIExtenderSettingsHelper();
        xSetWIFIExtenderSettingsHelper.setOnSetWifiExSettingsSuccessListener(result -> {
            Logs.t(TAG).ii("Extender_SetWIFIExtenderSettingsHelper: set successful");
            successNext(result);
        });
        xSetWIFIExtenderSettingsHelper.setOnSetWifiExSettingFailListener(() -> {
            Logs.t(TAG).ii("Extender_SetWIFIExtenderSettingsHelper: set failed");
            failedNext(null);
            resultErrorNext(null);
        });
        xSetWIFIExtenderSettingsHelper.setWifiExSettings(StationEnable);
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
    private void resultErrorNext(FwError attr) {
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
        void success(Object o);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(Object o) {
        if (onSuccessListener != null) {
            onSuccessListener.success(o);
        }
    }
}
