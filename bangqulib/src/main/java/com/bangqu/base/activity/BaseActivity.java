package com.bangqu.base.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.lib.R;
import com.bangqu.utils.Contact;
import com.longtu.base.notice.InitListener;
import com.longtu.base.util.StringUtils;
import com.longtu.base.util.ToastUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Random;
import cz.msebera.android.httpclient.Header;

public abstract class BaseActivity extends FragmentActivity implements InitListener, View.OnClickListener {

    public AsyncHttpClient httpClient;

    public FragmentManager fragmentManager;
    public FragmentTransaction transaction;
    public RequestParams params;
    public Intent intent;

    public static String BASE_URL = "http://api.eshow.org.cn/";
    private InputMethodManager inputMethodManager;
    /* 加载框 */
    private Dialog loadDialog;

    public static final int AREA=1;
    public static final int SELECTAREA=2;
    public static final int REQUEST_CODE_PICK_IMAGE = 0;
    public static final int SET_PHOTO = 3;
    public static final int REQUEST_C_IMAGE = 4;
    public static final int SEE_IMAGE = 40;
    public static final int SELECT_IMAGE = 41;
    public static final int QCODE=5;
    public static final int PHOTO=6;
    public static final int ADDPHOTO=88;
    public static final int Languge=7;
    public static final int SelecctLanguge=8;


    public final static int Venue = 1003;

    public ImageView ivBack;
    public TextView tvTitle, tvRight;
    public ImageView ivRight;

    public String token;

    public RelativeLayout rlBack;
    public MaterialMenuView material_back_button;
    public final static int NEWORDER=1001;
    public final static int NEWCUSTOM=1002;
    public final static String appKey="ZY4X2HYwhLIU9smY";
    public final static String appSecret="rg3pVd22g31Fv1mF";

    public static final String QINIU_URL = "http://oonc77hep.bkt.clouddn.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        httpClient = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(myCookieStore);
        httpClient.setTimeout(10000);

        fragmentManager = getSupportFragmentManager();

        setContentView();
        loadDialog = getRequestDg(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initViews();
        initHead();
        initDatas();

        setDatas();

        setListener();

    }

    public void initHead() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        tvRight = (TextView) findViewById(R.id.tvRight);

        material_back_button = (MaterialMenuView) findViewById(R.id.material_back_button);

