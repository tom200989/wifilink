package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2017/6/27.
 */

public class SmsInitState {

    public int state;

    public SmsInitState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SmsInitState{" + "state=" + state + '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
