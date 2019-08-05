package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2018/5/31 0031.
 */

public class FeedbackLoginResult {
    /**
     * uid : 1234567890
     * access_token : 9wegfweg8ge7erthrt6h54h5hr321
     * ExpiredAt : 1563636842
     * ExpiredTime : 2018-03-26
     */

    private String uid;
    private String access_token;
    private String ExpiredAt;
    private String ExpiredTime;

    public FeedbackLoginResult() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpiredAt() {
        return ExpiredAt;
    }

    public void setExpiredAt(String ExpiredAt) {
        this.ExpiredAt = ExpiredAt;
    }

    public String getExpiredTime() {
        return ExpiredTime;
    }

    public void setExpiredTime(String ExpiredTime) {
        this.ExpiredTime = ExpiredTime;
    }
}
