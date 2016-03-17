package com.bangqu.eshow.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.common.CommonActivity;
import com.bangqu.eshow.demo.guide.ParallaxFragment;
import com.bangqu.eshow.util.ESViewUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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

    }

    @Click(R.id.rlQuestion)
    void onQuestion(){

    }

    @Click(R.id.rlAbout)
    void onAbout(){

    }

    @Click(R.id.rlWelcome)
    void onWelcome(){
        Intent intent = new Intent(mContext, GuideActivity.class);
        intent.putExtra(ParallaxFragment.INTENT_ISSHOWBUTTON,false);
        startActivity(intent);
        overridePendingTransition(R.anim.scroll_in, R.anim.scroll_out);
    }
}
