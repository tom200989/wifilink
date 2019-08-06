package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetBlockDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetBlockDeviceListHelper extends BaseHelper{

    /**
     * 获取黑名单设备列表
     */
    public void getBlockDeviceList() {
        prepareHelperNext();
        XSmart<GetBlockDeviceListBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_BLOCK_DEVICE_LIST).xPost(new XNormalCallback<GetBlockDeviceListBean>() {
            @Override
            public void success(GetBlockDeviceListBean result) {
                getBlockDeviceListSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getBlockDeviceListFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getBlockDeviceListFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }


    /*----------------------------------获取黑名单设备列表成功的回调------------------------------*/
    public interface onGetBlockDeviceListSuccessListener {
        void getBlockDeviceListSuccess(GetBlockDeviceListBean bean);
    }

    private onGetBlockDeviceListSuccessListener onGetBlockDeviceListSuccessListener;

    //对外方式setonGetBlockDeviceListSuccessListener
    public void setonGetBlockDeviceListSuccessListener(onGetBlockDeviceListSuccessListener onGetBlockDeviceListSuccessListener) {
        this.onGetBlockDeviceListSuccessListener = onGetBlockDeviceListSuccessListener;
    }

    //封装方法getBlockDeviceListSuccessNext
    private void getBlockDeviceListSuccessNext(GetBlockDeviceListBean bean) {
        if (onGetBlockDeviceListSuccessListener != null) {
            onGetBlockDeviceListSuccessListener.getBlockDeviceListSuccess(bean);
        }
    }


    /*----------------------------------获取黑名单设备列表失败的回调------------------------------*/
    public interface OnGetBlockDeviceListFailListener {
        void getBlockDeiceListFail();
    }

    private OnGetBlockDeviceListFailListener onGetBlockDeviceListFailListener;

    //对外方式setOnGetBlockDeviceListFailListener
    public void setOnGetBlockDeviceListFailListener(OnGetBlockDeviceListFailListener onGetBlockDeviceListFailListener) {
        this.onGetBlockDeviceListFailListener = onGetBlockDeviceListFailListener;
    }

    //封装方法getBlockDeviceListFailNext
    private void getBlockDeviceListFailNext() {
        if (onGetBlockDeviceListFailListener != null) {
            onGetBlockDeviceListFailListener.getBlockDeiceListFail();
        }
    }
}
