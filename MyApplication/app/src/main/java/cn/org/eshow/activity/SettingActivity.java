package cn.org.eshow.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.org.eshow.R;
import cn.org.eshow.common.CommonActivity;
import cn.org.eshow.guide.ParallaxFragment;
import cn.org.eshow_structure.util.ESViewUtil;

/**
 * 设置页面
 * Created by daikting on 16/3/1.
 */
@EActivity(R.layout.activity_setting)
public class SettingActivity extends CommonActivity {
    private Context mContext = SettingActivity.this;
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;

    @ViewById(R.id.rlFeedback)
    RelativeLayout mRlFeedback;
    @ViewById(R.id.rlQuestion)
    RelativeLayout mRlQuestion;
    @ViewById(R.id.rlAbout)
    RelativeLayout mRlAbout;
    @ViewById(R.id.rlWelcome)
    RelativeLayout mRlWelcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        ESViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));

        mTvTitle.setText(getTitle());
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);
    }

    @Click(R.id.rlBack)
    void onBack(){
        finish();
    }

    @Click(R.id.rlFeedback)
    void onFeedBack(){
        String url = "http://api.eshow.org.cn/info/feedback";
        Intent intent = new Intent(mContext,WebActivity.class);
        intent.putExtra(WebActivity.INTENT_TAG_URL, url);
        intent.putExtra(WebActivity.INTENT_TAG_TITLE,"意见反馈");
        startActivity(intent);
    }

    @Click(R.id.rlQuestion)
    void onQuestion(){
        String url = "http://api.eshow.org.cn/info/question";
        Intent intent = new Intent(mContext,WebActivity.class);
        intent.putExtra(WebActivity.INTENT_TAG_URL, url);
        intent.putExtra(WebActivity.INTENT_TAG_TITLE,"常见问题");
        startActivity(intent);
    }

    @Click(R.id.rlAbout)
    void onAbout(){
        String url = "http://api.eshow.org.cn/info/about";
        Intent intent = new Intent(mContext,WebActivity.class);
        intent.putExtra(WebActivity.INTENT_TAG_URL,url);
        intent.putExtra(WebActivity.INTENT_TAG_TITLE,"关于我们");
        startActivity(intent);
    }

    @Click(R.id.rlWelcome)
    void onWelcome(){
        Intent intent = new Intent(mContext, GuideActivity.class);
        intent.putExtra(ParallaxFragment.INTENT_ISSHOWBUTTON,false);
        startActivity(intent);
        overridePendingTransition(R.anim.scroll_in_re, R.anim.scroll_out_re);
    }
}
