package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetDeviceNewVersionBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
public class GetDeviceNewVersionHelper extends BaseHelper {

    /**
     * 获取新版本信息
     */
    public void getDeviceNewVersion() {
        prepareHelperNext();
        XSmart<GetDeviceNewVersionBean> xNewVersion = new XSmart<>();
        xNewVersion.xMethod(XCons.METHOD_GET_DEVICE_NEW_VERSION);
        xNewVersion.xPost(new XNormalCallback<GetDeviceNewVersionBean>() {
            @Override
            public void success(GetDeviceNewVersionBean result) {
                GetDeviceNewVersionSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetDeviceNewVersionFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetDeviceNewVersionFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetDeviceNewVersionSuccessListener onGetDeviceNewVersionSuccessListener;

    // Inteerface--> 接口OnGetDeviceNewVersionSuccessListener
    public interface OnGetDeviceNewVersionSuccessListener {
        void GetDeviceNewVersionSuccess(GetDeviceNewVersionBean attr);
    }

    // 对外方式setOnGetDeviceNewVersionSuccessListener
    public void setOnGetDeviceNewVersionSuccessListener(OnGetDeviceNewVersionSuccessListener onGetDeviceNewVersionSuccessListener) {
        this.onGetDeviceNewVersionSuccessListener = onGetDeviceNewVersionSuccessListener;
    }

    // 封装方法GetDeviceNewVersionSuccessNext
    private void GetDeviceNewVersionSuccessNext(GetDeviceNewVersionBean attr) {
        if (onGetDeviceNewVersionSuccessListener != null) {
            onGetDeviceNewVersionSuccessListener.GetDeviceNewVersionSuccess(attr);
        }
    }

    private OnGetDeviceNewVersionFailedListener onGetDeviceNewVersionFailedListener;

    // Inteerface--> 接口OnGetDeviceNewVersionFailedListener
    public interface OnGetDeviceNewVersionFailedListener {
        void GetDeviceNewVersionFailed();
    }

    // 对外方式setOnGetDeviceNewVersionFailedListener
    public void setOnGetDeviceNewVersionFailedListener(OnGetDeviceNewVersionFailedListener onGetDeviceNewVersionFailedListener) {
        this.onGetDeviceNewVersionFailedListener = onGetDeviceNewVersionFailedListener;
    }

    // 封装方法GetDeviceNewVersionFailedNext
    private void GetDeviceNewVersionFailedNext() {
        if (onGetDeviceNewVersionFailedListener != null) {
            onGetDeviceNewVersionFailedListener.GetDeviceNewVersionFailed();
        }
    }
}
