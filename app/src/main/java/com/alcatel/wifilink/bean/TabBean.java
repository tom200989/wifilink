package com.alcatel.wifilink.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/8/29 0029.
 */
public class TabBean implements Serializable {
    
    private boolean isShowTab;// 是否显示TAB

    public TabBean() {
    }

    public TabBean(boolean isShowTab) {
        this.isShowTab = isShowTab;
    }

    public boolean isShowTab() {
        return isShowTab;
    }

    public void setShowTab(boolean showTab) {
        isShowTab = showTab;
    }
}
