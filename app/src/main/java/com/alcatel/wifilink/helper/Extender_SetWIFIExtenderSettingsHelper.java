package com.alcatel.wifilink.helper;

import com.p_xhelper_smart.p_xhelper_smart.helper.SetWIFIExtenderSettingsHelper;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_SetWIFIExtenderSettingsHelper {
    private String TAG = "Extender_SetWIFIExtenderSettingsHelper";

    public void set(int StationEnable) {
        SetWIFIExtenderSettingsHelper xSetWIFIExtenderSettingsHelper = new SetWIFIExtenderSettingsHelper();
        xSetWIFIExtenderSettingsHelper.setOnSetWifiExSettingsSuccessListener(this::successNext);
        xSetWIFIExtenderSettingsHelper.setOnSetWifiExSettingFailListener(this::Extender_SetWIFIExtenderSettingFailNext);
        xSetWIFIExtenderSettingsHelper.setWifiExSettings(StationEnable);
    }

    public interface OnExtender_SetWIFIExtenderSettingFailListener {
        void Extender_SetWIFIExtenderSettingFail();
    }

    private OnExtender_SetWIFIExtenderSettingFailListener onExtender_setWIFIExtenderSettingFailListener;

    //对外方式setOnExtender_SetWIFIExtenderSettingFailListener
    public void setOnExtender_SetWIFIExtenderSettingFailListener(OnExtender_SetWIFIExtenderSettingFailListener onExtender_setWIFIExtenderSettingFailListener) {
        this.onExtender_setWIFIExtenderSettingFailListener = onExtender_setWIFIExtenderSettingFailListener;
    }

    //封装方法
    private void Extender_SetWIFIExtenderSettingFailNext() {
        if (onExtender_setWIFIExtenderSettingFailListener != null) {
            onExtender_setWIFIExtenderSettingFailListener.Extender_SetWIFIExtenderSettingFail();
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
