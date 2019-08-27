package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.FeedbackLoginParam;
import com.alcatel.wifilink.root.bean.FeedbackLoginResult;
import com.alibaba.fastjson.JSONObject;

import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by qianli.ma on 2018/5/31 0031.
 */

public class FeedBackLoginHelperFeedback extends FeedbackBaseCallBack {

    private String url = BASE_URL + "/v1.0/wifi/device/login";
    private Class clz;

    public void login(String deviceName, String imei, String macAddr) {

        RequestParams entity = getLoginEntity(deviceName, imei, macAddr);
        x.http().post(entity, this);
    }

    @Override
    public void onSuccess(String json) {
        super.onSuccess(json);
        FeedbackLoginResult feedbackLoginResult = JSONObject.parseObject(json, FeedbackLoginResult.class);
        loginSuccessNext(feedbackLoginResult);
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
    private RequestParams getLoginEntity(String deviceName, String imei, String macAddr) {
        RequestParams entity = new RequestParams(url);
        // 1.设置超时
        entity.setConnectTimeout(TIME_OUT);
        // 2.设置请求文本格式
        entity.addHeader(CONTENT_TYPE, "application/json");
        // 3.根据设备名称设置KEY
        entity.addHeader(AUTHORIZATION, "key=" + (deviceName.equalsIgnoreCase(MW70)|deviceName.contains(MW70) ? MW70_KEY : MW120_KEY));
        // 4.设置为json提交
        entity.setAsJsonContent(true);
        // 5.生成登陆要素
        FeedbackLoginParam param = new FeedbackLoginParam();
        param.setImei(imei);
        param.setMac_addr(macAddr);
        String json = JSONObject.toJSONString(param).replace("\\n", "");
        // 6.绑定要素
        entity.setBodyContent(json);
        // 7.返回整合实体
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

    private OnLoginSuccessListener onLoginSuccessListener;

    // 接口OnLoginSuccessListener
    public interface OnLoginSuccessListener {
        void loginSuccess(FeedbackLoginResult feedbackLoginResult);
    }

    // 对外方式setOnLoginSuccessListener
    public void setOnLoginSuccessListener(OnLoginSuccessListener onLoginSuccessListener) {
        this.onLoginSuccessListener = onLoginSuccessListener;
    }

    // 封装方法loginSuccessNext
    private void loginSuccessNext(FeedbackLoginResult feedbackLoginResult) {
        if (onLoginSuccessListener != null) {
            onLoginSuccessListener.loginSuccess(feedbackLoginResult);
        }
    }
}
