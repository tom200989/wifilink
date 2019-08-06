package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetDLNASettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class SetDLNASettingsHelper extends BaseHelper {

    public void setDLNASettings(SetDLNASettingsParam param) {
        prepareHelperNext();
        XSmart xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_SET_DLNA_SETTINGS).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                setDLnaSettingsSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                setDLnaSettingsSuccessNext();
            }

            @Override
            public void fwError(FwError fwError) {
                setDLNASettingsFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------设置成功的回调------------------------------*/
    public interface OnSetDLNASettingsSuccessListener {
        void setDlNASettingsSuccess();
    }

    private OnSetDLNASettingsSuccessListener onSetDLNASettingsSuccessListener;

    //对外方式setOnSetDLNASettingsSuccessListener
    public void setOnSetDLNASettingsSuccessListener(OnSetDLNASettingsSuccessListener onSetDLNASettingsSuccessListener) {
        this.onSetDLNASettingsSuccessListener = onSetDLNASettingsSuccessListener;
    }

    //封装方法setDLnaSettingsSuccessNext
    private void setDLnaSettingsSuccessNext() {
        if (onSetDLNASettingsSuccessListener != null) {
            onSetDLNASettingsSuccessListener.setDlNASettingsSuccess();
        }
    }

    /*----------------------------------设置失败的回调------------------------------*/
    public interface OnSetDLNASettiingsFailListener {
        void setDLNASettingsFail();
    }

    private OnSetDLNASettiingsFailListener onSetDLNASettiingsFailListener;

    //对外方式setOnSetDLNASettiingsFailListener
    public void setOnSetDLNASettiingsFailListener(OnSetDLNASettiingsFailListener onSetDLNASettiingsFailListener) {
        this.onSetDLNASettiingsFailListener = onSetDLNASettiingsFailListener;
    }

    //封装方法setDLNASettingsFailNext
    private void setDLNASettingsFailNext() {
        if (onSetDLNASettiingsFailListener != null) {
            onSetDLNASettiingsFailListener.setDLNASettingsFail();
        }
    }

}
