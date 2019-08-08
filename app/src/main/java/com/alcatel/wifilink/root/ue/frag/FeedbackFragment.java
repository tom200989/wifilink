package com.alcatel.wifilink.root.ue.frag;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.root.adapter.FeedbackPhotoAdapter;
import com.alcatel.wifilink.root.adapter.FeedbackTypeAdapter;
import com.alcatel.wifilink.root.bean.FeedbackCommitParam;
import com.alcatel.wifilink.root.bean.FeedbackPhotoBean;
import com.alcatel.wifilink.root.bean.FeedbackTypeBean;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.FeedBackLoginHelperFeedback;
import com.alcatel.wifilink.root.helper.FeedbackCommitHelper;
import com.alcatel.wifilink.root.helper.FeedbackEnterWatcher;
import com.alcatel.wifilink.root.helper.FeedbackUploadPicHelper;
import com.alcatel.wifilink.root.ue.activity.HomeRxActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.FormatTools;
import com.alcatel.wifilink.root.utils.Lgg;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.PermissionUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.utils.fraghandler.FragmentBackHandler;
import com.alcatel.wifilink.root.widget.DialogOkWidget;
import com.alcatel.wifilink.root.widget.ExtenderWait;
import com.alibaba.fastjson.JSONObject;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by qianli.ma on 2018/2/6 0006.
 */

public class FeedbackFragment extends Fragment implements FragmentBackHandler {

    Unbinder unbinder;

    @BindView(R.id.iv_feedback_back)
    ImageView iv_back;// 返回键

    @BindView(R.id.rl_feedback_selectType)
    RelativeLayout rlSelectType;// 点击选择类型
    @BindView(R.id.et_feedback_selectType)
    EditText etSelectType;// 已选择类型

    @BindView(R.id.rl_feedback_selectType_choice_list)
    RelativeLayout rlSelectTypeChoiceList_pop;// 选择类型弹框
    @BindView(R.id.iv_feedback_selectType_choice_list_gray)
    ImageView ivSelectTypeChoiceListGray;// 选择类型弹框背景(点击后panel消隐)
    @BindView(R.id.rcv_feedback_selectType_choice_list)
    RecyclerView rcvSelectTypeChoiceList;// 选择类型列表


    @BindView(R.id.et_feedback_enterFeedback)
    EditText etEnterFeedback;// 输入建议
    @BindView(R.id.tv_feedback_stringNum)
    TextView tvStringNum;// 字符个数

    @BindView(R.id.tv_feedback_photo_count)
    TextView tvPhotoCount;// 已经添加的图片数量
    @BindView(R.id.rl_feedback_photo_logo)
    RelativeLayout rlPhotoLogo;// 添加图片按钮
    @BindView(R.id.rcv_feedback_photo)
    RecyclerView rcvPhoto;// 图片列表

    @BindView(R.id.rl_feedback_selectType_submit_ok)
    RelativeLayout rlSelectTypeSubmitOk_pop;// 提交成功面板
    @BindView(R.id.iv_feedback_selectType_submit_ok_gray)
    ImageView ivSelectTypeSubmitOkGray;// 提交成功后背景(点击后panel消隐)
    @BindView(R.id.iv_feedback_selectType_submit_ok_pop)
    RelativeLayout ivSelectTypeSubmitOkPop;// 提交成功后提示框(点击后panel消隐)

    @BindView(R.id.rl_feedback_show_photo)
    PercentRelativeLayout rlShowPhoto_pop;// 大图布局
    @BindView(R.id.iv_feedback_show_photo_bitmap)
    ImageView ivShowPhotoBitmap;// 大图控件
    @BindView(R.id.rl_feedback_show_photo_banner)
    RelativeLayout rlShowPhotoBanner;// 大图导航
    @BindView(R.id.iv_feedback_show_photo_back)
    ImageView ivShowPhotoBack;// 大图返回键
    @BindView(R.id.tv_feedback_show_photo_count)
    TextView tvShowPhotoCount;// 大图数量: 1/n
    @BindView(R.id.iv_feedback_show_photo_del)
    ImageView ivShowPhotoDel;// 大图删除

    @BindView(R.id.tv_feedback_submit)
    TextView tvSubmit;// 点击提交按钮

