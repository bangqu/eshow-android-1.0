package com.bangqu.eshow.demo.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.bean.Enum_ThirdType;
import com.bangqu.eshow.demo.common.Global;
import com.bangqu.eshow.demo.fragment.AccountFragment;
import com.bangqu.eshow.demo.fragment.BaseInfoFragment;
import com.bangqu.eshow.demo.fragment.PersonFragment;
import com.bangqu.eshow.demo.network.ESResponseListener;
import com.bangqu.eshow.demo.network.NetworkInterface;
import com.bangqu.eshow.fragment.ESProgressDialogFragment;
import com.bangqu.eshow.util.ESDialogUtil;
import com.bangqu.eshow.util.ESLogUtil;
import com.bangqu.eshow.util.ESToastUtil;
import com.bangqu.eshow.util.ESViewUtil;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.Map;

/**
 * 信息表单页面
 * Created by daikting on 16/3/30.
 */
@EActivity(R.layout.activity_infoform)
public class InfoFormActivity extends FragmentActivity {
    private Context mContext = InfoFormActivity.this;
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;

    LinearLayout llBaseInfo;
    @ViewById
    ImageView ivBaseInfo;
    @ViewById
    TextView tvBaseInfo;
    @ViewById
    LinearLayout llPerson;
    @ViewById
    ImageView ivPerson;
    @ViewById
    TextView tvPerson;
    @ViewById
    LinearLayout llAccount;
    @ViewById
    ImageView ivAccount;
    @ViewById
    TextView tvAccount;
    @ViewById
    FrameLayout container;
    int tab_title_color_choose;
    int tab_title_color_normal;
    Drawable ic_baseInfo_normal;
    Drawable ic_baseInfo_choose;
    Drawable ic_person_normal;
    Drawable ic_person_choose;
    Drawable ic_account_normal;
    Drawable ic_account_choose;

