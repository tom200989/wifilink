package com.alcatel.wifilink.root.bean;

/**
 * Created by tao.j on 2017/6/16.
 */

public class Sim_PinStateParams {

    String Pin;

    /**0: Disabled PIN, 1: Enabled PIN*/
    int State;

    public Sim_PinStateParams(String pin, int state) {
        Pin = pin;
        State = state;
    }
}
