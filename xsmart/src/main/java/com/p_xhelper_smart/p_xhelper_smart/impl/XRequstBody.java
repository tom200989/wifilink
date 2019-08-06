package com.p_xhelper_smart.p_xhelper_smart.impl;

public class XRequstBody {

    String jsonrpc = "2.0";// 固定2.0
    String method;
    Object params;// 可变param
    String id = "1.8";// 该值随意传

    public XRequstBody() {
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
