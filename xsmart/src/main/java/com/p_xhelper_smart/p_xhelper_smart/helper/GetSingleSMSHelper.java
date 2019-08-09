package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetSingleSMSBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSingleSMSParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/9 0009.
 */
public class GetSingleSMSHelper extends BaseHelper {

    /**
     * 获取单条短信
     *
     * @param smsId 短信ID
     */
    public void getSingleSMS(long smsId) {
        prepareHelperNext();
        XSmart<GetSingleSMSBean> xGetSingleSMSBeanXSmart = new XSmart<>();
        xGetSingleSMSBeanXSmart.xMethod(XCons.METHOD_GET_SINGLE_SMS);
        xGetSingleSMSBeanXSmart.xParam(new GetSingleSMSParam(smsId));
        xGetSingleSMSBeanXSmart.xPost(new XNormalCallback<GetSingleSMSBean>() {
            @Override
            public void success(GetSingleSMSBean result) {
                GetSingleSMSSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetSingleSMSFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetSingleSMSFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });

    }

    private OnGetSingleSMSSuccessListener onGetSingleSMSSuccessListener;

    // Inteerface--> 接口OnGetSingleSMSSuccessListener
    public interface OnGetSingleSMSSuccessListener {
        void GetSingleSMSSuccess(GetSingleSMSBean singleSMSBean);
    }

    // 对外方式setOnGetSingleSMSSuccessListener
    public void setOnGetSingleSMSSuccessListener(OnGetSingleSMSSuccessListener onGetSingleSMSSuccessListener) {
        this.onGetSingleSMSSuccessListener = onGetSingleSMSSuccessListener;
    }

    // 封装方法GetSingleSMSSuccessNext
    private void GetSingleSMSSuccessNext(GetSingleSMSBean singleSMSBean) {
        if (onGetSingleSMSSuccessListener != null) {
            onGetSingleSMSSuccessListener.GetSingleSMSSuccess(singleSMSBean);
        }
    }

    private OnGetSingleSMSFailedListener onGetSingleSMSFailedListener;

    // Inteerface--> 接口OnGetSingleSMSFailedListener
    public interface OnGetSingleSMSFailedListener {
        void GetSingleSMSFailed();
    }

    // 对外方式setOnGetSingleSMSFailedListener
    public void setOnGetSingleSMSFailedListener(OnGetSingleSMSFailedListener onGetSingleSMSFailedListener) {
        this.onGetSingleSMSFailedListener = onGetSingleSMSFailedListener;
    }

    // 封装方法GetSingleSMSFailedNext
    private void GetSingleSMSFailedNext() {
        if (onGetSingleSMSFailedListener != null) {
            onGetSingleSMSFailedListener.GetSingleSMSFailed();
        }
    }
}
