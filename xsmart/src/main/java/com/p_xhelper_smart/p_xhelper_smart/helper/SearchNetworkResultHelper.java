package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SearchNetworkResultBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class SearchNetworkResultHelper extends BaseHelper {

    /**
     * 搜索网络结果
     */
    public void searchNetworkResult() {
        prepareHelperNext();
        XSmart<SearchNetworkResultBean> xSearch = new XSmart<>();
        xSearch.xMethod(XCons.METHOD_SEARCH_NETWORK_RESULT);
        xSearch.xPost(new XNormalCallback<SearchNetworkResultBean>() {
            @Override
            public void success(SearchNetworkResultBean result) {
                searchNetworkResultSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                searchNetworkResultFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                searchNetworkResultFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnSearchNetworkResultSuccessListener onSearchNetworkResultSuccessListener;

    // Inteerface--> 接口OnSearchNetworkResultSuccessListener
    public interface OnSearchNetworkResultSuccessListener {
        void searchNetworkResultSuccess(SearchNetworkResultBean networkResultBean);
    }

    // 对外方式setOnSearchNetworkResultSuccessListener
    public void setOnSearchNetworkResultSuccessListener(OnSearchNetworkResultSuccessListener onSearchNetworkResultSuccessListener) {
        this.onSearchNetworkResultSuccessListener = onSearchNetworkResultSuccessListener;
    }

    // 封装方法searchNetworkResultSuccessNext
    private void searchNetworkResultSuccessNext(SearchNetworkResultBean networkResultBean) {
        if (onSearchNetworkResultSuccessListener != null) {
            onSearchNetworkResultSuccessListener.searchNetworkResultSuccess(networkResultBean);
        }
    }

    private OnSearchNetworkResultFailedListener onSearchNetworkResultFailedListener;

    // Inteerface--> 接口OnSearchNetworkResultFailedListener
    public interface OnSearchNetworkResultFailedListener {
        void searchNetworkResultFailed();
    }

    // 对外方式setOnSearchNetworkResultFailedListener
    public void setOnSearchNetworkResultFailedListener(OnSearchNetworkResultFailedListener onSearchNetworkResultFailedListener) {
        this.onSearchNetworkResultFailedListener = onSearchNetworkResultFailedListener;
    }

    // 封装方法searchNetworkResultFailedNext
    private void searchNetworkResultFailedNext() {
        if (onSearchNetworkResultFailedListener != null) {
            onSearchNetworkResultFailedListener.searchNetworkResultFailed();
        }
    }
}
