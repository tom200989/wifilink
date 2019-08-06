package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetWanSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
@SuppressWarnings("unchecked")
public class SetWanSettingsHelper extends BaseHelper {

    /**
     * 设置WAN口配置
     *
     * @param param 配置
     */
    public void setWanSettings(SetWanSettingsParam param) {
        prepareHelperNext();
        XSmart xSetWanSettings = new XSmart();
        xSetWanSettings.xMethod(XCons.METHOD_SET_WAN_SETTINGS);
        xSetWanSettings.xParam(param);
        xSetWanSettings.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                SetWanSettingsSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                SetWanSettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                SetWanSettingsFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnSetWanSettingsSuccessListener onSetWanSettingsSuccessListener;

    // Inteerface--> 接口OnSetWanSettingsSuccessListener
    public interface OnSetWanSettingsSuccessListener {
        void SetWanSettingsSuccess();
    }

    // 对外方式setOnSetWanSettingsSuccessListener
    public void setOnSetWanSettingsSuccessListener(OnSetWanSettingsSuccessListener onSetWanSettingsSuccessListener) {
        this.onSetWanSettingsSuccessListener = onSetWanSettingsSuccessListener;
    }

    // 封装方法SetWanSettingsSuccessNext
    private void SetWanSettingsSuccessNext() {
        if (onSetWanSettingsSuccessListener != null) {
            onSetWanSettingsSuccessListener.SetWanSettingsSuccess();
        }
    }

    private OnSetWanSettingsFailedListener onSetWanSettingsFailedListener;

    // Inteerface--> 接口OnSetWanSettingsFailedListener
    public interface OnSetWanSettingsFailedListener {
        void SetWanSettingsFailed();
    }

    // 对外方式setOnSetWanSettingsFailedListener
    public void setOnSetWanSettingsFailedListener(OnSetWanSettingsFailedListener onSetWanSettingsFailedListener) {
        this.onSetWanSettingsFailedListener = onSetWanSettingsFailedListener;
    }

    // 封装方法SetWanSettingsFailedNext
    private void SetWanSettingsFailedNext() {
        if (onSetWanSettingsFailedListener != null) {
            onSetWanSettingsFailedListener.SetWanSettingsFailed();
        }
    }
}
