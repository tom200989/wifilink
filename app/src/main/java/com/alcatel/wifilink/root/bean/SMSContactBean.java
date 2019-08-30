package com.alcatel.wifilink.root.bean;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContactListBean;

/**
 * Created by qianli.ma on 2017/12/17 0017.
 */

public class SMSContactBean {

    public static final int CLICK = 0;
    public static final int SELETE_ALL = 1;
    public static final int DESELETE_ALL = -1;

    private GetSMSContactListBean.SMSContacBean smscontact;
    private boolean isLongClick;
    private int state = 0;// CLICK,SELECT ALL,DESELECT ALL

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public GetSMSContactListBean.SMSContacBean getSmscontact() {
        return smscontact;
    }

    public void setSmscontact(GetSMSContactListBean.SMSContacBean smscontact) {
        this.smscontact = smscontact;
    }

    public boolean isLongClick() {
        return isLongClick;
    }

    public void setLongClick(boolean longClick) {
        isLongClick = longClick;
    }

}
