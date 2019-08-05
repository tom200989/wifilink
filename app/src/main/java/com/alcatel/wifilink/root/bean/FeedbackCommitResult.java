package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2018/6/1 0001.
 */

public class FeedbackCommitResult {
    /**
     * error_id : 0--> 如果是0则成功
     * error_msg : ok
     */

    private int error_id;
    private String error_msg;

    public int getError_id() {
        return error_id;
    }

    public void setError_id(int error_id) {
        this.error_id = error_id;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
