package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;
import java.util.List;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class SearchNetworkResultBean implements Serializable {


    /**
     * ListNetworkItem : [{"NetworkID":0,"State":1,"Numberic":"46001","Rat":2,"FullName":"UNICOM","ShortName":"UNICOM","mcc":"460","mnc":"1"},{"NetworkID":1,"State":3,"Numberic":"46000","Rat":1,"FullName":"CMCC","ShortName":"CMCC","mcc":"460","mnc":"0"}]
     * SearchState : 2
     * nw_list_len : 2
     */

    public static int CONS_SEARCH_NOT_NETWORK = 0;
    public static int CONS_SEARCHING = 1;
    public static int CONS_SEARCH_SUCCESSFUL = 2;
    public static int CONS_SEARCH_FAILED = 3;

    public static int CONS_NETWORK_UNKNOWN = 0;
    public static int CONS_NETWORK_AVAILABLE = 1;
    public static int CONS_CURRENT_REGISTER_NETWORK = 2;
    public static int CONS_FORBIDDEN_NETWORK = 3;

    public static int CONS_GSM = 1;
    public static int CONS_UMTS = 2;
    public static int CONS_LTE = 3;
    public static int CONS_CDMA = 4;
    public static int CONS_UNKNOWN = 5;

    private int SearchState;
    private int nw_list_len;
    private List<ListNetworkItemBean> ListNetworkItem;

    public SearchNetworkResultBean() {
    }

    public int getSearchState() {
        return SearchState;
    }

    public void setSearchState(int SearchState) {
        this.SearchState = SearchState;
    }

    public int getNw_list_len() {
        return nw_list_len;
    }

    public void setNw_list_len(int nw_list_len) {
        this.nw_list_len = nw_list_len;
    }

    public List<ListNetworkItemBean> getListNetworkItem() {
        return ListNetworkItem;
    }

    public void setListNetworkItem(List<ListNetworkItemBean> ListNetworkItem) {
        this.ListNetworkItem = ListNetworkItem;
    }

    public static class ListNetworkItemBean {
        /**
         * NetworkID : 0
         * State : 1
         * Numberic : 46001
         * Rat : 2
         * FullName : UNICOM
         * ShortName : UNICOM
         * mcc : 460
         * mnc : 1
         */

        private int NetworkID;
        private int State;
        private String Numberic;
        private int Rat;
        private String FullName;
        private String ShortName;
        private String mcc;
        private String mnc;

        public ListNetworkItemBean() {
        }

        public int getNetworkID() {
            return NetworkID;
        }

        public void setNetworkID(int NetworkID) {
            this.NetworkID = NetworkID;
        }

        public int getState() {
            return State;
        }

        public void setState(int State) {
            this.State = State;
        }

        public String getNumberic() {
            return Numberic;
        }

        public void setNumberic(String Numberic) {
            this.Numberic = Numberic;
        }

        public int getRat() {
            return Rat;
        }

        public void setRat(int Rat) {
            this.Rat = Rat;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String FullName) {
            this.FullName = FullName;
        }

        public String getShortName() {
            return ShortName;
        }

        public void setShortName(String ShortName) {
            this.ShortName = ShortName;
        }

        public String getMcc() {
            return mcc;
        }

        public void setMcc(String mcc) {
            this.mcc = mcc;
        }

        public String getMnc() {
            return mnc;
        }

        public void setMnc(String mnc) {
            this.mnc = mnc;
        }
    }
}
