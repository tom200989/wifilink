package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetSambaSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class SetSambaSettingsHelper extends BaseHelper {

    public void setSambaSettings(SetSambaSettingsParam param) {
        prepareHelperNext();
        XSmart xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_SET_SAMBA_SETTINGS).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                setSambaSettingsSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                setSambaSettingsFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                setSambaSettingsFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }


    /*----------------------------------设置成功的回调------------------------------*/
    public interface OnSetSambaSettingsSuccessListener {
        void setSambaSettingsSuccess();
    }

    private OnSetSambaSettingsSuccessListener onSetSambaSettingsSuccessListener;

    //对外方式setOnSetSambaSettingsSuccessListener
    public void setOnSetSambaSettingsSuccessListener(OnSetSambaSettingsSuccessListener onSetSambaSettingsSuccessListener) {
        this.onSetSambaSettingsSuccessListener = onSetSambaSettingsSuccessListener;
    }

    //封装方法setSambaSettingsSuccessNext
    private void setSambaSettingsSuccessNext() {
        if (onSetSambaSettingsSuccessListener != null) {
            onSetSambaSettingsSuccessListener.setSambaSettingsSuccess();
        }
    }


    /*----------------------------------设置失败的回调------------------------------*/
    public interface OnSetSambaSettingsFailListener {
        void setSambaSettingsFail();
    }

    private OnSetSambaSettingsFailListener onSetSambaSettingsFailListener;

    //对外方式setOnSetSambaSettingsFailListener
    public void setOnSetSambaSettingsFailListener(OnSetSambaSettingsFailListener onSetSambaSettingsFailListener) {
        this.onSetSambaSettingsFailListener = onSetSambaSettingsFailListener;
    }

    //封装方法setSambaSettingsFailNext
    private void setSambaSettingsFailNext() {
        if (onSetSambaSettingsFailListener != null) {
            onSetSambaSettingsFailListener.setSambaSettingsFail();
        }
    }

}
