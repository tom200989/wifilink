package com.alcatel.wifilink.root.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetProfileListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetProfileListHelper;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by qianli.ma on 2018/10/16 0016.
 */
public class ProfileHelper {
    
    public void get() {
        GetProfileListHelper xGetProfileListHelper = new GetProfileListHelper();
        xGetProfileListHelper.setOnGetProfileListListener(bean -> {
            GetProfileListBean result = new GetProfileListBean();
            result.setData_len(bean.getData_len());
            List<GetProfileListBean.ProfileBean> tempProfileList = new ArrayList<>();
            List<GetProfileListBean.ProfileBean> profileList =bean.getProfileList();
            for(GetProfileListBean.ProfileBean profileBean : profileList){
                GetProfileListBean.ProfileBean profileListBean = new GetProfileListBean.ProfileBean();
                profileListBean.setAPN(profileBean.getAPN());
                profileListBean.setAuthType(profileBean.getAuthType());
                profileListBean.setDailNumber(profileBean.getDailNumber());
                profileListBean.setDefault(profileBean.getDefault());
                profileListBean.setIPAdrress(profileBean.getIPAdrress());
                profileListBean.setIsPredefine(profileBean.getIsPredefine());
                profileListBean.setPassword(profileBean.getPassword());
                profileListBean.setPdpType(profileBean.getPdpType());
                profileListBean.setProfileID(profileBean.getProfileID());
                profileListBean.setProfileName(profileBean.getProfileName());
                profileListBean.setUserName(profileBean.getUserName());
            }
            result.setProfileList(tempProfileList);
            getProfileSuccessNext(result);
        });
        xGetProfileListHelper.setOnGetPrifileListFailListener(() -> {
            failedNext();
            resultErrorNext(null);
        });
        xGetProfileListHelper.getProfileList();
    }

    private OnFailedListener onFailedListener;

    // Inteerface--> 接口OnFailedListener
    public interface OnFailedListener {
        void failed();
    }

    // 对外方式setOnFailedListener
    public void setOnFailedListener(OnFailedListener onFailedListener) {
        this.onFailedListener = onFailedListener;
    }

    // 封装方法failedNext
    private void failedNext() {
        if (onFailedListener != null) {
            onFailedListener.failed();
        }
    }

    private OnResultErrorListener onResultErrorListener;

    // Inteerface--> 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError(FwError error);
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNext
    private void resultErrorNext(FwError error) {
        if (onResultErrorListener != null) {
            onResultErrorListener.resultError(error);
        }
    }

    private OnGetProfileSuccessListener onGetProfileSuccessListener;

    // Inteerface--> 接口OnGetProfileSuccessListener
    public interface OnGetProfileSuccessListener {
        void getProfileSuccess(GetProfileListBean profileList);
    }

    // 对外方式setOnGetProfileSuccessListener
    public void setOnGetProfileSuccessListener(OnGetProfileSuccessListener onGetProfileSuccessListener) {
        this.onGetProfileSuccessListener = onGetProfileSuccessListener;
    }

    // 封装方法getProfileSuccessNext
    private void getProfileSuccessNext(GetProfileListBean profileList) {
        if (onGetProfileSuccessListener != null) {
            onGetProfileSuccessListener.getProfileSuccess(profileList);
        }
    }
}
