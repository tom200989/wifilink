package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/29 0029.
 */
public class LoginParam implements Serializable {

    /**
     * UserName : admin
     * Password : admin
     */

    private String UserName;
    private String Password;

    public LoginParam() {
    }

    public LoginParam(String userName, String password) {
        UserName = userName;
        Password = password;
    }

    //@JSONField(name = "UserName") // 一定要加, 否则fastjson自动转为全小写
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    //@JSONField(name = "Password")// 一定要加, 否则fastjson自动转为全小写
    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LoginParam{");
        sb.append("\n").append("\t").append("UserName ='").append(UserName).append('\'');
        sb.append("\n").append("\t").append("Password ='").append(Password).append('\'');
        sb.append("\n}");
        return sb.toString();
    }
}
