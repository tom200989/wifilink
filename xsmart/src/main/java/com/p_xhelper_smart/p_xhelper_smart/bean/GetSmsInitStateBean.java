package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetSmsInitStateBean implements Serializable {

    private int state; // 短信初始化状态 .0: Complete  1: Initing

    public static final int CONS_SMS_INIT_STATUS_COMPLETE = 0;
    public static final int CONS_SMS_INIT_STATUS_INITING = 1;

    public GetSmsInitStateBean() {
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
