package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetSambaSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
public class GetSambaSettingsHelper extends BaseHelper {

    /**
     * 获取SAMBA配置
     */
    public void getSambaSettings() {
        prepareHelperNext();
        XSmart<GetSambaSettingsBean> xGetSambaSettings = new XSmart<>();
        xGetSambaSettings.xMethod(XCons.METHOD_GET_SAMBA_SETTINGS);
        xGetSambaSettings.xPost(new XNormalCallback<GetSambaSettingsBean>() {
            @Override
            public void success(GetSambaSettingsBean result) {
                GetSambaSettingsSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetSambaSettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetSambaSettingsFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetSambaSettingsSuccessListener onGetSambaSettingsSuccessListener;

    // Inteerface--> 接口OnGetSambaSettingsSuccessListener
    public interface OnGetSambaSettingsSuccessListener {
        void GetSambaSettingsSuccess(GetSambaSettingsBean attr);
    }

    // 对外方式setOnGetSambaSettingsSuccessListener
    public void setOnGetSambaSettingsSuccessListener(OnGetSambaSettingsSuccessListener onGetSambaSettingsSuccessListener) {
        this.onGetSambaSettingsSuccessListener = onGetSambaSettingsSuccessListener;
    }

    // 封装方法GetSambaSettingsSuccessNext
    private void GetSambaSettingsSuccessNext(GetSambaSettingsBean attr) {
        if (onGetSambaSettingsSuccessListener != null) {
            onGetSambaSettingsSuccessListener.GetSambaSettingsSuccess(attr);
        }
    }

    private OnGetSambaSettingsFailedListener onGetSambaSettingsFailedListener;

    // Inteerface--> 接口OnGetSambaSettingsFailedListener
    public interface OnGetSambaSettingsFailedListener {
        void GetSambaSettingsFailed();
    }

    // 对外方式setOnGetSambaSettingsFailedListener
    public void setOnGetSambaSettingsFailedListener(OnGetSambaSettingsFailedListener onGetSambaSettingsFailedListener) {
        this.onGetSambaSettingsFailedListener = onGetSambaSettingsFailedListener;
    }

    // 封装方法GetSambaSettingsFailedNext
    private void GetSambaSettingsFailedNext() {
        if (onGetSambaSettingsFailedListener != null) {
            onGetSambaSettingsFailedListener.GetSambaSettingsFailed();
        }
    }
}
