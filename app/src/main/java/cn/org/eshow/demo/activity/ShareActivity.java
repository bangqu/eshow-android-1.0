package cn.org.eshow.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.common.CommonActivity;
import cn.org.eshow.demo.view.ShareDialog;
import cn.org.eshow_framwork.image.AbImageLoader;
import cn.org.eshow_framwork.util.AbLogUtil;
import cn.org.eshow_framwork.util.AbToastUtil;
import cn.org.eshow_framwork.util.AbViewUtil;

/**
 * 分享功能页面
 * Created by daikting on 16/4/5.
 */
@EActivity(R.layout.activity_share)
public class ShareActivity extends CommonActivity {
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
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        mTvTitle.setText(getTitle());
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);

        ivShare.setVisibility(View.VISIBLE);
        AbImageLoader.getInstance(mContext).display(ivCode, "http://qr.topscan.com/api.php?text=");
    }


    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }

    @Click(R.id.btnShare)
    void onShare(){

        new ShareDialog(mContext,umShareListener).show();
    }
    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            AbLogUtil.i(mContext, "分享 结果");
            AbToastUtil.showToast(mContext, share_media + " 分享 成功 啦");
        }
        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            AbLogUtil.i(mContext, "分享 错误");
            AbToastUtil.showToast(mContext, share_media + " 分享 错误 啦");
        }
        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            AbLogUtil.i(mContext,"分享 取消");
            AbToastUtil.showToast(mContext, share_media + " 分享 取消 啦");
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AbLogUtil.i(mContext, "onActivityResult方法");
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
            AbLogUtil.i(mContext, "requestCode is " + requestCode + ",,resultCode is " + resultCode + ",,data is " + data.getDataString() + "--" + data.getAction());
        }else {
            AbLogUtil.i(mContext, "requestCode is " + requestCode + ",,resultCode is " + resultCode );
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Click(R.id.ivShare)
    void onShareRight(){
        new ShareDialog(mContext).show();
    }
}
