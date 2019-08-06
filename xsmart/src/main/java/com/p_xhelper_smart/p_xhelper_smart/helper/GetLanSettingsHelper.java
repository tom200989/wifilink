package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetLanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
public class GetLanSettingsHelper extends BaseHelper {

    /**
     * 获取LAN设置
     */
    public void getLanSettings() {
        prepareHelperNext();
        XSmart<GetLanSettingsBean> xGetLanSetting = new XSmart<>();
        xGetLanSetting.xMethod(XCons.METHOD_GET_LAN_SETTINGS);
        xGetLanSetting.xPost(new XNormalCallback<GetLanSettingsBean>() {
            @Override
            public void success(GetLanSettingsBean result) {
                GetLanSettingsSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetLanSettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetLanSettingsFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetLanSettingsSuccessListener onGetLanSettingsSuccessListener;

    // Inteerface--> 接口OnGetLanSettingsSuccessListener
    public interface OnGetLanSettingsSuccessListener {
        void GetLanSettingsSuccess(GetLanSettingsBean attr);
    }

    // 对外方式setOnGetLanSettingsSuccessListener
    public void setOnGetLanSettingsSuccessListener(OnGetLanSettingsSuccessListener onGetLanSettingsSuccessListener) {
        this.onGetLanSettingsSuccessListener = onGetLanSettingsSuccessListener;
    }

    // 封装方法GetLanSettingsSuccessNext
    private void GetLanSettingsSuccessNext(GetLanSettingsBean attr) {
        if (onGetLanSettingsSuccessListener != null) {
            onGetLanSettingsSuccessListener.GetLanSettingsSuccess(attr);
        }
    }

    private OnGetLanSettingsFailedListener onGetLanSettingsFailedListener;

    // Inteerface--> 接口OnGetLanSettingsFailedListener
    public interface OnGetLanSettingsFailedListener {
        void GetLanSettingsFailed();
    }

    // 对外方式setOnGetLanSettingsFailedListener
    public void setOnGetLanSettingsFailedListener(OnGetLanSettingsFailedListener onGetLanSettingsFailedListener) {
        this.onGetLanSettingsFailedListener = onGetLanSettingsFailedListener;
    }

    // 封装方法GetLanSettingsFailedNext
    private void GetLanSettingsFailedNext() {
        if (onGetLanSettingsFailedListener != null) {
            onGetLanSettingsFailedListener.GetLanSettingsFailed();
        }
    }
}
