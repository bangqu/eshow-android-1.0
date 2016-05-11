package cn.org.eshow.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.pingplusplus.android.PaymentActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.google.gson.Gson;
import cn.org.eshow.demo.R;
import cn.org.eshow.demo.common.CommonActivity;
import cn.org.eshow.demo.common.Global;
import cn.org.eshow_framwork.util.AbLogUtil;
import cn.org.eshow_framwork.util.AbViewUtil;

/**
 * 网页支付页面
 *
 * @author
 */
public class PayWebViewActivity extends CommonActivity implements OnClickListener {
    private static final String tag="PayWebViewActivity";
    public static final String INTENT_URL_TAG = "intent.url";
    private RelativeLayout rlBack;
    private MaterialMenuView mMaterialBackButton;
    private TextView tvTitle;

    private WebView webView;
    private String title = "";
    private Context mContext;
    ProgressBar progressBar;
    private String webUrl = "";

    /**
     * 开发者需要填一个服务端 CHARGE_URL 该 CHARGE_URL 是用来请求支付需要的 charge 。务必确保，CHARGE_URL
     * 能返回 json 格式的 charge 对象。 服务端生成 charge 的方式可以参考 ping++ 官方文档，地址
     * https://pingxx.com/guidance/server/import
     * <p/>
     * 【 http://218.244.151.190/demo/charge 】是 ping++ 为了方便开发者体验 sdk 而提供的一个临时 url
     * 。 改 url 仅能调用【模拟支付控件】，开发者需要改为自己服务端的 url 。
     */
    private String charge_url;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        mContext = this;
        webUrl = getIntent().getStringExtra(PayWebViewActivity.INTENT_URL_TAG);
        title = "支付订单";
        findView();
        fillDate();

    }

    private void findView() {

        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        rlBack.setOnClickListener(this);
        mMaterialBackButton = (MaterialMenuView) findViewById(R.id.material_back_button);
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void fillDate() {
        webView.loadUrl(webUrl);
        webView.getSettings().setJavaScriptEnabled(true);// 开启jacascript
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// 支持通过JS打开新窗口
//        webView.getSettings().setSupportZoom(true);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setLoadsImagesAutomatically(true);// 支持自动加载图片
//        webView.requestFocusFromTouch();
//        webView.getSettings().setBuiltInZoomControls(true);// 设置支持缩放
//        webView.getSettings().setDefaultZoom(ZoomDensity.FAR);// 屏幕自适应网页，如果没有这个在低分辨率手机上显示会异常
        //AbLogUtil.d(mContext, "webUrl:" + webUrl);
        Log.i(tag,"fillDate :webUrl is "+webUrl);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        progressBar.setVisibility(View.VISIBLE);
        /**
         * 设置 js 交互类
         */
        webView.addJavascriptInterface(new JSInterface(), "PINGPP_ANDROID_SDK");
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    // 内部类
    public class MyWebViewClient extends WebViewClient {

        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象。

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {// 拦截无资源的请求

            if (url != null && url.startsWith("uppay://")) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
            return false;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
        }

        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.INVISIBLE);
        }

        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            progressBar.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * JS回调java
     */
    class JSInterface {
        @JavascriptInterface
        // JS代码调用接口，调用方法：PINGPP_ANDROID_SDK.callPay(channel, amount);
        public void callPay(String url) {
            Log.i(tag,"callPay url is "+url);
            //AbLogUtil.i(mContext,"callPay url:"+url);
            url = url.substring(1, url.length());
            charge_url = Global.SERVER_URL + url;
            Log.i(tag,"callPay charge_url is "+charge_url);
            new PaymentTask().execute(new PaymentRequest(url));

        }
    }


    /**
     * http 请求
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    private static String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(url).post(body).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    /**
     * 请求支付凭据
     *
     * @author sunkai
     */
    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {
        @Override
        protected String doInBackground(PaymentRequest... pr) {
            PaymentRequest paymentRequest = pr[0];
            String data = null;
            String json = new Gson().toJson(paymentRequest);
            Log.i(tag,"json is "+json);
            try {
                // 向支付请求文件（例如 pay.php）请求数据
                //AbLogUtil.i(mContext, "charge_url:" + charge_url);
                Log.i(tag,"向支付请求文件（例如 pay.php）请求数据:charge_url is "+charge_url);
                data = postJson(charge_url, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            //AbLogUtil.d(mContext, data);
            Log.i(tag,"onPostExecute :data is "+data);

            Intent intent = new Intent(mContext, PaymentActivity.class);//??????
            intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
            startActivityForResult(intent, 1);
//            Intent intent = new Intent();
//            String packageName = getPackageName();
//            ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
//            intent.setComponent(componentName);
//            intent.putExtra(com.pingplusplus.android.PaymentActivity.EXTRA_CHARGE, data);
//            startActivityForResult(intent, 1);
        }
    }

    /**
     * 请求 Charge 的参数类
     *
     * @author sunkai
     */
    class PaymentRequest {
        String channel;
        int amount;

        public PaymentRequest(String url) {
            Map<String, Object> map = urlSplit(url);
            this.channel = map.get("channel").toString();
            this.amount = Integer.valueOf(map.get("amount").toString());
        }
    }

    public Map<String, Object> urlSplit(String data) {
        StringBuffer strbuf = new StringBuffer();
        StringBuffer strbuf2 = new StringBuffer();
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < data.length(); i++) {

            if (data.substring(i, i + 1).equals("=")) {

                for (int n = i + 1; n < data.length(); n++) {
                    if (data.substring(n, n + 1).equals("&") || n == data.length() - 1) {
                        map.put(strbuf.toString(), strbuf2);
                        strbuf = new StringBuffer("");
                        strbuf2 = new StringBuffer("");
                        i = n;
                        break;
                    }
                    strbuf2.append(data.substring(n, n + 1));
                }
                continue;
            }
            strbuf.append(data.substring(i, i + 1));
        }

        return map;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 支付页面返回处理
        AbLogUtil.i(mContext,"支付页面返回处理：请求码 requestCode ＝ "+requestCode+"，返回结果码 resultCode ＝ "+resultCode);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(tag,"requestCode == 1 && resultCode == Activity.RESULT_OK");
                String result = null;
                if(data!=null) {
                    result = data.getExtras().getString("pay_result");
                }

				/*
                 * 处理返回值 "success" - 支付成功 "fail" - 支付失败 "cancel" - 用户取消
				 * "invalid" - 未安装支付控件
				 */
                if ("success".equalsIgnoreCase(result)) {

                    AbLogUtil.d(mContext, "支付成功跳转");
                    Toast.makeText(this, "用户支付成功", Toast.LENGTH_SHORT).show();
                    String sucessUrl = "http://api.eshow.org.cn/pingpay/pay.jsp";
                    webView.loadUrl(sucessUrl);

                }else if("cancel".equalsIgnoreCase(result)){

                    AbLogUtil.d(mContext, "用户取消支付");
                    //AbToastUtil.showToast(mContext, "用户取消支付");
                    Toast.makeText(mContext,"用户取消支付",Toast.LENGTH_SHORT).show();

                    fillDate();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(tag,"requestCode == 1 && resultCode == Activity.RESULT_CANCELED");
                //AbLogUtil.i(mContext,"????:"+resultCode);
                Toast.makeText(this, "User canceled,resultCode = "+resultCode, Toast.LENGTH_SHORT).show();

                fillDate();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}