package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.WanSettingsResult;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;

/*
 * Created by qianli.ma on 2018/9/4 0004.
 */
public class GetWanSettingHelper {
    
    public void get( ) {
        RX.getInstant().getWanSettings(new ResponseObject<WanSettingsResult>() {
            @Override
            protected void onSuccess(WanSettingsResult result) {
                getWanSettingsSuccessNext(result);
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                getWanSettingsResultErrorNext(error);
            }

            @Override
            protected void onFailure() {
                getWanSettingsFailedNext();
            }

            @Override
            public void onError(Throwable e) {
                getWanSettingsErrorNext(e);
            }
        });
    }

    private OnGetwansettingsErrorListener onGetwansettingsErrorListener;

    // Inteerface--> 接口OnGetwansettingsErrorListener
    public interface OnGetwansettingsErrorListener {
        void getWanSettingsError(Throwable e);
    }

    // 对外方式setOnGetwansettingsErrorListener
    public void setOnGetwansettingsErrorListener(OnGetwansettingsErrorListener onGetwansettingsErrorListener) {
        this.onGetwansettingsErrorListener = onGetwansettingsErrorListener;
    }

    // 封装方法getWanSettingsErrorNext
    private void getWanSettingsErrorNext(Throwable e) {
        if (onGetwansettingsErrorListener != null) {
            onGetwansettingsErrorListener.getWanSettingsError(e);
        }
    }

    private OnGetWanSettingsFailedListener onGetWanSettingsFailedListener;

    // Inteerface--> 接口OnGetWanSettingsFailedListener
    public interface OnGetWanSettingsFailedListener {
        void getWanSettingsFailed();
    }

    // 对外方式setOnGetWanSettingsFailedListener
    public void setOnGetWanSettingsFailedListener(OnGetWanSettingsFailedListener onGetWanSettingsFailedListener) {
        this.onGetWanSettingsFailedListener = onGetWanSettingsFailedListener;
    }

    // 封装方法getWanSettingsFailedNext
    private void getWanSettingsFailedNext() {
        if (onGetWanSettingsFailedListener != null) {
            onGetWanSettingsFailedListener.getWanSettingsFailed();
        }
    }

    private OnGetWanSettingsResultErrorListener onGetWanSettingsResultErrorListener;

    // Inteerface--> 接口OnGetWanSettingsResultErrorListener
    public interface OnGetWanSettingsResultErrorListener {
        void getWanSettingsResultError(ResponseBody.Error error);
    }

    // 对外方式setOnGetWanSettingsResultErrorListener
    public void setOnGetWanSettingsResultErrorListener(OnGetWanSettingsResultErrorListener onGetWanSettingsResultErrorListener) {
        this.onGetWanSettingsResultErrorListener = onGetWanSettingsResultErrorListener;
    }

    // 封装方法getWanSettingsResultErrorNext
    private void getWanSettingsResultErrorNext(ResponseBody.Error error) {
        if (onGetWanSettingsResultErrorListener != null) {
            onGetWanSettingsResultErrorListener.getWanSettingsResultError(error);
        }
    }

    private OnGetWanSettingsSuccessListener onGetWanSettingsSuccessListener;

    // Inteerface--> 接口OnGetWanSettingsSuccessListener
    public interface OnGetWanSettingsSuccessListener {
        void getWanSettingsSuccess(WanSettingsResult wanSettings);
    }

    // 对外方式setOnGetWanSettingsSuccessListener
    public void setOnGetWanSettingsSuccessListener(OnGetWanSettingsSuccessListener onGetWanSettingsSuccessListener) {
        this.onGetWanSettingsSuccessListener = onGetWanSettingsSuccessListener;
    }

    // 封装方法getWanSettingsSuccessNext
    private void getWanSettingsSuccessNext(WanSettingsResult wanSettings) {
        if (onGetWanSettingsSuccessListener != null) {
            onGetWanSettingsSuccessListener.getWanSettingsSuccess(wanSettings);
        }
    }
}
