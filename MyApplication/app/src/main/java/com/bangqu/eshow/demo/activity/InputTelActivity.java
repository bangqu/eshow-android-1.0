package com.bangqu.eshow.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.bean.Enum_CodeType;
import com.bangqu.eshow.demo.bean.Switch_CodeType;
import com.bangqu.eshow.demo.common.CommonActivity;
import com.bangqu.eshow.demo.common.Global;
import com.bangqu.eshow.demo.common.SharedPrefUtil;
import com.bangqu.eshow.demo.network.ESResponseListener;
import com.bangqu.eshow.demo.network.NetworkInterface;
import com.bangqu.eshow.demo.view.LoginAutoCompleteEdit;
import com.bangqu.eshow.fragment.ESProgressDialogFragment;
import com.bangqu.eshow.util.ESDialogUtil;
import com.bangqu.eshow.util.ESLogUtil;
import com.bangqu.eshow.util.ESStrUtil;
import com.bangqu.eshow.util.ESToastUtil;
import com.bangqu.eshow.util.ESViewUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * 输入登录手机号码的页面,是注册和找回密码公用的页面
 * 通过Intent 传递isRegister状态标识是否是注册
 * Created by daikting on 16/2/24.
 */
@EActivity(R.layout.activity_inputtel)
public class InputTelActivity extends CommonActivity {

    //是否要进入注册页面
    public static final String INTENT_ISREGISTER = "InputTelType";

    private Context mContext = InputTelActivity.this;
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;
    @ViewById(R.id.etTel)
    LoginAutoCompleteEdit mEtTel;
    @ViewById(R.id.btnSubmit)
    Button mBtnSubmit;
    @ViewById(R.id.llAgreement)
    LinearLayout mLlAgreement;
    @ViewById(R.id.cbAgree)
    CheckBox mCbAgree;
    @ViewById(R.id.tvAgreement)
    TextView mTvAgreement;
    ESProgressDialogFragment progressDialog;

    //页面跳转的intent标识
    private Enum_CodeType intentExtra = Enum_CodeType.REGISTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        ESViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));

        intentExtra = (Enum_CodeType) getIntent().getSerializableExtra(INTENT_ISREGISTER);

        new Switch_CodeType(intentExtra) {
            @Override
            public void onRegister() {
                InputTelActivity.this.setTitle("注册");

            }

            @Override
            public void onFindPassword() {
                InputTelActivity.this.setTitle("找回密码");

            }
        };
        mTvTitle.setText(getTitle());
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);


    }

    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }

    @Click(R.id.btnSubmit)
    void onSubmit() {
        if(!mCbAgree.isChecked()){
            ESToastUtil.showToast(mContext,"请先同意使用协议！");
            return;
        }
        final String userName = mEtTel.getText().toString();
        if(!ESStrUtil.isMobileNo(userName)){
            ESToastUtil.showToast(mContext,"请输入正确的手机号码！");
            return;
        }
        new Switch_CodeType(intentExtra){
            @Override
            public void onRegister() {
                NetworkInterface.sendCode(mContext, userName, "register", checkResponseListener);

            }

            @Override
            public void onFindPassword() {
                NetworkInterface.sendCode(mContext, userName, "", checkResponseListener);

            }
        };
    }

    /**
     * 检验手机号接口
     */
    ESResponseListener checkResponseListener = new ESResponseListener(mContext) {
        @Override
        public void onStart() {
            progressDialog = ESDialogUtil.showProgressDialog(mContext, Global.LOADING_PROGRESSBAR_ID, "请求数据中...");

        }

        @Override
        public void onFinish() {
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            progressDialog.dismiss();
            ESToastUtil.showToast(mContext,"请求失败，错误码："+statusCode);
        }

        @Override
        public void onBQSucess(String esMsg, JSONObject resultJson) {
            SharedPrefUtil.setSendCodeTime(mContext);
            final String userName = mEtTel.getText().toString();
            InputPasswordActivity_.intent(mContext).extra(InputTelActivity_.INTENT_ISREGISTER, Enum_CodeType.REGISTER).extra(InputPasswordActivity_.INTENT_TEL,userName).start();

        }

        @Override
        public void onBQNoData() {
            ESLogUtil.d(mContext,"onBQNoData");

        }

        @Override
        public void onBQNotify(String bqMsg) {
            ESToastUtil.showToast(mContext,bqMsg);
        }

    };
    /**
     * 跳转使用协议
     */
    @Click(R.id.tvAgreement)
    void onReadAgreement() {

    }


}
