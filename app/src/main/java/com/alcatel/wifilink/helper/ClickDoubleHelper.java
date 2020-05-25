package com.alcatel.wifilink.helper;

/*
 * Created by qianli.ma on 2019/8/19 0019.
 */
public class ClickDoubleHelper {

    private long mkeyTime; //点击2次返回键的时间

    public void click() {
        if ((System.currentTimeMillis() - mkeyTime) > 2000) {
            mkeyTime = System.currentTimeMillis();
            clickOneNext();
        } else {
            clickDoubleNext();
        }
    }

    private OnClickOneListener onClickOneListener;

    // Inteerface--> 接口OnClickOneListener
    public interface OnClickOneListener {
        void clickOne();
    }

    // 对外方式setOnClickOneListener
    public void setOnClickOneListener(OnClickOneListener onClickOneListener) {
        this.onClickOneListener = onClickOneListener;
    }

    // 封装方法clickOneNext
    private void clickOneNext() {
        if (onClickOneListener != null) {
            onClickOneListener.clickOne();
        }
    }

    private OnClickDoubleListener onClickDoubleListener;

    // Inteerface--> 接口OnClickDoubleListener
    public interface OnClickDoubleListener {
        void clickDouble();
    }

    // 对外方式setOnClickDoubleListener
    public void setOnClickDoubleListener(OnClickDoubleListener onClickDoubleListener) {
        this.onClickDoubleListener = onClickDoubleListener;
    }

    // 封装方法clickDoubleNext
    private void clickDoubleNext() {
        if (onClickDoubleListener != null) {
            onClickDoubleListener.clickDouble();
        }
    }
}
