package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetWlanSupportModeBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
public class GetWlanSupportModeHelper extends BaseHelper {

    /**
     * 获取支持的WLAN模式
     */
    public void getWlanSupportMode() {
        prepareHelperNext();
        XSmart<GetWlanSupportModeBean> xWlanSupport = new XSmart<>();
        xWlanSupport.xMethod(XCons.METHOD_GET_WLAN_SUPPORT_MODE);
        xWlanSupport.xPost(new XNormalCallback<GetWlanSupportModeBean>() {
            @Override
            public void success(GetWlanSupportModeBean result) {
                GetWlanSupportModeSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetWlanSupportModeFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetWlanSupportModeFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetWlanSupportModeSuccessListener onGetWlanSupportModeSuccessListener;

    // Inteerface--> 接口OnGetWlanSupportModeSuccessListener
    public interface OnGetWlanSupportModeSuccessListener {
        void GetWlanSupportModeSuccess(GetWlanSupportModeBean attr);
    }

    // 对外方式setOnGetWlanSupportModeSuccessListener
    public void setOnGetWlanSupportModeSuccessListener(OnGetWlanSupportModeSuccessListener onGetWlanSupportModeSuccessListener) {
        this.onGetWlanSupportModeSuccessListener = onGetWlanSupportModeSuccessListener;
    }

    // 封装方法GetWlanSupportModeSuccessNext
    private void GetWlanSupportModeSuccessNext(GetWlanSupportModeBean attr) {
        if (onGetWlanSupportModeSuccessListener != null) {
            onGetWlanSupportModeSuccessListener.GetWlanSupportModeSuccess(attr);
        }
    }

    private OnGetWlanSupportModeFailedListener onGetWlanSupportModeFailedListener;

    // Inteerface--> 接口OnGetWlanSupportModeFailedListener
    public interface OnGetWlanSupportModeFailedListener {
        void GetWlanSupportModeFailed();
    }

    // 对外方式setOnGetWlanSupportModeFailedListener
    public void setOnGetWlanSupportModeFailedListener(OnGetWlanSupportModeFailedListener onGetWlanSupportModeFailedListener) {
        this.onGetWlanSupportModeFailedListener = onGetWlanSupportModeFailedListener;
    }

    // 封装方法GetWlanSupportModeFailedNext
    private void GetWlanSupportModeFailedNext() {
        if (onGetWlanSupportModeFailedListener != null) {
            onGetWlanSupportModeFailedListener.GetWlanSupportModeFailed();
        }
    }
}
