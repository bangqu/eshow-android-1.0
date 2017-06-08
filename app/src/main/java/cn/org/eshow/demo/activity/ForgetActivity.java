package cn.org.eshow.demo.activity;

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
import com.bangqu.utils.SharedUtils;
import com.bangqu.view.ClearEditText;
import com.longtu.base.util.StringUtils;
import com.longtu.base.util.ToastUtils;
import com.loopj.android.http.RequestParams;

import cn.org.eshow.demo.R;

public class ForgetActivity extends BaseActivity {

    private ClearEditText etPhone;
    private TextView tvSend;
    private ClearEditText etCode;
    private ClearEditText etPwd;
    private ClearEditText etQPwd;
    private Button btnOk;
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
                        ToastUtils.show(ForgetActivity.this,commonBean.getMsg());
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
                        ToastUtils.show(ForgetActivity.this, Contact.userLoginBean.getMsg());
                        if (Contact.userLoginBean.getStatus().equals("1")){
                            SharedUtils.setUserNamePwd(ForgetActivity.this,etPhone.getText().toString(),etPwd.getText().toString(),null);
                            finish();
                        }
                    }
                    break;
            }
        }
    };


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_forget);
    }

    @Override
    public void initViews() {
        etPhone = (ClearEditText) findViewById(R.id.etPhone);
        tvSend = (TextView) findViewById(R.id.tvSend);
        etCode = (ClearEditText) findViewById(R.id.etCode);
        etPwd = (ClearEditText) findViewById(R.id.etPwd);
        etQPwd = (ClearEditText) findViewById(R.id.etQPwd);
        btnOk = (Button) findViewById(R.id.btnOk);
    }

    @Override
    public void initDatas() {
        setTitle("忘记密码");
    }

    @Override
    public void setDatas() {

    }

    @Override
    public void setListener() {
        tvSend.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void ResumeDatas() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvSend:
                if (StringUtils.isEmpty(etPhone.getText().toString())){
                    ToastUtils.show(ForgetActivity.this,"请输入手机号");
                    return;
                }
                if (!Contact.isMobileNO(etPhone.getText().toString())){
                    ToastUtils.show(this,"手机格式化不正确");
                    return;
                }
                params=new RequestParams();
                params.put("captcha.mobile",etPhone.getText().toString());
                params.put("captcha.type","password");
                post("captcha/send",params);
                break;
            case R.id.btnOk:
                if (StringUtils.isEmpty(etPhone.getText().toString())){
                    ToastUtils.show(ForgetActivity.this,"请输入手机号");
                    return;
                }

                if (!Contact.isMobileNO(etPhone.getText().toString())){
                    ToastUtils.show(this,"手机格式化不正确");
                    return;
                }

                if (StringUtils.isEmpty(etCode.getText().toString())) {
                    ToastUtils.show(ForgetActivity.this, "请输入验证码");
                    return;
                }

                if (StringUtils.isEmpty(etPwd.getText().toString())) {
                    ToastUtils.show(ForgetActivity.this, "请输入新密码");
                    return;
                }

                if (StringUtils.isEmpty(etQPwd.getText().toString())) {
                    ToastUtils.show(ForgetActivity.this, "请输入确认密码");
                    return;
                }

                if (etPwd.getText().toString().length()<6){
                    ToastUtils.show(ForgetActivity.this, "密码必须6-20位");
                    return;
                }

                if (!etPwd.getText().toString().equals(etQPwd.getText().toString())){
                    ToastUtils.show(ForgetActivity.this, "密码不一致");
                    return;
                }

                params=new RequestParams();
                params.put("user.username",etPhone.getText().toString());
                params.put("code",etCode.getText().toString());
                params.put("user.password",etPwd.getText().toString());
                params.put("user.confirmPassword",etQPwd.getText().toString());
                post("forget-password/reset",params);

                break;
        }
    }

    @Override
    public void OnReceive(String requestname, String response) {
        msg=new Message();
        if (requestname.equals("captcha/send")){
            msg.what=1;
        }else if (requestname.equals("forget-password/reset")){
            msg.what=3;
        }
        msg.obj=response;
        handler.sendMessage(msg);
    }
}
