package com.alcatel.wifilink.root.bean;

/**
 * Created by ZQ on 2017/6/21.
 */

public class Update_DeviceUpgradeState {


    /**
     *  Current Check State
     * 0: No start update(UI does not send the startAnim update command)
     1: updating (Download Firmware phase)
     2: complete
     */
    private int Status;
    /**
     *  If Status =1,Process:1- 100
     */
    private int Process;



    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getProcess() {
        return Process;
    }

    public void setProcess(int process) {
        Process = process;
    }

    @Override
    public String toString() {
        return "Update_DeviceUpgradeState{" +
                "Status=" + Status +
                ", Process=" + Process +
                '}';
    }
}