        if (material_back_button != null) {
            material_back_button.setState(MaterialMenuDrawable.IconState.ARROW);
        }
        if (ivBack != null) {
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (rlBack != null) {
            rlBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if (ivRight != null) {
            ivRight.setOnClickListener(this);
        }
        if (tvRight != null) {
            tvRight.setOnClickListener(this);
        }
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (tvTitle != null)
            tvTitle.setText(title);
    }

    /**
     * 设置右边图片是否显示
     *
     * @param visible
     */
    public void setRightVisibility(int visible) {
        if (ivRight != null)
            ivRight.setVisibility(visible);
    }

    public void setRightResource(int id) {
        if (ivRight != null)
            ivRight.setImageResource(id);
    }

    /**
     * 设置右边字体是否显示
     *
     * @param visible
     */
    public void setRightTeTVisibility(int visible) {
        if (tvRight != null)
            tvRight.setVisibility(visible);
    }

    public void setRightText(String text) {
        if (tvRight != null)
            tvRight.setText(text);
    }


    public void setBackVisibility(int visible) {
        if (ivBack != null)
            ivBack.setVisibility(visible);
    }

    public void setBackResource(int id) {
        if (ivBack != null)
            ivBack.setImageResource(id);
    }

    public void loading(ImageView iv) {
        iv.setImageResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv.getDrawable();
        animationDrawable.start();
    }

    public void post(final String requestname,final RequestParams params) {
        if(params!=null){
            Log.e(requestname, BASE_URL + requestname + ".json?" + params.toString());
        }

        httpClient.post(BASE_URL + requestname + ".json", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e(requestname, new String(responseBody));
                Log.e("statusCode==>", statusCode + "");
                monitor("POST",BASE_URL + requestname + ".json",params==null?"":params.toString(),
                        new String(responseBody),statusCode+"");
                OnReceive(requestname, new String(responseBody));
                for (Header h : headers) {
                    Log.e(h.getName()+"==>", h.getValue());
                }
                disMiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("statusCode==>", statusCode + "");
                monitor("POST",BASE_URL + requestname + ".json",params==null?"":params.toString(),
                        new String(responseBody),statusCode+"");
                if (statusCode == 0) {
                    ToastUtils.show(BaseActivity.this, "请检查你的网络状况");
                }
                disMiss();
            }

        });

    }

    public void pullpost(final String requestname,final RequestParams params) {
        if(params!=null){
            Log.e(requestname, BASE_URL + requestname + ".json?" + params.toString());
        }
        httpClient.get(this, BASE_URL + requestname + ".json", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("responseBody==>", new String(responseBody));
                if (requestname.equals("qiniu/uptoken")) {
                    try {
                        monitor("POST",BASE_URL + requestname + ".json",params==null?"":params.toString(),
                                new String(responseBody),statusCode+"");
                        JSONObject jsonObject = new JSONObject(new String(responseBody));
                        if (jsonObject.has("msg")) {
                            token = jsonObject.getString("msg");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                OnReceive(requestname, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailure==>", statusCode + "");
                monitor("POST",BASE_URL + requestname + ".json",params==null?"":params.toString(),
                        new String(responseBody),statusCode+"");
                if (statusCode == 0) {
                    ToastUtils.show(BaseActivity.this, "请检查你的网络状况");
                }
//                OnReceive(requestname, "");
//                onError(requestname,statusCode+"");

                disMiss();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                BaseActivity.this.onProgress(bytesWritten, totalSize);
            }
        });
    }

    private void monitor(String mode,String url,String param,String result,String statusCode){
        params=new RequestParams();
        params.put("appSecret",appSecret);
        params.put("appKey",appKey);
        params.put("record.mode",mode);
        params.put("record.url",url);
        if (!StringUtils.isEmpty(param)) {
            params.put("record.params", param);
        }
        params.put("record.network", Contact.getNetworkStateName(this));
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            params.put("record.versionName",info.versionName);
            params.put("record.versionCode",info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        params.put("record.model",android.os.Build.MODEL);
        params.put("record.osName","Android");
        params.put("record.osVersion",android.os.Build.VERSION.RELEASE);
        params.put("record.crash",false);
        if (!StringUtils.isEmpty(result)) {
            params.put("record.result", result);
            params.put("record.responseSize",result.length());
        }
        params.put("record.statusCode",statusCode);
        Random random=new Random();
        params.put("record.responseTime",random.nextInt(1000));
        otherPost("https://api.monitor.easyapi.com/","record/save",params);
    }

    public void otherPost(final String Url, final String requestname, RequestParams params) {

        httpClient.post(this, Url + requestname + ".json", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("responseBody==>", new String(responseBody));
                if (requestname.equals("qiniu/uptoken")) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(responseBody));
                        if (jsonObject.has("msg")) {
                            token = jsonObject.getString("msg");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                OnReceive(requestname, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailure==>", statusCode + "");
                if (statusCode == 0) {
                    ToastUtils.show(BaseActivity.this, "请检查你的网络状况");
                }
                disMiss();
            }
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                BaseActivity.this.onProgress(bytesWritten, totalSize);
            }
        });
    }

    public void otherGet(final String Url, final String requestname, RequestParams params) {

        httpClient.get(this, Url + requestname , params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("responseBody==>", new String(responseBody));
                if (requestname.equals("qiniu/uptoken")) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(responseBody));
                        if (jsonObject.has("msg")) {
                            token = jsonObject.getString("msg");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                OnReceive(requestname, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailure==>", statusCode + "");
                if (statusCode == 0) {
                    ToastUtils.show(BaseActivity.this, "请检查你的网络状况");
                }
                disMiss();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                BaseActivity.this.onProgress(bytesWritten, totalSize);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ResumeDatas();
    }

    /***
     * 显示加载对话框
     */
    public void show() {
        if (loadDialog != null && !loadDialog.isShowing()) {
            loadDialog.show();
        }
    }

    public void disMiss() {
        if (loadDialog != null) {
            loadDialog.dismiss();
        }
    }

    public void Jump(Intent intent) {
        startActivity(intent);
    }

    /***
     * 设置加载框样式
     *
     * @param context
     * @return
     */
    private Dialog getRequestDg(Context context) {
        Dialog dgloading;
        dgloading = null;
        dgloading = new Dialog(context, R.style.loadingDialog);
        LinearLayout layout = new LinearLayout(context);

        layout.setBackgroundColor(context.getResources().getColor(
                R.color.transparent));
        View view = LayoutInflater.from(this).inflate(R.layout.bar, null);
        layout.addView(view);
        dgloading.setContentView(layout);
        dgloading.setCanceledOnTouchOutside(false);
        dgloading.setCancelable(false);

        return dgloading;
    }


    public void OnReceive(String requestname, String response) {

    }

    public void onProgress(long bytesWritten, long totalSize) {

    }

    /***
     * 跳转无参数
     *
     * @param cls
     */
    public void Jump(Class cls) {
        intent = new Intent(this, cls);
        startActivity(intent);
    }

    /***
     * 跳转有返回
     *
     * @param
     */
    public void Jump(Intent intent, int code) {
        startActivityForResult(intent, code);
    }

    /***
     * 替换当前页面显示
     */
    public void setReplace(int index) {

    }

    public void hideSoftInput(EditText editText) {
        if (inputMethodManager != null) {
            View v = this.getCurrentFocus();
            if (v == null) {
                return;
            }

            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            editText.clearFocus();
        }
    }

    public void onError(String requestname, String response){

    }


    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }


    /**
     * 上传返回地址
     *
     * @param imageurl
     */
    public void onComplete(String imageurl) {
    }


    public void getWx(final String requestname,RequestParams params) {
        if(params!=null){
            Log.e(requestname, BASE_URL + requestname + "?" + params.toString());
        }

        httpClient.post(this, "https://api.weixin.qq.com/sns/" + requestname, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e(requestname + "==>", new String(responseBody));

                OnReceive(requestname, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailure==>", statusCode + "");
                if (statusCode == 0) {
                    ToastUtils.show(BaseActivity.this, "请检查你的网络状况");
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                BaseActivity.this.onProgress(bytesWritten, totalSize);
            }
        });
    }

    public void get(final String url){

        httpClient.get(this, url, null,  new AsyncHttpResponseHandler()
        {
            @Override
            public void onStart() {
//                show();
            }

            @Override
            public void onSuccess(int statusCode , Header[] headers, byte[] responseBody) {
                Log.e("statusCode==>", statusCode+"");
                Log.e("url==>", new String(responseBody));
                OnReceive(url, new String(responseBody));

                for (Header h : headers) {
                    Log.e(h.getName(), h.getValue());
                }
//                disMiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("statusCode==>", statusCode+"");
                if(statusCode==0){
                    ToastUtils.show(BaseActivity.this, "请检查你的网络状况");
                }
//                disMiss();
            }
        });
    }

}
