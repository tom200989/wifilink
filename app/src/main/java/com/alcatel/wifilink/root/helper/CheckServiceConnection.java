package com.alcatel.wifilink.root.helper;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by qianli.ma on 2018/1/11 0011.
 */

public class CheckServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        // 1.此处通过强转获取到服务
        CheckService service = ((CheckService.CheckBinder) binder).getService();
        // 2.通过接口把服务对象传递出去
        getServiceNext(service);
        Log.v("service","CheckServiceConnection onServiceConnected");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.v("service","CheckServiceConnection onServiceDisconnected");
    }

    private OnServiceListener onServiceListener;

    // 接口OnServiceListener
    public interface OnServiceListener {
        void getService(CheckService service);
    }

    // 对外方式setOnServiceListener
    public void setOnServiceListener(OnServiceListener onServiceListener) {
        this.onServiceListener = onServiceListener;
    }

    // 封装方法getServiceNext
    private void getServiceNext(CheckService attr) {
        if (onServiceListener != null) {
            onServiceListener.getService(attr);
        }
    }
}
