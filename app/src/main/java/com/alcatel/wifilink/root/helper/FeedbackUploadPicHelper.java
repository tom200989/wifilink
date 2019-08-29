package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.FeedbackPicFidBean;
import com.alibaba.fastjson.JSONObject;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by qianli.ma on 2018/5/31 0031.
 */

public class FeedbackUploadPicHelper extends FeedbackBaseCallBackHelper {

    String url = BASE_URL + "/v1.0/fs";

    public void upload(String access_token, String deviceName, File file) {

        RequestParams entity = getUploadPicEntity(access_token, deviceName, file);
        x.http().post(entity, this);
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        FeedbackPicFidBean feedbackPicFid = JSONObject.parseObject(result, FeedbackPicFidBean.class);
        String fid = feedbackPicFid.getFid();
        successNext(fid);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
        errorNext(ex);
    }

    @Override
    public void onCancelled(CancelledException cex) {
        super.onCancelled(cex);
        errorNext(cex);
    }

    @Override
    public void onFinished() {
        super.onFinished();
    }

    /**
     * 获取登陆的请求参数
     */
    private RequestParams getUploadPicEntity(String access_token, String deviceName, File file) {
        RequestParams entity = new RequestParams(url);
        // 1.设置超时
        entity.setConnectTimeout(TIME_OUT);
        // 2.根据设备名称设置KEY
        String key = "key=" + ((deviceName.equalsIgnoreCase(MW70)|deviceName.contains(MW70) ? MW70_KEY : MW120_KEY));
        key = key + (";token=" + access_token);
        entity.addHeader(AUTHORIZATION, key);
        entity.addBodyParameter("file", file);
        entity.setMultipart(true);/* 这一句必须要加才能上传成功 */
        // 3.返回整合实体
        return entity;
    }

    private OnErrorListener onErrorListener;

    // 接口OnErrorListener
    public interface OnErrorListener {
        void error(Throwable error);
    }

    // 对外方式setOnErrorListener
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    // 封装方法errorNext
    private void errorNext(Throwable error) {
        if (onErrorListener != null) {
            onErrorListener.error(error);
        }
    }

    private OnUploadSuccessListener onUploadSuccessListener;

    // 接口OnUploadSuccessListener
    public interface OnUploadSuccessListener {
        void success(String fid);
    }

    // 对外方式setOnUploadSuccessListener
    public void setOnUploadSuccessListener(OnUploadSuccessListener onUploadSuccessListener) {
        this.onUploadSuccessListener = onUploadSuccessListener;
    }

    // 封装方法successNext
    private void successNext(String fid) {
        if (onUploadSuccessListener != null) {
            onUploadSuccessListener.success(fid);
        }
    }

}
