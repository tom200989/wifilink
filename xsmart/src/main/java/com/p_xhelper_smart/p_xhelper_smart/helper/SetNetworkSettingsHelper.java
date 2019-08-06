package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetNetworkSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
@SuppressWarnings("unchecked")
public class SetNetworkSettingsHelper extends BaseHelper {

    /**
     * 设置网络配置
     *
     * @param param 配置
     */
    public void setNetworkSettings(SetNetworkSettingsParam param) {
        prepareHelperNext();
        XSmart xSetNetworkSetting = new XSmart();
        xSetNetworkSetting.xMethod(XCons.METHOD_SET_NETWORK_SETTINGS);
        xSetNetworkSetting.xParam(param);
        xSetNetworkSetting.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                SetNetworkSettingsSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                SetNetworkSettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                SetNetworkSettingsFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnSetNetworkSettingsSuccessListener onSetNetworkSettingsSuccessListener;

    // Inteerface--> 接口OnSetNetworkSettingsSuccessListener
    public interface OnSetNetworkSettingsSuccessListener {
        void SetNetworkSettingsSuccess();
    }

    // 对外方式setOnSetNetworkSettingsSuccessListener
    public void setOnSetNetworkSettingsSuccessListener(OnSetNetworkSettingsSuccessListener onSetNetworkSettingsSuccessListener) {
        this.onSetNetworkSettingsSuccessListener = onSetNetworkSettingsSuccessListener;
    }

    // 封装方法SetNetworkSettingsSuccessNext
    private void SetNetworkSettingsSuccessNext() {
        if (onSetNetworkSettingsSuccessListener != null) {
            onSetNetworkSettingsSuccessListener.SetNetworkSettingsSuccess();
        }
    }

    private OnSetNetworkSettingsFailedListener onSetNetworkSettingsFailedListener;

    // Inteerface--> 接口OnSetNetworkSettingsFailedListener
    public interface OnSetNetworkSettingsFailedListener {
        void SetNetworkSettingsFailed();
    }

    // 对外方式setOnSetNetworkSettingsFailedListener
    public void setOnSetNetworkSettingsFailedListener(OnSetNetworkSettingsFailedListener onSetNetworkSettingsFailedListener) {
        this.onSetNetworkSettingsFailedListener = onSetNetworkSettingsFailedListener;
    }

    // 封装方法SetNetworkSettingsFailedNext
    private void SetNetworkSettingsFailedNext() {
        if (onSetNetworkSettingsFailedListener != null) {
            onSetNetworkSettingsFailedListener.SetNetworkSettingsFailed();
        }
    }
}
