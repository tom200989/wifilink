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
            profileSuccessNext(result);
        });
        xGetProfileListHelper.setOnGetPrifileListFailListener(this::profileFailedNext);
        xGetProfileListHelper.getProfileList();
    }

    /* ************************** Success ***************************** */

    private OnProfileSuccessListener onProfileSuccessListener;

    // Inteerface--> 接口OnProfileSuccessListener
    public interface OnProfileSuccessListener {
        void profileSuccess(GetProfileListBean profileList);
    }

    // 对外方式setOnProfileSuccessListener
    public void setOnProfileSuccessListener(OnProfileSuccessListener onProfileSuccessListener) {
        this.onProfileSuccessListener = onProfileSuccessListener;
    }

    // 封装方法ProfileSuccessNext
    private void profileSuccessNext(GetProfileListBean profileList) {
        if (onProfileSuccessListener != null) {
            onProfileSuccessListener.profileSuccess(profileList);
        }
    }

    /* ************************** Failed ***************************** */

    private OnProfileFailedListener onProfileFailedListener;

    // Inteerface--> 接口OnProfileFailedListener
    public interface OnProfileFailedListener {
        void profileFailed();
    }

    // 对外方式setOnProfileFailedListener
    public void setOnProfileFailedListener(OnProfileFailedListener onProfileFailedListener) {
        this.onProfileFailedListener = onProfileFailedListener;
    }

    // 封装方法ProfileFailedNext
    private void profileFailedNext() {
        if (onProfileFailedListener != null) {
            onProfileFailedListener.profileFailed();
        }
    }
}
