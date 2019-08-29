package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.ConnectBean;
import com.alcatel.wifilink.root.bean.ConnectedListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianli.ma on 2017/6/24.
 */

public class ModelHelper {

    public static List<ConnectBean> getConnectModel(ConnectedListBean result) {
        List<ConnectBean> connectBeans = new ArrayList<>();
        List<ConnectedListBean.Device> connectedList = result.getConnectedList();
        if (connectedList.size() > 0) {
            for (ConnectedListBean.Device device : connectedList) {
                connectBeans.add(new ConnectBean(device, false));
            }
        }

        return connectBeans;
    }

}
