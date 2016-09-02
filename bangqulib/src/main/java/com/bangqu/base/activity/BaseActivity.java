package com.bangqu.base.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.bangqu.lib.R;
import com.longtu.base.notice.InitListener;
import com.longtu.base.util.ToastUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;

public abstract class BaseActivity extends FragmentActivity implements InitListener,View.OnClickListener{

    public AsyncHttpClient httpClient;

    public FragmentManager fragmentManager;
    public FragmentTransaction transaction;
    public RequestParams params;
    public Intent intent;

    public static String BASE_URL="http://www.xxtime.net/student/";
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

    public ImageView ivBack;
    private TextView tvTitle;
    public ImageView ivRight;

    public String token;

    public static final String QINIU_URL="http://img.yinwan.bangqu.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        httpClient=new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        httpClient.setCookieStore(myCookieStore);
        httpClient.setTimeout(10000);

        fragmentManager=getSupportFragmentManager();

        setContentView();
        loadDialog=getRequestDg(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initViews();
        initHead();
        initDatas();

        setDatas();

        setListener();

    }

    public  void initHead(){
        ivBack=(ImageView)findViewById(R.id.ivBack);
        tvTitle=(TextView)findViewById(R.id.tvTitle);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        if (ivBack!=null) {
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if (ivRight!=null) {
            ivRight.setOnClickListener(this);
        }
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
//        if (tvTitle!=null)
        tvTitle.setText(title);
    }

    /**
     * 设置右边是否显示
     * @param visible
     */
    public void setRightVisibility(int visible){
        if (ivRight!=null)
        ivRight.setVisibility(visible);
    }

    public void setRightResource(int id){
        if (ivRight!=null)
        ivRight.setImageResource(id);
    }

    public void seBackVisibility(int visible){
        if (ivBack!=null)
        ivBack.setVisibility(visible);
    }

    public void seBackResource(int id){
        if (ivBack!=null)
            ivBack.setImageResource(id);
    }

    public void loading(ImageView iv){
        iv.setImageResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv.getDrawable();
        animationDrawable.start();
    }

    public void post(final String requestname, RequestParams params){

        httpClient.post(this, BASE_URL+requestname+".json", params,  new AsyncHttpResponseHandler()
        {
            @Override
            public void onStart() {
                show();
            }

            @Override
            public void onSuccess(int statusCode , Header[] headers, byte[] responseBody) {
                Log.e(requestname, new String(responseBody));
                Log.e("statusCode==>", statusCode+"");
                OnReceive(requestname, new String(responseBody));

                for (Header h : headers) {
                    Log.e(h.getName(), h.getValue());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("statusCode==>", statusCode+"");
                if(statusCode==0){
                    ToastUtils.show(BaseActivity.this, "请检查你的网络状况");
                }
                disMiss();
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

    public  void pullpost(final String requestname, RequestParams params){

        httpClient.post(this, BASE_URL+requestname+".json", params,  new AsyncHttpResponseHandler()
        {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("responseBody==>",new String(responseBody));

                OnReceive(requestname, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
               Log.e("onFailure==>",statusCode+"");
                if(statusCode==0){
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

        if(loadDialog!=null&&!loadDialog.isShowing()){
            loadDialog.show();
        }

    }

    public void disMiss(){
       if(loadDialog!=null){
           loadDialog.cancel();
       }
    }

    public void Jump(Intent intent){
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


    public void OnReceive(String requestname, String response){

    }

    public void onProgress(long bytesWritten, long totalSize ){

    }

    /***
     * 跳转无参数
     * @param cls
     */
    public void Jump(Class cls)
    {
        intent=new Intent(this, cls);
        startActivity(intent);
    }

    /***
     * 跳转有返回
     * @param
     */
    public void Jump(Intent intent, int code)
    {
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


    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }



    /**
     * 上传返回地址
     * @param imageurl
     */
    public  void onComplete(String imageurl){
    }

}
