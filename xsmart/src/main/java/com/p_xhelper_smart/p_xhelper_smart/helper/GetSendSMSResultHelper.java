package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetSendSMSResultBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetSendSMSResultHelper extends BaseHelper {

    /**
     * 获取发送短信结果
     */
    public void getSMSContactList() {
        prepareHelperNext();
        XSmart<GetSendSMSResultBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_SEND_SMS_RESULT).xPost(new XNormalCallback<GetSendSMSResultBean>() {
            @Override
            public void success(GetSendSMSResultBean result) {
                getSendSMSResultSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getSendSmsResultFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getSendSmsResultFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*---------------------------------获取发送短信结果成功的回调-------------------------------*/
    public interface OnGetSendSmsResultSuccessListener {
        void getSendSmsResultSuccess(GetSendSMSResultBean bean);
    }

    private OnGetSendSmsResultSuccessListener onGetSendSmsResultSuccessListener;

    //对外方式setOnGetSendSmsResultSuccessListener
    public void setOnGetSendSmsResultSuccessListener(OnGetSendSmsResultSuccessListener onGetSendSmsResultSuccessListener) {
        this.onGetSendSmsResultSuccessListener = onGetSendSmsResultSuccessListener;
    }

    //封装方法getSendSMSResultSuccessNext
    private void getSendSMSResultSuccessNext(GetSendSMSResultBean bean) {
        if (onGetSendSmsResultSuccessListener != null) {
            onGetSendSmsResultSuccessListener.getSendSmsResultSuccess(bean);
        }
    }

    /*----------------------------------获取发送短信结果的失败的回调------------------------------*/
    public interface OnGetSendSmsResultFailListener {
        void getSendSmsResultFail();
    }

    private OnGetSendSmsResultFailListener onGetSendSmsResultFailListener;

    //对外方式setOnGetSendSmsResultFailListener
    public void setOnGetSendSmsResultFailListener(OnGetSendSmsResultFailListener onGetSendSmsResultFailListener) {
        this.onGetSendSmsResultFailListener = onGetSendSmsResultFailListener;
    }

    //封装方法getSendSmsResultFailNext
    private void getSendSmsResultFailNext() {
        if (onGetSendSmsResultFailListener != null) {
            onGetSendSmsResultFailListener.getSendSmsResultFail();
        }
    }
}
