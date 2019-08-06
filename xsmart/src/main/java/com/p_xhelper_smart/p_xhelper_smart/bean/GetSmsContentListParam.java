package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/7/31
 */
public class GetSmsContentListParam implements Serializable {

    /**
     * Page : 0
     * ContactId : 1
     */

    private int Page; //页数 0：返回所有 1:返回第一页，每页有10条
    private int ContactId;//The id in 6.2 GetSMSContactList

    public GetSmsContentListParam() {
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int Page) {
        this.Page = Page;
    }

    public int getContactId() {
        return ContactId;
    }

    public void setContactId(int ContactId) {
        this.ContactId = ContactId;
    }
}
