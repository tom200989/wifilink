package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/27 0027.
 */
public class GetLoginStateBean implements Serializable {

    /**
     * State : 0
     * LoginRemainingTimes : 3
     * LockedRemainingTime : 0
     */

    public static int CONS_PWENCRYPT_ON = 1;// 需加密状态
    public static int CONS_PWENCRYPT_OFF = 0;// 无需加密状态
    public static int CONS_LOGOUT = 0;// 登出
    public static int CONS_LOGIN = 1;// 登陆
    public static int CONS_LOGIN_TIME_USER_OUT = 2;// 登陆次数用尽

    private int State;// 登陆状态
    private int LoginRemainingTimes;// 登陆剩余次数
    private int LockedRemainingTime;// 锁定剩余时间
    private int PwEncrypt;// 是否需要兼容EE的加密, 1:需要加密; 0:不需要加密

    public GetLoginStateBean() {
    }

    public int getPwEncrypt() {
        return PwEncrypt;
    }

    public void setPwEncrypt(int pwEncrypt) {
        PwEncrypt = pwEncrypt;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public int getLoginRemainingTimes() {
        return LoginRemainingTimes;
    }

    public void setLoginRemainingTimes(int LoginRemainingTimes) {
        this.LoginRemainingTimes = LoginRemainingTimes;
    }

    public int getLockedRemainingTime() {
        return LockedRemainingTime;
    }

    public void setLockedRemainingTime(int LockedRemainingTime) {
        this.LockedRemainingTime = LockedRemainingTime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GetLoginStateBean{");
        sb.append("\n").append("\t").append("State =").append(State);
        sb.append("\n").append("\t").append("LoginRemainingTimes =").append(LoginRemainingTimes);
        sb.append("\n").append("\t").append("LockedRemainingTime =").append(LockedRemainingTime);
        sb.append("\n").append("\t").append("PwEncrypt =").append(PwEncrypt);
        sb.append("\n}");
        return sb.toString();
    }
}
