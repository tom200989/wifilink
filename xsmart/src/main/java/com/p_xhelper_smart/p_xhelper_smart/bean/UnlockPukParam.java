package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class UnlockPukParam implements Serializable {

    /**
     * Puk : 12345678
     * Pin : 1234
     */

    private String Puk;
    private String Pin;

    public UnlockPukParam() {
    }

    public UnlockPukParam(String puk, String pin) {
        Puk = puk;
        Pin = pin;
    }

    public String getPuk() {
        return Puk;
    }

    public void setPuk(String Puk) {
        this.Puk = Puk;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String Pin) {
        this.Pin = Pin;
    }
}
