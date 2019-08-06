package com.p_xhelper_smart.p_xhelper_smart.impl;

/*
 * Created by qianli.ma on 2019/7/26 0026.
 */
public abstract class XNormalListener<T> implements XBaseListener {

    /*
     * 为了让外部使用的时候, 在没有返回参数的时候, 不自动重写onNext()
     * 此处不能使用interface,而是要用abstract.并且提供给xhelper的方法只能使用普通方法
     * 不能使用abstract抽象方法.
     * 
     * 采取这种方案的主要目的:
     * 消除外部在没有响应参数时自动重写onNext的问题
     * 
     * */

    /**
     * 请求成功
     *
     * @param result 返回结果:注意这里的T是XResponceBody
     */
    public void onNext(T result) {

    }

}
