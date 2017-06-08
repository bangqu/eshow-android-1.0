package cn.org.eshow.demo.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bangqu.base.activity.BaseActivity;
import com.bangqu.bean.CommonBean;
import com.bangqu.bean.UserLoginBean;
import com.bangqu.utils.Contact;
import com.bangqu.view.ClearEditText;
import com.longtu.base.util.StringUtils;
import com.longtu.base.util.ToastUtils;
import com.loopj.android.http.RequestParams;

import cn.org.eshow.demo.R;

public class CodeLoginActivity extends BaseActivity {

    private ClearEditText etPhone;
    private TextView tvSend;
    private ClearEditText etCode;
    private Button btnSubmit;

    private Message msg;
    private CommonBean commonBean;
    private int  second=60;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    commonBean= JSONObject.parseObject(msg.obj.toString(),CommonBean.class);
                    if (commonBean!=null){
                        ToastUtils.show(CodeLoginActivity.this,commonBean.getMsg());
                        if (commonBean.getStatus().equals("1")){
                           /* intent=new Intent(RegisterActivity.this,SetPwdActivity.class);
                            intent.putExtra("phone",etPhone.getText().toString());
                            Jump(intent);
                            finish();*/
                            second=60;
                            tvSend.setEnabled(false);
                            sendEmptyMessageDelayed(2,1000);
                        }
                    }
                    break;
                case 2:
                    second--;
                    tvSend.setText(second+"s");
                    if (second>0){
                        sendEmptyMessageDelayed(2,1000);
                    }else {
                        tvSend.setText("获取验证码");
                        tvSend.setEnabled(true);
                    }
                    break;
                case 3:
                    Contact.userLoginBean= JSONObject.parseObject(msg.obj.toString(), UserLoginBean.class);
                    if (Contact.userLoginBean!=null){
                        ToastUtils.show(CodeLoginActivity.this, Contact.userLoginBean.getMsg());
                        if (Contact.userLoginBean.getStatus().equals("1")){

                            intent=new Intent(CodeLoginActivity.this, MainActivity_.class);
                            /**
                             * 顶部跳转结束之前所有Activity
                             */
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Jump(intent);
                        }
                    }
                    break;
            }
        }
    };


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_code_login);
    }

    @Override
    public void initViews() {
        etPhone = (ClearEditText) findViewById(R.id.etPhone);
        tvSend = (TextView) findViewById(R.id.tvSend);
        etCode = (ClearEditText) findViewById(R.id.etCode);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    @Override
    public void initDatas() {
        setTitle("验证码登录");
    }

    @Override
    public void setDatas() {

    }

    @Override
    public void setListener() {
        tvSend.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void ResumeDatas() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvSend:
                if (StringUtils.isEmpty(etPhone.getText().toString())){
                    ToastUtils.show(CodeLoginActivity.this,"请输入手机号");
                    return;
                }
                if (!Contact.isMobileNO(etPhone.getText().toString())){
                    ToastUtils.show(this,"手机格式化不正确");
                    return;
                }
                params=new RequestParams();
                params.put("captcha.mobile",etPhone.getText().toString());
                params.put("captcha.type","login");
                post("captcha/send",params);
                break;
            case R.id.btnSubmit:
                if (StringUtils.isEmpty(etPhone.getText().toString())){
                    ToastUtils.show(CodeLoginActivity.this,"请输入手机号");
                    return;
                }

                if (!Contact.isMobileNO(etPhone.getText().toString())){
                    ToastUtils.show(this,"手机格式化不正确");
                    return;
                }

                if (StringUtils.isEmpty(etCode.getText().toString())) {
                    ToastUtils.show(CodeLoginActivity.this, "请输入验证码");
                    return;
                }
                params=new RequestParams();
                params.put("user.username",etPhone.getText().toString());
                params.put("code",etCode.getText().toString());
                post("captcha/login",params);
                break;
        }
    }

    @Override
    public void OnReceive(String requestname, String response) {
        msg=new Message();
        if (requestname.equals("captcha/send")){
            msg.what=1;
        }else if (requestname.equals("captcha/login")){
            msg.what=3;
        }
        msg.obj=response;
        handler.sendMessage(msg);
    }
}
