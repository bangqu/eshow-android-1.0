package cn.org.eshow.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import cn.org.eshow.demo.R;
import cn.org.eshow.demo.bean.Enum_CodeType;
import cn.org.eshow.demo.bean.Switch_CodeType;
import cn.org.eshow.demo.common.CommonActivity;
import cn.org.eshow.demo.common.Global;
import cn.org.eshow.demo.common.SharedPrefUtil;
import cn.org.eshow.demo.network.ESResponseListener;
import cn.org.eshow.demo.network.NetworkInterface;
import cn.org.eshow.demo.view.LoginAutoCompleteEdit;
import cn.org.eshow.fragment.ESProgressDialogFragment;
import cn.org.eshow.global.ESAppConfig;
import cn.org.eshow.util.ESDialogUtil;
import cn.org.eshow.util.ESLogUtil;
import cn.org.eshow.util.ESStrUtil;
import cn.org.eshow.util.ESToastUtil;
import cn.org.eshow.util.ESViewUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
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
    //进行第三方账号登录操作时需要的token
    public static final String INTENT_THIRDTOEKN = "ThirdToken";

    public static final String INTENT_TEL = "InputTel";


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
    //第三方授权成功得到的token
    String thirdToken = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        ESViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));

        intentExtra = (Enum_CodeType) getIntent().getSerializableExtra(INTENT_ISREGISTER);
        thirdToken = getIntent().getStringExtra(INTENT_THIRDTOEKN);
        String tel = getIntent().getStringExtra(INTENT_TEL);
        mEtTel.setText(tel);

        new Switch_CodeType(intentExtra) {
            @Override
            public void onRegister() {
                InputTelActivity.this.setTitle("注册");
                mBtnSubmit.setText("提交");
                mLlAgreement.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFindPassword() {
                InputTelActivity.this.setTitle("找回密码");
                mBtnSubmit.setText("获取验证码");
                mLlAgreement.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onRePassword() {
                InputTelActivity.this.setTitle("重置密码");
                mBtnSubmit.setText("获取验证码");
                mLlAgreement.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onBoundTel() {
                InputTelActivity.this.setTitle("绑定账号");
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

        SharedPrefUtil.setTempTel(mContext,userName);

        if(intentExtra == Enum_CodeType.BOUND){
            ESLogUtil.d(mContext,"userName:"+userName+"   ， thirdToken:"+thirdToken);
            NetworkInterface.thirdBound(mContext, userName, thirdToken, boundResponseListener);
        }else{
            NetworkInterface.sendCode(mContext, userName, intentExtra, checkResponseListener);
        }
    }

    /**
     * 绑定账号回调
     */
    ESResponseListener boundResponseListener = new ESResponseListener(mContext) {
        @Override
        public void onBQSucess(String esMsg, JSONObject resultJson) {
            try {
                String userStr = resultJson.getJSONObject("user").toString();
                ESLogUtil.d(mContext, "Login  userStr:" + userStr);
                SharedPrefUtil.setUser(mContext, userStr);
                JSONObject tokenJson = resultJson.getJSONObject("accessToken");
                String token = tokenJson.getString("accessToken");
                SharedPrefUtil.setAccesstoken(mContext,token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MainActivity_.intent(mContext).start();
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
            progressDialog = ESDialogUtil.showProgressDialog(mContext, Global.LOADING_PROGRESSBAR_ID, "请求数据中...");
        }

        @Override
        public void onFinish() {
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            ESLogUtil.i(mContext, "2onFailure:statusCode = " + statusCode + ", content is " + content);
            if(error!=null){
                error.printStackTrace();
            }
            progressDialog.dismiss();


        }
    };
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
            ESLogUtil.i(mContext, "1onFailure:statusCode = " + statusCode + ", content is " + content);
            if(error!=null){
                error.printStackTrace();
            }
            progressDialog.dismiss();
            try {
                JSONObject ret=new JSONObject(content);
                int rr=ret.getInt("status");
                ESLogUtil.i(mContext,"status =="+rr);
                if(rr==-5){
                    ESLogUtil.i(mContext,ESAppConfig.ACCOUNT_ISEXIST);
                    ESToastUtil.showToast(mContext, ESAppConfig.ACCOUNT_ISEXIST);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBQSucess(String esMsg, JSONObject resultJson) {
            SharedPrefUtil.setSendCodeTime(mContext);
            final String userName = mEtTel.getText().toString();
            InputPasswordActivity_.intent(mContext).extra(InputTelActivity_.INTENT_ISREGISTER, intentExtra).extra(InputPasswordActivity_.INTENT_TEL,userName).start();
            finish();

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
        String url = "http://api.eshow.org.cn/info/agreement";
        Intent intent = new Intent(mContext,WebActivity.class);
        intent.putExtra(WebActivity.INTENT_TAG_URL,url);
        intent.putExtra(WebActivity.INTENT_TAG_TITLE,"使用协议");
        startActivity(intent);
    }


}
