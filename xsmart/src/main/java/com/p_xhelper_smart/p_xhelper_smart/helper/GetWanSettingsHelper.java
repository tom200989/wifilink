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
                GetWanSettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetWanSettingsFailedNext();
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

    private OnGetWanSettingsFailedListener onGetWanSettingsFailedListener;

    // Inteerface--> 接口OnGetWanSettingsFailedListener
    public interface OnGetWanSettingsFailedListener {
        void GetWanSettingsFailed();
    }

    // 对外方式setOnGetWanSettingsFailedListener
    public void setOnGetWanSettingsFailedListener(OnGetWanSettingsFailedListener onGetWanSettingsFailedListener) {
        this.onGetWanSettingsFailedListener = onGetWanSettingsFailedListener;
    }

    // 封装方法GetWanSettingsFailedNext
    private void GetWanSettingsFailedNext() {
        if (onGetWanSettingsFailedListener != null) {
            onGetWanSettingsFailedListener.GetWanSettingsFailed();
        }
    }
}
