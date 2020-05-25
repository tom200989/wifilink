package com.alcatel.wifilink.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by qianli.ma on 2018/2/9 0009.
 */

public class FeedbackPhotoBean implements Serializable {
    private String url;
    private Bitmap bitmap;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
