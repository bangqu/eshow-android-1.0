package cn.org.eshow.demo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.bean.Enum_CodeType;
import cn.org.eshow.demo.bean.Enum_ThirdType;
import cn.org.eshow.demo.bluetooth.StringUtil;
import cn.org.eshow.demo.common.CommonActivity;
import cn.org.eshow.demo.common.Global;
import cn.org.eshow.demo.common.SharedPrefUtil;
import cn.org.eshow.demo.network.ESResponseListener;
import cn.org.eshow.demo.network.NetworkInterface;
import cn.org.eshow.demo.view.LoginAutoCompleteEdit;
import cn.org.eshow.framwork.fragment.AbProgressDialogFragment;
import cn.org.eshow.framwork.util.AbDialogUtil;
import cn.org.eshow.framwork.util.AbLogUtil;
import cn.org.eshow.framwork.util.AbStrUtil;
import cn.org.eshow.framwork.util.AbToastUtil;
import cn.org.eshow.framwork.util.AbViewUtil;


/**
 * 登录页面
 * Created by daikting on 16/2/24.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends CommonActivity {
    private Context mContext = LoginActivity.this;
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;
    @ViewById(R.id.tvSubTitle)
    TextView mTvSubTitle;
    @ViewById(R.id.etTel)
    LoginAutoCompleteEdit mEtTel;
    @ViewById(R.id.etPassword)
    LoginAutoCompleteEdit mEtPassword;
    @ViewById(R.id.btnLogin)
    Button mBtnLogin;
    @ViewById(R.id.tvForgetPW)
    TextView mTvForgetPW;
    @ViewById(R.id.llQQLogin)
    LinearLayout mLlQQLogin;
    @ViewById(R.id.llWechatLogin)
    LinearLayout mLlWechatLogin;
    //友盟分享
    UMShareAPI umShareAPI;

    AbProgressDialogFragment progressDialog;

    String userName = "";
    //第三方授权成功得到的token
    String thirdToken = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        AbViewUtil.scaleContentView((RelativeLayout) findViewById(R.id.rlParent));
        mTvTitle.setText(getTitle());
        mTvSubTitle.setVisibility(View.VISIBLE);
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);
        mMaterialBackButton.setVisibility(View.VISIBLE);

        userName = getIntent().getStringExtra(InputPasswordActivity.INTENT_TEL);
        if(cn.org.eshow.framwork.util.AbStrUtil.isEmpty(userName)){
            userName = SharedPrefUtil.getTempTel(mContext);
        }
        mEtTel.setText(userName);
    }
    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }

    @Click(R.id.btnLogin)
    void onLogin() {

        String userName = mEtTel.getText().toString();
        final String password = mEtPassword.getText().toString();

        if (AbStrUtil.isEmpty(userName) && !AbStrUtil.isMobileNo(userName)) {
            AbToastUtil.showToast(mContext, "请输入账号！！");
            return;
        }

        if (AbStrUtil.isEmpty(password)) {
            AbToastUtil.showToast(mContext, "请输入密码！");
            return;
        }

        if (AbStrUtil.strLength(password) > 20) {
            AbToastUtil.showToast(mContext, "密码长度过长！");
            return;
        }
        ESResponseListener responseListener = new ESResponseListener(mContext) {
            @Override
            public void onBQSucess(String esMsg, JSONObject resultJson) {
                try {
                    String userStr = resultJson.getJSONObject("user").toString();
                    AbLogUtil.d(mContext, "Login  userStr:" + userStr);
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
                AbToastUtil.showToast(mContext, bqMsg);
            }

            @Override
            public void onStart() {
                progressDialog = AbDialogUtil.showProgressDialog(mContext, Global.LOADING_PROGRESSBAR_ID, "请求数据中...");
            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Log.i("LoginActivity", "1onFailure:statusCode = " + statusCode + ", content is " + content);
                if(error!=null){
                    error.printStackTrace();
                }
                progressDialog.dismiss();
                try {
                    JSONObject ret=new JSONObject(content);
                    int rr=ret.getInt("status");
                    Log.i("LoginActivity","status =="+rr);
                    if(rr==-5){
                        Log.i("LoginActivity", ret.getString("msg"));
                        AbToastUtil.showToast(mContext, ret.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        NetworkInterface.login(mContext, userName, password, responseListener);
    }

    @Click(R.id.tvForgetPW)
    void onForgetPW() {
        String userName = mEtTel.getText().toString();
        InputTelActivity_.intent(mContext).extra(InputTelActivity_.INTENT_ISREGISTER, Enum_CodeType.FINDPASSWORD).extra(InputTelActivity_.INTENT_TEL,userName).start();
    }

    /**
     * 点击注册
     */
    @Click(R.id.tvSubTitle)
    void onRegister() {
        String userName = mEtTel.getText().toString();
        InputTelActivity_.intent(mContext).extra(InputTelActivity_.INTENT_ISREGISTER, Enum_CodeType.REGISTER).extra(InputTelActivity_.INTENT_TEL,userName).start();
    }

    /**
     * 微信授权登录
     */
    @Click(R.id.llWechatLogin)
    void onWechatLogin() {
        Log.i("LoginActivity", "微信授权登录");
        //AbToastUtil.showToast(mContext,"功能正在完善开发中...");
        Config.dialog = ProgressDialog.show(mContext, "提示", "正在请求跳转....");
        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
        umShareAPI = UMShareAPI.get(mContext);
        umShareAPI.doOauthVerify(this, platform, umAuthListener);
    }

    /**
     * QQ授权登录
     */
    @Click(R.id.llQQLogin)
    void onQQLogin() {
        Log.i("LoginActivity","QQ授权登录");
        //AbToastUtil.showToast(mContext,"功能正在完善开发中...");
        Config.dialog = ProgressDialog.show(mContext, "提示", "正在请求跳转....");
        SHARE_MEDIA platform = SHARE_MEDIA.QQ;
        umShareAPI = UMShareAPI.get(mContext);
        umShareAPI.doOauthVerify(this, platform, umAuthListener);

    }

    /**
     * 授权回调监听
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.i("LoginActivity", "platform is " + platform.name() + ",,action = " + action + "");
            work(data);//

            thirdToken = data.get("access_token");
            Log.i("LoginActivity","======before thirdToken is == "+thirdToken);
            //截取前16位
            thirdToken = thirdToken.substring(0,16);
            Enum_ThirdType thirdType = Enum_ThirdType.QQ;//
            Log.i("LoginActivity", "======after thirdToken is == " + thirdToken);

            if(platform.name().equals(SHARE_MEDIA.WEIXIN.toString())){
                thirdType = Enum_ThirdType.WeChat;//
            }else if(platform.name().equals(SHARE_MEDIA.QQ.toString())){
                thirdType = Enum_ThirdType.QQ;//
            }
            NetworkInterface.thirdLogin(mContext,thirdToken,thirdType,thirdLoginResListener);

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Log.i("LoginActivity", "SHARE_MEDIA is " + platform.name() + ",,action = " + action + ",,Throwable is " + t.toString());
            AbToastUtil.showToast(mContext, platform.name() + " Authorize fail");

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Log.i("LoginActivity", "SHARE_MEDIA is " + platform.name() + ",,action = " + action);
            AbToastUtil.showToast(mContext, platform.name() + " Authorize cancel");
        }
    };

    public void work(Map<String, String> map) {
        if(map==null){
            return;
        }
        Collection<String> c = map.values();
        Iterator it = c.iterator();
        for (; it.hasNext();) {
            Log.i("LoginActivity","--------"+it.next());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (umShareAPI != null) {
            umShareAPI.onActivityResult(requestCode, resultCode, data);
        } else {
            //应用未审核
            Log.i("LoginActivity","应用未审核，平台拒绝了应用的授权请求！");
            AbToastUtil.showToast(mContext, "应用未审核，平台拒绝了应用的授权请求！");
        }
    }

    /**
     * 第三方登录接口回调
     */
    ESResponseListener thirdLoginResListener = new ESResponseListener(mContext) {
        @Override
        public void onBQSucess(String esMsg, JSONObject resultJson) {
            if(!StringUtil.isBlank(esMsg)){
                Log.i("LoginActivity","esMsg is "+esMsg);
            }
            if(resultJson!=null){
                Log.i("LoginActivity","resultJson is "+resultJson.toString());
            }
            try {
                boolean isBound = resultJson.getBoolean("type");
                if(isBound){
                    String userStr = resultJson.getJSONObject("user").toString();
                    AbLogUtil.d(mContext, "Login  userStr:" + userStr);
                    SharedPrefUtil.setUser(mContext, userStr);
                    JSONObject tokenJson = resultJson.getJSONObject("accessToken");
                    String token = tokenJson.getString("accessToken");
                    SharedPrefUtil.setAccesstoken(mContext,token);

                    MainActivity_.intent(mContext).start();
                }else{//进行手机号码绑定
                    String userName = mEtTel.getText().toString();
                    InputTelActivity_.intent(mContext).extra(InputTelActivity_.INTENT_ISREGISTER, Enum_CodeType.BOUND).extra(InputTelActivity_.INTENT_THIRDTOEKN, thirdToken).extra(InputTelActivity_.INTENT_TEL,userName).start();
                }
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
            progressDialog = AbDialogUtil.showProgressDialog(mContext, Global.LOADING_PROGRESSBAR_ID, "请求登录中...");
        }

        @Override
        public void onFinish() {
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            Log.i("LoginActivity", "2onFailure:statusCode = " + statusCode + ", content is " + content);
            if(error!=null){
                error.printStackTrace();
            }
            progressDialog.dismiss();

            AbToastUtil.showToast(mContext, content);
        }
    };
}
