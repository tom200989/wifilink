package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class SearchHotspotHelper extends BaseHelper {

    public void searchHotSpot() {
        prepareHelperNext();
        XSmart xSmart = new XSmart();
        xSmart.xMethod(XCons.METHOD_SEARCH_HOTSPOT).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                searchHotSpotSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                searchHotSpotFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                searchHotSpotFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }
    
    /*----------------------------------触发搜索热点成功的回调------------------------------*/
    public interface OnSearchHotSpotSuccessListener {
        void searchHotSpotSuccess();
    }

    private OnSearchHotSpotSuccessListener onSearchHotSpotSuccessListener;

    //对外方式setOnSearchHotSpotSuccessListener
    public void setOnSearchHotSpotSuccessListener(OnSearchHotSpotSuccessListener onSearchHotSpotSuccessListener) {
        this.onSearchHotSpotSuccessListener = onSearchHotSpotSuccessListener;
    }

    //封装方法
    private void searchHotSpotSuccessNext() {
        if (onSearchHotSpotSuccessListener != null) {
            onSearchHotSpotSuccessListener.searchHotSpotSuccess();
        }
    }

    /*----------------------------------触发搜索热点失败的回调------------------------------*/
    public interface OnSearchHotSpotFailListener {
        void searchHotSpotFail();
    }

    private OnSearchHotSpotFailListener onSearchHotSpotFailListener;

    //对外方式setOnSearchHotSpotFailListener
    public void setOnSearchHotSpotFailListener(OnSearchHotSpotFailListener onSearchHotSpotFailListener) {
        this.onSearchHotSpotFailListener = onSearchHotSpotFailListener;
    }

    //封装方法searchHotSpotFailNext
    private void searchHotSpotFailNext() {
        if (onSearchHotSpotFailListener != null) {
            onSearchHotSpotFailListener.searchHotSpotFail();
        }
    }
}
