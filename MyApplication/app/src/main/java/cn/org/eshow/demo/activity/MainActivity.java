package cn.org.eshow.demo.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.socialize.UMShareAPI;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.bean.Enum_CodeType;
import cn.org.eshow.demo.common.CommonActivity;
import cn.org.eshow.demo.common.Global;
import cn.org.eshow.demo.common.SharedPrefUtil;
import cn.org.eshow.demo.fragment.MainFragment;
import cn.org.eshow.demo.fragment.NaviFragment;
import cn.org.eshow.demo.view.AddPopupwindow;
import cn.org.eshow.demo.view.ConfirmDialog;
import cn.org.eshow.fragment.ESProgressDialogFragment;
import cn.org.eshow.global.ESActivityManager;
import cn.org.eshow.util.ESAppUtil;
import cn.org.eshow.util.ESLogUtil;
import cn.org.eshow.util.ESToastUtil;
import cn.org.eshow.util.ESViewUtil;
import cn.org.eshow.view.slidingmenu.SlidingMenu;

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
    private static final String tag="TPush_MainActivity";

    private SlidingMenu menu;
    private AddPopupwindow addPopupwindow;

    private MainFragment mainFragment;
    ESProgressDialogFragment progressDialog;

    NaviFragment naviFragment;
    Message m = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(tag,"onCreate");
        super.onCreate(savedInstanceState);
        // 1.获取设备Token
        Handler handler = new HandlerExtension(MainActivity.this);
        m = handler.obtainMessage();
        // 注册接口
        XGPushManager.registerPush(getApplicationContext(), "18749490020",
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Log.i(tag, "+++ register push sucess. token:" + data + ",flag = " + flag);
                        m.obj = "+++ register push sucess. token:" + data;
                        m.sendToTarget();
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.w(tag, "+++ register push fail. token:" + data + ", errCode:" + errCode + ",msg:" + msg);
                        m.obj = "+++ register push fail. token:" + data + ", errCode:" + errCode + ",msg:" + msg;
                        m.sendToTarget();
                    }
                }
        );

        SharedPrefUtil.setSecondIn(mContext);

        List<Activity> agoActivity = ESActivityManager.getInstance().getActivityList();
        for (int i = 0; i < agoActivity.size(); i++) {
            String activityName = agoActivity.get(i).getLocalClassName();
            if (!activityName.equals("activity.MainActivity_")) {
                Log.i(tag, "结束：" + activityName);
                agoActivity.get(i).finish();
            }
        }

        registerBroadcast();
    }
    private static class HandlerExtension extends Handler {
        WeakReference<MainActivity> mActivity;

        HandlerExtension(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity theActivity = mActivity.get();
            if (theActivity == null) {
                theActivity = new MainActivity();
            }
            if (msg != null) {
                Log.w(tag, "信鸽注册接口返回结果："+msg.obj.toString());
                Log.w(tag,"XGPushConfig.getToken(theActivity) is = "+ XGPushConfig.getToken(theActivity));
            }
            // XGPushManager.registerCustomNotification(theActivity,
            // "BACKSTREET", "BOYS", System.currentTimeMillis() + 5000, 0);
        }
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
        Log.i(tag, "llmenu onclick");
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

        DisplayMetrics displayMetrics =  ESAppUtil.getDisplayMetrics(mContext);
        int width = displayMetrics.widthPixels;
        int slidingMenuOffset = width/5;
        int shadowWidth = slidingMenuOffset/3;


        //SlidingMenu的配置
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);

        //slidingmenu的事件模式，如果里面有可以滑动的请用TOUCHMODE_MARGIN
        //可解决事件冲突问题
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        menu.setShadowWidth(shadowWidth);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffset(slidingMenuOffset);
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
        naviFragment.setNaviCallbacks(
                new NaviFragment.NaviCallbacks() {
                    @Override
                    public void onNaviItemSelected(int position) {
                        switch (position) {
                            case 0:
                                SettingActivity_.intent(mContext).start();
                                overridePendingTransition(R.anim.scroll_in_re, R.anim.scroll_out_re);
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
                                        overridePendingTransition(R.anim.scroll_in_re, R.anim.scroll_out_re);
                                    }

                                    @Override
                                    public void OnCustomDialogCancel(String str) {

                                    }
                                };
                                ConfirmDialog confirmDialog = new ConfirmDialog(mContext,"提示","确定进行重置密码操作？","确定","取消",onCustomDialogListener);
                                confirmDialog.show();

                                break;
                            case 2:
                                ESLogUtil.i(mContext,"发送退出广播");
                                Intent intent = new Intent(Global.EShow_Broadcast_Action.ACTION_EXIT);
                                sendBroadcast(intent);
                                break;
                        }
                    }
                }
        );
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
                Log.i(tag, "++++++++++broadcast action:" + action);
                if (action.equals(Global.EShow_Broadcast_Action.ACTION_LOGIN_SUCESS)) {
                    if(naviFragment != null){
                        naviFragment.updateUserInfo();
                    }
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        Log.i(tag,"onResume");
        super.onResume();
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        Log.i(tag, "onResumeXGPushClickedResult:" + click);
        if (click != null) { // 判断是否来自信鸽的打开方式
            Toast.makeText(this, "通知被点击:" + click.toString(), Toast.LENGTH_SHORT).show();
            Log.i(tag,"通知被点击:" + click.toString());
        }
    }

    @Override
    protected void onPause() {
        Log.i(tag,"onPause");
        super.onPause();
        XGPushManager.onActivityStoped(this);
    }
}
