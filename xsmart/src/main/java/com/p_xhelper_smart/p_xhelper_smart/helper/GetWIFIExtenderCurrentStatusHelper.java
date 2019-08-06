package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetWIFIExtenderCurrentStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetWIFIExtenderCurrentStatusHelper extends BaseHelper {

    /**
     * 获取wifi外部扩展器当前状态
     */
    public void getWIFIExtenderCurrentStatus() {
        prepareHelperNext();
        XSmart<GetWIFIExtenderCurrentStatusBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_WIFI_EXTENDER_CURRENT_STATUS).xPost(new XNormalCallback<GetWIFIExtenderCurrentStatusBean>() {
            @Override
            public void success(GetWIFIExtenderCurrentStatusBean result) {
                getWifiExCurStatusSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getWifiExCurStatusFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getWifiExCurStatusFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------获取外部扩展器当前状态成功的回调------------------------------*/
    public interface OnGetWifiExCurStatusSuccessListener {
        void getWifiExCurStatusSuccess(GetWIFIExtenderCurrentStatusBean bean);
    }

    private OnGetWifiExCurStatusSuccessListener onGetWifiExCurStatusSuccessListener;

    //对外方式setOnGetWifiExCurStatusSuccessListener
    public void setOnGetWifiExCurStatusSuccessListener(OnGetWifiExCurStatusSuccessListener onGetWifiExCurStatusSuccessListener) {
        this.onGetWifiExCurStatusSuccessListener = onGetWifiExCurStatusSuccessListener;
    }

    //封装方法getWifiExCurStatusSuccessNext
    private void getWifiExCurStatusSuccessNext(GetWIFIExtenderCurrentStatusBean bean) {
        if (onGetWifiExCurStatusSuccessListener != null) {
            onGetWifiExCurStatusSuccessListener.getWifiExCurStatusSuccess(bean);
        }
    }

    /*----------------------------------获取外部扩展器当前状态失败的回调------------------------------*/
    public interface OnGetWifiExCurStatusFailListener {
        void getWifiExCurStatusFail();
    }

    private OnGetWifiExCurStatusFailListener onGetWifiExCurStatusFailListener;

    //对外方式setOnGetWifiExCurStatusFailListener
    public void setOnGetWifiExCurStatusFailListener(OnGetWifiExCurStatusFailListener onGetWifiExCurStatusFailListener) {
        this.onGetWifiExCurStatusFailListener = onGetWifiExCurStatusFailListener;
    }

    //封装方法getWifiExCurStatusFailNext
    private void getWifiExCurStatusFailNext() {
        if (onGetWifiExCurStatusFailListener != null) {
            onGetWifiExCurStatusFailListener.getWifiExCurStatusFail();
        }
    }


}
