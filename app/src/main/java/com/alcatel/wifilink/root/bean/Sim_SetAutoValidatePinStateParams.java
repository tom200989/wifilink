package com.alcatel.wifilink.root.bean;

/**
 * Created by tao.j on 2017/6/16.
 */

public class Sim_SetAutoValidatePinStateParams {

    String Pin;
    /**
     0: Disable
     1: Enable
     */
    int State;

    public Sim_SetAutoValidatePinStateParams(String pin, int state) {
        Pin = pin;
        State = state;
    }
}
