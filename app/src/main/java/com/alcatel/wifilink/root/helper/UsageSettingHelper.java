package com.alcatel.wifilink.root.helper;

import android.content.Context;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetUsageSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetUsageSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetUsageSettingsHelper;

/**
 * Created by qianli.ma on 2017/12/13 0013.
 */

public class UsageSettingHelper {

    private Context context;

    public UsageSettingHelper(Context context) {
        this.context = context;
    }

    /**
     * 获取流量设置
     */
    public void getUsageSetting() {
        GetUsageSettingsHelper xGetUsageSettingsHelper = new GetUsageSettingsHelper();
        xGetUsageSettingsHelper.setOnGetUSageSettingsSuccessListener(result -> {
            getSuccessNext(result);
        });
        xGetUsageSettingsHelper.setOnGetUsageSettingsFailListener(() -> {
            toast(R.string.connect_failed);
            errorNext();
        });
        xGetUsageSettingsHelper.getUsageSetting();

    }

    private OngetSuccessListener ongetSuccessListener;

    // 接口OngetSuccessListener
    public interface OngetSuccessListener {
        void getSuccess(GetUsageSettingsBean attr);
    }

    // 对外方式setOngetSuccessListener
    public void setOngetSuccessListener(OngetSuccessListener ongetSuccessListener) {
        this.ongetSuccessListener = ongetSuccessListener;
    }

    // 封装方法getSuccessNext
    private void getSuccessNext(GetUsageSettingsBean attr) {
        if (ongetSuccessListener != null) {
            ongetSuccessListener.getSuccess(attr);
        }
    }

    /**
     * 提交流量设置
     *
     * @param us
     */
    public void setUsageSetting(GetUsageSettingsBean us) {
        SetUsageSettingsParam param = new SetUsageSettingsParam();
        param.copy(us);
        SetUsageSettingsHelper xSetUsageSettingsHelper = new SetUsageSettingsHelper();
        xSetUsageSettingsHelper.setOnSetUsageSettingsSuccessListener(() -> {
            GetUsageSettingsHelper xGetUsageSettingsHelper = new GetUsageSettingsHelper();
            xGetUsageSettingsHelper.setOnGetUSageSettingsSuccessListener(result -> {
                setSuccessNext(result);
            });
            xGetUsageSettingsHelper.setOnGetUsageSettingsFailListener(() -> {
                toast(R.string.connect_failed);
                errorNext();
            });
            xGetUsageSettingsHelper.getUsageSetting();
        });
        xSetUsageSettingsHelper.setOnSetUsageSettingsFailListener(() -> {
            toast(R.string.connect_failed);
            errorNext();
        });
        xSetUsageSettingsHelper.setUsageSettings(param);

    }

    private OnSetSuccessListener onSetSuccessListener;

    // 接口OnSetSuccessListener
    public interface OnSetSuccessListener {
        void setSuccess(GetUsageSettingsBean attr);
    }

    // 对外方式setOnSetSuccessListener
    public void setOnSetSuccessListener(OnSetSuccessListener onSetSuccessListener) {
        this.onSetSuccessListener = onSetSuccessListener;
    }

    // 封装方法setSuccessNext
    private void setSuccessNext(GetUsageSettingsBean attr) {
        if (onSetSuccessListener != null) {
            onSetSuccessListener.setSuccess(attr);
        }
    }


    private OnErrorListener onErrorListener;

    // 接口OnErrorListener
    public interface OnErrorListener {
        void error();
    }

    // 对外方式setOnErrorListener
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    // 封装方法errorNext
    private void errorNext() {
        if (onErrorListener != null) {
            onErrorListener.error();
        }
    }

    private void toast(int resId) {
        ToastUtil_m.show(context, resId);
    }

    private void toastLong(int resId) {
        ToastUtil_m.showLong(context, resId);
    }

    private void toast(String content) {
        ToastUtil_m.show(context, content);
    }

    private void to(Class ac, boolean isFinish) {
        CA.toActivity(context, ac, false, isFinish, false, 0);
    }

}
