package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetHotSpotListBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetHotspotListHelper extends BaseHelper {

    /**
     * 获取热点列表
     */
    public void getHotSpotList() {
        prepareHelperNext();
        XSmart<GetHotSpotListBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_HOTSPOT_LIST).xPost(new XNormalCallback<GetHotSpotListBean>() {
            @Override
            public void success(GetHotSpotListBean result) {
                getHotSpotListSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getHotSpotListFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getHotSpotListFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------获取热点列表成功的回调------------------------------*/
    public interface OnGetHotSpotListSuccessListener {
        void getHotSpotListSuccess(GetHotSpotListBean bean);
    }

    private OnGetHotSpotListSuccessListener onGetHotSpotListSuccessListener;

    //对外方式setOnGetHotSpotListSuccessListener
    public void setOnGetHotSpotListSuccessListener(OnGetHotSpotListSuccessListener onGetHotSpotListSuccessListener) {
        this.onGetHotSpotListSuccessListener = onGetHotSpotListSuccessListener;
    }

    //封装方法getHotSpotListSuccessNext
    private void getHotSpotListSuccessNext(GetHotSpotListBean bean) {
        if (onGetHotSpotListSuccessListener != null) {
            onGetHotSpotListSuccessListener.getHotSpotListSuccess(bean);
        }
    }


    /*----------------------------------获取热点列表失败的回调------------------------------*/
    public interface OnGetHotSpotListFailListener {
        void getHotSpotListFail();
    }

    private OnGetHotSpotListFailListener onGetHotSpotListFailListener;

    //对外方式setOnGetHotSpotListFailListener
    public void setOnGetHotSpotListFailListener(OnGetHotSpotListFailListener onGetHotSpotListFailListener) {
        this.onGetHotSpotListFailListener = onGetHotSpotListFailListener;
    }

    //封装方法getHotSpotListFailNext
    private void getHotSpotListFailNext() {
        if (onGetHotSpotListFailListener != null) {
            onGetHotSpotListFailListener.getHotSpotListFail();
        }
    }
}
