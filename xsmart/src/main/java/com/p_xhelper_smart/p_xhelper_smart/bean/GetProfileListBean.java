package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetProfileListBean implements Serializable {

    /**
     * ProfileList : [{"ProfileID":1,"ProfileName":"China Unicom","APN":"3GNET","Password":"","AuthType":-1,"DailNumber":"*99#","UserName":"","Default":1,"IsPredefine":0,"IPAdrress":"","PdpType":-1}]
     * data_len : 1
     */

    private int data_len;
    private List<ProfileBean> ProfileList;

    public GetProfileListBean() {
    }

    public int getData_len() {
        return data_len;
    }

    public void setData_len(int data_len) {
        this.data_len = data_len;
    }

    public List<ProfileBean> getProfileList() {
        return ProfileList;
    }

    public void setProfileList(List<ProfileBean> ProfileList) {
        this.ProfileList = ProfileList;
    }

    public static class ProfileBean implements Serializable {
        /**
         * ProfileID : 1
         * ProfileName : China Unicom
         * APN : 3GNET
         * Password :
         * AuthType : -1
         * DailNumber : *99#
         * UserName :
         * Default : 1
         * IsPredefine : 0
         * IPAdrress :
         * PdpType : -1
         */

        private int ProfileID;
        private String ProfileName;
        private String APN;//APN name
        private String Password;
        private int AuthType; //0: None 1: PAP 2: CHAP  3: PAP& CHAP
        private String DailNumber;
        private String UserName;
        private int Default; //0: not Default 1: Default
        private int IsPredefine;//Number | Is predefine or user create profile
        private String IPAdrress;
        private int PdpType;//Number | 0:IPV4  1:IPV6  2:IPV4V6

        public static final int CONS_AUTH_TYPE_NONE = 0;
        public static final int CONS_AUTH_TYPE_PAP = 1;
        public static final int CONS_AUTH_TYPE_CHAP = 2;
        public static final int CONS_AUTH_TYPE_PAN_AND_CHAP = 3;

        public static final int CONS_DEFAULT_NOT = 0;
        public static final int CONS_DEFAULT_DEFAULT = 1;

        public static final int CONS_PREDEFINE_TRUE = 0;
        public static final int CONS_PREDEFINE_FAULT = 1;

        public static final int CONS_PDP_TYPE_IPV4 = 0;
        public static final int CONS_PDP_TYPE_IPV6 = 1;
        public static final int CONS_PDP_TYPE_IPV4v6 = 2;

        public ProfileBean() {
        }

        public int getProfileID() {
            return ProfileID;
        }

        public void setProfileID(int ProfileID) {
            this.ProfileID = ProfileID;
        }

        public String getProfileName() {
            return ProfileName;
        }

        public void setProfileName(String ProfileName) {
            this.ProfileName = ProfileName;
        }

        public String getAPN() {
            return APN;
        }

        public void setAPN(String APN) {
            this.APN = APN;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public int getAuthType() {
            return AuthType;
        }

        public void setAuthType(int AuthType) {
            this.AuthType = AuthType;
        }

        public String getDailNumber() {
            return DailNumber;
        }

        public void setDailNumber(String DailNumber) {
            this.DailNumber = DailNumber;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public int getDefault() {
            return Default;
        }

        public void setDefault(int Default) {
            this.Default = Default;
        }

        public int getIsPredefine() {
            return IsPredefine;
        }

        public void setIsPredefine(int IsPredefine) {
            this.IsPredefine = IsPredefine;
        }

        public String getIPAdrress() {
            return IPAdrress;
        }

        public void setIPAdrress(String IPAdrress) {
            this.IPAdrress = IPAdrress;
        }

        public int getPdpType() {
            return PdpType;
        }

        public void setPdpType(int PdpType) {
            this.PdpType = PdpType;
        }
    }
}
