package com.bangqu.eshow.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.common.CommonActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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

//    @ViewById(R.id.etTel)
//    LoginAutoCompleteEdit mEtTel;
//    @ViewById(R.id.etPassword)
//    LoginAutoCompleteEdit mEtPassword;
    @ViewById(R.id.btnLogin)
    Button mBtnLogin;
    @ViewById(R.id.tvForgetPW)
    TextView mTvForgetPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        mTvTitle.setText(getTitle());
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);
    }
    @Click(R.id.btnLogin)
    void onLogin(){
        MainActivity_.intent(mContext).start();
    }
    @Click(R.id.tvForgetPW)
    void onForgetPW(){
        InputTelActivity_.intent(mContext).extra(InputTelActivity.INTENT_ISREGISTER,false).start();
    }

    @Click(R.id.rlBack)
    void onBack(){
        finish();
    }

}
