package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class ChangePinCodeParam implements Serializable {


    /**
     * NewPin : 1234
     * CurrentPin : 1111
     */

    private String NewPin;
    private String CurrentPin;

    public ChangePinCodeParam() {
    }

    public ChangePinCodeParam(String newPin, String currentPin) {
        NewPin = newPin;
        CurrentPin = currentPin;
    }

    public String getNewPin() {
        return NewPin;
    }

    public void setNewPin(String NewPin) {
        this.NewPin = NewPin;
    }

    public String getCurrentPin() {
        return CurrentPin;
    }

    public void setCurrentPin(String CurrentPin) {
        this.CurrentPin = CurrentPin;
    }
}
