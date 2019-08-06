package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/7/31
 */
public class GetUsageRecordParam implements Serializable {

    private String current_time;//格式是yyyy-mm-dd HH:MM:SS

    public GetUsageRecordParam() {
    }

    public GetUsageRecordParam(String current_time) {
        this.current_time = current_time;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }
}
