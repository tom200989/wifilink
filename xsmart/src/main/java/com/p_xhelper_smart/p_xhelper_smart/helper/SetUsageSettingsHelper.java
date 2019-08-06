package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetUsageSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class SetUsageSettingsHelper extends BaseHelper {

    public void setUsageSettings(SetUsageSettingsParam param) {
        prepareHelperNext();
        XSmart xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_SET_USAGE_SETTING).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                setUsageSettingsSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                setUsageSettingsFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                setUsageSettingsFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------设置用户流量设置 的请求成功回调------------------------------*/
    public interface OnSetUsageSettingsSuccessListener {
        void setUsageSettingsSuccess();
    }

    private OnSetUsageSettingsSuccessListener onSetUsageSettingsSuccessListener;

    //对外方式setOnSetSusageSettingsSuccessListener
    public void setOnSetUsageSettingsSuccessListener(OnSetUsageSettingsSuccessListener onSetUsageSettingsSuccessListener) {
        this.onSetUsageSettingsSuccessListener = onSetUsageSettingsSuccessListener;
    }

    //封装方法
    private void setUsageSettingsSuccessNext() {
        if (onSetUsageSettingsSuccessListener != null) {
            onSetUsageSettingsSuccessListener.setUsageSettingsSuccess();
        }
    }

    /*----------------------------------设置用户流量设置的请求失败回调------------------------------*/
    public interface OnSetUsageSettingsFailListener {
        void setUsageSettingsFail();
    }

    private OnSetUsageSettingsFailListener onSetUsageSettingsFailListener;

    //对外方式setOnSetUsageSettingsFailListener
    public void setOnSetUsageSettingsFailListener(OnSetUsageSettingsFailListener onSetUsageSettingsFailListener) {
        this.onSetUsageSettingsFailListener = onSetUsageSettingsFailListener;
    }

    //封装方法setUsageSettingsFailNext
    private void setUsageSettingsFailNext() {
        if (onSetUsageSettingsFailListener != null) {
            onSetUsageSettingsFailListener.setUsageSettingsFail();
        }
    }
}
