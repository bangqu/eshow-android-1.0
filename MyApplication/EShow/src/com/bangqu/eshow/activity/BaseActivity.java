package com.bangqu.eshow.activity;

import com.bangqu.eshow.global.ESActivityManager;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {

    /**
     * LayoutInflater.
     */
    public LayoutInflater mInflater;

    /**
     * Application.
     */
    public Application mApplication = null;

    /**
     * 主题ID.
     */
    public int mThemeId = -1;

    protected View mTopBar;//topbar
    protected ImageView mBtnLeft;//topbar左侧按钮
    protected TextView mBtnLeftText;//topbar左侧按钮文字
    protected ImageView mBtnRight;//topbar右侧按钮
    protected TextView mTitleView;//topbar标题
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mInflater = LayoutInflater.from(this);
        mApplication = this.getApplication();
        if (mThemeId != -1) {
            this.setTheme(mThemeId);
        } else {
            if (savedInstanceState != null) {
                if (savedInstanceState.getInt("theme", -1) != -1) {
                    mThemeId = savedInstanceState.getInt("theme");
                    this.setTheme(mThemeId);
                }
            }
        }
        ESActivityManager.getInstance().addActivity(this);
    }

    /**
     * 初始化主题ID
     *
     * @param themeId
     */
    public void initAppTheme(int themeId) {
        this.mThemeId = themeId;
    }

    /**
     * 设置主题ID
     *
     * @param themeId
     */
    public void setAppTheme(int themeId) {
        this.mThemeId = themeId;
        this.recreate();
    }

    /**
     * 保存主题ID，onCreate 时读取主题.
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", this.mThemeId);
    }

    /**
     * 返回默认
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * finish.
     *
     * @see android.app.Activity#finish()
     */
    @Override
    public void finish() {
        ESActivityManager.getInstance().removeActivity(this);
        super.finish();
    }

    /**
     * 使用Toast short 方式显示信息
     *
     * @param content
     */
    public void showShortToast(Context mContext, String content) {
        if (null != mContext && null != content && content.length() > 1) {
            Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 使用Toast long 方式显示信息
     *
     * @param content
     */
    public void showLongToast(Context mContext, String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_LONG).show();
    }

    /**
     * 设置topbar
     *
     * @param idL
     * @param title
     * @param idR
     */
    protected void initTopBar(int idL, boolean hasLeft, String tl, String title, int idR, boolean hasRight) {
        if (null == tl) {
            tl = "返回";
        }
        mBtnLeftText.setText(tl);
        if (hasLeft) {
            mBtnLeft.setImageResource(idL);
            mBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBtnLeftClickstener();
                }
            });
        }
        if (hasRight) {
            mBtnRight.setImageResource(idR);
            mBtnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBtnRightClickstener();
                }
            });
        }
        if (null != title && title.length() > 0) {
            mTitleView.setText(title);
        }
    }

    /**
     * 单击左侧按钮
     */
    protected void onBtnLeftClickstener() {
        finish();
    }

    /**
     * 单击右侧按钮
     */
    protected void onBtnRightClickstener() {
        finish();
    }
}
