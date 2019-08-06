package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/8/1
 */
public class SetUsageRecordClearParam implements Serializable {


    /**
     * clear_time : 2014-11-06 14:03:20
     */

    private String clear_time;//清除时间 格式是yyyy-mm-dd HH:MM:SS

    public SetUsageRecordClearParam() {
    }

    public SetUsageRecordClearParam(String clear_time) {
        this.clear_time = clear_time;
    }

    public String getClear_time() {
        return clear_time;
    }

    public void setClear_time(String clear_time) {
        this.clear_time = clear_time;
    }
}
