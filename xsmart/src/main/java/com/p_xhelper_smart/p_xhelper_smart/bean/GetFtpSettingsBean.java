package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
public class GetFtpSettingsBean implements Serializable {

    /**
     * FtpStatus : 1
     * Anonymous : 0
     * AuthType : 0
     */

    private int FtpStatus;
    private int Anonymous;
    private int AuthType;

    public static int CONS_FTPSTATUS_DISABLE = 0;
    public static int CONS_FTPSTATUS_ENABLE = 1;
    
    public static int CONS_ANONYMOUS_DISABLE = 0;
    public static int CONS_ANONYMOUS_ENABLE = 1;
    
    public static int CONS_READONLY = 0;
    public static int CONS_READWRITE = 1;

    public GetFtpSettingsBean() {
    }

    public int getFtpStatus() {
        return FtpStatus;
    }

    public void setFtpStatus(int FtpStatus) {
        this.FtpStatus = FtpStatus;
    }

    public int getAnonymous() {
        return Anonymous;
    }

    public void setAnonymous(int Anonymous) {
        this.Anonymous = Anonymous;
    }

    public int getAuthType() {
        return AuthType;
    }

    public void setAuthType(int AuthType) {
        this.AuthType = AuthType;
    }
}
