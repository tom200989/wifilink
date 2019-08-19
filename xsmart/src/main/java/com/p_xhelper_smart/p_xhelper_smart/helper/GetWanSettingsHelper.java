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
                getWanSettingFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getWanSettingFailedNext();
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
        void GetWanSettingsSuccess(GetWanSettingsBean wanSettingsBean);
    }

    // 对外方式setOnGetWanSettingsSuccessListener
    public void setOnGetWanSettingsSuccessListener(OnGetWanSettingsSuccessListener onGetWanSettingsSuccessListener) {
        this.onGetWanSettingsSuccessListener = onGetWanSettingsSuccessListener;
    }

    // 封装方法GetWanSettingsSuccessNext
    private void GetWanSettingsSuccessNext(GetWanSettingsBean wanSettingsBean) {
        if (onGetWanSettingsSuccessListener != null) {
            onGetWanSettingsSuccessListener.GetWanSettingsSuccess(wanSettingsBean);
        }
    }

    private OnGetWanSettingFailedListener onGetWanSettingFailedListener;

    // Inteerface--> 接口OnGetWanSettingFailedListener
    public interface OnGetWanSettingFailedListener {
        void getWanSettingFailed();
    }

    // 对外方式setOnGetWanSettingFailedListener
    public void setOnGetWanSettingFailedListener(OnGetWanSettingFailedListener onGetWanSettingFailedListener) {
        this.onGetWanSettingFailedListener = onGetWanSettingFailedListener;
    }

    // 封装方法getWanSettingFailedNext
    private void getWanSettingFailedNext() {
        if (onGetWanSettingFailedListener != null) {
            onGetWanSettingFailedListener.getWanSettingFailed();
        }
    }

}
