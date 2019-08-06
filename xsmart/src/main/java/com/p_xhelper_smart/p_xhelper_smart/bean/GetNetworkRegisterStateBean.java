package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class GetNetworkRegisterStateBean implements Serializable {


    /**
     * regist_state : 3
     */

    public static int CONS_NOT_REGISETER = 0;
    public static int CONS_REGISTTING = 1;
    public static int CONS_REGISTER_SUCCESSFUL = 2;
    public static int CONS_REGISTRATION_FAILED = 3;

    private int regist_state;// 注册状态

    public GetNetworkRegisterStateBean() {
    }

    public int getRegist_state() {
        return regist_state;
    }

    public void setRegist_state(int regist_state) {
        this.regist_state = regist_state;
    }
}
