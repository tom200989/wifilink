package com.alcatel.wifilink.root.bean;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetBlockDeviceListBean;

/**
 * Created by qianli.ma on 2017/6/24.
 */

public class BlockBean {
    
    public GetBlockDeviceListBean.BlockDeviceBean block;
    public boolean isEdit;

    public BlockBean(GetBlockDeviceListBean.BlockDeviceBean block, boolean isEdit) {
        this.block = block;
        this.isEdit = isEdit;
    }

    public GetBlockDeviceListBean.BlockDeviceBean getBlock() {
        return block;
    }

    public void setBlock(GetBlockDeviceListBean.BlockDeviceBean block) {
        this.block = block;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
