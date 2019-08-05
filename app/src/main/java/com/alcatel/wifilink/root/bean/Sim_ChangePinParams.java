package com.alcatel.wifilink.root.bean;

/**
 * Created by tao.j on 2017/6/16.
 */

public class Sim_ChangePinParams {
        String NewPin;
        String CurrentPin;
    public Sim_ChangePinParams(String newPin, String currentPin) {
        NewPin = newPin;
        CurrentPin = currentPin;
    }
}
