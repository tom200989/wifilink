package com.alcatel.wifilink.root.bean;

/**
 * Created by tao.j on 2017/6/15.
 */

public class User_LoginParams {
    private String UserName;
    private String Password;

    public User_LoginParams(String userName, String password) {
        UserName = userName;
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
