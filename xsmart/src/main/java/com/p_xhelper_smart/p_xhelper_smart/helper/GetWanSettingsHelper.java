package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetWanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
public class GetWanSettingsHelper extends BaseHelper {

    /**
     * 获取WAN口设置
     */
    public void getWanSettings() {
        prepareHelperNext();
        XSmart<GetWanSettingsBean> xWanSetting = new XSmart<>();
        xWanSetting.xMethod(XCons.METHOD_GET_WAN_SETTINGS);
        xWanSetting.xPost(new XNormalCallback<GetWanSettingsBean>() {
            @Override
            public void success(GetWanSettingsBean result) {
                GetWanSettingsSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                appErrorNext();
            }

            @Override
            public void fwError(FwError fwError) {
                fwErrorNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetWanSettingsSuccessListener onGetWanSettingsSuccessListener;

    // Inteerface--> 接口OnGetWanSettingsSuccessListener
    public interface OnGetWanSettingsSuccessListener {
        void GetWanSettingsSuccess(GetWanSettingsBean attr);
    }

    // 对外方式setOnGetWanSettingsSuccessListener
    public void setOnGetWanSettingsSuccessListener(OnGetWanSettingsSuccessListener onGetWanSettingsSuccessListener) {
        this.onGetWanSettingsSuccessListener = onGetWanSettingsSuccessListener;
    }

    // 封装方法GetWanSettingsSuccessNext
    private void GetWanSettingsSuccessNext(GetWanSettingsBean attr) {
        if (onGetWanSettingsSuccessListener != null) {
            onGetWanSettingsSuccessListener.GetWanSettingsSuccess(attr);
        }
    }

    private OnAppErrorListener onAppErrorListener;

    // Inteerface--> 接口OnAppErrorListener
    public interface OnAppErrorListener {
        void appError();
    }

    // 对外方式setOnAppErrorListener
    public void setOnAppErrorListener(OnAppErrorListener onAppErrorListener) {
        this.onAppErrorListener = onAppErrorListener;
    }

    // 封装方法appErrorNext
    private void appErrorNext() {
        if (onAppErrorListener != null) {
            onAppErrorListener.appError();
        }
    }

    private OnFwErrorListener onFwErrorListener;

    // Inteerface--> 接口OnFwErrorListener
    public interface OnFwErrorListener {
        void fwError();
    }

    // 对外方式setOnFwErrorListener
    public void setOnFwErrorListener(OnFwErrorListener onFwErrorListener) {
        this.onFwErrorListener = onFwErrorListener;
    }

    // 封装方法fwErrorNext
    private void fwErrorNext() {
        if (onFwErrorListener != null) {
            onFwErrorListener.fwError();
        }
    }


}
