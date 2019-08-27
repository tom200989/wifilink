package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.SMSContentList;
import com.alcatel.wifilink.root.utils.RootUtils;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by qianli.ma on 2017/6/29.
 */

public class SmsContentSortHelper implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        SMSContentList.SMSContentBean s1 = (SMSContentList.SMSContentBean) o1;
        SMSContentList.SMSContentBean s2 = (SMSContentList.SMSContentBean) o2;
        Date d1 = RootUtils.transferDateForText(s1.getSMSTime());
        Date d2 = RootUtils.transferDateForText(s2.getSMSTime());
        assert d1 != null;
        if (d1.after(d2))
            return 1;
        if (d1.equals(d2))
            return 0;
        return -1;
    }
}
