package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
public class GetDeviceNewVersionBean implements Serializable {


    /**
     * State : 0
     * Version : 0100100_._
     * total_size : 0
     */

    private int State;
    private String Version;
    private int total_size;

    public static int CONS_CHECKING = 0;
    public static int CONS_NEW_VERSION = 1;
    public static int CONS_NO_NEW_VERSION = 2;
    public static int CONS_NO_CONNECT = 3;
    public static int CONS_SERVICE_NOT_AVAILABLE = 4;
    public static int CONS_CHECK_ERROR = 5;// 出错说明没有新版本

    public GetDeviceNewVersionBean() {
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public int getTotal_size() {
        return total_size;
    }

    public void setTotal_size(int total_size) {
        this.total_size = total_size;
    }
}
