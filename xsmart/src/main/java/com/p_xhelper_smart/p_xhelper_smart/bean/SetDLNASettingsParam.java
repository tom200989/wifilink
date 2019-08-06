package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/8/1
 */
public class SetDLNASettingsParam implements Serializable {

    /**
     * DlnaStatus : 1
     * DlnaName : DLNA Server
     */

    private int DlnaStatus;//0: disable 1: enable
    private String DlnaName;//名字

    public static final int CONS_DLNA_DISABLE = 0;
    public static final int CONS_DLNA_ABLE = 1;

    public SetDLNASettingsParam() {
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
