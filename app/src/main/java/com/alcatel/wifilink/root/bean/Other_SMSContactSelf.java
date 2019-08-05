package com.alcatel.wifilink.root.bean;

import com.alcatel.wifilink.root.bean.SMSContactList;

/**
 * Created by qianli.ma on 2017/12/17 0017.
 */

public class Other_SMSContactSelf {

    private SMSContactList.SMSContact smscontact;
    private boolean isLongClick;
    private int state = 0;// CLICK,SELECT ALL,DESELECT ALL

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public SMSContactList.SMSContact getSmscontact() {
        return smscontact;
    }

    public void setSmscontact(SMSContactList.SMSContact smscontact) {
        this.smscontact = smscontact;
    }

    public boolean isLongClick() {
        return isLongClick;
    }

    public void setLongClick(boolean longClick) {
        isLongClick = longClick;
    }

}
