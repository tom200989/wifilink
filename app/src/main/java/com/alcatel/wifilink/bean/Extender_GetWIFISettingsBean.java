package com.alcatel.wifilink.bean;

import java.io.Serializable;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_GetWIFISettingsBean implements Serializable {

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
