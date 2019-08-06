package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageRecordBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageRecordParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetUsageRecordHelper extends BaseHelper {

    public void getUsageRecord(String currentTime) {
        prepareHelperNext();
        GetUsageRecordParam param = new GetUsageRecordParam(currentTime);
        XSmart<GetUsageRecordBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_USAGE_RECORD).xParam(param).xPost(new XNormalCallback<GetUsageRecordBean>() {
            @Override
            public void success(GetUsageRecordBean result) {
                getUsageRecordSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getUsageRecordFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getUsageRecordFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }


    /*----------------------------------获取用户流量使用记录成功的回调------------------------------*/
    public interface OnGetUsageRecordSuccessListener {
        void getUsageRecordSuccess(GetUsageRecordBean bean);
    }

    private OnGetUsageRecordSuccessListener onGetUsageRecordSuccessListener;

    //对外方式setOnGetUsageRecordHel
    public void setOnGetUsageRecordHel(OnGetUsageRecordSuccessListener onGetUsageRecordHe) {
        this.onGetUsageRecordSuccessListener = onGetUsageRecordHe;
    }

    //封装方法getUsageRecordSuccessNext
    private void getUsageRecordSuccessNext(GetUsageRecordBean bean) {
        if (onGetUsageRecordSuccessListener != null) {
            onGetUsageRecordSuccessListener.getUsageRecordSuccess(bean);
        }
    }

    /*----------------------------------获取用户流量使用记录失败的回调------------------------------*/
    public interface OnGetUsageRecordFailListener {
        void getUsageRecordFail();
    }

    private OnGetUsageRecordFailListener onGetUsageRecordFailListener;

    //对外方式setOnGetUsageRecordFailListener
    public void setOnGetUsageRecordFailListener(OnGetUsageRecordFailListener onGetUsageRecordFailListener) {
        this.onGetUsageRecordFailListener = onGetUsageRecordFailListener;
    }

    //封装方法getUsageRecordFailNext
    private void getUsageRecordFailNext() {
        if (onGetUsageRecordFailListener != null) {
            onGetUsageRecordFailListener.getUsageRecordFail();
        }
    }
}
