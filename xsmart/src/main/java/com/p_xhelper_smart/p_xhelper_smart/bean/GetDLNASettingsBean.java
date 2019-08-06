package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
public class GetDLNASettingsBean implements Serializable {


    /**
     * DlnaStatus : 1
     * DlnaName : DLNA Server
     */

    private int DlnaStatus;
    private String DlnaName;

    public static int CONS_DISABLE = 0;
    public static int CONS_ENABLE = 1;

    public GetDLNASettingsBean() {
    }

    public int getDlnaStatus() {
        return DlnaStatus;
    }

    public void setDlnaStatus(int DlnaStatus) {
        this.DlnaStatus = DlnaStatus;
    }

    public String getDlnaName() {
        return DlnaName;
    }

    public void setDlnaName(String DlnaName) {
        this.DlnaName = DlnaName;
    }
}
