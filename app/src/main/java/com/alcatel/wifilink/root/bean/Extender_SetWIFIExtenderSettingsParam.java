package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_SetWIFIExtenderSettingsParam {

    /**
     * StationEnable : 0
     */

    private int StationEnable;

    public Extender_SetWIFIExtenderSettingsParam(int stationEnable) {
        StationEnable = stationEnable;
    }

    public int getStationEnable() {
        return StationEnable;
    }

    public void setStationEnable(int StationEnable) {
        this.StationEnable = StationEnable;
    }
}
