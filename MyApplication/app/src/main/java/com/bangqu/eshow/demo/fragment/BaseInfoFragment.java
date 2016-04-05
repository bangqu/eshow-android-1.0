package com.bangqu.eshow.demo.fragment;

import android.content.Context;
import android.os.Bundle;
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

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.activity.InfoFormActivity_;
import com.bangqu.eshow.demo.activity.ModifySexActivity_;
import com.bangqu.eshow.demo.activity.ModifyStringValueActivity_;
import com.bangqu.eshow.demo.bean.UserBean;
import com.bangqu.eshow.demo.common.SharedPrefUtil;
import com.bangqu.eshow.util.ESViewUtil;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_baseinfo, null);
        ESViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.llParent));

        tvNotify = (TextView) view.findViewById(R.id.tvNotify);
        tvNotify.setVisibility(View.GONE);

        rlIcon = (RelativeLayout) view.findViewById(R.id.rlIcon);
//        rlIcon.setOnClickListener(this);
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
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlIcon:

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
    public void setNickName(String nickName){
        tvNickName.setText(nickName);
        showNotify();
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
}
