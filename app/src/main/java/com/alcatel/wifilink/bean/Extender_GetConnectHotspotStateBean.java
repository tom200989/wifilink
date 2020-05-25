package com.alcatel.wifilink.bean;

import java.io.Serializable;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_GetConnectHotspotStateBean implements Serializable {
    /**
     * State : 0
     */

    private int State;

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }
}
