package cn.org.eshow.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;

import cn.org.eshow.R;
import cn.org.eshow.common.CommonActivity;
import cn.org.eshow_structure.util.ESLogUtil;
import cn.org.eshow_structure.util.ESStrUtil;
import cn.org.eshow_structure.util.ESViewUtil;

/**
 * 呈现网页的界面
 * Created by daikting on 15/9/29.
 */
public class WebActivity extends CommonActivity implements View.OnClickListener {
    /*
    * intent传递url参数到到此页面
    * */
    public static final String INTENT_TAG_URL = "url";
    /*
    * intent传递标题到此页面
    * */
    public static final String INTENT_TAG_TITLE = "title";

    private RelativeLayout rlBack;
    private MaterialMenuView mMaterialBackButton;
    private TextView tvTitle;

    private WebView webView;
    private String webUrl = "";
    private String title = "";

    private Context mContext = WebActivity.this;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ESViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        webUrl = getIntent().getStringExtra(INTENT_TAG_URL);
        ESLogUtil.d("WebActivity", webUrl);
        title = getIntent().getStringExtra(INTENT_TAG_TITLE);

        findView();
        fillDate();

    }

    private void findView() {

        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        rlBack.setOnClickListener(this);
        mMaterialBackButton = (MaterialMenuView) findViewById(R.id.material_back_button);
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        if(ESStrUtil.isEmpty(title)){
            tvTitle.setText(getTitle());
        }else{
            tvTitle.setText(title);
        }

        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void fillDate() {
        webView.getSettings().setJavaScriptEnabled(true);// 开启jacascript
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// 支持通过JS打开新窗口
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadsImagesAutomatically(true);// 支持自动加载图片


        webView.requestFocusFromTouch();
        webView.getSettings().setBuiltInZoomControls(true);// 设置支持缩放
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);// 屏幕自适应网页，如果没有这个在低分辨率手机上显示会异常
        webView.loadUrl(webUrl);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        progressBar.setVisibility(View.VISIBLE);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            tvTitle.setText(title);
        }
    }

    // 内部类
    public class MyWebViewClient extends WebViewClient {

        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象。

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {// 拦截无资源的请求

            view.loadUrl(url);
            return false;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
        }

        public void onPageFinished(WebView view, String url) {
            progressBar.setProgress(100);
            progressBar.setVisibility(View.INVISIBLE);
        }

        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            progressBar.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
}
