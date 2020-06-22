package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class LoginBean implements Serializable {

    private String token;// token
    private String param0;// key
    private String param1;// iv
    private String uid;// 5G-CPE特有字段

    public LoginBean() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getParam0() {
        return param0;
    }

    public void setParam0(String param0) {
        this.param0 = param0;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
    
    
}
