package com.alcatel.wifilink.root.bean;

import android.graphics.Bitmap;

/**
 * Created by qianli.ma on 2018/2/9 0009.
 */

public class FeedbackPhotoBean {
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
