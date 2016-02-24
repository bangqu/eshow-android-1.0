package com.bangqu.eshow.demo.activity;


import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.common.SharedPrefUtil;
import com.bangqu.eshow.demo.fragment.NaviFragment;
import com.bangqu.eshow.util.ESLogUtil;
import com.bangqu.eshow.util.ESToastUtil;
import com.bangqu.eshow.view.slidingmenu.DrawerArrowDrawable;
import com.bangqu.eshow.view.slidingmenu.SlidingMenu;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends CommonActivity {
    @ViewById(R.id.rlMenu)
    RelativeLayout mRlMenu;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;
    @ViewById(R.id.llMessage)
    LinearLayout mLlMessage;
    @ViewById(R.id.ivSearch)
    ImageView mIvSearch;
    @ViewById(R.id.ivAdd)
    ImageView mIvAdd;

    private Context mContext = MainActivity.this;
    private DrawerArrowDrawable drawerArrowDrawable;
    private SlidingMenu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefUtil.setSecondIn(mContext);
    }

    @AfterViews
    void init(){
        initSliding();

    }


    @Click(R.id.rlMenu)
    protected void onMenuClick(){
        ESLogUtil.d(mContext, "llmenu onclick");
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            menu.showMenu();

        }

    }

    /**
     * 初始化滑动分页
     */
    private void initSliding() {
        //SlidingMenu的配置
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);

        //slidingmenu的事件模式，如果里面有可以滑动的请用TOUCHMODE_MARGIN
        //可解决事件冲突问题
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //menu视图的Fragment添加
        menu.setMenu(R.layout.fragment_menu);
        NaviFragment naviFragment = new NaviFragment();
        naviFragment.setNaviCallbacks(new NaviFragment.NaviCallbacks() {
            @Override
            public void onNaviItemSelected(int position) {
                switch (position){
                    case 0:
                        ESToastUtil.showToast(mContext,"case 0");
                        break;
                    case 1:
                        ESToastUtil.showToast(mContext,"case 1");

                        break;
                    case 2:
                        ESToastUtil.showToast(mContext,"case 2");

                        break;
                }
                if (menu.isMenuShowing()) {
                    menu.showContent();
                } else {
                    menu.showMenu();

                }
            }
        });
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, naviFragment)
                .commit();
    }
}
