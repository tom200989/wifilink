package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class UnlockPinParam implements Serializable {
    /**
     * Pin : 1234
     */

    private String Pin;

    public UnlockPinParam() {
    }

    public UnlockPinParam(String pin) {
        Pin = pin;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String Pin) {
        this.Pin = Pin;
    }
}
