package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.DeleteSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class DeleteSMSHelper extends BaseHelper {


    public void deleteSms(DeleteSmsParam param) {
        prepareHelperNext();
        XSmart xSmart = new XSmart();
        xSmart.xMethod(XCons.METHOD_DELETE_SMS).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                deleteSmsSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                deleteSmsFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                deleteSmsFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }


    public interface OnDeleteSmsSuccessListener {
        void delteSmsSuccess();
    }

    private OnDeleteSmsSuccessListener onDeleteSmsSuccessListener;

    //对外方式setOnDeleteSmsSuccessListener
    public void setOnDeleteSmsSuccessListener(OnDeleteSmsSuccessListener onDeleteSmsSuccessListener) {
        this.onDeleteSmsSuccessListener = onDeleteSmsSuccessListener;
    }

    //封装方法deleteSmsSuccessNext
    private void deleteSmsSuccessNext() {
        if (onDeleteSmsSuccessListener != null) {
            onDeleteSmsSuccessListener.delteSmsSuccess();
        }
    }

    public interface OnDeleteSmsFailListener {
        void deleteSmsFail();
    }

    private OnDeleteSmsFailListener onDeleteSmsFailListener;

    //对外方式setOnDeleteSmsFailListener
    public void setOnDeleteSmsFailListener(OnDeleteSmsFailListener onDeleteSmsFailListener) {
        this.onDeleteSmsFailListener = onDeleteSmsFailListener;
    }

    //封装方法deleteSmsFailNext
    private void deleteSmsFailNext() {
        if (onDeleteSmsFailListener != null) {
            onDeleteSmsFailListener.deleteSmsFail();
        }
    }

}
