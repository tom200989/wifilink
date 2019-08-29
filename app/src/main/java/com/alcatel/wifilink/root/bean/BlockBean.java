package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2017/6/24.
 */

public class BlockBean {
    public BlockListBean.BlockDevice block;
    public boolean isEdit;

    public BlockBean(BlockListBean.BlockDevice block, boolean isEdit) {
        this.block = block;
        this.isEdit = isEdit;
    }

    @Override
    public String toString() {
        return "BlockBean{" + "block=" + block + ", isEdit=" + isEdit + '}';
    }

    public BlockListBean.BlockDevice getBlock() {
        return block;
    }

    public void setBlock(BlockListBean.BlockDevice block) {
        this.block = block;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
