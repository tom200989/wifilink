package com.alcatel.wifilink.helper;

import com.alcatel.wifilink.utils.RootUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContentListBean;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by qianli.ma on 2017/6/29.
 */

public class SmsContentSortHelper implements Comparator<GetSMSContentListBean.SMSContentListBean> {

    @Override
    public int compare(GetSMSContentListBean.SMSContentListBean s1, GetSMSContentListBean.SMSContentListBean s2) {
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
