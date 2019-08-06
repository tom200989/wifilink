package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/7/31
 */
public class GetWIFIExtenderSettingsBean implements Serializable {

    /**
     * StationEnable : 0
     * ExtenderInitingStatus : 0
     */

    public static final int CONS_STATION_DISABLE = 0;
    public static final int CONS_STATION_ENABLE = 1;

    public static final int CONS_INIT_ING = 0;
    public static final int CONS_INIT_COMPLETE = 1;
    public static final int CONS_INIT_FAIL = 2;

    private int StationEnable; //0: disable  1: enable
    private int ExtenderInitingStatus; //0:Initing  1:Complete  2:Inited Failed

    public GetWIFIExtenderSettingsBean() {
    }

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
