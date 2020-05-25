package com.alcatel.wifilink.helper;

import com.alcatel.wifilink.ue.activity.HomeActivity;

import java.util.Set;

/**
 * Created by qianli.ma on 2017/6/17.
 */

public class SmsCountHelper {

    /**
     * 获取缓冲区里的未读短信
     */
    public static int getUnreadCache(long contactId) {
        int unreadCache = 0;
        Set<Long> contactIds = HomeActivity.smsUnreadMap.keySet();
        for (Long id : contactIds) {
            if (id == contactId) {
                unreadCache = HomeActivity.smsUnreadMap.get(id);
            }
        }
        return unreadCache;
    }

}
