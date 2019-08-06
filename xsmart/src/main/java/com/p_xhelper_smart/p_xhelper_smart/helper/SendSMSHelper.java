package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SendSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class SendSMSHelper extends BaseHelper {

    public void sendSms(SendSmsParam param) {
        prepareHelperNext();
        XSmart xSmart = new XSmart();
        xSmart.xMethod(XCons.METHOD_SEND_SMS).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                sendSmsSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                sendSmsFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                sendSmsFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }


    /*----------------------------------发送短信成功回调------------------------------*/
    public interface OnSendSmsSuccessListener {
        void sendSmsSuccess();
    }

    private OnSendSmsSuccessListener onSendSmsSuccessListener;

    //对外方式setOnSendSmsSuccessListener
    public void setOnSendSmsSuccessListener(OnSendSmsSuccessListener onSendSmsSuccessListener) {
        this.onSendSmsSuccessListener = onSendSmsSuccessListener;
    }

    //封装方法sendSmsSuccessNext
    private void sendSmsSuccessNext() {
        if (onSendSmsSuccessListener != null) {
            onSendSmsSuccessListener.sendSmsSuccess();
        }
    }

    /*----------------------------------发送短信失败的回调------------------------------*/
    public interface OnSendSmsFailListener {
        void sendSmsFail();
    }

    private OnSendSmsFailListener onSendSmsFailListener;

    //对外方式setOnSendSmsFailListener
    public void setOnSendSmsFailListener(OnSendSmsFailListener onSendSmsFailListener) {
        this.onSendSmsFailListener = onSendSmsFailListener;
    }

    //封装方法sendSmsFailNext
    private void sendSmsFailNext() {
        if (onSendSmsFailListener != null) {
            onSendSmsFailListener.sendSmsFail();
        }
    }


}
