package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetWIFIExtenderSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class SetWIFIExtenderSettingsHelper extends BaseHelper{


    public void setWifiExSettings(int stationEnable) {
        prepareHelperNext();
        SetWIFIExtenderSettingsParam param = new SetWIFIExtenderSettingsParam(stationEnable);
        XSmart xSmart = new XSmart();
        xSmart.xMethod(XCons.METHOD_SET_WIFI_EXTENDER_SETTINGS).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                setWifiExSettingSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                setWifiExSettingsFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                setWifiExSettingsFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------设置外部扩展成功回调------------------------------*/
    public interface OnSetWifiExSettingsSuccessListener {
        void setWifiExSettingSuccess();
    }

    private OnSetWifiExSettingsSuccessListener onSetWifiExSettingsSuccessListener;

    //对外方式setOnSetWifiExSettingsSuccessListener
    public void setOnSetWifiExSettingsSuccessListener(OnSetWifiExSettingsSuccessListener onSetWifiExSettingsSuccessListener) {
        this.onSetWifiExSettingsSuccessListener = onSetWifiExSettingsSuccessListener;
    }

    //封装方法setWifiExSettingSuccessNext
    private void setWifiExSettingSuccessNext() {
        if (onSetWifiExSettingsSuccessListener != null) {
            onSetWifiExSettingsSuccessListener.setWifiExSettingSuccess();
        }
    }

    /*----------------------------------设置外部扩展失败回调------------------------------*/
    public interface OnSetWifiExSettingFailListener {
        void setWifiExSettingsFail();
    }

    private OnSetWifiExSettingFailListener onSetWifiExSettingFailListener;

    //对外方式setOnSetWifiExSettingFailListener
    public void setOnSetWifiExSettingFailListener(OnSetWifiExSettingFailListener onSetWifiExSettingFailListener) {
        this.onSetWifiExSettingFailListener = onSetWifiExSettingFailListener;
    }

    //封装方法setWifiExSettingsFailNext
    private void setWifiExSettingsFailNext() {
        if (onSetWifiExSettingFailListener != null) {
            onSetWifiExSettingFailListener.setWifiExSettingsFail();
        }
    }

}
