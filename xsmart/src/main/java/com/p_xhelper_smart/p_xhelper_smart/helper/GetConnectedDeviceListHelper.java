package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetConnectedDeviceListHelper extends BaseHelper {

    /**
     * 获取已连接的设备列表
     */
    public void getConnectDeviceList() {
        prepareHelperNext();
        XSmart<GetConnectDeviceListBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_CONNECTED_DEVICE_LIST).xPost(new XNormalCallback<GetConnectDeviceListBean>() {
            @Override
            public void success(GetConnectDeviceListBean result) {
                getDeviceListSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getDeviceListFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getDeviceListFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------获取设备列表成功的回调------------------------------*/
    public interface OnGetDeviceListSuccessListener {
        void getDeviceListSuccess(GetConnectDeviceListBean bean);
    }

    private OnGetDeviceListSuccessListener onGetDeviceListSuccessListener;

    //对外方式setOnGetDeviceListSuccessListener
    public void setOnGetDeviceListSuccessListener(OnGetDeviceListSuccessListener onGetDeviceListSuccessListener) {
        this.onGetDeviceListSuccessListener = onGetDeviceListSuccessListener;
    }

    //封装方法 getDeviceListSuccessNext
    private void getDeviceListSuccessNext(GetConnectDeviceListBean bean) {
        if (onGetDeviceListSuccessListener != null) {
            onGetDeviceListSuccessListener.getDeviceListSuccess(bean);
        }
    }


    /*----------------------------------获取设备列表失败的回调------------------------------*/
    public interface OnGetDeviceListFailListener {
        void getDeviceListFail();
    }

    private OnGetDeviceListFailListener onGetDeviceListFailListener;

    //对外方式setOnGetDeviceListFailListener
    public void setOnGetDeviceListFailListener(OnGetDeviceListFailListener onGetDeviceListFailListener) {
        this.onGetDeviceListFailListener = onGetDeviceListFailListener;
    }

    //封装方法getDeviceListFailNext
    private void getDeviceListFailNext() {
        if (onGetDeviceListFailListener != null) {
            onGetDeviceListFailListener.getDeviceListFail();
        }
    }
}
