package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/8/1
 */
public class SetSambaSettingsParam implements Serializable {


    /**
     * SambaStatus : 1
     * Anonymous : 0
     * AuthType : 0
     */

    private int SambaStatus;//0: disable 1: enable
    private int Anonymous;//是否允许匿名用户登录 0: disable 1: enable
    private int AuthType;//The auth type of sharing 0: ReadOnly 1: ReadWrite

    public static final int CONS_SAMBA_DISABLE = 0;
    public static final int CONS_SAMBA_ENABLE = 1;

    public static final int CONS_ANONYMMOUS_DISABLE = 0;
    public static final int CONS_ANONYMMOUS_ENABLE = 1;

    public static final int CONS_AUTY_READ_ONLY = 0;
    public static final int CONS_AUTY_READ_AND_WIRTE = 1;

    public SetSambaSettingsParam() {
    }

    public int getSambaStatus() {
        return SambaStatus;
    }

    public void setSambaStatus(int SambaStatus) {
        this.SambaStatus = SambaStatus;
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
