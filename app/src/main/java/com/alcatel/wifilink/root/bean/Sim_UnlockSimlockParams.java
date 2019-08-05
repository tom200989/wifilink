package com.alcatel.wifilink.root.bean;

/**
 * Created by tao.j on 2017/6/16.
 */

public class Sim_UnlockSimlockParams {
    /**
     * SIMLockState:
     0: nck
     1: nsck
     2: spck
     3: cck
     4: pck
     15,16,17,18,19: rck
     */
    int SIMLockState;
    String SIMLockCode;

    public Sim_UnlockSimlockParams(int SIMLockState, String SIMLockCode) {
        this.SIMLockState = SIMLockState;
        this.SIMLockCode = SIMLockCode;
    }
}
