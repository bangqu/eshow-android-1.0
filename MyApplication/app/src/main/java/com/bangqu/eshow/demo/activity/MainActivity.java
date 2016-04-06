package com.bangqu.eshow.demo.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.bean.Enum_CodeType;
import com.bangqu.eshow.demo.common.CommonActivity;
import com.bangqu.eshow.demo.common.Global;
import com.bangqu.eshow.demo.common.SharedPrefUtil;
import com.bangqu.eshow.demo.fragment.MainFragment;
import com.bangqu.eshow.demo.fragment.NaviFragment;
import com.bangqu.eshow.demo.view.AddPopupwindow;
import com.bangqu.eshow.demo.view.ConfirmDialog;
import com.bangqu.eshow.fragment.ESProgressDialogFragment;
import com.bangqu.eshow.global.ESActivityManager;
import com.bangqu.eshow.util.ESLogUtil;
import com.bangqu.eshow.util.ESToastUtil;
import com.bangqu.eshow.util.ESViewUtil;
import com.bangqu.eshow.view.slidingmenu.SlidingMenu;
import com.umeng.socialize.UMShareAPI;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends CommonActivity {
    @ViewById(R.id.material_menu_button)
    MaterialMenuView materialMenuView;
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
    private SlidingMenu menu;
    private AddPopupwindow addPopupwindow;

    private MainFragment mainFragment;
    ESProgressDialogFragment progressDialog;

    NaviFragment naviFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefUtil.setSecondIn(mContext);

        List<Activity> agoActivity = ESActivityManager.getInstance().getActivityList();
        for (int i = 0; i < agoActivity.size(); i++) {
            String activityName = agoActivity.get(i).getLocalClassName();
            if (!activityName.equals("activity.MainActivity_")) {
                ESLogUtil.d(mContext, "结束：" + activityName);
                agoActivity.get(i).finish();
            }
        }

        registerBroadcast();
    }

    @AfterViews
    void init() {
        ESViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        addPopupwindow = new AddPopupwindow(this);
        initSliding();
        mainFragment = new MainFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mainFragment)
                .commit();
    }


    @Click(R.id.rlMenu)
    protected void onMenuClick() {
        ESLogUtil.d(mContext, "llmenu onclick");
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            menu.showMenu();
        }

    }

    @Click(R.id.ivSearch)
    protected void onSearch() {

    }

    @Click(R.id.ivAdd)
    protected void onAddClick() {
        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.pop_add_show_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        operatingAnim.setFillAfter(true);
        mIvAdd.startAnimation(operatingAnim);
        addPopupwindow.show(mContext, mIvAdd);

        addPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.pop_add_dismiss_rotate);
                LinearInterpolator lin = new LinearInterpolator();
                operatingAnim.setInterpolator(lin);
                operatingAnim.setFillAfter(true);
                mIvAdd.startAnimation(operatingAnim);
            }
        });
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
        menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                materialMenuView.animateState(MaterialMenuDrawable.IconState.ARROW);

            }
        });

        menu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                materialMenuView.animateState(MaterialMenuDrawable.IconState.BURGER);
                //关闭侧边栏的时候通知MainFragment
                if (mainFragment != null) {
                    mainFragment.onSlindingClose((int) getResources().getDimension(R.dimen.slidingmenu_offset));
                }
            }
        });
        naviFragment = new NaviFragment();
        naviFragment.setNaviCallbacks(new NaviFragment.NaviCallbacks() {
            @Override
            public void onNaviItemSelected(int position) {
                switch (position) {
                    case 0:
                        SettingActivity_.intent(mContext).start();
                        overridePendingTransition(R.anim.downtoup_in, R.anim.downtoup_out);
                        break;
                    case 1:
                        if(!SharedPrefUtil.isLogin(mContext)) {
                            ESToastUtil.showToast(mContext, "您还未登录！");
                            LoginActivity_.intent(mContext).start();
                            return;
                        }
                        ConfirmDialog.OnCustomDialogListener onCustomDialogListener = new ConfirmDialog.OnCustomDialogListener() {
                            @Override
                            public void OnCustomDialogConfim(String str) {
                                String userName = SharedPrefUtil.getUser(mContext).getUsername();
                                InputTelActivity_.intent(mContext).extra(InputTelActivity_.INTENT_ISREGISTER, Enum_CodeType.REPASSWORD).extra(InputTelActivity_.INTENT_TEL,userName).start();
                                overridePendingTransition(R.anim.downtoup_in, R.anim.downtoup_out);
                            }

                            @Override
                            public void OnCustomDialogCancel(String str) {

                            }
                        };
                        ConfirmDialog confirmDialog = new ConfirmDialog(mContext,"提示","确定进行重置密码操作？","确定","取消",onCustomDialogListener);
                        confirmDialog.show();

                        break;
                    case 2:
                        Intent intent = new Intent(Global.EShow_Broadcast_Action.ACTION_EXIT);
                        sendBroadcast(intent);
                        break;
                }
            }
        });
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, naviFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private long exitTime = 0;

    private void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ESToastUtil.showToast(mContext, "再按一次退出EShowAndroid");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult( requestCode, resultCode, data);
    }

    /**
     * 注册用于更新界面的广播
     */
    private void registerBroadcast() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Global.EShow_Broadcast_Action.ACTION_LOGIN_SUCESS);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                ESLogUtil.d(mContext, "++++++++++broadcast action:" + action);
                if (action.equals(Global.EShow_Broadcast_Action.ACTION_LOGIN_SUCESS)) {
                    if(naviFragment != null){
                        naviFragment.updateUserInfo();
                    }
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);
    }
}
