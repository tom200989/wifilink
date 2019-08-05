package com.alcatel.wifilink.root.helper;

import android.app.Activity;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.ConnectionSettings;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.helper.ConnectSettingHelper;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.ToastUtil_m;

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
        cshelper.setOnNormalConnResultListener(result -> {
            ConnectionSettings cs = result.deepClone();
            cs.setConnectMode(connMode);// 2.更改连接模式
            // 3.提交请求
            RX.getInstant().setConnectionSettings(cs, new ResponseObject() {
                @Override
                protected void onSuccess(Object result) {
                    // 4.再次获取连接模式
                    RX.getInstant().getConnectionSettings(new ResponseObject<ConnectionSettings>() {
                        @Override
                        protected void onSuccess(ConnectionSettings result) {
                            connModeSuccessNext(result);
                        }

                        @Override
                        protected void onResultError(ResponseBody.Error error) {
                            toast(R.string.connect_failed);
                        }

                        @Override
                        public void onError(Throwable e) {
                            toast(R.string.connect_failed);
                        }
                    });
                }

                @Override
                protected void onResultError(ResponseBody.Error error) {
                    toast(R.string.connect_failed);
                }

                @Override
                public void onError(Throwable e) {
                    toast(R.string.connect_failed);
                }
            });
        });
        cshelper.getConnSettingStatus();
    }

    private OnConnModeSuccessListener onConnModeSuccessListener;

    // 接口OnConnModeSuccessListener
    public interface OnConnModeSuccessListener {
        void connModeSuccess(ConnectionSettings attr);
    }

    // 对外方式setOnConnModeSuccessListener
    public void setOnConnModeSuccessListener(OnConnModeSuccessListener onConnModeSuccessListener) {
        this.onConnModeSuccessListener = onConnModeSuccessListener;
    }

    // 封装方法connModeSuccessNext
    private void connModeSuccessNext(ConnectionSettings attr) {
        if (onConnModeSuccessListener != null) {
            onConnModeSuccessListener.connModeSuccess(attr);
        }
    }

    public void toast(int resId) {
        ToastUtil_m.show(activity, resId);
    }

    public void toastLong(int resId) {
        ToastUtil_m.showLong(activity, resId);
    }

    public void toast(String content) {
        ToastUtil_m.show(activity, content);
    }

    public void to(Class ac, boolean isFinish) {
        CA.toActivity(activity, ac, false, isFinish, false, 0);
    }
}
