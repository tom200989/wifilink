package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContactListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSmsContactListParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetSMSContactListHelper extends BaseHelper {

    /**
     * 获取短信联系人列表
     */
    public void getSMSContactList(int page) {
        prepareHelperNext();
        GetSmsContactListParam param = new GetSmsContactListParam(page);
        XSmart<GetSMSContactListBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_SMS_CONTACT_LIST).xParam(param).xPost(new XNormalCallback<GetSMSContactListBean>() {
            @Override
            public void success(GetSMSContactListBean result) {
                getSmsContactListSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getSmsContactListFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getSmsContactListFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------获取短信联系人信息成功的回调------------------------------*/
    public interface OnGetSmsContactListSuccessListener {
        void getSmsContactListSuccess(GetSMSContactListBean bean);
    }

    private OnGetSmsContactListSuccessListener onGetSmsContactListSuccessListener;

    //对外方式setOnGetSmsContactListSuccessListener
    public void setOnGetSmsContactListSuccessListener(OnGetSmsContactListSuccessListener onGetSmsContactListSuccessListener) {
        this.onGetSmsContactListSuccessListener = onGetSmsContactListSuccessListener;
    }

    //封装方法getSmsContactListSuccessNext
    private void getSmsContactListSuccessNext(GetSMSContactListBean bean) {
        if (onGetSmsContactListSuccessListener != null) {
            onGetSmsContactListSuccessListener.getSmsContactListSuccess(bean);
        }
    }

    /*----------------------------------获取短信联系人信息是失败的回调------------------------------*/
    public interface OnGetSmsContactListFailListener {
        void getSmsContactListFail();
    }

    private OnGetSmsContactListFailListener onGetSmsContactListFailListener;

    //对外方式setOnGetSmsContactListFailListener
    public void setOnGetSmsContactListFailListener(OnGetSmsContactListFailListener onGetSmsContactListFailListener) {
        this.onGetSmsContactListFailListener = onGetSmsContactListFailListener;
    }

    //封装方法getSmsContactListFailNext
    private void getSmsContactListFailNext() {
        if (onGetSmsContactListFailListener != null) {
            onGetSmsContactListFailListener.getSmsContactListFail();
        }
    }


}
