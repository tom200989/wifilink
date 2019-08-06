package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetConnectionSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
@SuppressWarnings("unchecked")
public class SetConnectionSettingsHelper extends BaseHelper {

    /**
     * 设置连接配置
     *
     * @param connectMode    连接模式
     * @param roamingConnect 漫游连接
     * @param pdpType        PDP
     * @param connOffTime    离线次数
     */
    public void setConnectionSettings(int connectMode, int roamingConnect, int pdpType, int connOffTime) {
        prepareHelperNext();
        XSmart xSetConnSetting = new XSmart();
        xSetConnSetting.xMethod(XCons.METHOD_SET_CONNECTION_SETTINGS);
        xSetConnSetting.xParam(new SetConnectionSettingsParam(connectMode, roamingConnect, pdpType, connOffTime));
        xSetConnSetting.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                setConnectionSettingsSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                setConnectionSettingsFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                setConnectionSettingsFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnsetConnectionSettingsSuccessListener onsetConnectionSettingsSuccessListener;

    // Inteerface--> 接口OnsetConnectionSettingsSuccessListener
    public interface OnsetConnectionSettingsSuccessListener {
        void setConnectionSettingsSuccess();
    }

    // 对外方式setOnsetConnectionSettingsSuccessListener
    public void setOnsetConnectionSettingsSuccessListener(OnsetConnectionSettingsSuccessListener onsetConnectionSettingsSuccessListener) {
        this.onsetConnectionSettingsSuccessListener = onsetConnectionSettingsSuccessListener;
    }

    // 封装方法setConnectionSettingsSuccessNext
    private void setConnectionSettingsSuccessNext() {
        if (onsetConnectionSettingsSuccessListener != null) {
            onsetConnectionSettingsSuccessListener.setConnectionSettingsSuccess();
        }
    }

    private OnsetConnectionSettingsFailedListener onsetConnectionSettingsFailedListener;

    // Inteerface--> 接口OnsetConnectionSettingsFailedListener
    public interface OnsetConnectionSettingsFailedListener {
        void setConnectionSettingsFailed();
    }

    // 对外方式setOnsetConnectionSettingsFailedListener
    public void setOnsetConnectionSettingsFailedListener(OnsetConnectionSettingsFailedListener onsetConnectionSettingsFailedListener) {
        this.onsetConnectionSettingsFailedListener = onsetConnectionSettingsFailedListener;
    }

    // 封装方法setConnectionSettingsFailedNext
    private void setConnectionSettingsFailedNext() {
        if (onsetConnectionSettingsFailedListener != null) {
            onsetConnectionSettingsFailedListener.setConnectionSettingsFailed();
        }
    }
}
