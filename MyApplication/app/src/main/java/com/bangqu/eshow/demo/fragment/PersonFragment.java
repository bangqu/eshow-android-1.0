package com.bangqu.eshow.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.activity.InfoFormActivity_;
import com.bangqu.eshow.demo.activity.ModifyStringValueActivity_;
import com.bangqu.eshow.demo.bean.AreaBean;
import com.bangqu.eshow.demo.bean.CityBean;
import com.bangqu.eshow.demo.bean.ProviceBean;
import com.bangqu.eshow.demo.bean.UserBean;
import com.bangqu.eshow.demo.common.SharedPrefUtil;
import com.bangqu.eshow.demo.network.ESResponseListener;
import com.bangqu.eshow.demo.network.MyHttpUtil;
import com.bangqu.eshow.demo.network.NetworkInterface;
import com.bangqu.eshow.demo.view.ChooseCityDialog;
import com.bangqu.eshow.demo.view.ChooseYearMonthDayDialog;
import com.bangqu.eshow.http.ESRequestParams;
import com.bangqu.eshow.util.ESLogUtil;
import com.bangqu.eshow.util.ESToastUtil;
import com.bangqu.eshow.util.ESViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 个人信息修改分页
 * Created by daikting on 16/2/19.
 */
public class PersonFragment extends Fragment implements View.OnClickListener {
    private Context mContext;

    TextView tvNotify;
    RelativeLayout rlTel;
    TextView tvTel;
    RelativeLayout rlEmail;
    TextView tvEmail;
    RelativeLayout rlBirthday;
    TextView tvBirthday;
    RelativeLayout rlCity;
    TextView tvCity;
    RelativeLayout rlSignature;
    TextView tvSignature;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_person, null);
        ESViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.llParent));
        tvNotify = (TextView) view.findViewById(R.id.tvNotify);
        tvNotify.setVisibility(View.GONE);

        rlTel = (RelativeLayout) view.findViewById(R.id.rlTel);
        tvTel = (TextView) view.findViewById(R.id.tvTel);
        rlEmail = (RelativeLayout) view.findViewById(R.id.rlEmail);
        rlEmail.setOnClickListener(this);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        rlBirthday = (RelativeLayout) view.findViewById(R.id.rlBirthday);
        rlBirthday.setOnClickListener(this);
        tvBirthday = (TextView) view.findViewById(R.id.tvBirthday);

        rlCity = (RelativeLayout) view.findViewById(R.id.rlCity);
        rlCity.setOnClickListener(this);
        tvCity = (TextView) view.findViewById(R.id.tvCity);

        rlSignature = (RelativeLayout) view.findViewById(R.id.rlSignature);
        rlSignature.setOnClickListener(this);
        tvSignature = (TextView) view.findViewById(R.id.tvSignature);
        setPersonInfo(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlEmail:
                String email = tvEmail.getText().toString();
                ModifyStringValueActivity_.intent(mContext)
                        .extra(ModifyStringValueActivity_.INTENT_PARAMVALUE_TAG, email)
                        .extra(ModifyStringValueActivity_.INTNET_PARAMKEY_TAG, "user.email")
                        .extra(ModifyStringValueActivity_.INTNET_INTERFACE_TAG, "user/update")
                        .extra(ModifyStringValueActivity_.INTNET_HINTTEXT_TAG, "请输入电子邮箱")
                        .extra(ModifyStringValueActivity_.INTENT_TITLE_TAG, "电子邮箱")
                        .extra(ModifyStringValueActivity_.INTENT_RETURN_CODE_TAG, InfoFormActivity_.RETURN_PERSON_CODE).startForResult(0x22);
                break;
            case R.id.rlBirthday:
                ChooseYearMonthDayDialog chooseYearMonthDayDialog = new ChooseYearMonthDayDialog(mContext,tvBirthday,1970,50);
                chooseYearMonthDayDialog.setOnFinishListener(new ChooseYearMonthDayDialog.OnFinishListener() {
                    @Override
                    public void onFinishListener() {
                        String date = tvBirthday.getText().toString();
                        saveInfo(date);
                    }
                });
                chooseYearMonthDayDialog.show();
                break;
            case R.id.rlCity:
                ChooseCityDialog chooseCityDialog = new ChooseCityDialog(mContext);
                chooseCityDialog.setOnFinishListener(new ChooseCityDialog.OnFinishListener() {
                    @Override
                    public void onFinishListener(ProviceBean proviceBean, CityBean cityBean, AreaBean areaBean) {
                        String provice = proviceBean.getName();
                        String city = cityBean.getName();
                        String area = areaBean.getDisName();
                        String all = provice + "-" + city + "-" + area;
                        tvCity.setText(all);
                    }
                });
                chooseCityDialog.show();
                break;
            case R.id.rlSignature:
                String intro = tvSignature.getText().toString();
                ModifyStringValueActivity_.intent(mContext)
                        .extra(ModifyStringValueActivity_.INTENT_PARAMVALUE_TAG, intro)
                        .extra(ModifyStringValueActivity_.INTNET_PARAMKEY_TAG, "user.intro")
                        .extra(ModifyStringValueActivity_.INTNET_INTERFACE_TAG, "user/update")
                        .extra(ModifyStringValueActivity_.INTNET_HINTTEXT_TAG, "请输入个性签名")
                        .extra(ModifyStringValueActivity_.INTENT_TITLE_TAG, "个性签名")
                        .extra(ModifyStringValueActivity_.INTENT_RETURN_CODE_TAG, InfoFormActivity_.RETURN_PERSON_CODE).startForResult(0x22);
                break;
        }
    }

    /**
     * 设置个人信息页面数据
     */
    public void setPersonInfo(boolean isShowNofity) {
        UserBean userBean = SharedPrefUtil.getUser(mContext);
        String username = userBean.getUsername();
        String email = userBean.getEmail();
        String birthday = userBean.getBirthday();
        String intro = userBean.getIntro();

        tvTel.setText(username);
        tvEmail.setText(email);
        tvBirthday.setText(birthday);
        tvSignature.setText(intro);

        if(isShowNofity){
            showNotify();
        }
    }

    /**
     * 显示提示修改成功的小弹窗
     */
    private void showNotify() {
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


    private void saveInfo(String inputStr) {
        ESRequestParams abRequestParams = new ESRequestParams();
        abRequestParams.put("accessToken", SharedPrefUtil.getAccessToken(mContext));
        abRequestParams.put("user.birthday", inputStr);
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
            ESToastUtil.showToast(mContext, bqMsg);
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            ESToastUtil.showToast(mContext, "请求失败，错误码：" + statusCode);
        }
    };

    ESResponseListener userBeanResponseListener = new ESResponseListener(mContext) {
        @Override
        public void onBQSucess(String esMsg, JSONObject resultJson) {
            String userStr = null;
            try {
                userStr = resultJson.getJSONObject("user").toString();
                ESLogUtil.d(mContext, "Login  userStr:" + userStr);
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
            ESToastUtil.showToast(mContext, bqMsg);
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            ESToastUtil.showToast(mContext, "请求失败，错误码：" + statusCode);
        }
    };
}
