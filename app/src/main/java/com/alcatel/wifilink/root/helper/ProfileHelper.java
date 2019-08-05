package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.ProfileList;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;

/*
 * Created by qianli.ma on 2018/10/16 0016.
 */
public class ProfileHelper {
    
    public void get() {
        RX.getInstant().getProfileList(new ResponseObject<ProfileList>() {
            @Override
            protected void onSuccess(ProfileList result) {
                getProfileSuccessNext(result);
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                resultErrorNext(error);
            }

            @Override
            protected void onFailure() {
                failedNext();
            }
        });
    }

    private OnFailedListener onFailedListener;

    // Inteerface--> 接口OnFailedListener
    public interface OnFailedListener {
        void failed();
    }

    // 对外方式setOnFailedListener
    public void setOnFailedListener(OnFailedListener onFailedListener) {
        this.onFailedListener = onFailedListener;
    }

    // 封装方法failedNext
    private void failedNext() {
        if (onFailedListener != null) {
            onFailedListener.failed();
        }
    }

    private OnResultErrorListener onResultErrorListener;

    // Inteerface--> 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError(ResponseBody.Error error);
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNext
    private void resultErrorNext(ResponseBody.Error error) {
        if (onResultErrorListener != null) {
            onResultErrorListener.resultError(error);
        }
    }

    private OnGetProfileSuccessListener onGetProfileSuccessListener;

    // Inteerface--> 接口OnGetProfileSuccessListener
    public interface OnGetProfileSuccessListener {
        void getProfileSuccess(ProfileList profileList);
    }

    // 对外方式setOnGetProfileSuccessListener
    public void setOnGetProfileSuccessListener(OnGetProfileSuccessListener onGetProfileSuccessListener) {
        this.onGetProfileSuccessListener = onGetProfileSuccessListener;
    }

    // 封装方法getProfileSuccessNext
    private void getProfileSuccessNext(ProfileList profileList) {
        if (onGetProfileSuccessListener != null) {
            onGetProfileSuccessListener.getProfileSuccess(profileList);
        }
    }
}
