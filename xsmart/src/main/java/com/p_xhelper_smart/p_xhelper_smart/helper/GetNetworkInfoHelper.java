package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
public class GetNetworkInfoHelper extends BaseHelper {

    /**
     * 获取网络信息
     */
    public void getNetworkInfo() {
        prepareHelperNext();
        XSmart<GetNetworkInfoBean> xNetworkInfo = new XSmart<>();
        xNetworkInfo.xMethod(XCons.METHOD_GET_NETWORK_INFO);
        xNetworkInfo.xPost(new XNormalCallback<GetNetworkInfoBean>() {
            @Override
            public void success(GetNetworkInfoBean result) {
                GetNetworkInfoSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetNetworkInfoFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetNetworkInfoFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetNetworkInfoSuccessListener onGetNetworkInfoSuccessListener;

    // Inteerface--> 接口OnGetNetworkInfoSuccessListener
    public interface OnGetNetworkInfoSuccessListener {
        void GetNetworkInfoSuccess(GetNetworkInfoBean networkInfoBean);
    }

    // 对外方式setOnGetNetworkInfoSuccessListener
    public void setOnGetNetworkInfoSuccessListener(OnGetNetworkInfoSuccessListener onGetNetworkInfoSuccessListener) {
        this.onGetNetworkInfoSuccessListener = onGetNetworkInfoSuccessListener;
    }

    // 封装方法GetNetworkInfoSuccessNext
    private void GetNetworkInfoSuccessNext(GetNetworkInfoBean networkInfoBean) {
        if (onGetNetworkInfoSuccessListener != null) {
            onGetNetworkInfoSuccessListener.GetNetworkInfoSuccess(networkInfoBean);
        }
    }

    private OnGetNetworkInfoFailedListener onGetNetworkInfoFailedListener;

    // Inteerface--> 接口OnGetNetworkInfoFailedListener
    public interface OnGetNetworkInfoFailedListener {
        void GetNetworkInfoFailed();
    }

    // 对外方式setOnGetNetworkInfoFailedListener
    public void setOnGetNetworkInfoFailedListener(OnGetNetworkInfoFailedListener onGetNetworkInfoFailedListener) {
        this.onGetNetworkInfoFailedListener = onGetNetworkInfoFailedListener;
    }

    // 封装方法GetNetworkInfoFailedNext
    private void GetNetworkInfoFailedNext() {
        if (onGetNetworkInfoFailedListener != null) {
            onGetNetworkInfoFailedListener.GetNetworkInfoFailed();
        }
    }
}
