package com.alcatel.wifilink.root.bean;

import com.alcatel.wifilink.root.utils.C_DataUti;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by qianli.ma on 2017/12/17 0017.
 */

public class Other_SMSContactSelfSort implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Other_SMSContactSelf s1 = (Other_SMSContactSelf) o1;
        Other_SMSContactSelf s2 = (Other_SMSContactSelf) o2;
        Date d1 = C_DataUti.formatDateFromString(s1.getSmscontact().getSMSTime());
        Date d2 = C_DataUti.formatDateFromString(s2.getSmscontact().getSMSTime());
        if (d1.after(d2))
            return -1;
        if (d1.equals(d2))
            return 0;
        return 1;
    }
}
