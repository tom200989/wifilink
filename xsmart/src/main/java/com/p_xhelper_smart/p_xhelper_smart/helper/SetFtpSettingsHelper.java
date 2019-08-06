package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetFtpSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
@SuppressWarnings("unchecked")
public class SetFtpSettingsHelper extends BaseHelper {

    /**
     * 设置FTP配置
     *
     * @param param 配置
     */
    public void setFtpSettings(SetFtpSettingsParam param) {
        prepareHelperNext();
        XSmart xSetFtpSettings = new XSmart();
        xSetFtpSettings.xParam(param);
        xSetFtpSettings.xMethod(XCons.METHOD_SET_FTP_SETTINGS);
        xSetFtpSettings.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                SetFtpSettingsSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                SetFtpSettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                SetFtpSettingsFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnSetFtpSettingsSuccessListener onSetFtpSettingsSuccessListener;

    // Inteerface--> 接口OnSetFtpSettingsSuccessListener
    public interface OnSetFtpSettingsSuccessListener {
        void SetFtpSettingsSuccess();
    }

    // 对外方式setOnSetFtpSettingsSuccessListener
    public void setOnSetFtpSettingsSuccessListener(OnSetFtpSettingsSuccessListener onSetFtpSettingsSuccessListener) {
        this.onSetFtpSettingsSuccessListener = onSetFtpSettingsSuccessListener;
    }

    // 封装方法SetFtpSettingsSuccessNext
    private void SetFtpSettingsSuccessNext() {
        if (onSetFtpSettingsSuccessListener != null) {
            onSetFtpSettingsSuccessListener.SetFtpSettingsSuccess();
        }
    }

    private OnSetFtpSettingsFailedListener onSetFtpSettingsFailedListener;

    // Inteerface--> 接口OnSetFtpSettingsFailedListener
    public interface OnSetFtpSettingsFailedListener {
        void SetFtpSettingsFailed();
    }

    // 对外方式setOnSetFtpSettingsFailedListener
    public void setOnSetFtpSettingsFailedListener(OnSetFtpSettingsFailedListener onSetFtpSettingsFailedListener) {
        this.onSetFtpSettingsFailedListener = onSetFtpSettingsFailedListener;
    }

    // 封装方法SetFtpSettingsFailedNext
    private void SetFtpSettingsFailedNext() {
        if (onSetFtpSettingsFailedListener != null) {
            onSetFtpSettingsFailedListener.SetFtpSettingsFailed();
        }
    }
}
