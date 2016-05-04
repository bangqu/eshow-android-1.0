package cn.org.eshow.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import cn.org.eshow.demo.R;
import cn.org.eshow.demo.common.CommonActivity;
import cn.org.eshow.demo.common.Global;
import cn.org.eshow.demo.common.SharedPrefUtil;
import cn.org.eshow.demo.network.ESResponseListener;
import cn.org.eshow.demo.network.MyHttpUtil;
import cn.org.eshow.demo.network.NetworkInterface;
import cn.org.eshow.demo.view.LoginAutoCompleteEdit;
import cn.org.eshow.fragment.ESProgressDialogFragment;
import cn.org.eshow.http.ESRequestParams;
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
 * 修改表单页面，只修改某项字符串类型的值
 * Created by daikting on 16/3/30.
 */@EActivity(R.layout.activity_modifystringvalue)
public class ModifyStringValueActivity extends CommonActivity {
    private Context mContext = ModifyStringValueActivity.this;
    private static final String tag="ModifyStringValue";
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
    //页面需要传递返回码
    public static final String INTENT_RETURN_CODE_TAG = "intent.returncode";

    private String interfaceName = "";
    private String paramKey = "";
    private String paramValue = "";
    private int returnCode = InfoFormActivity_.RETURN_BASEINFO_CODE;
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

        returnCode = getIntent().getIntExtra(INTENT_RETURN_CODE_TAG,InfoFormActivity_.RETURN_BASEINFO_CODE);
        String title = getIntent().getStringExtra(INTENT_TITLE_TAG);
        mTvTitle.setText(title);
        String hintText = getIntent().getStringExtra(INTNET_HINTTEXT_TAG);
        etValue.setHint(hintText);


        interfaceName = getIntent().getStringExtra(INTNET_INTERFACE_TAG);
        paramKey = getIntent().getStringExtra(INTNET_PARAMKEY_TAG);
        if(paramKey.equals("user.age")){
            etValue.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        }else if(paramKey.equals("user.intro")){
            etValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        }

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
        if(paramKey.equals("user.age")) {
            int age =Integer.valueOf(inputStr);
            if(age < 0){
                ESToastUtil.showToast(mContext,"年龄不能为负数！");
                return;
            }else if(age > 50 && age < 100){
                ESToastUtil.showToast(mContext,"您的年纪可真大呀！");
            }else if(age > 100){
                ESToastUtil.showToast(mContext,"这是您的真实年龄！？反正我是不信！");
                return;
            }
        }else if(paramKey.equals("user.email")){
            if(!ESStrUtil.isEmail(inputStr)){
                ESToastUtil.showToast(mContext,"请输入正确的邮箱地址！");
                return;
            }
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

        Log.i(tag,"修改XX信息请求地址："+
                Global.SERVER_URL + interfaceName + ".json?"+
                "accessToken=" + SharedPrefUtil.getAccessToken(mContext)+
                "&" + paramKey + "=" + inputStr
        );
        new MyHttpUtil(mContext).post(interfaceName, abRequestParams, responseListener);
    }

    ESResponseListener responseListener = new ESResponseListener(mContext) {
        @Override
        public void onBQSucess(String esMsg, JSONObject resultJson) {
            NetworkInterface.refreshUserInfo(mContext,userBeanResponseListener);
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
            progressDialog.setCancelable(false);
        }

        @Override
        public void onFinish() {
            Log.i(tag,"1onFinish");
        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            progressDialog.dismiss();

            Log.i(tag, "1onFailure:statusCode is " + statusCode + ",content is " + content + ",error is "+error.toString());
            error.printStackTrace();
            try {
                JSONObject ret=new JSONObject(content);
                int rr=ret.getInt("status");
                ESLogUtil.i(mContext,"status =="+rr);
                if(rr==-9){
                    ModifyStringValueActivity.this.finish();
                    Intent intent = new Intent(Global.EShow_Broadcast_Action.ACTION_MISSING_TOKEN);
                    sendBroadcast(intent);

                    ESLogUtil.i(mContext, "登录过期，重新登录");
                    ESToastUtil.showToast(mContext,"登录过期，重新登录");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if(error.toString().contains("登录过期")){
                    ESLogUtil.i(mContext, "error is "+error.toString());
                    ESToastUtil.showToast(mContext,"登录过期，重新登录");

                    ModifyStringValueActivity.this.finish();
                    Intent intent = new Intent(Global.EShow_Broadcast_Action.ACTION_MISSING_TOKEN);
                    sendBroadcast(intent);
                }
            }
            //ESToastUtil.showToast(mContext, "请求失败，错误码：" + statusCode);
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
            Intent backData = new Intent();
            backData.putExtra(INTENT_PARAMVALUE_TAG,paramValue);
            setResult(returnCode,backData);
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
        }

        @Override
        public void onFinish() {
            Log.i(tag,"2onFinish");

            progressDialog.dismiss();
        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            Log.i(tag,"2onFailure:statusCode is "+statusCode+",content is "+content+",error is "+error.toString());
            error.printStackTrace();

            progressDialog.dismiss();
            ESToastUtil.showToast(mContext, "请求失败，错误码：" + statusCode);
        }
    };
}