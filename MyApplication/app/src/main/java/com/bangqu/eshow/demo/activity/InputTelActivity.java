package com.bangqu.eshow.demo.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.common.BackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 输入登录手机号码的页面,是注册和找回密码公用的页面
 * 通过Intent 传递isRegister状态标识是否是注册
 * Created by daikting on 16/2/24.
 */
@EActivity(R.layout.activity_inputtel)
public class InputTelActivity extends BackActivity {
    //是否要进入注册页面
    public static final String INTENT_ISREGISTER = "IsRegister";

    @ViewById(R.id.etTel)
    EditText mEtTel;
    @ViewById(R.id.btnSubmit)
    Button mBtnSubmit;
    @ViewById(R.id.llAgreement)
    LinearLayout mLlAgreement;
    @ViewById(R.id.cbAgree)
    CheckBox mCbAgree;
    @ViewById(R.id.tvAgreement)
    TextView mTvAgreement;

    private boolean isRegisterUI = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRegisterUI = getIntent().getBooleanExtra(INTENT_ISREGISTER,true);
    }

    @AfterViews
    void init() {
        if(isRegisterUI){
            this.setTitle("注册");
        }else{
            this.setTitle("找回密码");

        }
    }

}
