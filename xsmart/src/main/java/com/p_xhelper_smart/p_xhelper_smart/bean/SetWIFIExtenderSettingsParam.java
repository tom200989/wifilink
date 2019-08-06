package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/8/1
 */
public class SetWIFIExtenderSettingsParam implements Serializable {

    public static final int CONS_STATION_DISABLE = 0;
    public static final int CONS_STATION_ENABLE = 1;

    private int StationEnable; //0: disable  1: enable

    public SetWIFIExtenderSettingsParam() {
    }

    public SetWIFIExtenderSettingsParam(int stationEnable) {
        StationEnable = stationEnable;
    }

    public int getStationEnable() {
        return StationEnable;
    }

    public void setStationEnable(int stationEnable) {
        StationEnable = stationEnable;
    }
}
