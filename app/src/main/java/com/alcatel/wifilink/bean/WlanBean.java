package com.alcatel.wifilink.bean;

import com.hiber.tools.DeepCloneBean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/8/20 0020.
 */
public class WlanBean extends DeepCloneBean implements Serializable {

    private boolean mSsidBroadcast;// 发送+返回
    private boolean mApIsolation;// 发送+返回
    private String mCountry;// 发送+返回
    private int mChannel;// 发送+返回
    private int mFrequency;// 从WLAN返回时不用设置
    private int mBandwidth;// 发送+返回
    private int mMode;// 发送+返回

    public WlanBean() {
    }

    public boolean ismSsidBroadcast() {
        return mSsidBroadcast;
    }

    public void setmSsidBroadcast(boolean mSsidBroadcast) {
        this.mSsidBroadcast = mSsidBroadcast;
    }

    public boolean ismApIsolation() {
        return mApIsolation;
    }

    public void setmApIsolation(boolean mApIsolation) {
        this.mApIsolation = mApIsolation;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public int getmChannel() {
        return mChannel;
    }

    public void setmChannel(int mChannel) {
        this.mChannel = mChannel;
    }

    public int getmFrequency() {
        return mFrequency;
    }

    public void setmFrequency(int mFrequency) {
        this.mFrequency = mFrequency;
    }

    public int getmBandwidth() {
        return mBandwidth;
    }

    public void setmBandwidth(int mBandwidth) {
        this.mBandwidth = mBandwidth;
    }

    public int getmMode() {
        return mMode;
    }

    public void setmMode(int mMode) {
        this.mMode = mMode;
    }


    @Override
    public String toString() {
        return "WlanBean{" + "mSsidBroadcast=" + mSsidBroadcast + ", mApIsolation=" + mApIsolation + ", mCountry='" + mCountry + '\'' + ", mChannel=" + mChannel + ", mFrequency=" + mFrequency + ", mBandwidth=" + mBandwidth + ", mMode=" + mMode + '}';
    }
}
