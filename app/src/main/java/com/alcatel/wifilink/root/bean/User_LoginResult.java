package com.alcatel.wifilink.root.bean;

/**
 * Created by tao.j on 2017/6/22.
 */

public class User_LoginResult {
    
    private String token;
    private String param0;
    private String param1;

    public String getToken() {
        return token;
    }

    public String getKey() {
        return param0;
    }

    public String getIv() {
        return param1;
    }
}