    @BindView(R.id.rl_feedback_wait)
    ExtenderWait rlFeedbackWait;// 等待UI

    @BindView(R.id.dg_feedback_tip)
    DialogOkWidget dgFeedbackTip;// 确定取消面板

    private HomeRxActivity activity;
    private View inflate;
    private FeedbackPhotoAdapter feedbackPhotoAdapter;
    private LinearLayoutManager lm_photo;
    private String[] feedbackTypes;
    private List<FeedbackTypeBean> ftbs;
    private List<FeedbackPhotoBean> bbs = new ArrayList<>();
    private List<String> fids = new ArrayList<>();
    private FeedbackTypeAdapter feedbackTypeAdapter;
    private LinearLayoutManager lm_selectType;
    private boolean isPopShow = false;// 弹窗显示标记位
    private int REQUEST_IMAGE = 123;// 请求本地图片约定码
    private int REQUEST_EXTERNAL_STORAGE = 123;// 权限约定码
    private String clickShowUrl = "";// 点击大图的url
    private String TAG = "FeedbackFragment";// 日志
    private int count = 0;
    private int max = 5;// 最大图片数量

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (HomeRxActivity) getActivity();
        inflate = View.inflate(getActivity(), R.layout.fra_feedback_rx, null);
        unbinder = ButterKnife.bind(this, inflate);
        initRes();
        resetUi();
        initAdapter();
        initListener();
        clearAndResetData();
        return inflate;
    }

    /**
     * 初始化资源
     */
    private void initRes() {
        count = 0;
        fids.clear();
        feedbackTypes = getResources().getStringArray(R.array.feedbacktype);
        ftbs = setFeedbackTypeBeans(feedbackTypes);
        tvStringNum.setOnClickListener(null);
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        /* 1.初始化selectType适配器 */
        feedbackTypeAdapter = new FeedbackTypeAdapter(getActivity(), ftbs);
        // 设置选择监听
        feedbackTypeAdapter.setOnSelectTypeListener(attr -> {
            rlSelectTypeChoiceList_pop.setVisibility(View.GONE);// 0.列表界面隐藏
            ftbs = attr;// 1.更新数据
            for (FeedbackTypeBean ftb : ftbs) {
                if (ftb.isSelected()) {
                    etSelectType.setText(ftb.getTypeName());// 2.显示数据
                    break;
                }
            }
            // 4.恢复标记位
            isPopShow = false;

        });
        lm_selectType = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rcvSelectTypeChoiceList.setLayoutManager(lm_selectType);
        rcvSelectTypeChoiceList.setAdapter(feedbackTypeAdapter);

        /* 2.初始化图片适配器 */
        feedbackPhotoAdapter = new FeedbackPhotoAdapter(activity, bbs);
        // 设置点击删除监听
        feedbackPhotoAdapter.setOnDeletedPhotoListener(position -> {
            bbs.remove(position);
            refreshAdapter();
        });

        /* 3.设置点击图片显示大图监听 */
        feedbackPhotoAdapter.setOnClickPhotoShowListener(fpb -> {
            // 0.享元
            clickShowUrl = fpb.getUrl();
            // 1.添加一个方法用于show visible大图预览页(参照方法clickSelectType, 留意各个标记位的切换问题) √
            // 2.进入大图预览页之后需要完成2件任务.
            // --> 2.1.点击图片, banner消隐 √
            // --> 2.2.显示banner需要显示的信息以及对应 的返回, 删除等逻辑行为 
            clickShowPhoto(true);
            // 显示大图
            Bitmap bm = FormatTools.getInstance().file2FitPhoneBitmap(getActivity(), fpb.getUrl());
            ivShowPhotoBitmap.setImageBitmap(bm);
        });
        lm_photo = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        rcvPhoto.setLayoutManager(lm_photo);
        rcvPhoto.setAdapter(feedbackPhotoAdapter);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        // 添加字符数监听器
        etEnterFeedback.addTextChangedListener(new FeedbackEnterWatcher() {
            @Override
            public void getCurrentLength(int length) {
                tvStringNum.setText(String.valueOf(length + "/2000"));
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            count = 0;
            resetUi();
        } else {
            isPopShow = false;
        }
    }

    private void resetUi() {
        // 1.初始化导航栏等
        if (activity == null) {
            activity = (HomeRxActivity) getActivity();
        }
        activity.tabFlag = Cons.TAB_FEEDBACK;
        activity.llNavigation.setVisibility(View.GONE);
        activity.rlBanner.setVisibility(View.GONE);
    }

    @Override
    public boolean onBackPressed() {
        // 如果等待界面显示--> 提示等待
        if (rlFeedbackWait.getVisibility() == View.VISIBLE) {
            toast(R.string.the_data_is_being_submitted_please_wait_a_moment);
        } else if (dgFeedbackTip.getVisibility() == View.VISIBLE) {
            dgFeedbackTip.setVisibility(View.GONE);
        } else {
            back();
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_feedback_back,// 返回
            R.id.rl_feedback_selectType,// 选择类型
            R.id.rl_feedback_photo_logo,// 选择图片
            R.id.tv_feedback_submit,// 提交
            R.id.iv_feedback_selectType_choice_list_gray,// 选择类型列表背景
            R.id.iv_feedback_selectType_submit_ok_gray,// 提交成功背景
            R.id.iv_feedback_selectType_submit_ok_pop,// 提交类型弹窗
            R.id.iv_feedback_show_photo_bitmap,// 显示大图
            R.id.iv_feedback_show_photo_back,// 大图返回
            R.id.iv_feedback_show_photo_del // 大图删除
    })

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_feedback_back:// 返回
                back();
                break;
            case R.id.rl_feedback_selectType:// 选择类型
                clickSelectType(true);
                break;
            case R.id.rl_feedback_photo_logo:// 选择图片
                clickSelectPhoto();
                break;
            case R.id.tv_feedback_submit:// 提交
                commit();
                break;
            case R.id.iv_feedback_selectType_choice_list_gray:// 选择类型列表背景
                clickSelectType(false);
                break;
            case R.id.iv_feedback_selectType_submit_ok_gray:// 提交成功背景
            case R.id.iv_feedback_selectType_submit_ok_pop:// 提交类型弹窗
                sumbitOkPop(false);
                backAndClear();
                break;
            case R.id.iv_feedback_show_photo_bitmap:// 大图导航消隐
                clickShowOrHideTopBanner(false);
                break;
            case R.id.iv_feedback_show_photo_back:// 大图返回
                clickShowPhoto(false);
                break;
            case R.id.iv_feedback_show_photo_del:// 大图删除
                clickShowPhotoDel();
                break;
            case R.id.rl_feedback_wait:// 等待
                toast(R.string.the_data_is_being_submitted_please_wait_a_moment);
                break;
        }
    }

    /**
     * 提交数据到服务器
     */
    private void commit() {
        GetSystemInfoHelper getSystemInfoHelper = new GetSystemInfoHelper();
        getSystemInfoHelper.setOnGetSystemInfoSuccessListener(result -> toCommitLoginBegin(result));
        getSystemInfoHelper.setOnAppErrorListener(() -> connnFailed("commit", null));
        getSystemInfoHelper.setOnFwErrorListener(() -> connnFailed("commit", null));
        getSystemInfoHelper.getSystemInfo();
    }

    /**
     * 连接失败
     *
     * @param methodName 方法名
     * @param error      错误对象
     * @param des        附加描述
     */
    public void connnFailed(String methodName, ResponseBody.Error error, String... des) {
        String err = "occur failed;";
        if (error != null) {
            err = error.getMessage();
        }
        String dess = "";
        for (String de : des) {
            dess = String.valueOf(dess + de);
        }
        Logs.t("ma_feedback_error").ee(methodName + " : " + err + "des: " + dess);
        toast(R.string.connect_failed);
    }

    /**
     * C1.登陆连接服务器提交数据
     */
    private void toCommitLoginBegin(GetSystemInfoBean systemSystemInfo) {

        // 1.判断问题类型以及建议字符
        String questionType = etSelectType.getText().toString();// 问题类型
        String suggestion = etEnterFeedback.getText().toString();// 建议字符
        boolean isTypeEmpty = TextUtils.isEmpty(questionType);
        boolean isSuggestionEmpty = TextUtils.isEmpty(suggestion);
        if (isTypeEmpty) {
            toast(R.string.you_have_not_chosen_the_problem_type);
            return;
        } else if (isSuggestionEmpty) {
            toast(R.string.you_have_not_filled_out_the_suggestion);
            return;
        } else if (suggestion.length() < 10) {
            toast(R.string.at_least_10_characters);
            return;
        }

        // 2.获取图片集合的file对象
        List<File> pics = new ArrayList<>();
        if (bbs != null & bbs.size() > 0) {
            for (FeedbackPhotoBean bb : bbs) {
                File pic = new File(bb.getUrl());
                if (pic.exists()) {// 文件存在
                    // if (pic.length() > 200 * 1024) {// 文件不能超过200KB
                    //     toast(R.string.each_picture_can_not_be_more_than_200kb);
                    //     pics.clear();
                    //     return;
                    // }
                    pics.add(pic);
                }
            }
        }

        // 3.以IMEI+MAC登陆服务器
        rlFeedbackWait.setVisibility(View.VISIBLE);
        String deviceName = systemSystemInfo.getDeviceName();
        String imei = systemSystemInfo.getIMEI();
        String macId = systemSystemInfo.getMacAddress();
        FeedBackLoginHelperFeedback flh = new FeedBackLoginHelperFeedback();
        flh.setOnLoginSuccessListener(feedbackLoginResult -> {
            // 3.1.清空fid集合
            fids.clear();
            // 4.获取到access_Token 以及 uid
            String uid = feedbackLoginResult.getUid();
            String access_token = feedbackLoginResult.getAccess_token();
            if (pics.size() > 0) {// 5.图片集合有元素--> 先提交图片--> 后提交feedback
                count = 0;
                toUpload(access_token, deviceName, uid, pics);
            } else {// 5.图片集合没有元素--> 直接提交
                commitFeedback(deviceName, access_token, uid);
            }

        });
        flh.setOnErrorListener(error -> {
            fids.clear();
            rlFeedbackWait.setVisibility(View.GONE);
            toast(R.string.the_connection_failed_please_try_again_extender);
            Lgg.t(TAG).ee("");
        });
        flh.login(deviceName, imei, macId);
    }

    /**
     * C2.上传图片
     *
     * @param access_token
     * @param deviceName
     * @param uid
     * @param pics
     */
    public void toUpload(String access_token, String deviceName, String uid, List<File> pics) {
        // 小于文件数才进入上传逻辑
        if (count < pics.size()) {
            File pic = pics.get(count);
            FeedbackUploadPicHelper fup = new FeedbackUploadPicHelper();
            fup.setOnUploadSuccessListener(fid -> {
                fids.add(fid);
                count++;
                toUpload(access_token, deviceName, uid, pics);
            });
            fup.setOnErrorListener(error -> {
                count = 0;
                fids.clear();
                rlFeedbackWait.setVisibility(View.GONE);
                toast(R.string.upload_failure);
                Logs.t(TAG).ee("toUpload error: " + error.getMessage());
            });
            fup.upload(access_token, deviceName, pic);
        } else {// 否则开始上传
            count = 0;
            commitFeedback(deviceName, access_token, uid);
        }

    }

    /**
     * C3.请求[提交feedback接口]
     *
     * @param deviceName
     * @param access_Token
     * @param uid
     */
    public void commitFeedback(String deviceName, String access_Token, String uid) {
        // 1.--> (; sign=YOUR_SIGN; timestamp=YOUR_TIMESTAMP; newtoken=YOUR_NEW_TOKEN)
        String sign_timeStamp_newToken = OtherUtils.getCommitFeedbackHead(uid);
        // 2.--> 创建提交要素
        FeedbackCommitParam fcb = new FeedbackCommitParam();
        fcb.setUid(uid);
        fcb.setType(etSelectType.getText().toString());
        fcb.setIssue_type(etSelectType.getText().toString());
        fcb.setMessage(etEnterFeedback.getText().toString());
        fcb.setDate(String.valueOf(System.currentTimeMillis() / 1000));// 以秒为单位
        if (fids.size() > 0) {
            fcb.setAttachment(OtherUtils.getFidAppendString(fids));
        }
        // 3.--> 转换成json
        String json = JSONObject.toJSONString(fcb);
        // 3.--> 创建提交对象
        FeedbackCommitHelper fch = new FeedbackCommitHelper();
        fch.setOnCommitSuccessListener(commitResult -> {
            if (commitResult.getError_id() == 0) {
                rlFeedbackWait.setVisibility(View.GONE);
                sumbitOkPop(true);
            } else {
                rlFeedbackWait.setVisibility(View.GONE);
                toast(R.string.upload_failure);
                Logs.t(TAG).ee("commitResult.getError_id(): " + commitResult.getError_id());
            }
        });
        fch.setOnErrorListener(error -> {
            fids.clear();
            rlFeedbackWait.setVisibility(View.GONE);
            toast(R.string.upload_failure);
            Logs.t(TAG).ee("commitFeedback error: " + error.getMessage());
        });
        fch.commit(deviceName, access_Token, sign_timeStamp_newToken, json);
    }

    /**
     * 在大图模式下删除
     */
    private void clickShowPhotoDel() {
        if (TextUtils.isEmpty(clickShowUrl)) {
            toast(R.string.error_info);
            return;
        }
        // 找到匹配大图对象
        int position = 0;
        for (int i = 0; i < bbs.size(); i++) {
            if (bbs.get(i).getUrl().equalsIgnoreCase(clickShowUrl)) {
                position = i;
                break;
            }
        }
        // 移除该对象
        bbs.remove(position);
        // 刷新适配器
        refreshAdapter();
        // 回退到上一级
        back();
    }

    /**
     * 点击显示|消隐顶部导航
     *
     * @param isShow 是否显示banner
     */
    private void clickShowOrHideTopBanner(boolean isShow) {
        if (isShow) {// 如果进行了强制指定--> 则按照指定的来
            rlShowPhotoBanner.setVisibility(isShow ? View.VISIBLE : View.GONE);
        } else {// 否则自动判断
            boolean isShowBanner = rlShowPhotoBanner.getVisibility() == View.VISIBLE;
            rlShowPhotoBanner.setVisibility(isShowBanner ? View.GONE : View.VISIBLE);
        }

    }

    /**
     * 点击选择照片
     */
    private void clickSelectPhoto() {
        PermissionUtils pu = new PermissionUtils(getActivity());
        pu.setOnWriteReadPermissonListener(attr -> toPhoto());
        pu.getWriteReadExtenalPermisson();
    }

    /**
     * 大图显示|隐藏
     *
     * @param isShowPhoto 是否显示大图
     */
    private void clickShowPhoto(boolean isShowPhoto) {
        isPopShow = isShowPhoto;
        rlShowPhoto_pop.setVisibility(isShowPhoto ? View.VISIBLE : View.GONE);
        if (rlShowPhoto_pop.getVisibility() == View.VISIBLE) {// 显示大图的情况下--> 显示当前已选图片数量
            tvShowPhotoCount.setText(String.valueOf("1/" + bbs.size()));
        }
        clickShowOrHideTopBanner(true);// 恢复顶部导航
    }

    /**
     * 前往相册选择页
     */
    private void toPhoto() {
        MultiImageSelector
                // 1.创建图片集合对象
                .create(getActivity())
                // 2.是否显示相机. 默认为显示
                .showCamera(true)
                // 3.最大选择图片数量, 默认为9. 只有在选择模式为多选时有效
                .count(max)
                // 4.多选模式, 默认模式
                .multi()
                // 5.传递默认选中的图片(地址集合)
                .origin(OtherUtils.transferString(getActivity(), bbs))
                // 6.注意:第一个参数写this即可(是fragment就写framgent,activity就写activity)
                .start(this, REQUEST_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQUEST_EXTERNAL_STORAGE) {
            toPhoto();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Logs.t("ma_pics").ii("user select more pics");
                // 1.获取返回的「图片地址」列表
                List<String> urls = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 2.更新图片显示
                bbs = OtherUtils.transferBitmap(getActivity(), urls);
            } else if (resultCode == MultiImageSelectorActivity.NO_SELECT_CHOICE) {
                Logs.t("ma_pics").ii("user no select any pics");
                // 1.清空集合
                bbs.clear();
            }
            // 刷新适配器以及以选择数量显示
            refreshAdapter();
        }
    }

    public void refreshAdapter() {
        // 刷新适配器
        feedbackPhotoAdapter.notifys(bbs);
        // 更新图片数量
        int itemCount = feedbackPhotoAdapter.getItemCount();
        tvPhotoCount.setText(String.valueOf(itemCount + "/5"));
    }

    /**
     * 提交成功弹框
     *
     * @param isShow
     */
    private void sumbitOkPop(boolean isShow) {
        isPopShow = isShow;
        rlSelectTypeSubmitOk_pop.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 弹出选择类型
     */
    private void clickSelectType(boolean isShow) {
        isPopShow = isShow;
        rlSelectTypeChoiceList_pop.setVisibility(isShow ? View.VISIBLE : View.GONE);
        feedbackTypeAdapter.notifys(ftbs);
    }

    /**
     * 回退
     */
    private void back() {
        if (isPopShow) {
            rlSelectTypeChoiceList_pop.setVisibility(View.GONE);// 类型选择弹窗隐藏
            rlSelectTypeSubmitOk_pop.setVisibility(View.GONE);// 提交成功弹窗隐藏
            rlShowPhoto_pop.setVisibility(View.GONE);// 显示大图隐藏
            isPopShow = false;
            clickShowOrHideTopBanner(true);// 恢复大图导航
            clickShowUrl = "";// 清空临时大图地址
        } else {
            showExitDialog();
        }

    }

    /**
     * 显示退出对话框
     */
    private void showExitDialog() {
        // 如果用户没有填任何数据, 则直接返回
        boolean isTypeEmpty = TextUtils.isEmpty(etSelectType.getText().toString());
        boolean isEnterEmpty = TextUtils.isEmpty(etEnterFeedback.getText().toString());
        boolean isPhotoEmpty = bbs.isEmpty();
        if (isTypeEmpty & isEnterEmpty & isPhotoEmpty) {
            backAndClear();
            return;
        }
        // 否则弹出对话框
        dgFeedbackTip.setVisibility(View.VISIBLE);
        dgFeedbackTip.setTitle(R.string.return_feedback_title);
        dgFeedbackTip.setDes(R.string.return_feedback_des);
        dgFeedbackTip.setOnBgClickListener(() -> Lgg.t(TAG).ii("click not area"));
        dgFeedbackTip.setOnCancelClickListener(() -> dgFeedbackTip.setVisibility(View.GONE));
        dgFeedbackTip.setOnOkClickListener(this::backAndClear);
    }

    /**
     * 回退并清空数据
     */
    private void backAndClear() {
        dgFeedbackTip.setVisibility(View.GONE);
        activity.fraHelpers.transfer(activity.clazz[Cons.TAB_SETTING]);
        // 清除重置数据
        clearAndResetData();
    }

    /**
     * 清除重置数据
     */
    private void clearAndResetData() {
        // 类型适配器重置
        if (ftbs != null && ftbs.size() > 0) {
            for (int i = 0; i < ftbs.size(); i++) {
                ftbs.get(i).setSelected(i == 0);
            }
            feedbackTypeAdapter.notifys(ftbs);
        }
        // 图片适配器重置
        if (bbs != null && bbs.size() > 0) {
            bbs.clear();
            refreshAdapter();
        }
        // 编辑域重置
        etSelectType.setText("");
        etEnterFeedback.setText("");
    }

    /**
     * 封装类型对象
     *
     * @param feedbackTypes
     * @return
     */
    private List<FeedbackTypeBean> setFeedbackTypeBeans(String[] feedbackTypes) {
        List<FeedbackTypeBean> fts = new ArrayList<>();
        for (int i = 0; i < feedbackTypes.length; i++) {
            FeedbackTypeBean ft = new FeedbackTypeBean();
            ft.setTypeName(feedbackTypes[i]);
            ft.setSelected(i == 0);
            fts.add(ft);
        }
        return fts;
    }

    private void toast(int resId) {
        ToastUtil_m.show(getActivity(), resId);
    }

    private void toastLong(int resId) {
        ToastUtil_m.showLong(getActivity(), resId);
    }

    private void toast(String content) {
        ToastUtil_m.show(getActivity(), content);
    }

    private void to(Class ac, boolean isFinish) {
        CA.toActivity(getActivity(), ac, false, isFinish, false, 0);
    }
}
