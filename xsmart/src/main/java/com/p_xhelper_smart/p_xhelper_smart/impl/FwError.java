package com.p_xhelper_smart.p_xhelper_smart.impl;

/*
 * Created by qianli.ma on 2019/7/26 0026.
 */
public class FwError {

    public String code;
    public String message;

    public FwError() {
    }

    public FwError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FwError{");
        sb.append("\n").append("\t").append("code ='").append(code).append('\'');
        sb.append("\n").append("\t").append("message ='").append(message).append('\'');
        sb.append("\n}");
        return sb.toString();
    }
}
