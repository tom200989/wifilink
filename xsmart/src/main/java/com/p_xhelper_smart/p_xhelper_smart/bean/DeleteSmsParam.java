package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wzhiqiang on 2019/8/1
 */
public class DeleteSmsParam implements Serializable {


    /**
     * DelFlag : 1
     * SMSArray : [1,2]
     */

    private int DelFlag;//删除类型 0: delete all SMS
                        //1: delete one record in Contact SMS list
                        //2: delete one record in Content SMS list
                        //3: delete one or more SMS
    private List<Integer> SMSArray;//要删除的sms的短信id

    public static final int CONS_DELETE_ALL = 0;
    public static final int CONS_DELETE_ONY_BY_CONTACT = 1;
    public static final int CONS_DELETE_ONY_BY_CONTENT = 2;
    public static final int CONS_DELETE_MORE = 3;


    public DeleteSmsParam() {
    }

    public int getDelFlag() {
        return DelFlag;
    }

    public void setDelFlag(int DelFlag) {
        this.DelFlag = DelFlag;
    }

    public List<Integer> getSMSArray() {
        return SMSArray;
    }

    public void setSMSArray(List<Integer> SMSArray) {
        this.SMSArray = SMSArray;
    }
}
