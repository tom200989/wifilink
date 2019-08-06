package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/7/31
 */
public class GetConnectHotspotStateBean implements Serializable {


    /**
     * State : 0
     */


    private int State;//当前热点的连接状态 0: none  1: connecting  2: success  3: password error
                        //4: need password  5: fail 6: open 确认,有些ap从有加密改为了open的,需要用户确认才能连接

    public static final int CONS_CONNECT_STATE_NONE = 0;
    public static final int CONS_CONNECT_STATE_CONNECTING = 1;
    public static final int CONS_CONNECT_STATE_SUCCESS = 2;
    public static final int CONS_CONNECT_STATE_PW_ERROR = 3;
    public static final int CONS_CONNECT_STATE_PW_NEED = 4;
    public static final int CONS_CONNECT_STATE_FAIL = 5;
    public static final int CONS_CONNECT_STATE_OPEN = 6;

    public GetConnectHotspotStateBean() {
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }
}
