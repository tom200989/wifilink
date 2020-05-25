package com.alcatel.wifilink.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/8/20 0020.
 */
public class LanguageBean implements Serializable {
    
    private String language_country;// 文本(es-default,es-mx)
    private boolean isCurrent;// 是否为当前

    public LanguageBean() {
    }

    public String getLanguage_country() {
        return language_country;
    }

    public void setLanguage_country(String language_country) {
        this.language_country = language_country;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }
}
