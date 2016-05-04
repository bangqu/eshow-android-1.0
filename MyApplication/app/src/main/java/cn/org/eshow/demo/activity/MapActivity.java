package cn.org.eshow.demo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import cn.org.eshow.demo.R;
import cn.org.eshow.demo.bean.AMapLocationBean;
import cn.org.eshow.demo.common.CommonActivity;
import cn.org.eshow.demo.common.Global;
import cn.org.eshow.demo.view.LoginAutoCompleteEdit;
import cn.org.eshow.util.ESLogUtil;
import cn.org.eshow.util.ESViewUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 地图功能页面
 * Created by daikting on 16/4/1.
 */
@EActivity(R.layout.activity_map)
public class MapActivity extends CommonActivity {
    private Context mContext = MapActivity.this;
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;

    @ViewById
    LinearLayout llParent;
    @ViewById
    TextView tvAddress;
    @ViewById
    LoginAutoCompleteEdit etSubAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    void init() {
        ESViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        mTvTitle.setText(getTitle());
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);
        registerBroadcast();
    }


    @Click(R.id.tvAddress)
    void onEtAddress(){
        ChooseLocationActivity_.intent(mContext).startForResult(11);

    }

    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }

    /**
     * 注册用于更新界面的广播
     */
    private void registerBroadcast(){
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Global.EShow_Broadcast_Action.ACTION_LOCATION_GOT);
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String action = intent.getAction();
                ESLogUtil.d(mContext, "broadcast action:" + action);

                if(action.equals(Global.EShow_Broadcast_Action.ACTION_LOCATION_GOT)){//要求刷新列表
                    ESLogUtil.d(mContext, "broadcast");
                    AMapLocationBean aMapLocationBean = (AMapLocationBean) intent.getSerializableExtra("location");
                    String address = aMapLocationBean.getName();
                    String subAddress = aMapLocationBean.getAddress();
                    tvAddress.setText(address);
                    etSubAddress.setText(subAddress);
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
    }
}