package com.alcatel.wifilink.bean;

import java.io.Serializable;

/**
 * Created by qianli.ma on 2018/2/7 0007.
 */

public class FeedbackTypeBean implements Serializable {
    private String typeName;
    private boolean isSelected;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "FeedbackTypeBean{" + "typeName='" + typeName + '\'' + ", isSelected=" + isSelected + '}';
    }
}
