package com.alcatel.wifilink.root.bean;

/**
 * Created by ZQ on 2017/6/22.
 */

public class Sharing_SambaSettings {

    /**
     *0: disable
      1: enable
     */
    private int SambaStatus;
    /**
     *  Whether allowing anonymous user to login
     *  0: disable
        1: enable
     */
    private int Anonymous;

    /**The auth type of sharing
     * 0: ReadOnly
       1: ReadWrite
     */
    private int AuthType;


    public int getSambaStatus() {
        return SambaStatus;
    }

    public void setSambaStatus(int sambaStatus) {
        SambaStatus = sambaStatus;
    }

    public int getAnonymous() {
        return Anonymous;
    }

    public void setAnonymous(int anonymous) {
        Anonymous = anonymous;
    }

    public int getAuthType() {
        return AuthType;
    }

    public void setAuthType(int authType) {
        AuthType = authType;
    }

    @Override
    public String toString() {
        return "Sharing_SambaSettings{" +
                "SambaStatus=" + SambaStatus +
                ", Anonymous=" + Anonymous +
                ", AuthType=" + AuthType +
                '}';
    }
}
