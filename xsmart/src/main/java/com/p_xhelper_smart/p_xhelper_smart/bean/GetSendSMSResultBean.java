package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/7/31
 */
public class GetSendSMSResultBean implements Serializable {

    /**
     * SendStatus : 2
     */

    private int SendStatus; //0 : none   1 : sending  2 : success  3: fail still sending last message
    // 4 : fail with Memory full  5: fail

    public static final int CONS_SEND_STATUS_NONE = 0;
    public static final int CONS_SEND_STATUS_SENDING = 1;
    public static final int CONS_SEND_STATUS_SUCCESS = 2;
    public static final int CONS_SEND_STATUS_FAIL_LAST_MSG = 3;
    public static final int CONS_SEND_STATUS_FAIL_MEMORY_FULL = 4;
    public static final int CONS_SEND_STATUS_FAIL = 5;

    public GetSendSMSResultBean() {
    }

    public int getSendStatus() {
        return SendStatus;
    }

    public void setSendStatus(int SendStatus) {
        this.SendStatus = SendStatus;
    }
}
