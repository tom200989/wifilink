package com.alcatel.wifilink.network;

import android.content.Context;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.app.SmartLinkV3App;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by tao.j on 2017/6/15.
 */

public abstract class ResponseObject<T> extends Subscriber<ResponseBody<T>> {

    private static final String LOGGER_TAG = "ma";
    private Context mAppContext;


    public ResponseObject() {
        mAppContext = SmartLinkV3App.getInstance().getApplicationContext();
    }


    @Override
    public void onStart() {
        // Logs.d(LOGGER_TAG, "onStart");
    }

    @Override
    public void onCompleted() {
       // Logs.d(LOGGER_TAG, "onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        Logs.d(LOGGER_TAG, "onError: " + e.getMessage().toString());
        Logs.d("ma_api", "onError: " + e.getMessage().toString());
        if (e instanceof SocketTimeoutException) {
            ToastUtil.showMessage(mAppContext, R.string.connection_timed_out);
        } else if (e instanceof ConnectException) {
            ToastUtil.showMessage(mAppContext, R.string.network_disconnected);
        }

        onFailure();
    }

    @Override
    public  void onNext(ResponseBody<T> responseBody) {

        if (responseBody.getError() != null) {
            onResultError(responseBody.getError());
        } else {
            onSuccess(responseBody.getResult());
        }
    }

    protected abstract void onSuccess(T result);

    protected void onResultError(ResponseBody.Error error) {
        Logs.d(LOGGER_TAG, "onResultError: " + error.getMessage().toString());
        ToastUtil.showMessage(mAppContext, error.message);
    }

    protected void onFailure() {
        
    }
}
