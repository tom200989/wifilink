package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
public class GetNetworkSettingsBeanHelper extends BaseHelper {

    /**
     * 获取网络设置
     */
    public void getNetworkSettingsBean() {
        prepareHelperNext();
        XSmart<GetNetworkSettingsBean> xNetworkSetting = new XSmart<>();
        xNetworkSetting.xMethod(XCons.METHOD_GET_NETWORK_SETTINGS);
        xNetworkSetting.xPost(new XNormalCallback<GetNetworkSettingsBean>() {
            @Override
            public void success(GetNetworkSettingsBean result) {
                GetNetworkSettingsSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetNetworkSettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetNetworkSettingsFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetNetworkSettingsSuccessListener onGetNetworkSettingsSuccessListener;

    // Inteerface--> 接口OnGetNetworkSettingsSuccessListener
    public interface OnGetNetworkSettingsSuccessListener {
        void GetNetworkSettingsSuccess(GetNetworkSettingsBean attr);
    }

    // 对外方式setOnGetNetworkSettingsSuccessListener
    public void setOnGetNetworkSettingsSuccessListener(OnGetNetworkSettingsSuccessListener onGetNetworkSettingsSuccessListener) {
        this.onGetNetworkSettingsSuccessListener = onGetNetworkSettingsSuccessListener;
    }

    // 封装方法GetNetworkSettingsSuccessNext
    private void GetNetworkSettingsSuccessNext(GetNetworkSettingsBean attr) {
        if (onGetNetworkSettingsSuccessListener != null) {
            onGetNetworkSettingsSuccessListener.GetNetworkSettingsSuccess(attr);
        }
    }

    private OnGetNetworkSettingsFailedListener onGetNetworkSettingsFailedListener;

    // Inteerface--> 接口OnGetNetworkSettingsFailedListener
    public interface OnGetNetworkSettingsFailedListener {
        void GetNetworkSettingsFailed();
    }

    // 对外方式setOnGetNetworkSettingsFailedListener
    public void setOnGetNetworkSettingsFailedListener(OnGetNetworkSettingsFailedListener onGetNetworkSettingsFailedListener) {
        this.onGetNetworkSettingsFailedListener = onGetNetworkSettingsFailedListener;
    }

    // 封装方法GetNetworkSettingsFailedNext
    private void GetNetworkSettingsFailedNext() {
        if (onGetNetworkSettingsFailedListener != null) {
            onGetNetworkSettingsFailedListener.GetNetworkSettingsFailed();
        }
    }
}
