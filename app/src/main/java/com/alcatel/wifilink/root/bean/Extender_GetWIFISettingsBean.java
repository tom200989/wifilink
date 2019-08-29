package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_GetWIFISettingsBean {

    /**
     * StationEnable : 0
     */

    private int StationEnable;
    
    /**
     * ExtenderInitingStatus : 0
     */
    private int ExtenderInitingStatus;

    public int getStationEnable() {
        return StationEnable;
    }

    public void setStationEnable(int StationEnable) {
        this.StationEnable = StationEnable;
    }

    public int getExtenderInitingStatus() {
        return ExtenderInitingStatus;
    }

    public void setExtenderInitingStatus(int ExtenderInitingStatus) {
        this.ExtenderInitingStatus = ExtenderInitingStatus;
    }
}
