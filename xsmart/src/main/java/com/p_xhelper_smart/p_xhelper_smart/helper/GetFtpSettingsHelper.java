package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetFtpSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
public class GetFtpSettingsHelper extends BaseHelper {

    /**
     * 获取FTP配置
     */
    public void getFtpSettings() {
        prepareHelperNext();
        XSmart<GetFtpSettingsBean> xGetFtpSettings = new XSmart<>();
        xGetFtpSettings.xMethod(XCons.METHOD_GET_FTP_SETTINGS);
        xGetFtpSettings.xPost(new XNormalCallback<GetFtpSettingsBean>() {
            @Override
            public void success(GetFtpSettingsBean result) {
                GetFtpSettingsSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetFtpSettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetFtpSettingsFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetFtpSettingsSuccessListener onGetFtpSettingsSuccessListener;

    // Inteerface--> 接口OnGetFtpSettingsSuccessListener
    public interface OnGetFtpSettingsSuccessListener {
        void GetFtpSettingsSuccess(GetFtpSettingsBean attr);
    }

    // 对外方式setOnGetFtpSettingsSuccessListener
    public void setOnGetFtpSettingsSuccessListener(OnGetFtpSettingsSuccessListener onGetFtpSettingsSuccessListener) {
        this.onGetFtpSettingsSuccessListener = onGetFtpSettingsSuccessListener;
    }

    // 封装方法GetFtpSettingsSuccessNext
    private void GetFtpSettingsSuccessNext(GetFtpSettingsBean attr) {
        if (onGetFtpSettingsSuccessListener != null) {
            onGetFtpSettingsSuccessListener.GetFtpSettingsSuccess(attr);
        }
    }

    private OnGetFtpSettingsFailedListener onGetFtpSettingsFailedListener;

    // Inteerface--> 接口OnGetFtpSettingsFailedListener
    public interface OnGetFtpSettingsFailedListener {
        void GetFtpSettingsFailed();
    }

    // 对外方式setOnGetFtpSettingsFailedListener
    public void setOnGetFtpSettingsFailedListener(OnGetFtpSettingsFailedListener onGetFtpSettingsFailedListener) {
        this.onGetFtpSettingsFailedListener = onGetFtpSettingsFailedListener;
    }

    // 封装方法GetFtpSettingsFailedNext
    private void GetFtpSettingsFailedNext() {
        if (onGetFtpSettingsFailedListener != null) {
            onGetFtpSettingsFailedListener.GetFtpSettingsFailed();
        }
    }
}
