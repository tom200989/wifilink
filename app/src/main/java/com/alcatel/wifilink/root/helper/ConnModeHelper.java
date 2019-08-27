package com.alcatel.wifilink.root.helper;

import android.app.Activity;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.ToastTool;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectionSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectionSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetConnectionSettingsHelper;

/**
 * Created by qianli.ma on 2017/12/11 0011.
 */

public class ConnModeHelper {

    private Activity activity;

    public ConnModeHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * 切换连接模式(手动|自动)
     *
     * @param connMode
     */
    public void transferConnMode(int connMode) {
        // 1.获取当前的连接模式
        ConnectSettingHelper cshelper = new ConnectSettingHelper();
        cshelper.setOnNormalConnResultListener(result1 -> {
            GetConnectionSettingsBean cs = result1.deepClone();
            cs.setConnectMode(connMode);// 2.更改连接模式
            // 3.提交请求
            int connectMode = cs.getConnectMode();
            int roamingConnect = cs.getRoamingConnect();
            int pdpType = cs.getPdpType();
            int connOffTime = cs.getConnOffTime();
            SetConnectionSettingsHelper xSetConnectionSettingsHelper = new SetConnectionSettingsHelper();
            xSetConnectionSettingsHelper.setOnsetConnectionSettingsSuccessListener(() -> {
                // 4.再次获取连接模式
                GetConnectionSettingsHelper xGetConnectionSettingsHelper = new GetConnectionSettingsHelper();
                xGetConnectionSettingsHelper.setOnGetConnectionSettingsSuccessListener(attr -> connModeSuccessNext(result1));
                xGetConnectionSettingsHelper.setOnGetConnectionSettingsFailedListener(() -> toast(R.string.connect_failed));
                xGetConnectionSettingsHelper.getConnectionSettings();
            });
            xSetConnectionSettingsHelper.setOnsetConnectionSettingsFailedListener(() -> toast(R.string.connect_failed));
            xSetConnectionSettingsHelper.setConnectionSettings(connectMode, roamingConnect, pdpType, connOffTime);

        });
        cshelper.getConnSettingStatus();
    }

    private OnConnModeSuccessListener onConnModeSuccessListener;

    // 接口OnConnModeSuccessListener
    public interface OnConnModeSuccessListener {
        void connModeSuccess(GetConnectionSettingsBean attr);
    }

    // 对外方式setOnConnModeSuccessListener
    public void setOnConnModeSuccessListener(OnConnModeSuccessListener onConnModeSuccessListener) {
        this.onConnModeSuccessListener = onConnModeSuccessListener;
    }

    // 封装方法connModeSuccessNext
    private void connModeSuccessNext(GetConnectionSettingsBean attr) {
        if (onConnModeSuccessListener != null) {
            onConnModeSuccessListener.connModeSuccess(attr);
        }
    }

    public void toast(int resId) {
        ToastTool.show(activity, resId);
    }

}
