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
import com.bangqu.eshow.demo.activity.ModifyStringValueActivity_;
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
        rlIcon.setOnClickListener(this);
        ivIcon = (ImageView) view.findViewById(R.id.ivIcon);

        rlAccount = (RelativeLayout) view.findViewById(R.id.rlAccount);
        rlAccount.setOnClickListener(this);
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

                break;
            case R.id.rlNickName:
                String nickName = tvNickName.getText().toString();
                ModifyStringValueActivity_.intent(mContext)
                        .extra(ModifyStringValueActivity_.INTENT_PARAMVALUE_TAG, nickName)
                        .extra(ModifyStringValueActivity_.INTNET_PARAMKEY_TAG,"user.nickname")
                        .extra(ModifyStringValueActivity_.INTNET_INTERFACE_TAG, "user/update")
                        .extra(ModifyStringValueActivity_.INTNET_HINTTEXT_TAG,"请输入昵称")
                        .extra(ModifyStringValueActivity_.INTENT_TITLE_TAG,"昵称").startForResult(0x11);
                break;
            case R.id.rlAge:

                break;
            case R.id.rlSex:

                break;
        }
    }
    public void setNickName(String nickName){
        tvNickName.setText(nickName);

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
