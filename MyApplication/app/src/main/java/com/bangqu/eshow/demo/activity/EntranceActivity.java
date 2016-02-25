package com.bangqu.eshow.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.common.CommonActivity;
import com.bangqu.eshow.demo.common.SharedPrefUtil;
import com.bangqu.eshow.demo.common.WeakRefHandler;
import com.bangqu.eshow.util.ESLogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 启动页面
 * Created by daikting on 16/2/19.
 */
@EActivity(R.layout.activity_entrance)
public class EntranceActivity extends CommonActivity implements Handler.Callback {
    private Context mContext = EntranceActivity.this;
    private static final int HANDLER_MESSAGE_ANIMATION = 0;
    private static final int HANDLER_MESSAGE_NEXT_ACTIVITY = 1;
    @ViewById
    ImageView image;
    @ViewById
    TextView title;
    @ViewById
    View foreMask;
    @ViewById
    View logo;
    WeakRefHandler mWeakRefHandler;
    Animation entrance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    void init() {
        ESLogUtil.d(mContext, "init");
        mWeakRefHandler = new WeakRefHandler(this);
        entrance = AnimationUtils.loadAnimation(mContext, R.anim.entrance);
        entrance.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mWeakRefHandler.start(HANDLER_MESSAGE_NEXT_ACTIVITY, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mWeakRefHandler.start(HANDLER_MESSAGE_ANIMATION, 900);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == HANDLER_MESSAGE_ANIMATION) {
            foreMask.startAnimation(entrance);
        } else if (msg.what == HANDLER_MESSAGE_NEXT_ACTIVITY) {
            if (SharedPrefUtil.isFirstIn(mContext)) {
                Intent intent = new Intent(mContext, GuideActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.scroll_in, R.anim.scroll_out);
                finish();
            } else {
                Intent intent = new Intent(mContext, MainActivity_.class);
                startActivity(intent);
                overridePendingTransition(R.anim.scroll_in, R.anim.scroll_out);
                finish();
            }

        }
        return true;
    }
}
