package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
public class GetDeviceUpgradeStateBean implements Serializable {
    /**
     * Status : 0
     * Process : 0
     */

    private int Status;
    private int Process;
    
    public static int CONS_NO_START_UPDATE = 0;
    public static int CONS_UPDATING = 1;
    public static int CONS_COMPLETE = 2;

    public GetDeviceUpgradeStateBean() {
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getProcess() {
        return Process;
    }

    public void setProcess(int Process) {
        this.Process = Process;
    }
}
