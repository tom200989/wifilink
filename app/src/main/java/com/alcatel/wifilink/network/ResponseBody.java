package com.alcatel.wifilink.network;

/**
 * Created by tao.j on 2017/6/14.
 */

public class ResponseBody<T> {

    String jsonrpc;
    T result;
    String id;
    Error error;

    public Error getError() {
        return error;
    }

    public T getResult() {
        return result;
    }

    public class Error{
        String code;
        String message;

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
