package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
@SuppressWarnings("unchecked")
public class SetFOTAStartDownloadHelper extends BaseHelper {

    /**
     * 触发FW开始下载
     */
    public void setFOTAStartDownload() {
        prepareHelperNext();
        XSmart xSetFOTAStartDown = new XSmart();
        xSetFOTAStartDown.xMethod(XCons.METHOD_SET_FOTA_START_DOWNLOAD);
        xSetFOTAStartDown.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                SetFOTAStartDownloadSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                SetFOTAStartDownloadFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                SetFOTAStartDownloadFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnSetFOTAStartDownloadSuccessListener onSetFOTAStartDownloadSuccessListener;

    // Inteerface--> 接口OnSetFOTAStartDownloadSuccessListener
    public interface OnSetFOTAStartDownloadSuccessListener {
        void SetFOTAStartDownloadSuccess();
    }

    // 对外方式setOnSetFOTAStartDownloadSuccessListener
    public void setOnSetFOTAStartDownloadSuccessListener(OnSetFOTAStartDownloadSuccessListener onSetFOTAStartDownloadSuccessListener) {
        this.onSetFOTAStartDownloadSuccessListener = onSetFOTAStartDownloadSuccessListener;
    }

    // 封装方法SetFOTAStartDownloadSuccessNext
    private void SetFOTAStartDownloadSuccessNext() {
        if (onSetFOTAStartDownloadSuccessListener != null) {
            onSetFOTAStartDownloadSuccessListener.SetFOTAStartDownloadSuccess();
        }
    }

    private OnSetFOTAStartDownloadFailedListener onSetFOTAStartDownloadFailedListener;

    // Inteerface--> 接口OnSetFOTAStartDownloadFailedListener
    public interface OnSetFOTAStartDownloadFailedListener {
        void SetFOTAStartDownloadFailed();
    }

    // 对外方式setOnSetFOTAStartDownloadFailedListener
    public void setOnSetFOTAStartDownloadFailedListener(OnSetFOTAStartDownloadFailedListener onSetFOTAStartDownloadFailedListener) {
        this.onSetFOTAStartDownloadFailedListener = onSetFOTAStartDownloadFailedListener;
    }

    // 封装方法SetFOTAStartDownloadFailedNext
    private void SetFOTAStartDownloadFailedNext() {
        if (onSetFOTAStartDownloadFailedListener != null) {
            onSetFOTAStartDownloadFailedListener.SetFOTAStartDownloadFailed();
        }
    }
}
