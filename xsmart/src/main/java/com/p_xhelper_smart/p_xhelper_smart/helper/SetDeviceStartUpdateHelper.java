package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
@SuppressWarnings("unchecked")
public class SetDeviceStartUpdateHelper extends BaseHelper {

    /**
     * 触发FW启动升级
     */
    public void setDeviceStartUpdate() {
        prepareHelperNext();
        XSmart xStartUpdate = new XSmart();
        xStartUpdate.xMethod(XCons.METHOD_SET_DEVICE_START_UPDATE);
        xStartUpdate.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                SetDeviceStartUpdateSuccesNext();
            }

            @Override
            public void appError(Throwable ex) {
                SetDeviceStartUpdateFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                SetDeviceStartUpdateFailedNext();
            }
            
            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnSetDeviceStartUpdateSuccessListener onSetDeviceStartUpdateSuccessListener;

    // Inteerface--> 接口OnSetDeviceStartUpdateSuccessListener
    public interface OnSetDeviceStartUpdateSuccessListener {
        void SetDeviceStartUpdateSuccess();
    }

    // 对外方式setOnSetDeviceStartUpdateSuccessListener
    public void setOnSetDeviceStartUpdateSuccessListener(OnSetDeviceStartUpdateSuccessListener onSetDeviceStartUpdateSuccessListener) {
        this.onSetDeviceStartUpdateSuccessListener = onSetDeviceStartUpdateSuccessListener;
    }

    // 封装方法SetDeviceStartUpdateSuccesNext
    private void SetDeviceStartUpdateSuccesNext() {
        if (onSetDeviceStartUpdateSuccessListener != null) {
            onSetDeviceStartUpdateSuccessListener.SetDeviceStartUpdateSuccess();
        }
    }

    private OnSetDeviceStartUpdateFailedListener onSetDeviceStartUpdateFailedListener;

    // Inteerface--> 接口OnSetDeviceStartUpdateFailedListener
    public interface OnSetDeviceStartUpdateFailedListener {
        void SetDeviceStartUpdateFailed();
    }

    // 对外方式setOnSetDeviceStartUpdateFailedListener
    public void setOnSetDeviceStartUpdateFailedListener(OnSetDeviceStartUpdateFailedListener onSetDeviceStartUpdateFailedListener) {
        this.onSetDeviceStartUpdateFailedListener = onSetDeviceStartUpdateFailedListener;
    }

    // 封装方法SetDeviceStartUpdateFailedNext
    private void SetDeviceStartUpdateFailedNext() {
        if (onSetDeviceStartUpdateFailedListener != null) {
            onSetDeviceStartUpdateFailedListener.SetDeviceStartUpdateFailed();
        }
    }
}
