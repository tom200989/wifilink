package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class ChangePasswordParam implements Serializable {

    /**
     * UserName : admin
     * CurrPassword : admin
     * NewPassword : admin
     */

    private String UserName;
    private String CurrPassword;
    private String NewPassword;

    public ChangePasswordParam() {
    }

    public ChangePasswordParam(String userName, String currPassword, String newPassword) {
        UserName = userName;
        CurrPassword = currPassword;
        NewPassword = newPassword;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getCurrPassword() {
        return CurrPassword;
    }

    public void setCurrPassword(String CurrPassword) {
        this.CurrPassword = CurrPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String NewPassword) {
        this.NewPassword = NewPassword;
    }
}
