package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2017/6/24.
 */

public class UsageRecordParam {
    public String current_time;

    @Override
    public String toString() {
        return "UsageRecordParam{" + "current_time='" + current_time + '\'' + '}';
    }

    public UsageRecordParam(String current_time) {
        this.current_time = current_time;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }
}
