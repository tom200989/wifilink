package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SaveSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
@SuppressWarnings("unchecked")
public class SaveSMSHelper extends BaseHelper {

    public void saveSms(SaveSmsParam param) {
        prepareHelperNext();
        XSmart xSmart = new XSmart();
        xSmart.xMethod(XCons.METHOD_SAVE_SMS).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                saveSmsSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                saveSmsFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                if(fwError != null){
                    if (fwError.getCode().equals("060801")) {
                        saveFailNext();
                    } else if (fwError.getCode().equals("060802")) {
                        spaceFullNext();
                    }
                }
                saveSmsFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }


    /*----------------------------------保存sms成功回调------------------------------*/
    public interface OnSaveSMSSuccessListener {
        void saveSmsSuccess();
    }

    private OnSaveSMSSuccessListener onSaveSMSSuccessListener;

    //对外方式setOnSaveSMSSuccessListener
    public void setOnSaveSMSSuccessListener(OnSaveSMSSuccessListener onSaveSMSSuccessListener) {
        this.onSaveSMSSuccessListener = onSaveSMSSuccessListener;
    }

    //封装方法saveSmsSuccessNext
    private void saveSmsSuccessNext() {
        if (onSaveSMSSuccessListener != null) {
            onSaveSMSSuccessListener.saveSmsSuccess();
        }
    }

    /*----------------------------------保存sms失败回调------------------------------*/
    public interface OnSaveSmsFailListener {
        void saveSmsFail();
    }

    private OnSaveSmsFailListener onSaveSmsFailListener;

    //对外方式setOnSaveSmsFailListener
    public void setOnSaveSmsFailListener(OnSaveSmsFailListener onSaveSmsFailListener) {
        this.onSaveSmsFailListener = onSaveSmsFailListener;
    }

    //封装方法saveSmsFailNext
    private void saveSmsFailNext() {
        if (onSaveSmsFailListener != null) {
            onSaveSmsFailListener.saveSmsFail();
        }
    }

    /*----------------------------------保存失败回调------------------------------*/
    public interface OnSaveFailListener {
        void SaveFail();
    }

    private OnSaveFailListener onSaveFailListener;

    //对外方式setOnSaveFailListener
    public void setOnSaveFailListener(OnSaveFailListener onSaveFailListener) {
        this.onSaveFailListener = onSaveFailListener;
    }

    //封装方法SaveFailNext
    private void saveFailNext() {
        if (onSaveFailListener != null) {
            onSaveFailListener.SaveFail();
        }
    }


    /*---------------------------------- 存储满了 ------------------------------*/
    public interface OnSpaceFullListener {
        void SpaceFull();
    }

    private OnSpaceFullListener onSpaceFullListener;

    //对外方式setOnSpaceFullListener
    public void setOnSpaceFullListener(OnSpaceFullListener onSpaceFullListener) {
        this.onSpaceFullListener = onSpaceFullListener;
    }

    //封装方法SpaceFullNext
    private void spaceFullNext() {
        if (onSpaceFullListener != null) {
            onSpaceFullListener.SpaceFull();
        }
    }
}
