package cn.org.eshow.demo.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.activity.CropImageActivity;
import cn.org.eshow.demo.activity.InfoFormActivity_;
import cn.org.eshow.demo.activity.ModifySexActivity_;
import cn.org.eshow.demo.activity.ModifyStringValueActivity_;
import cn.org.eshow.demo.bean.UserBean;
import cn.org.eshow.demo.common.SharedPrefUtil;
import cn.org.eshow.demo.network.ESResponseListener;
import cn.org.eshow.demo.network.MyHttpUtil;
import cn.org.eshow.demo.network.NetworkInterface;
import cn.org.eshow.demo.view.ChoosePhotoDialog;
import cn.org.eshow_framwork.http.AbRequestParams;
import cn.org.eshow_framwork.image.AbImageLoader;
import cn.org.eshow_framwork.util.AbFileUtil;
import cn.org.eshow_framwork.util.AbLogUtil;
import cn.org.eshow_framwork.util.AbStrUtil;
import cn.org.eshow_framwork.util.AbToastUtil;
import cn.org.eshow_framwork.util.AbViewUtil;

/**
 * 基本信息修改分页
 * Created by daikting on 16/2/19.
 */
public class BaseInfoFragment extends Fragment implements View.OnClickListener {
    private Context mContext;


    TextView tvNotify;
    RelativeLayout rlIcon;
    ImageView ivIcon;
    RelativeLayout rlAccount;
    TextView tvAccount;
    RelativeLayout rlName;
    TextView tvName;
    RelativeLayout rlNickName;
    TextView tvNickName;
    RelativeLayout rlAge;
    TextView tvAge;
    RelativeLayout rlSex;
    TextView tvSex;

    /* 用来标识请求照相功能的activity */
    public static final int CAMERA_WITH_DATA = 10;
    /* 用来标识请求gallery的activity */
    public static final int PHOTO_PICKED_WITH_DATA = 11;
    /* 用来标识请求裁剪图片后的activity */
    public static final int CAMERA_CROP_DATA = 12;
    /* 拍照的照片存储位置 */
    private File PHOTO_DIR = null;
    // 照相机拍照得到的图片
    private File mCurrentPhotoFile;
    private String mFileName;
    String photo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_baseinfo, null);
        AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.llParent));

        tvNotify = (TextView) view.findViewById(R.id.tvNotify);
        tvNotify.setVisibility(View.GONE);

        rlIcon = (RelativeLayout) view.findViewById(R.id.rlIcon);
        rlIcon.setOnClickListener(this);
        ivIcon = (ImageView) view.findViewById(R.id.ivIcon);

        rlAccount = (RelativeLayout) view.findViewById(R.id.rlAccount);
