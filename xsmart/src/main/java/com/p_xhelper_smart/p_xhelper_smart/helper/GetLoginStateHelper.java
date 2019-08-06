package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/29 0029.
 */
public class GetLoginStateHelper extends BaseHelper {

    /**
     * 获取登陆状态
     */
    public void getLoginState() {
        prepareHelperNext();
        XSmart<GetLoginStateBean> xsmart = new XSmart<>();
        xsmart.xMethod(XCons.METHOD_GET_LOGIN_STATE).xPost(new XNormalCallback<GetLoginStateBean>() {

            @Override
            public void success(GetLoginStateBean getLoginStateBean) {
                getLoginStateSuccessNext(getLoginStateBean);
            }

            @Override
            public void appError(Throwable ex) {
                getLoginStateFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getLoginStateFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetLoginStateSuccessListener onGetLoginStateSuccessListener;

    // Inteerface--> 接口OnGetLoginStateSuccessListener
    public interface OnGetLoginStateSuccessListener {
        void getLoginStateSuccess(GetLoginStateBean getLoginStateBean);
    }

    // 对外方式setOnGetLoginStateSuccessListener
    public void setOnGetLoginStateSuccessListener(OnGetLoginStateSuccessListener onGetLoginStateSuccessListener) {
        this.onGetLoginStateSuccessListener = onGetLoginStateSuccessListener;
    }

    // 封装方法getLoginStateSuccessNext
    private void getLoginStateSuccessNext(GetLoginStateBean getLoginStateBean) {
        if (onGetLoginStateSuccessListener != null) {
            onGetLoginStateSuccessListener.getLoginStateSuccess(getLoginStateBean);
        }
    }

    private OnGetLoginStateFailedListener onGetLoginStateFailedListener;

    // Inteerface--> 接口OnGetLoginStateFailedListener
    public interface OnGetLoginStateFailedListener {
        void getLoginStateFailed();
    }

    // 对外方式setOnGetLoginStateFailedListener
    public void setOnGetLoginStateFailedListener(OnGetLoginStateFailedListener onGetLoginStateFailedListener) {
        this.onGetLoginStateFailedListener = onGetLoginStateFailedListener;
    }

    // 封装方法getLoginStateFailedNext
    private void getLoginStateFailedNext() {
        if (onGetLoginStateFailedListener != null) {
            onGetLoginStateFailedListener.getLoginStateFailed();
        }
    }

}
