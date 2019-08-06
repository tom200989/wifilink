package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.SetUsageRecordClearParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class SetUsageRecordClearHelper extends BaseHelper {

    public void setUsageRecordClear(String clearTime) {
        prepareHelperNext();
        XSmart xSmart = new XSmart<>();
        SetUsageRecordClearParam param = new SetUsageRecordClearParam(clearTime);
        xSmart.xMethod(XCons.METHOD_SET_USAGE_RECORD_CLEAR).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                setUsageRecordClearSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                setUsageRecordClearFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                setUsageRecordClearFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------设置清除成功请求的回调------------------------------*/
    public interface OnSetUsageRecordClearSuccessListener {
        void setUsageRecordClearSuccess();
    }

    private OnSetUsageRecordClearSuccessListener onSetUsageRecordClearSuccessListener;

    //对外方式setOnSetUsageRecordClearSuccessListener
    public void setOnSetUsageRecordClearSuccessListener(OnSetUsageRecordClearSuccessListener onSetUsageRecordClearSuccessListener) {
        this.onSetUsageRecordClearSuccessListener = onSetUsageRecordClearSuccessListener;
    }

    //封装方法setUsageRecordClearSuccessNext
    private void setUsageRecordClearSuccessNext() {
        if (onSetUsageRecordClearSuccessListener != null) {
            onSetUsageRecordClearSuccessListener.setUsageRecordClearSuccess();
        }
    }

    /*----------------------------------设置清除请求失败的回调------------------------------*/
    public interface OnSetUsageRecordClearFailListener {
        void setUsageRecordClearFail();
    }

    private OnSetUsageRecordClearFailListener onSetUsageRecordClearFailListener;

    //对外方式setOnSetUsageRecordClearFailListener
    public void setOnSetUsageRecordClearFailListener(OnSetUsageRecordClearFailListener onSetUsageRecordClearFailListener) {
        this.onSetUsageRecordClearFailListener = onSetUsageRecordClearFailListener;
    }

    //封装方法setUsageRecordClearFailNext
    private void setUsageRecordClearFailNext() {
        if (onSetUsageRecordClearFailListener != null) {
            onSetUsageRecordClearFailListener.setUsageRecordClearFail();
        }
    }
}
