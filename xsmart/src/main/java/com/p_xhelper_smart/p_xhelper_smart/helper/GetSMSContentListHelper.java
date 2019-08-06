package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContentListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSmsContentListParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetSMSContentListHelper extends BaseHelper {

    /**
     * 获取短信内容列表
     */
    public void getSMSContentList(GetSmsContentListParam param) {
        prepareHelperNext();
        XSmart<GetSMSContentListBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_SMS_CONTENT_LIST).xParam(param).xPost(new XNormalCallback<GetSMSContentListBean>() {
            @Override
            public void success(GetSMSContentListBean result) {
                getSmsContentListSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getSmsContentListFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getSmsContentListFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }


    /*----------------------------------获取短信内容列表成功的回调------------------------------*/
    public interface OnGetSmsContentListSuccessListener {
        void getSmsContentListSuccess(GetSMSContentListBean bean);
    }

    private OnGetSmsContentListSuccessListener onGetSmsContentListSuccessListener;

    //对外方式setOnGetSmsContentListSuccessListener
    public void setOnGetSmsContentListSuccessListener(OnGetSmsContentListSuccessListener onGetSmsContentListSuccessListener) {
        this.onGetSmsContentListSuccessListener = onGetSmsContentListSuccessListener;
    }

    //封装方法getSmsContentListSuccessNext
    private void getSmsContentListSuccessNext(GetSMSContentListBean bean) {
        if (onGetSmsContentListSuccessListener != null) {
            onGetSmsContentListSuccessListener.getSmsContentListSuccess(bean);
        }
    }


    /*----------------------------------获取短信内容列表失败的回调------------------------------*/
    public interface OnGetSmsContentListFailListener {
        void getSmsContentListFail();
    }

    private OnGetSmsContentListFailListener onGetSmsContentListFailListener;

    //对外方式setOnGetSmsContentListFailListener
    public void setOnGetSmsContentListFailListener(OnGetSmsContentListFailListener onGetSmsContentListFailListener) {
        this.onGetSmsContentListFailListener = onGetSmsContentListFailListener;
    }

    //封装方法getSmsContentListFailNext
    private void getSmsContentListFailNext() {
        if (onGetSmsContentListFailListener != null) {
            onGetSmsContentListFailListener.getSmsContentListFail();
        }
    }

}
