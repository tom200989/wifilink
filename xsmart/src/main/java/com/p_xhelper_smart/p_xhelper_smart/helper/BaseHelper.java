package com.p_xhelper_smart.p_xhelper_smart.helper;

/*
 * Created by qianli.ma on 2019/7/29 0029.
 */
public class BaseHelper {

    /* 请求前接口 */
    private OnPrepareHelperListener onPrepareHelperListener;

    // Inteerface--> 接口OnPrepareHelperListener
    public interface OnPrepareHelperListener {
        void prepareHelper();
    }

    // 对外方式setOnPrepareHelperListener
    public void setOnPrepareHelperListener(OnPrepareHelperListener onPrepareHelperListener) {
        this.onPrepareHelperListener = onPrepareHelperListener;
    }

    // 封装方法prepareHelperNext
    protected void prepareHelperNext() {
        if (onPrepareHelperListener != null) {
            onPrepareHelperListener.prepareHelper();
        }
    }

    /* 请求结束接口 */
    private OnDoneHelperListener onDoneHelperListener;

    // Inteerface--> 接口OnDoneHelperListener
    public interface OnDoneHelperListener {
        void doneHelper();
    }

    // 对外方式setOnDoneHelperListener
    public void setOnDoneHelperListener(OnDoneHelperListener onDoneHelperListener) {
        this.onDoneHelperListener = onDoneHelperListener;
    }

    // 封装方法doneHelperNext
    protected void doneHelperNext() {
        if (onDoneHelperListener != null) {
            onDoneHelperListener.doneHelper();
        }
    }

}
