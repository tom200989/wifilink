package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetProfileListBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetProfileListHelper extends BaseHelper {

    public void getProfileList() {
        prepareHelperNext();
        XSmart<GetProfileListBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_PROFILE_LIST).xPost(new XNormalCallback<GetProfileListBean>() {
            @Override
            public void success(GetProfileListBean result) {
                getProfileListSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getProfileListFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getProfileListFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }


    /*----------------------------------获取配置列表成功的回调------------------------------*/
    public interface OnGetProfileListListener {
        void getProfileListSuccess(GetProfileListBean bean);
    }

    private OnGetProfileListListener onGetProfileListListener;

    //对外方式setOnGetProfileListListener
    public void setOnGetProfileListListener(OnGetProfileListListener onGetProfileListListener) {
        this.onGetProfileListListener = onGetProfileListListener;
    }

    //封装方法getProfileListSuccessNext
    private void getProfileListSuccessNext(GetProfileListBean bean) {
        if (onGetProfileListListener != null) {
            onGetProfileListListener.getProfileListSuccess(bean);
        }
    }

    /*----------------------------------获取配置列表失败的回调------------------------------*/
    public interface OnGetPrifileListFailListener {
        void getProfileListFail();
    }

    private OnGetPrifileListFailListener onGetPrifileListFailListener;

    //对外方式setOnGetPrifileListFailListener
    public void setOnGetPrifileListFailListener(OnGetPrifileListFailListener onGetPrifileListFailListener) {
        this.onGetPrifileListFailListener = onGetPrifileListFailListener;
    }

    //封装方法getProfileListFailNext
    private void getProfileListFailNext() {
        if (onGetPrifileListFailListener != null) {
            onGetPrifileListFailListener.getProfileListFail();
        }
    }

}
