package com.p_xhelper_smart.p_xhelper_smart.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/8/1
 */
public class SetDeviceUnblockParam implements Serializable {

    /**
     * DeviceName : huide-iPhone
     * MacAddress : a0:99:9b:70:0b:c4
     */
    @JSONField(name = "DeviceName")
    private String DeviceName;//设备名
    @JSONField(name =  "MacAddress")
    private String MacAddress;//设备mac地址

    public SetDeviceUnblockParam() {
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String DeviceName) {
        this.DeviceName = DeviceName;
    }

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String MacAddress) {
        this.MacAddress = MacAddress;
    }
}
