package com.bangqu.eshow.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.common.CommonActivity;
import com.bangqu.eshow.demo.view.ShareDialog;
import com.bangqu.eshow.image.ESImageLoader;
import com.bangqu.eshow.util.ESViewUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 分享功能页面
 * Created by daikting on 16/4/5.
 */
@EActivity(R.layout.activity_share)
public class ShareActivity extends CommonActivity{
    private Context mContext = ShareActivity.this;
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;

    @ViewById(R.id.ivCode)
    ImageView ivCode;

    @ViewById(R.id.ivShare)
    ImageView ivShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        ESViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        mTvTitle.setText(getTitle());
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);

        ivShare.setVisibility(View.VISIBLE);
        ESImageLoader.getInstance(mContext).display(ivCode, "http://qr.topscan.com/api.php?text=");
    }


    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }

    @Click(R.id.btnShare)
    void onShare(){
        new ShareDialog(mContext).show();
    }

    @Click(R.id.ivShare)
    void onShareRight(){
        new ShareDialog(mContext).show();
    }
}
