package com.alcatel.wifilink.helper;

import com.alcatel.wifilink.bean.FeedbackCommitBean;
import com.alibaba.fastjson.JSONObject;

import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by qianli.ma on 2018/6/1 0001.
 */

public class FeedbackCommitHelper extends FeedbackBaseCallBackHelper {

    private String url = BASE_URL + "/v1.0/fbs/feedback";

    public void commit(String deviceName, String access_token, String sign_timeStamp_newToken, String commitJson) {
        RequestParams entity = getCommitEntity(deviceName, access_token, sign_timeStamp_newToken, commitJson);
        x.http().post(entity, this);
    }

    private RequestParams getCommitEntity(String deviceName, String access_token, String sign_timeStamp_newToken, String commitJson) {
        RequestParams entity = new RequestParams(url);
        // 1.设置超时
        entity.setConnectTimeout(TIME_OUT);
        // 2.设置请求文本格式
        entity.addHeader(CONTENT_TYPE, "application/json");
        // 3.根据设备名称设置KEY
        String key = "key=" + ((deviceName.equalsIgnoreCase(MW70)|deviceName.contains(MW70) ? MW70_KEY : MW120_KEY));
        key = key + ";token=" + access_token;
        key = key + sign_timeStamp_newToken;
        entity.addHeader(AUTHORIZATION, key);
        // 4.设置为json提交
        entity.setAsJsonContent(true);
        // 5.绑定提交要素
        entity.setBodyContent(commitJson);
        // 6.返回整合实体
        return entity;
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        FeedbackCommitBean feedbackCommitBean = JSONObject.parseObject(result, FeedbackCommitBean.class);
        successNext(feedbackCommitBean);
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

    private OnCommitSuccessListener onCommitSuccessListener;

    // 接口OnCommitSuccessListener
    public interface OnCommitSuccessListener {
        void success(FeedbackCommitBean commitResult);
    }

    // 对外方式setOnCommitSuccessListener
    public void setOnCommitSuccessListener(OnCommitSuccessListener onCommitSuccessListener) {
        this.onCommitSuccessListener = onCommitSuccessListener;
    }

    // 封装方法successNext
    private void successNext(FeedbackCommitBean commitResult) {
        if (onCommitSuccessListener != null) {
            onCommitSuccessListener.success(commitResult);
        }
    }

}