    BaseInfoFragment baseInfoFragment;
    PersonFragment personFragment;
    AccountFragment accountFragment;
    int currentTabIndex = 0;
    //友盟分享
    UMShareAPI umShareAPI;
    ESProgressDialogFragment progressDialog;

    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        ESViewUtil.scaleContentView((RelativeLayout) findViewById(R.id.rlParent));
        mTvTitle.setText(getTitle());
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);
        mMaterialBackButton.setVisibility(View.VISIBLE);

        tab_title_color_choose = getResources().getColor(R.color.colorAccent);
        tab_title_color_normal = getResources().getColor(R.color.text_gray);

        ic_baseInfo_normal = getResources().getDrawable(R.drawable.ic_baseinfo_normal);
        ic_baseInfo_choose = getResources().getDrawable(R.drawable.ic_baseinfo_pressed);

        ic_person_normal = getResources().getDrawable(R.drawable.ic_person_normal);
        ic_person_choose = getResources().getDrawable(R.drawable.ic_person_pressed);

        ic_account_normal = getResources().getDrawable(R.drawable.ic_account_normal);
        ic_account_choose = getResources().getDrawable(R.drawable.ic_account_pressed);

        baseInfoFragment = new BaseInfoFragment();
        personFragment = new PersonFragment();
        accountFragment = new AccountFragment();
        chooseBaseInfo();
        registerBroadcast();
    }

    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }

    @Click(R.id.llBaseInfo)
    void onBaseInfo() {
        if (checkClickState()) {
            chooseBaseInfo();

        }
    }

    @Click(R.id.llPerson)
    void onPerson() {
        if (checkClickState()) {
            choosePerson();
        }

    }

    @Click(R.id.llAccount)
    void onAccount() {
        if (checkClickState()) {
            chooseAccount();

        }
    }

    private void chooseBaseInfo() {
        int lastIndex = currentTabIndex;
        currentTabIndex = 0;
        ivBaseInfo.setImageDrawable(ic_baseInfo_choose);
        tvBaseInfo.setTextColor(tab_title_color_choose);

        ivPerson.setImageDrawable(ic_person_normal);
        tvPerson.setTextColor(tab_title_color_normal);

        tvAccount.setTextColor(tab_title_color_normal);
        ivAccount.setImageDrawable(ic_account_normal);
        lastClickTime = System.currentTimeMillis();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (lastIndex != 0) {
            fragmentTransaction.setCustomAnimations(R.anim.fragment_slid_left_in, R.anim.fragment_slid_right_out);
        }
        fragmentTransaction.replace(R.id.container, baseInfoFragment);
        fragmentTransaction.commit();
    }

    private void choosePerson() {
        int lastIndex = currentTabIndex;
        currentTabIndex = 1;

        ivBaseInfo.setImageDrawable(ic_baseInfo_normal);
        tvBaseInfo.setTextColor(tab_title_color_normal);

        ivPerson.setImageDrawable(ic_person_choose);
        tvPerson.setTextColor(tab_title_color_choose);

        tvAccount.setTextColor(tab_title_color_normal);
        ivAccount.setImageDrawable(ic_account_normal);
        lastClickTime = System.currentTimeMillis();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (lastIndex == 0) {
            fragmentTransaction.setCustomAnimations(R.anim.fragment_slid_right_in, R.anim.fragment_slid_left_out);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.fragment_slid_left_in, R.anim.fragment_slid_right_out);

        }

        fragmentTransaction.replace(R.id.container, personFragment);
        fragmentTransaction.commit();

    }

    private void chooseAccount() {
        int lastIndex = currentTabIndex;
        currentTabIndex = 2;

        ivBaseInfo.setImageDrawable(ic_baseInfo_normal);
        tvBaseInfo.setTextColor(tab_title_color_normal);

        ivPerson.setImageDrawable(ic_person_normal);
        tvPerson.setTextColor(tab_title_color_normal);

        tvAccount.setTextColor(tab_title_color_choose);
        ivAccount.setImageDrawable(ic_account_choose);
        lastClickTime = System.currentTimeMillis();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_slid_right_in, R.anim.fragment_slid_left_out);
        fragmentTransaction.replace(R.id.container, accountFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //这里应该可以定义一个枚举来处理多种返回情况，目前只做了昵称的修改，看看效果吧
        if (resultCode == 0x22) {
            ESLogUtil.d(mContext, "onActivityResult+++++++");
//            data.setAction(Global.EShow_Broadcast_Action.ACTION_USERBASEINFO_CHANGED);
//            sendBroadcast(data);
            String nickName = data.getStringExtra(ModifyStringValueActivity_.INTENT_PARAMVALUE_TAG);
            baseInfoFragment.setNickName(nickName);
        } else {
            if (umShareAPI != null) {
                umShareAPI.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * 注册用于更新界面的广播
     */
    private void registerBroadcast() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Global.EShow_Broadcast_Action.ACTION_QQ_AUTHORIZE);
        intentFilter.addAction(Global.EShow_Broadcast_Action.ACTION_WECHAT_AUTHORIZE);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                ESLogUtil.d(mContext, "++++++++++broadcast action:" + action);
                if (action.equals(Global.EShow_Broadcast_Action.ACTION_QQ_AUTHORIZE)) {
                    Config.dialog = ProgressDialog.show(mContext, "提示", "正在请求跳转....");
                    SHARE_MEDIA platform = SHARE_MEDIA.QQ;
                    umShareAPI = UMShareAPI.get(mContext);
                    umShareAPI.doOauthVerify(InfoFormActivity.this, platform, umAuthListener);
                } else if (action.equals(Global.EShow_Broadcast_Action.ACTION_WECHAT_AUTHORIZE)) {
                    Config.dialog = ProgressDialog.show(mContext, "提示", "正在请求跳转....");
                    SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
                    umShareAPI = UMShareAPI.get(mContext);
                    umShareAPI.doOauthVerify(InfoFormActivity.this, platform, umAuthListener);
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    /**
     * 授权回调监听
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String thirdToken = data.get("access_token");
            //截取前16位
            thirdToken = thirdToken.substring(0, 16);
            Enum_ThirdType thirdType = Enum_ThirdType.QQ;

            ESLogUtil.d(mContext, "thirdToken:" + thirdToken);
            if (platform.name().equals(SHARE_MEDIA.WEIXIN.toString())) {
                thirdType = Enum_ThirdType.WeChat;
            } else if (platform.name().equals(SHARE_MEDIA.QQ.toString())) {
                thirdType = Enum_ThirdType.QQ;
            }
            final Enum_ThirdType tempThirdType = thirdType;
            NetworkInterface.thirdBound(mContext, tempThirdType, thirdToken, new ESResponseListener(mContext) {
                @Override
                public void onBQSucess(String esMsg, JSONObject resultJson) {
                    switch (tempThirdType) {
                        case WeChat:
                            accountFragment.setWechatStateUI(true);
                            break;
                        case QQ:
                            accountFragment.setQQStateUI(true);
                            break;
                    }
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
                    progressDialog = ESDialogUtil.showProgressDialog(mContext, Global.LOADING_PROGRESSBAR_ID, "绑定第三方账号中...");
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
            });
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ESToastUtil.showToast(mContext, platform.name() + " Authorize fail");

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ESToastUtil.showToast(mContext, platform.name() + " Authorize cancel");
        }
    };

    /**
     * 检测是否可以点击，在进行Fragment之间的切换动画的时候，如果过于频繁的点击会导致程序奔溃
     */
    private boolean checkClickState() {
        long curTime = System.currentTimeMillis();
        long time = curTime - lastClickTime;
        if (time > 500) {
            return true;
        } else {
            return false;
        }
    }
}
