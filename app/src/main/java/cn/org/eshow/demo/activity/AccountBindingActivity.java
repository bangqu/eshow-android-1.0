package cn.org.eshow.demo.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bangqu.base.activity.BaseActivity;
import com.bangqu.utils.Contact;
import com.longtu.base.util.StringUtils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.bean.WxUserInfoBean;

/**
 * 账号绑定
 */
public class AccountBindingActivity extends BaseActivity {

    private TextView tvSource;
    private TextView tvName;
    private ImageView ivEshow;
    private ImageView ivAvatar;
    private Button btnNew;
    private Button btnOld;
    private String access_token;
    private String username;
    private String platform;
    private String nickname;
    private String photo;

    private Message msg;
    private WxUserInfoBean wxUserInfoBean;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    wxUserInfoBean= JSONObject.parseObject(msg.obj.toString(),WxUserInfoBean.class);
                    if (wxUserInfoBean!=null){
                        setUserInfo();
                    }
                    break;
            }
        }
    };

    private void setUserInfo() {
        if (!StringUtils.isEmpty(wxUserInfoBean.getHeadimgurl())){
            photo=wxUserInfoBean.getHeadimgurl();
            ImageLoader.getInstance().displayImage(wxUserInfoBean.getHeadimgurl(),ivAvatar);
        }

        if (!StringUtils.isEmpty(wxUserInfoBean.getNickname())){
            nickname=wxUserInfoBean.getNickname();
            tvName.setText(wxUserInfoBean.getNickname());
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_account_binding);
    }

    @Override
    public void initViews() {
        tvSource = (TextView) findViewById(R.id.tvSource);
        ivEshow = (ImageView) findViewById(R.id.ivEshow);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        btnNew = (Button) findViewById(R.id.btnNew);
        btnOld = (Button) findViewById(R.id.btnOld);
        tvName=(TextView)findViewById(R.id.tvName);
    }

    @Override
    public void initDatas() {
        setTitle("绑定账号");
        access_token=getIntent().getStringExtra("access_token");
        username=getIntent().getStringExtra("username");
        platform=getIntent().getStringExtra("platform");
        nickname=getIntent().getStringExtra("nickname");
        photo=getIntent().getStringExtra("photo");

        if (platform.equals("weixin")) {
            params = new RequestParams();
            params.put("access_token", access_token);
            params.put("openid", username);
            getWx("userinfo", params);
        }else {
            if (!StringUtils.isEmpty(photo)){
                ImageLoader.getInstance().displayImage(photo,ivAvatar);
            }

            if (!StringUtils.isEmpty(nickname)){
                tvName.setText(nickname);
            }
        }
    }

    @Override
    public void setDatas() {

    }

    @Override
    public void setListener() {
        btnOld.setOnClickListener(this);
        btnNew.setOnClickListener(this);
    }

    @Override
    public void ResumeDatas() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNew:
                intent=new Intent(this,RegisterActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("nickname",nickname);
                intent.putExtra("photo",photo);
                intent.putExtra("platform",platform);
                Jump(intent);
                break;
            case R.id.btnOld:
                intent=new Intent(this,CodeLoginActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("nickname",nickname);
                intent.putExtra("photo",photo);
                intent.putExtra("platform",platform);
                Jump(intent);
                break;
        }
    }

    @Override
    public void OnReceive(String requestname, String response) {
        msg=new Message();
        if (requestname.equals("userinfo")){
            msg.what=1;
        }
        msg.obj=response;
        handler.sendMessage(msg);
    }
}
