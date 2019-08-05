package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2017/11/15 0015.
 */

public class Other_PinPukBean {
    public int flag;// 0:pin 1:puk

    public Other_PinPukBean(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
