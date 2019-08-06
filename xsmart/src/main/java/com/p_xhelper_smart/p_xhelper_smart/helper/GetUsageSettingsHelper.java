package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetUsageSettingsHelper extends BaseHelper {

    public void getUsageSetting() {
        prepareHelperNext();
        XSmart<GetUsageSettingsBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_USAGE_SETTING).xPost(new XNormalCallback<GetUsageSettingsBean>() {
            @Override
            public void success(GetUsageSettingsBean result) {
                getUsageSettingsSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getUsageSetingsFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getUsageSetingsFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------获取流量使用设置的成功回调------------------------------*/
    public interface OnGetUsageSettingsSuccessListener {
        void getUsageSettingsSuccess(GetUsageSettingsBean bean);
    }

    private OnGetUsageSettingsSuccessListener onGetUSageSettingsSuccessListener;

    //对外方式setOnGetUSageSettingsSuccessListener
    public void setOnGetUSageSettingsSuccessListener(OnGetUsageSettingsSuccessListener onGetUSageSettingsSuccessListener) {
        this.onGetUSageSettingsSuccessListener = onGetUSageSettingsSuccessListener;
    }

    //封装方法
    private void getUsageSettingsSuccessNext(GetUsageSettingsBean bean) {
        if (onGetUSageSettingsSuccessListener != null) {
            onGetUSageSettingsSuccessListener.getUsageSettingsSuccess(bean);
        }
    }


    /*----------------------------------获取流量使用设置的失败回调------------------------------*/
    public interface OnGetUsageSettingsFailListener {
        void getUsageSettingsFail();
    }

    private OnGetUsageSettingsFailListener onGetUsageSettingsFailListener;

    //对外方式setOnGetUsageSettingsFailListener
    public void setOnGetUsageSettingsFailListener(OnGetUsageSettingsFailListener onGetUsageSettingsFailListener) {
        this.onGetUsageSettingsFailListener = onGetUsageSettingsFailListener;
    }

    //封装方法getUsageSetingsFailNext
    private void getUsageSetingsFailNext() {
        if (onGetUsageSettingsFailListener != null) {
            onGetUsageSettingsFailListener.getUsageSettingsFail();
        }
    }

}
