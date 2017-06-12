package com.bangqu.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bangqu.base.activity.BaseActivity;
import com.bangqu.bean.UserLoginBean;
import com.bangqu.utils.Contact;
import com.bangqu.utils.SharedUtils;
import com.bangqu.zhiying.R;
import com.bangqu.zhiying.bean.AccessTokenBean;
import com.longtu.base.util.StringUtils;
import com.longtu.base.util.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements IUiListener{

    private EditText etName ,etPwd;
    private Button btnLogin ;
    private TextView tvForget;
    private TextView tvWechat, tvQQ;
    private TextView tvShort;

    private Message msg;
    private String username;
    private AccessTokenBean accessTokenBean;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Contact.userLoginBean= JSONObject.parseObject(msg.obj.toString(),UserLoginBean.class);
                    if (Contact.userLoginBean!=null){
                        ToastUtils.show(LoginActivity.this, Contact.userLoginBean.getMsg());
                        if (Contact.userLoginBean.getStatus().equals("1")){
                            SharedUtils.setUserNamePwd(LoginActivity.this,etName.getText().toString(),etPwd.getText().toString(),null);
                            if (Contact.userLoginBean.getTeacher()!=null){
                                Jump(TraderActivity.class);
                            }else {
                                Jump(HomeActivity.class);
                            }
                            finish();
                        }
                    }
                    break;
                case 2:
                    Contact.userLoginBean= JSONObject.parseObject(msg.obj.toString(),UserLoginBean.class);
                    if (Contact.userLoginBean!=null){
                        if (Contact.userLoginBean.type){
                            ToastUtils.show(LoginActivity.this, Contact.userLoginBean.getMsg());
                            if (Contact.userLoginBean.getStatus().equals("1")) {
                                SharedUtils.setUserNamePwd(LoginActivity.this, etName.getText().toString(), etPwd.getText().toString(), null);
                                if (Contact.userLoginBean.getTeacher() != null) {
                                    Jump(TraderActivity.class);
                                } else {
                                    Jump(HomeActivity.class);
                                }
                                finish();
                            }
                        }else {
                            intent=new Intent(LoginActivity.this,BindingMobileActivity.class);
                            intent.putExtra("platform",platform);
                            intent.putExtra("username",username);
                            Jump(intent);
                        }
                    }

                    break;
                case 3:
                    accessTokenBean= JSONObject.parseObject(msg.obj.toString(),AccessTokenBean.class);
                    if (accessTokenBean!=null){
                        setQQBind();
                    }
                    break;
            }
        }
    };

    private void setQQBind() {
        platform="qq";
        params=new RequestParams();
        username=accessTokenBean.getOpenid();
        params.put("thirdParty.platform",platform);
        params.put("thirdParty.username",accessTokenBean.getOpenid());
        params.put("user.clientId", Contact.deviceToken);
        post("user/third",params);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initViews() {
        etName =(EditText)findViewById(R.id.etName);
        etPwd =(EditText)findViewById(R.id.etPwd);
        btnLogin =(Button) findViewById(R.id.btnLogin);
        tvForget =(TextView) findViewById(R.id.tvForget);
        tvWechat =(TextView) findViewById(R.id.tvWechat);
        tvQQ =(TextView) findViewById(R.id.tvQQ);
        tvShort =(TextView) findViewById(R.id.tvShort);
        setBackVisibility(View.GONE);

    }

    @Override
    public void initDatas() {
        setTitle("登录");
        setRightTeTVisibility(View.VISIBLE);
        setRightText("注册");
        if (rlBack!=null){
            rlBack.setVisibility(View.GONE);
        }
    }

    @Override
    public void setDatas() {

    }

    @Override
    public void setListener() {
        btnLogin.setOnClickListener(this);
        tvForget.setOnClickListener(this);

        tvWechat.setOnClickListener(this);
        tvQQ.setOnClickListener(this);
        tvShort.setOnClickListener(this);

    }

    private String platform;
    @Override
    public void ResumeDatas() {
        Contact.verify=false;
        if (!StringUtils.isEmpty(SharedUtils.getUserName(this))) {
            etName.setText(SharedUtils.getUserName(this));
        }

        if (!StringUtils.isEmpty(Contact.Openid)){
            platform="weixin";
            params=new RequestParams();
            params.put("thirdParty.platform",platform);
            params.put("thirdParty.username", Contact.Openid);
            post("user/third",params);
            username= Contact.Openid;
            Contact.Openid="";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvRight:
                Jump(RegisterActivity.class);
                break;
            case R.id.tvForget:
                Jump(ForgetActivity.class);
                break;
            case R.id.btnLogin:

                if (StringUtils.isEmpty(etName.getText().toString())){
                    ToastUtils.show(LoginActivity.this,"请输入用户名");
                    return;
                }

                if (StringUtils.isEmpty(etPwd.getText().toString())){
                    ToastUtils.show(LoginActivity.this,"请输入密码");
                    return;
                }

                params=new RequestParams();
                params.put("user.username",etName.getText().toString());
                params.put("user.password",etPwd.getText().toString());
                params.put("user.clientId", Contact.deviceToken);
                post("user/login",params);

                break;
            case R.id.tvWechat:
                if (Contact.isWeixinAvilible(this)) {
                    //微信登录发起
                    IWXAPI api = WXAPIFactory.createWXAPI(this, "wxce3287b52eccea84", true);

                    api.registerApp("wxce3287b52eccea84");
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo_test";
                    api.sendReq(req);
                } else {
                    ToastUtils.show(this, "请安装微信");
                }
                break;
            case R.id.tvQQ:
                LoginQQ();
                break;
            case R.id.tvShort:
                Jump(CodeLoginActivity.class);
                break;
        }
    }

    private Tencent mTencent;
    public void LoginQQ()
    {
        mTencent = Tencent.createInstance("1106166424", this.getApplicationContext());
        if (!mTencent.isSessionValid())
        {
            mTencent.login(this, "all", this);
        }
    }

    @Override
    public void OnReceive(String requestname, String response) {
        msg=new Message();
        if (requestname.equals("user/login")){
            msg.what=1;
        }else if (requestname.equals("user/third")){
            msg.what=2;
        }
        msg.obj=response;
        handler.sendMessage(msg);
    }

    @Override
    public void onComplete(Object o) {
        msg=new Message();
        msg.what=3;
        msg.obj=o;
        handler.sendMessage(msg);
    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,this);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
