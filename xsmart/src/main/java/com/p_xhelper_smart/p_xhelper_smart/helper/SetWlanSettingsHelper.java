package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetWlanSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
@SuppressWarnings("unchecked")
public class SetWlanSettingsHelper extends BaseHelper {

    /**
     * 设置WLAN配置
     *
     * @param param 配置
     */
    public void setWlanSettings(SetWlanSettingsParam param) {
        prepareHelperNext();
        XSmart xWlanSetting = new XSmart();
        xWlanSetting.xMethod(XCons.METHOD_SET_WLAN_SETTINGS);
        xWlanSetting.xParam(param);
        xWlanSetting.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                SetWlanSettingsSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                SetWlanSettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                SetWlanSettingsFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnSetWlanSettingsSuccessListener onSetWlanSettingsSuccessListener;

    // Inteerface--> 接口OnSetWlanSettingsSuccessListener
    public interface OnSetWlanSettingsSuccessListener {
        void SetWlanSettingsSuccess();
    }

    // 对外方式setOnSetWlanSettingsSuccessListener
    public void setOnSetWlanSettingsSuccessListener(OnSetWlanSettingsSuccessListener onSetWlanSettingsSuccessListener) {
        this.onSetWlanSettingsSuccessListener = onSetWlanSettingsSuccessListener;
    }

    // 封装方法SetWlanSettingsSuccessNext
    private void SetWlanSettingsSuccessNext() {
        if (onSetWlanSettingsSuccessListener != null) {
            onSetWlanSettingsSuccessListener.SetWlanSettingsSuccess();
        }
    }

    private OnSetWlanSettingsFailedListener onSetWlanSettingsFailedListener;

    // Inteerface--> 接口OnSetWlanSettingsFailedListener
    public interface OnSetWlanSettingsFailedListener {
        void SetWlanSettingsFailed();
    }

    // 对外方式setOnSetWlanSettingsFailedListener
    public void setOnSetWlanSettingsFailedListener(OnSetWlanSettingsFailedListener onSetWlanSettingsFailedListener) {
        this.onSetWlanSettingsFailedListener = onSetWlanSettingsFailedListener;
    }

    // 封装方法SetWlanSettingsFailedNext
    private void SetWlanSettingsFailedNext() {
        if (onSetWlanSettingsFailedListener != null) {
            onSetWlanSettingsFailedListener.SetWlanSettingsFailed();
        }
    }
}
