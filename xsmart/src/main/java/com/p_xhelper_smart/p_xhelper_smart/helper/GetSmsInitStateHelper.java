package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetSmsInitStateBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetSmsInitStateHelper extends BaseHelper {

    /**
     * 获取黑名单设备列表
     */
    public void getSmsInitState() {
        prepareHelperNext();
        XSmart<GetSmsInitStateBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_SMS_INIT_STATE).xPost(new XNormalCallback<GetSmsInitStateBean>() {
            @Override
            public void success(GetSmsInitStateBean result) {
                getSmsInitStateSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getSmsInitStateFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getSmsInitStateFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------获取sms初始化状态成功的回调------------------------------*/
    public interface OnGetSmsInitStateSuccessListener {
        void getSmsInitStateSuccess(GetSmsInitStateBean bean);
    }

    private OnGetSmsInitStateSuccessListener onGetSmsInitStateSuccessListener;

    //对外方式setOnGetSmsInitStateSuccessListener
    public void setOnGetSmsInitStateSuccessListener(OnGetSmsInitStateSuccessListener onGetSmsInitStateSuccessListener) {
        this.onGetSmsInitStateSuccessListener = onGetSmsInitStateSuccessListener;
    }

    //封装方法getSmsInitStateSuccessNext
    private void getSmsInitStateSuccessNext(GetSmsInitStateBean bean) {
        if (onGetSmsInitStateSuccessListener != null) {
            onGetSmsInitStateSuccessListener.getSmsInitStateSuccess(bean);
        }
    }

    /*------------------------------获取sms初始化状态失败的回调----------------------------------*/
    public interface OnGetSmsInitStateFailListener {
        void getSmsInitStateFail();
    }

    private OnGetSmsInitStateFailListener onGetSmsInitStateFailListener;

    //对外方式setOnGetSmsInitStateFailListener
    public void setOnGetSmsInitStateFailListener(OnGetSmsInitStateFailListener onGetSmsInitStateFailListener) {
        this.onGetSmsInitStateFailListener = onGetSmsInitStateFailListener;
    }

    //封装方法getSmsInitStateFailNext
    private void getSmsInitStateFailNext() {
        if (onGetSmsInitStateFailListener != null) {
            onGetSmsInitStateFailListener.getSmsInitStateFail();
        }
    }
}
