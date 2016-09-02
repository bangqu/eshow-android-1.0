package com.bangqu.base.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangqu.lib.R;
import com.bangqu.listener.ReceiveListener;
import com.longtu.base.notice.InitListener;
import com.longtu.base.util.ToastUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;


import cz.msebera.android.httpclient.Header;

public abstract class BaseFragment extends Fragment implements InitListener,OnClickListener,ReceiveListener {
    
    public View view;
    
    public int layout;

    public AsyncHttpClient httpClient;

    public RequestParams params;
    public Intent intent;

    public static String BASE_URL="http://www.xxtime.net/student/";
    private InputMethodManager inputMethodManager;
    /* 加载框 */
    private Dialog loadDialog;

    public static final int CITYCHOOSE=1;

    private final static String QINIU_URL="http://7sbsl4.com1.z0.glb.clouddn.com/";
    private ImageView ivBack;
    public TextView tvTitle;
    private ImageView ivRight;
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setContentView();
       
        view=inflater.inflate(layout, null);
       
        initViews();

        inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        httpClient=new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getActivity());
        httpClient.setCookieStore(myCookieStore);
        httpClient.setTimeout(10000);
        loadDialog=getRequestDg(getActivity());

        initHead();
        
        initDatas();
        
        setDatas();
        
        setListener();

        
        return view;
    }

    public void loading(ImageView iv){
        iv.setImageResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv.getDrawable();
        animationDrawable.start();
    }

    public  void initHead(){
        ivBack=(ImageView)view.findViewById(R.id.ivBack);
        tvTitle=(TextView)view.findViewById(R.id.tvTitle);
        ivRight=(ImageView)view.findViewById(R.id.ivRight);
        if (ivBack!=null) {
            ivBack.setVisibility(View.GONE);
        }
        if (ivRight!=null) {
            ivRight.setOnClickListener(this);
        }

        if (tvTitle!=null) {
            tvTitle.setOnClickListener(this);
        }
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        if (tvTitle!=null)
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


    public void post(final String requestname, RequestParams params, final String requestCode){

        httpClient.post(getActivity(),BASE_URL+requestname+".json", params,  new AsyncHttpResponseHandler()
        {

            @Override
            public void onStart() {
                show();
            }

            @Override
            public void onSuccess(int statusCode , Header[] headers, byte[] responseBody) {
                Log.e(requestname, new String(responseBody));
                Log.e("statusCode==>", statusCode+"");
                Receive(requestCode, new String(responseBody));
                disMiss();
                for (Header h : headers) {
                    Log.e(h.getName(), h.getValue());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("statusCode==>", statusCode+"");
                if(statusCode==0){
                    ToastUtils.show(getActivity(), "请检查你的网络状况");
                }
              disMiss();
            }

        });

    }

    public  void pullpost(final String requestname, RequestParams params, final String requestCode){

        httpClient.post(getActivity(),  BASE_URL+requestname+".json", params,  new AsyncHttpResponseHandler()
        {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("responseBody==>",new String(responseBody));

                Receive(requestCode, new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailure==>",statusCode+"");
                if(statusCode==0){
                    ToastUtils.show(getActivity(), "请检查你的网络状况");
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
            }
        });
    }

    public void Jump(Intent intent){
        startActivity(intent);
    }

    /***
     * 跳转无参数
     * @param cls
     */
    public void Jump(Class cls)
    {
        intent=new Intent(getActivity(), cls);
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


    @Override
    public void onResume() {
        ResumeDatas();
        super.onResume();
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.bar, null);
        layout.addView(view);
        dgloading.setContentView(layout);
        dgloading.setCanceledOnTouchOutside(false);
        dgloading.setCancelable(false);

        return dgloading;
    }

    public void hideSoftInput(EditText editText) {
        if (inputMethodManager != null) {
            View v = getActivity().getCurrentFocus();
            if (v == null) {
                return;
            }

            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            editText.clearFocus();
        }
    }



}
