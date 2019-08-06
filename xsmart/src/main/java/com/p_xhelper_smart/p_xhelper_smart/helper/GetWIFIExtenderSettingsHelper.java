package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetWIFIExtenderSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetWIFIExtenderSettingsHelper extends BaseHelper {

    /**
     * 获取wifi外部扩展器设置
     */
    public void getWIFIExtenderSettings() {
        prepareHelperNext();
        XSmart<GetWIFIExtenderSettingsBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_WIFI_EXTENDER_SETTINGS).xPost(new XNormalCallback<GetWIFIExtenderSettingsBean>() {
            @Override
            public void success(GetWIFIExtenderSettingsBean result) {
                getWifiExtenderSettingSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getWifiExtenderSettingsFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getWifiExtenderSettingsFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------获取wifi外部扩展器的成功的回调------------------------------*/
    public interface GetWifiExtenderSettingsSuccessListener {
        void getWifiExtenderSettingsSuccess(GetWIFIExtenderSettingsBean bean);
    }

    private GetWifiExtenderSettingsSuccessListener getWifiExtenderSettingsSuccessListener;

    //对外方式setGetWifiExtenderSettingsSuccessListener
    public void setGetWifiExtenderSettingsSuccessListener(GetWifiExtenderSettingsSuccessListener getWifiExtenderSettingsSuccessListener) {
        this.getWifiExtenderSettingsSuccessListener = getWifiExtenderSettingsSuccessListener;
    }

    //封装方法getWifiExtenderSettingSuccessNext
    private void getWifiExtenderSettingSuccessNext(GetWIFIExtenderSettingsBean bean) {
        if (getWifiExtenderSettingsSuccessListener != null) {
            getWifiExtenderSettingsSuccessListener.getWifiExtenderSettingsSuccess(bean);
        }
    }

    /*----------------------------------获取wifi外部扩展器失败的回调------------------------------*/
    public interface GetWifiExtenderSettingsFailListener {
        void getWifiExtenderSettingsFail();
    }

    private GetWifiExtenderSettingsFailListener getWifiExtenderSettingsFailListener;

    //对外方式setGetWifiExtenderSettingsFailListener
    public void setGetWifiExtenderSettingsFailListener(GetWifiExtenderSettingsFailListener getWifiExtenderSettingsFailListener) {
        this.getWifiExtenderSettingsFailListener = getWifiExtenderSettingsFailListener;
    }

    //封装方法getWifiExtenderSettingsFailNext
    private void getWifiExtenderSettingsFailNext() {
        if (getWifiExtenderSettingsFailListener != null) {
            getWifiExtenderSettingsFailListener.getWifiExtenderSettingsFail();
        }
    }



}
