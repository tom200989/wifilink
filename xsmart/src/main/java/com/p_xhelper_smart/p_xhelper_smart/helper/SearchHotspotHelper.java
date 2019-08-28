package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
@SuppressWarnings("unchecked")
public class SearchHotspotHelper extends BaseHelper {

    public void searchHotSpot() {
        prepareHelperNext();
        XSmart xSmart = new XSmart();
        xSmart.xMethod(XCons.METHOD_SEARCH_HOTSPOT).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                searchHotSpotSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                appErrorNext();
            }

            @Override
            public void fwError(FwError fwError) {
                fwErrorNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }
    
    /*----------------------------------触发搜索热点成功的回调------------------------------*/
    public interface OnSearchHotSpotSuccessListener {
        void searchHotSpotSuccess(Object object);
    }

    private OnSearchHotSpotSuccessListener onSearchHotSpotSuccessListener;

    //对外方式setOnSearchHotSpotSuccessListener
    public void setOnSearchHotSpotSuccessListener(OnSearchHotSpotSuccessListener onSearchHotSpotSuccessListener) {
        this.onSearchHotSpotSuccessListener = onSearchHotSpotSuccessListener;
    }

    //封装方法
    private void searchHotSpotSuccessNext(Object object) {
        if (onSearchHotSpotSuccessListener != null) {
            onSearchHotSpotSuccessListener.searchHotSpotSuccess(object);
        }
    }

    /*----------------------------------触发搜索热点失败的回调------------------------------*/
    public interface OnAppErrorListener {
        void appError();
    }

    private OnAppErrorListener onAppErrorListener;

    //对外方式setOnAppErrorListener
    public void setOnAppErrorListener(OnAppErrorListener onAppErrorListener) {
        this.onAppErrorListener = onAppErrorListener;
    }

    //封装方法appErrorNext
    private void appErrorNext() {
        if (onAppErrorListener != null) {
            onAppErrorListener.appError();
        }
    }

    public interface OnFwErrorListener {
        void fwError();
    }

    private OnFwErrorListener onFwErrorListener;

    //对外方式setOnFwErrorListener
    public void setOnFwErrorListener(OnFwErrorListener onFwErrorListener) {
        this.onFwErrorListener = onFwErrorListener;
    }

    //封装方法fwErrorNext
    private void fwErrorNext() {
        if (onFwErrorListener != null) {
            onFwErrorListener.fwError();
        }
    }

}
