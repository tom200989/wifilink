package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/8/2
 */
public class GetSmsContactListParam implements Serializable {

    private int Page; //页数，0：返回所有 1-~:返回相应的页数，每页有10条


    public GetSmsContactListParam() {
    }

    public GetSmsContactListParam(int page) {
        Page = page;
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }
}
