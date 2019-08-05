package com.alcatel.wifilink.root.bean;

/**
 * Created by tao.j on 2017/6/15.
 */

public class User_NewPasswdParams {
    String UserName;
    String CurrPassword;
    String NewPassword;

    public User_NewPasswdParams(String userName, String currPassword, String newPassword) {
        UserName = userName;
        CurrPassword = currPassword;
        NewPassword = newPassword;
    }
}
