package com.alcatel.wifilink.root.helper;

import java.util.Set;

import static com.alcatel.wifilink.root.ue.activity.HomeActivity.smsUnreadMap;

/**
 * Created by qianli.ma on 2017/6/17.
 */

public class SmsCountHelper {

    /**
     * 获取缓冲区里的未读短信
     */
    public static int getUnreadCache(long contactId) {
        int unreadCache = 0;
        Set<Long> contactIds = smsUnreadMap.keySet();
        for (Long id : contactIds) {
            if (id == contactId) {
                unreadCache = smsUnreadMap.get(id);
            }
        }
        return unreadCache;
    }

}