//        rlAccount.setOnClickListener(this);
        tvAccount = (TextView) view.findViewById(R.id.tvAccount);

        rlName = (RelativeLayout) view.findViewById(R.id.rlName);
        rlName.setOnClickListener(this);
        tvName = (TextView) view.findViewById(R.id.tvName);

        rlNickName = (RelativeLayout) view.findViewById(R.id.rlNickName);
        rlNickName.setOnClickListener(this);
        tvNickName = (TextView) view.findViewById(R.id.tvNickName);

        rlAge = (RelativeLayout) view.findViewById(R.id.rlAge);
        rlAge.setOnClickListener(this);
        tvAge = (TextView) view.findViewById(R.id.tvAge);

        rlSex = (RelativeLayout) view.findViewById(R.id.rlSex);
        rlSex.setOnClickListener(this);
        tvSex = (TextView) view.findViewById(R.id.tvSex);

        setBaseInfo(false);

        //初始化图片保存路径
        String photo_dir = AbFileUtil.getImageDownloadDir(mContext);
        if (AbStrUtil.isEmpty(photo_dir)) {
            AbToastUtil.showToast(mContext, "存储卡不存在");
        } else {
            PHOTO_DIR = new File(photo_dir);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlIcon:
                ChoosePhotoDialog choosePhotoDialog = new ChoosePhotoDialog(mContext);
                choosePhotoDialog.setOnChooseListener(new ChoosePhotoDialog.OnChooseListener() {
                    @Override
                    public void onLocalChooseListener() {
                        // 从相册中去获取
                        try {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                            intent.setType("image/*");
                            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
                        } catch (ActivityNotFoundException e) {
                            AbToastUtil.showToast(mContext, "没有找到照片");
                        }
                    }

                    @Override
                    public void onTakePhotoListener() {
                        doPickPhotoAction();
                    }
                });
                choosePhotoDialog.show();
                break;
            case R.id.rlAccount:

                break;
            case R.id.rlName:
                String name = tvName.getText().toString();
                ModifyStringValueActivity_.intent(mContext)
                        .extra(ModifyStringValueActivity_.INTENT_PARAMVALUE_TAG, name)
                        .extra(ModifyStringValueActivity_.INTNET_PARAMKEY_TAG, "user.realname")
                        .extra(ModifyStringValueActivity_.INTNET_INTERFACE_TAG, "user/update")
                        .extra(ModifyStringValueActivity_.INTNET_HINTTEXT_TAG, "请输入姓名")
                        .extra(ModifyStringValueActivity_.INTENT_TITLE_TAG, "姓名")
                        .extra(ModifyStringValueActivity_.INTENT_RETURN_CODE_TAG, InfoFormActivity_.RETURN_BASEINFO_CODE).startForResult(0x11);
                break;
            case R.id.rlNickName:
                String nickName = tvNickName.getText().toString();
                ModifyStringValueActivity_.intent(mContext)
                        .extra(ModifyStringValueActivity_.INTENT_PARAMVALUE_TAG, nickName)
                        .extra(ModifyStringValueActivity_.INTNET_PARAMKEY_TAG,"user.nickname")
                        .extra(ModifyStringValueActivity_.INTNET_INTERFACE_TAG, "user/update")
                        .extra(ModifyStringValueActivity_.INTNET_HINTTEXT_TAG,"请输入昵称")
                        .extra(ModifyStringValueActivity_.INTENT_TITLE_TAG,"昵称")
                        .extra(ModifyStringValueActivity_.INTENT_RETURN_CODE_TAG, InfoFormActivity_.RETURN_BASEINFO_CODE).startForResult(0x11);
                break;
            case R.id.rlAge:
                String age = tvAge.getText().toString();
                ModifyStringValueActivity_.intent(mContext)
                        .extra(ModifyStringValueActivity_.INTENT_PARAMVALUE_TAG, age)
                        .extra(ModifyStringValueActivity_.INTNET_PARAMKEY_TAG,"user.age")
                        .extra(ModifyStringValueActivity_.INTNET_INTERFACE_TAG, "user/update")
                        .extra(ModifyStringValueActivity_.INTNET_HINTTEXT_TAG,"请输入年龄")
                        .extra(ModifyStringValueActivity_.INTENT_TITLE_TAG,"年龄")
                        .extra(ModifyStringValueActivity_.INTENT_RETURN_CODE_TAG, InfoFormActivity_.RETURN_BASEINFO_CODE).startForResult(0x11);
                break;
            case R.id.rlSex:
                String sex = tvSex.getText().toString();
                ModifySexActivity_.intent(mContext)
                        .extra(ModifyStringValueActivity_.INTENT_PARAMVALUE_TAG, sex)
                        .extra(ModifyStringValueActivity_.INTNET_PARAMKEY_TAG,"user.male")
                        .extra(ModifyStringValueActivity_.INTNET_INTERFACE_TAG, "user/update")
                        .extra(ModifyStringValueActivity_.INTENT_TITLE_TAG,"性别")
                        .extra(ModifyStringValueActivity_.INTENT_RETURN_CODE_TAG, InfoFormActivity_.RETURN_BASEINFO_CODE).startForResult(0x11);
                break;
        }
    }

    /**
     * 设置基本信息页面数据
     */
    public void setBaseInfo(boolean isShowNofity) {
        UserBean userBean = SharedPrefUtil.getUser(mContext);
        String username = userBean.getUsername();
        String nickname = userBean.getNickname();
        String photo = userBean.getPhoto();
        String realname = userBean.getRealname();
        String age = userBean.getAge();
        String sexStr = userBean.getSexStr();

        if(!AbStrUtil.isEmpty(photo) && photo.startsWith("http")){
            ivIcon.setImageResource(R.drawable.icon_progressbar);
            cn.org.eshow_framwork.image.AbImageLoader.getInstance(mContext).display(ivIcon,photo);
        }

        tvAccount.setText(username);
        tvNickName.setText(nickname);
        tvName.setText(realname);
        tvAge.setText(age);
        tvSex.setText(sexStr);

        if(isShowNofity){
            showNotify();
        }
    }

    /**
     * 显示提示修改成功的小弹窗
     */
    private void showNotify(){
        tvNotify.setVisibility(View.VISIBLE);
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(1000);
        mHiddenAction.setStartOffset(1000);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvNotify.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tvNotify.startAnimation(mHiddenAction);
    }

    /**
     * 从照相机获取
     */
    private void doPickPhotoAction() {
        String status = Environment.getExternalStorageState();
        //判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            doTakePhoto();
        } else {
            AbToastUtil.showToast(mContext, "没有可用的存储卡");
        }
    }
    /**
     * 拍照获取图片
     */
    private void doTakePhoto() {
        try {
            mFileName = System.currentTimeMillis() + ".jpg";
            mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = Uri.fromFile(mCurrentPhotoFile);
            AbLogUtil.d(mContext, "save uri:" + uri.getPath());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (Exception e) {
            AbToastUtil.showToast(mContext, "未找到系统相机程序");
        }
    }

    public void doCammera(){
        if (mCurrentPhotoFile != null) {
            AbLogUtil.d(mContext, "将要进行裁剪的图片的路径是 = " + mCurrentPhotoFile.getPath());
            String currentFilePath2 = mCurrentPhotoFile.getPath();
            Intent intent2 = new Intent(mContext, CropImageActivity.class);
            intent2.putExtra("PATH", currentFilePath2);
            startActivityForResult(intent2, BaseInfoFragment.CAMERA_CROP_DATA);
        } else {
            AbToastUtil.showToast(mContext, "图片地址获取异常，请重试！");
        }
    }


    public void setIconLoading(){
        ivIcon.setImageResource(R.drawable.icon_progressbar);
    }

    public void setIconImage(String url){
        AbImageLoader.getInstance(mContext).display(ivIcon,url);
        saveIconUrl(url);
    }

    private void saveIconUrl(String url) {
        AbRequestParams abRequestParams = new cn.org.eshow_framwork.http.AbRequestParams();
        abRequestParams.put("accessToken", SharedPrefUtil.getAccessToken(mContext));
        abRequestParams.put("user.photo", url);
        AbLogUtil.d(mContext,"保存头像地址："+url);
        new MyHttpUtil(mContext).post("user/update", abRequestParams, responseListener);
    }

    ESResponseListener responseListener = new ESResponseListener(mContext) {
        @Override
        public void onBQSucess(String esMsg, JSONObject resultJson) {
            NetworkInterface.refreshUserInfo(mContext, userBeanResponseListener);
        }

        @Override
        public void onBQNoData() {

        }

        @Override
        public void onBQNotify(String bqMsg) {
            AbToastUtil.showToast(mContext, bqMsg);
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            AbToastUtil.showToast(mContext, "请求失败，错误码：" + statusCode);
        }
    };

    ESResponseListener userBeanResponseListener = new ESResponseListener(mContext) {
        @Override
        public void onBQSucess(String esMsg, JSONObject resultJson) {
            String userStr = null;
            try {
                userStr = resultJson.getJSONObject("user").toString();
                AbLogUtil.d(mContext, "Login  userStr:" + userStr);
                SharedPrefUtil.setUser(mContext, userStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBQNoData() {

        }
        @Override
        public void onBQNotify(String bqMsg) {
            AbToastUtil.showToast(mContext, bqMsg);
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            AbToastUtil.showToast(mContext, "请求失败，错误码：" + statusCode);
        }
    };
}
