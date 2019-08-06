package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
public class GetSystemStatusHelper extends BaseHelper {

    /**
     * 获取系统状态
     */
    public void getSystemStatus() {
        prepareHelperNext();
        XSmart<GetSystemStatusBean> xSystemStatus = new XSmart<>();
        xSystemStatus.xMethod(XCons.METHOD_GET_SYSTEM_STATUS);
        xSystemStatus.xPost(new XNormalCallback<GetSystemStatusBean>() {
            @Override
            public void success(GetSystemStatusBean result) {
                GetSystemStatusSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetSystemStatusFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetSystemStatusFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetSystemStatusSuccessListener onGetSystemStatusSuccessListener;

    // Inteerface--> 接口OnGetSystemStatusSuccessListener
    public interface OnGetSystemStatusSuccessListener {
        void GetSystemStatusSuccess(GetSystemStatusBean attr);
    }

    // 对外方式setOnGetSystemStatusSuccessListener
    public void setOnGetSystemStatusSuccessListener(OnGetSystemStatusSuccessListener onGetSystemStatusSuccessListener) {
        this.onGetSystemStatusSuccessListener = onGetSystemStatusSuccessListener;
    }

    // 封装方法GetSystemStatusSuccessNext
    private void GetSystemStatusSuccessNext(GetSystemStatusBean attr) {
        if (onGetSystemStatusSuccessListener != null) {
            onGetSystemStatusSuccessListener.GetSystemStatusSuccess(attr);
        }
    }

    private OnGetSystemStatusFailedListener onGetSystemStatusFailedListener;

    // Inteerface--> 接口OnGetSystemStatusFailedListener
    public interface OnGetSystemStatusFailedListener {
        void GetSystemStatusFailed();
    }

    // 对外方式setOnGetSystemStatusFailedListener
    public void setOnGetSystemStatusFailedListener(OnGetSystemStatusFailedListener onGetSystemStatusFailedListener) {
        this.onGetSystemStatusFailedListener = onGetSystemStatusFailedListener;
    }

    // 封装方法GetSystemStatusFailedNext
    private void GetSystemStatusFailedNext() {
        if (onGetSystemStatusFailedListener != null) {
            onGetSystemStatusFailedListener.GetSystemStatusFailed();
        }
    }
}
