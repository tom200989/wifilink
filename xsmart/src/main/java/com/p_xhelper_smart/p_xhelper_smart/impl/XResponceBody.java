package com.p_xhelper_smart.p_xhelper_smart.impl;

/*
 * Created by qianli.ma on 2019/7/26 0026.
 */
public class XResponceBody<T> {

    private String jsonrpc;
    private T result;// 可变响应体
    private String id;
    private FwError error;

    public XResponceBody() {
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FwError getError() {
        return error;
    }

    public void setError(FwError error) {
        this.error = error;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("XResponceBody{");
        sb.append("\n").append("\t").append("jsonrpc ='").append(jsonrpc).append('\'');
        sb.append("\n").append("\t").append("result =").append(result);
        sb.append("\n").append("\t").append("id ='").append(id).append('\'');
        sb.append("\n").append("\t").append("error =").append(error);
        sb.append("\n}");
        return sb.toString();
    }
}
