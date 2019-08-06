package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetWlanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
public class GetWlanSettingsHelper extends BaseHelper {

    /**
     * 获取wlan设置
     */
    public void getWlanSettings() {
        prepareHelperNext();
        XSmart<GetWlanSettingsBean> xWlanSetting = new XSmart<>();
        xWlanSetting.xMethod(XCons.METHOD_GET_WLAN_SETTINGS);
        xWlanSetting.xPost(new XNormalCallback<GetWlanSettingsBean>() {
            @Override
            public void success(GetWlanSettingsBean result) {
                GetWlanSettingsSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetWlanSettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetWlanSettingsFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetWlanSettingsSuccessListener onGetWlanSettingsSuccessListener;

    // Inteerface--> 接口OnGetWlanSettingsSuccessListener
    public interface OnGetWlanSettingsSuccessListener {
        void GetWlanSettingsSuccess(GetWlanSettingsBean settingsBean);
    }

    // 对外方式setOnGetWlanSettingsSuccessListener
    public void setOnGetWlanSettingsSuccessListener(OnGetWlanSettingsSuccessListener onGetWlanSettingsSuccessListener) {
        this.onGetWlanSettingsSuccessListener = onGetWlanSettingsSuccessListener;
    }

    // 封装方法GetWlanSettingsSuccessNext
    private void GetWlanSettingsSuccessNext(GetWlanSettingsBean settingsBean) {
        if (onGetWlanSettingsSuccessListener != null) {
            onGetWlanSettingsSuccessListener.GetWlanSettingsSuccess(settingsBean);
        }
    }

    private OnGetWlanSettingsFailedListener onGetWlanSettingsFailedListener;

    // Inteerface--> 接口OnGetWlanSettingsFailedListener
    public interface OnGetWlanSettingsFailedListener {
        void GetWlanSettingsFailed();
    }

    // 对外方式setOnGetWlanSettingsFailedListener
    public void setOnGetWlanSettingsFailedListener(OnGetWlanSettingsFailedListener onGetWlanSettingsFailedListener) {
        this.onGetWlanSettingsFailedListener = onGetWlanSettingsFailedListener;
    }

    // 封装方法GetWlanSettingsFailedNext
    private void GetWlanSettingsFailedNext() {
        if (onGetWlanSettingsFailedListener != null) {
            onGetWlanSettingsFailedListener.GetWlanSettingsFailed();
        }
    }
}
