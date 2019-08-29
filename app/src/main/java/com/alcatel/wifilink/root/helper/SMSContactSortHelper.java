package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.SMSContactBean;
import com.alcatel.wifilink.root.utils.RootUtils;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by qianli.ma on 2017/12/17 0017.
 */

public class SMSContactSortHelper implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        SMSContactBean s1 = (SMSContactBean) o1;
        SMSContactBean s2 = (SMSContactBean) o2;
        Date d1 = RootUtils.transferDateForText(s1.getSmscontact().getSMSTime());
        Date d2 = RootUtils.transferDateForText(s2.getSmscontact().getSMSTime());
        assert d1 != null;
        if (d1.after(d2))
            return -1;
        if (d1.equals(d2))
            return 0;
        return 1;
    }
}
