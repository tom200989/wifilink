package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetDLNASettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
public class GetDLNASettingsHelper extends BaseHelper {


    /**
     * 获取DLNA配置
     */
    public void getDLNASettings() {
        prepareHelperNext();
        XSmart<GetDLNASettingsBean> xGetDLNASettings = new XSmart<>();
        xGetDLNASettings.xMethod(XCons.METHOD_GET_DLNA_SETTINGS);
        xGetDLNASettings.xPost(new XNormalCallback<GetDLNASettingsBean>() {
            @Override
            public void success(GetDLNASettingsBean result) {
                GetDLNASettingsSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetDLNASettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetDLNASettingsFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetDLNASettingsSuccessListener onGetDLNASettingsSuccessListener;

    // Inteerface--> 接口OnGetDLNASettingsSuccessListener
    public interface OnGetDLNASettingsSuccessListener {
        void GetDLNASettingsSuccess(GetDLNASettingsBean attr);
    }

    // 对外方式setOnGetDLNASettingsSuccessListener
    public void setOnGetDLNASettingsSuccessListener(OnGetDLNASettingsSuccessListener onGetDLNASettingsSuccessListener) {
        this.onGetDLNASettingsSuccessListener = onGetDLNASettingsSuccessListener;
    }

    // 封装方法GetDLNASettingsSuccessNext
    private void GetDLNASettingsSuccessNext(GetDLNASettingsBean attr) {
        if (onGetDLNASettingsSuccessListener != null) {
            onGetDLNASettingsSuccessListener.GetDLNASettingsSuccess(attr);
        }
    }

    private OnGetDLNASettingsFailedListener onGetDLNASettingsFailedListener;

    // Inteerface--> 接口OnGetDLNASettingsFailedListener
    public interface OnGetDLNASettingsFailedListener {
        void GetDLNASettingsFailed();
    }

    // 对外方式setOnGetDLNASettingsFailedListener
    public void setOnGetDLNASettingsFailedListener(OnGetDLNASettingsFailedListener onGetDLNASettingsFailedListener) {
        this.onGetDLNASettingsFailedListener = onGetDLNASettingsFailedListener;
    }

    // 封装方法GetDLNASettingsFailedNext
    private void GetDLNASettingsFailedNext() {
        if (onGetDLNASettingsFailedListener != null) {
            onGetDLNASettingsFailedListener.GetDLNASettingsFailed();
        }
    }
}
