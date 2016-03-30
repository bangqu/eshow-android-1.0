package com.bangqu.eshow.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.common.CommonActivity;
import com.bangqu.eshow.demo.common.Global;
import com.bangqu.eshow.demo.common.SharedPrefUtil;
import com.bangqu.eshow.demo.network.ESResponseListener;
import com.bangqu.eshow.demo.network.MyHttpUtil;
import com.bangqu.eshow.demo.view.LoginAutoCompleteEdit;
import com.bangqu.eshow.fragment.ESProgressDialogFragment;
import com.bangqu.eshow.http.ESRequestParams;
import com.bangqu.eshow.util.ESDialogUtil;
import com.bangqu.eshow.util.ESStrUtil;
import com.bangqu.eshow.util.ESToastUtil;
import com.bangqu.eshow.util.ESViewUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * 修改表单页面，只修改某项字符串类型的值
 * Created by daikting on 16/3/30.
 */@EActivity(R.layout.activity_modifystringvalue)
public class ModifyStringValueActivity extends CommonActivity {
    private Context mContext = ModifyStringValueActivity.this;
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;
    @ViewById(R.id.tvSubTitle)
    TextView mTvSubTitle;

    @ViewById
    LoginAutoCompleteEdit etValue;

    //页面需要传递页面标题
    public static final String INTENT_TITLE_TAG ="intent.title";
    //页面需要传递输入框提示文本
    public static final String INTNET_HINTTEXT_TAG ="intent.hintText";
    //页面需要传递保存操作的接口名字
    public static final String INTNET_INTERFACE_TAG ="intent.interface";
    //页面需要传递保存操作的发送的参数键名
    public static final String INTNET_PARAMKEY_TAG ="intent.paramKey";
    //页面需要传递保存操作的发送的参数的值
    public static final String INTENT_PARAMVALUE_TAG = "intent.paramValue";

    private String interfaceName = "";
    private String paramKey = "";
    private String paramValue = "";
    ESProgressDialogFragment progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        ESViewUtil.scaleContentView((RelativeLayout) findViewById(R.id.rlParent));
        mTvSubTitle.setText("保存");
        mTvSubTitle.setVisibility(View.VISIBLE);
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);

        String title = getIntent().getStringExtra(INTENT_TITLE_TAG);
        mTvTitle.setText(title);
        String hintText = getIntent().getStringExtra(INTNET_HINTTEXT_TAG);
        etValue.setHint(hintText);

        interfaceName = getIntent().getStringExtra(INTNET_INTERFACE_TAG);
        paramKey = getIntent().getStringExtra(INTNET_PARAMKEY_TAG);
        String paramValue = getIntent().getStringExtra(INTENT_PARAMVALUE_TAG);
        if(!ESStrUtil.isEmpty(paramValue)){
            etValue.setText(paramValue);
        }
    }
    @Click(R.id.tvSubTitle)
    void onSave(){
        String inputStr = etValue.getText().toString();
        if(ESStrUtil.isEmpty(inputStr)){
            ESToastUtil.showToast(mContext,"请输入信息再保存！");
          return;
        }
        saveInfo(inputStr);
    }

    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }
    private void saveInfo(String inputStr) {
        ESRequestParams abRequestParams = new ESRequestParams();
        abRequestParams.put("accessToken", SharedPrefUtil.getAccessToken(mContext));
        paramValue = inputStr;
        abRequestParams.put(paramKey, inputStr);
        new MyHttpUtil(mContext).post(interfaceName, abRequestParams, responseListener);
    }

    ESResponseListener responseListener = new ESResponseListener(mContext) {
        @Override
        public void onBQSucess(String esMsg, JSONObject resultJson) {
            Intent backData = new Intent();
            backData.putExtra(INTENT_PARAMVALUE_TAG,paramValue);
            setResult(0x22,backData);
            finish();
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
            progressDialog.dismiss();
            ESToastUtil.showToast(mContext, "请求失败，错误码：" + statusCode);
        }
    };
}
