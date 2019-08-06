package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectionSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class GetConnectionSettingsHelper extends BaseHelper {

    /**
     * 获取连接配置
     */
    public void getConnectionSettings() {
        prepareHelperNext();
        XSmart<GetConnectionSettingsBean> xConnSetting = new XSmart<>();
        xConnSetting.xMethod(XCons.METHOD_GET_CONNECTION_SETTINGS);
        xConnSetting.xPost(new XNormalCallback<GetConnectionSettingsBean>() {
            @Override
            public void success(GetConnectionSettingsBean result) {
                GetConnectionSettingsSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetConnectionSettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetConnectionSettingsFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetConnectionSettingsSuccessListener onGetConnectionSettingsSuccessListener;

    // Inteerface--> 接口OnGetConnectionSettingsSuccessListener
    public interface OnGetConnectionSettingsSuccessListener {
        void GetConnectionSettingsSuccess(GetConnectionSettingsBean attr);
    }

    // 对外方式setOnGetConnectionSettingsSuccessListener
    public void setOnGetConnectionSettingsSuccessListener(OnGetConnectionSettingsSuccessListener onGetConnectionSettingsSuccessListener) {
        this.onGetConnectionSettingsSuccessListener = onGetConnectionSettingsSuccessListener;
    }

    // 封装方法GetConnectionSettingsSuccessNext
    private void GetConnectionSettingsSuccessNext(GetConnectionSettingsBean attr) {
        if (onGetConnectionSettingsSuccessListener != null) {
            onGetConnectionSettingsSuccessListener.GetConnectionSettingsSuccess(attr);
        }
    }

    private OnGetConnectionSettingsFailedListener onGetConnectionSettingsFailedListener;

    // Inteerface--> 接口OnGetConnectionSettingsFailedListener
    public interface OnGetConnectionSettingsFailedListener {
        void GetConnectionSettingsFailed();
    }

    // 对外方式setOnGetConnectionSettingsFailedListener
    public void setOnGetConnectionSettingsFailedListener(OnGetConnectionSettingsFailedListener onGetConnectionSettingsFailedListener) {
        this.onGetConnectionSettingsFailedListener = onGetConnectionSettingsFailedListener;
    }

    // 封装方法GetConnectionSettingsFailedNext
    private void GetConnectionSettingsFailedNext() {
        if (onGetConnectionSettingsFailedListener != null) {
            onGetConnectionSettingsFailedListener.GetConnectionSettingsFailed();
        }
    }
}
