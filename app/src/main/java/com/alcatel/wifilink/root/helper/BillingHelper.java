package com.alcatel.wifilink.root.helper;

import android.app.Activity;

/**
 * Created by qianli.ma on 2017/12/13 0013.
 */

public class BillingHelper {
    private Activity activity;

    public BillingHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * 设置结算日
     */
    public void setBillingDay(int day) {
        // 1. 先获取当前的流量对象
        UsageSettingHelper helper = new UsageSettingHelper();
        helper.setOnGetUsageSettingsSuccessListener(attr -> {
            // 2.修改结算日
            attr.setBillingDay(day);
            // 3.提交最新请求
            UsageSettingHelper ush_s = new UsageSettingHelper();
            ush_s.setOnSetUsageSettingSuccessListener(attr1 -> setBillSuccessNext());
            ush_s.setUsageSetting(attr);
        });
        helper.getUsageSetting();
    }

    private OnSetBillSuccessListener onSetBillSuccessListener;

    // Inteerface--> 接口OnSetBillSuccessListener
    public interface OnSetBillSuccessListener {
        void setBillSuccess();
    }

    // 对外方式setOnSetBillSuccessListener
    public void setOnSetBillSuccessListener(OnSetBillSuccessListener onSetBillSuccessListener) {
        this.onSetBillSuccessListener = onSetBillSuccessListener;
    }

    // 封装方法setBillSuccessNext
    private void setBillSuccessNext() {
        if (onSetBillSuccessListener != null) {
            onSetBillSuccessListener.setBillSuccess();
        }
    }


}
