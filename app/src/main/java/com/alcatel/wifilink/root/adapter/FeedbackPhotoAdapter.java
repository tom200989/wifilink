package com.alcatel.wifilink.root.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.FeedbackPhotoBean;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.ToastUtil_m;

import java.util.List;

/**
 * Created by qianli.ma on 2018/2/7 0007.
 */

public class FeedbackPhotoAdapter extends RecyclerView.Adapter<FeedbackPhotoHolder> {

    private Context context;
    private List<FeedbackPhotoBean> bbs;

    public FeedbackPhotoAdapter(Context context, List<FeedbackPhotoBean> bbs) {
        this.context = context;
        this.bbs = bbs;
    }

    public void notifys(List<FeedbackPhotoBean> bbs) {
        this.bbs = bbs;
        notifyDataSetChanged();
    }

    @Override
    public FeedbackPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedbackPhotoHolder(LayoutInflater.from(context).inflate(R.layout.item_feedback_photo_rx, parent, false));
    }

    @Override
    public void onBindViewHolder(FeedbackPhotoHolder holder, int position) {
        holder.iv_photo.setImageBitmap(bbs.get(position).getBitmap());
        holder.iv_photo.setOnClickListener(v -> {
            // 打开图片
            clickPhotoShowNext(bbs.get(position));
        });
        holder.iv_photo_del.setOnClickListener(v -> {
            // 删除图片
            deletedPhotoNext(position);
        });
    }

    private OnDeletedPhotoListener onDeletedPhotoListener;

    // 接口OnDeletedPhotoListener
    public interface OnDeletedPhotoListener {
        void deletedPhoto(int position);
    }

    // 对外方式setOnDeletedPhotoListener
    public void setOnDeletedPhotoListener(OnDeletedPhotoListener onDeletedPhotoListener) {
        this.onDeletedPhotoListener = onDeletedPhotoListener;
    }

    // 封装方法deletedPhotoNext
    private void deletedPhotoNext(int position) {
        if (onDeletedPhotoListener != null) {
            onDeletedPhotoListener.deletedPhoto(position);
        }
    }

    private OnClickPhotoShowListener onClickPhotoShowListener;

    // 接口OnClickPhotoShowListener
    public interface OnClickPhotoShowListener {
        void clickPhotoShow(FeedbackPhotoBean feedbackPhotoBean);
    }

    // 对外方式setOnClickPhotoShowListener
    public void setOnClickPhotoShowListener(OnClickPhotoShowListener onClickPhotoShowListener) {
        this.onClickPhotoShowListener = onClickPhotoShowListener;
    }

    // 封装方法clickPhotoShowNext
    private void clickPhotoShowNext(FeedbackPhotoBean feedbackPhotoBean) {
        if (onClickPhotoShowListener != null) {
            onClickPhotoShowListener.clickPhotoShow(feedbackPhotoBean);
        }
    }

    @Override
    public int getItemCount() {
        return bbs != null | bbs.size() > 0 ? bbs.size() : 0;
    }

    public void toast(int resId) {
        ToastUtil_m.show(context, resId);
    }

    public void toastLong(int resId) {
        ToastUtil_m.showLong(context, resId);
    }

    public void toast(String content) {
        ToastUtil_m.show(context, content);
    }

    public void to(Class ac, boolean isFinish) {
        CA.toActivity(context, ac, false, isFinish, false, 0);
    }
}
