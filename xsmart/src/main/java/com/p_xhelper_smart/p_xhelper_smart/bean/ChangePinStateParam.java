package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class ChangePinStateParam implements Serializable {

    /**
     * Pin : 1234
     * State : 1
     */

    public static int CONS_Disabled_PIN = 0;
    public static int CONS_Enabled_PIN = 1;

    private String Pin;
    private int State;

    public ChangePinStateParam() {
    }

    public ChangePinStateParam(String pin, int state) {
        Pin = pin;
        State = state;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String Pin) {
        this.Pin = Pin;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }
}
