package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.utils.Logs;

import org.xutils.common.Callback;
import org.xutils.http.request.UriRequest;

/**
 * Created by qianli.ma on 2018/5/31 0031.
 */

public class FeedbackBaseCallBack implements Callback.CommonCallback<String>,Callback.ProgressCallback<String>{

    public String TAG = "FeedbackBaseCallBack";

    // public String BASE_URL="http://api.tcl-move.com";
    public String BASE_URL="https://www.alcatel-move.com";
    public String MW70_KEY = "5_fIYG5qO75t6XsftwccC6lkFM7qfCZRbUaIbfygaxSft6XI3iI";
    public String MW120_KEY = "-uhEwPon1Wn79y0-Dj6ZWBBWqz8cGuGEES34hrsLjqV4RVwoMMs";
    public String MW70 = "MW70";
    public String MW120 = "MW120";
    public int TIME_OUT = 3000;
    public String AUTHORIZATION = "Authorization";
    public String CONTENT_TYPE = "Content-Type";

    @Override
    public void responseBody(UriRequest uriRequest) {
        
    }

    @Override
    public void onSuccess(String result) {
        Logs.t(TAG).ii("Successful : " + result);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        Logs.t(TAG).ee("errors : " + ex.getMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {
        Logs.t(TAG).ee("cancel : " + cex.getMessage());
    }

    @Override
    public void onFinished() {
        Logs.t(TAG).vv("Finish");
    }

    @Override
    public void onWaiting() {
        
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {
        
    }
}
