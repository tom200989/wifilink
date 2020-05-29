package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by Administrator on 2020/5/29.
 * 该类仅提供给HH42(外包)项目使用
 */
public class DisConnectHH42Bean implements Serializable {

    public static final int NOT_RECONNECT = 0;// 切断后不重拨
    public static final int RECONNECT = 1;// 切断后重拨

    private int ReconnectFlag;// HH42特有的标记

    public int getReconnectFlag() {
        return ReconnectFlag;
    }

    public void setReconnectFlag(int reconnectFlag) {
        ReconnectFlag = reconnectFlag;
    }
}
