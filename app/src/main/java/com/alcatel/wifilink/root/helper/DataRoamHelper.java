package com.alcatel.wifilink.root.helper;

import android.app.Activity;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectionSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectionSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetConnectionSettingsHelper;

/**
 * Created by qianli.ma on 2017/12/11 0011.
 */

public class DataRoamHelper {

    private Activity activity;

    public DataRoamHelper(Activity activity) {
        this.activity = activity;
    }

    public void transfer() {
        // 1.先获取当前的漫游允许连接状态
        ConnectSettingHelper cshelper = new ConnectSettingHelper();
        cshelper.setOnNormalConnResultListener(result -> {
            int roamingConnect = result.getRoamingConnect();
            int needRoam = roamingConnect == Cons.WHEN_ROAM_CAN_CONNECT ? Cons.WHEN_ROAM_NOT_CONNECT : Cons.WHEN_ROAM_CAN_CONNECT;
            result.setRoamingConnect(needRoam);// 2.切换连接状态
            // 3.提交请求
            int connectMode = result.getConnectMode();
            int pdpType = result.getPdpType();
            int connOffTime = result.getConnOffTime();
            SetConnectionSettingsHelper xSetConnectionSettingsHelper = new SetConnectionSettingsHelper();
            xSetConnectionSettingsHelper.setOnsetConnectionSettingsSuccessListener(() -> {
                // 4.再次获取
                GetConnectionSettingsHelper xGetConnectionSettingsHelper = new GetConnectionSettingsHelper();
                xGetConnectionSettingsHelper.setOnGetConnectionSettingsSuccessListener(attr -> roamConnSuccessNext(result));
                xGetConnectionSettingsHelper.setOnGetConnectionSettingsFailedListener(() -> toast(R.string.connect_failed));
                xGetConnectionSettingsHelper.getConnectionSettings();

            });
            xSetConnectionSettingsHelper.setOnsetConnectionSettingsFailedListener(() -> toast(R.string.connect_failed));
            xSetConnectionSettingsHelper.setConnectionSettings(connectMode, roamingConnect, pdpType, connOffTime);

        });
        cshelper.getConnSettingStatus();
    }

    private OnRoamConnSuccessListener onRoamConnSuccessListener;

    // 接口OnRoamConnSuccessListener
    public interface OnRoamConnSuccessListener {
        void roamConnSuccess(GetConnectionSettingsBean attr);
    }

    // 对外方式setOnRoamConnSuccessListener
    public void setOnRoamConnSuccessListener(OnRoamConnSuccessListener onRoamConnSuccessListener) {
        this.onRoamConnSuccessListener = onRoamConnSuccessListener;
    }

    // 封装方法roamConnSuccessNext
    private void roamConnSuccessNext(GetConnectionSettingsBean attr) {
        if (onRoamConnSuccessListener != null) {
            onRoamConnSuccessListener.roamConnSuccess(attr);
        }
    }

    public void toast(int resId) {
        ToastUtil_m.show(activity, resId);
    }



}
