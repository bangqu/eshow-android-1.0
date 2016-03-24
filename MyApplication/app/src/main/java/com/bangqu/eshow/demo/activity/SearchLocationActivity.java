package com.bangqu.eshow.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.adapter.PlaceAdapter;
import com.bangqu.eshow.demo.bean.AMapLocationBean;
import com.bangqu.eshow.demo.bean.AMapLocationListResultBean;
import com.bangqu.eshow.demo.common.CommonActivity;
import com.bangqu.eshow.demo.network.AMapResponseListener;
import com.bangqu.eshow.demo.network.NetworkInterface;
import com.bangqu.eshow.demo.view.LoginAutoCompleteEdit;
import com.bangqu.eshow.util.ESJsonUtil;
import com.bangqu.eshow.util.ESLogUtil;
import com.bangqu.eshow.util.ESToastUtil;
import com.bangqu.eshow.util.ESViewUtil;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jazzylistview.JazzyListView;

/**
 * 根据用户输入地址搜索周边
 * Created by daikting on 16/3/24.
 */
@EActivity(R.layout.activity_searchplace)
public class SearchLocationActivity extends CommonActivity {
    public static final String INTENT_CITYNAME = "intent.cityName";
    private Context mContext = SearchLocationActivity.this;
    @ViewById(R.id.rlBack)
    RelativeLayout rlBack;
    @ViewById(R.id.tvSubTitle)
    TextView tvSubTitle;
    @ViewById(R.id.material_back_button)
    MaterialMenuView material_back_button;
    @ViewById(R.id.etSearch)
    LoginAutoCompleteEdit etSearch;

    @ViewById(R.id.listview)
    JazzyListView listView;
    private List<AMapLocationBean> aroundList;
    private PlaceAdapter placeAdapter;
    @ViewById(R.id.tvNoData)
    TextView tvNoData;
    @ViewById(R.id.rlProgress)
    RelativeLayout rlProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        ESViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        material_back_button.setState(MaterialMenuDrawable.IconState.ARROW);
        tvSubTitle.setVisibility(View.VISIBLE);

        tvNoData.setVisibility(View.GONE);
        rlProgress.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        aroundList = new ArrayList<AMapLocationBean>();
        placeAdapter = new PlaceAdapter(mContext,aroundList);
        listView.setAdapter(placeAdapter);
    }

    @AfterTextChange(R.id.etSearch)
    void onSearchTextChange() {
        // Something Here
        String text = etSearch.getText().toString();
        NetworkInterface.searchPlace(mContext,text,"wuxi",searchPlaceListener);
    }

    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }

    @Click(R.id.tvSubTitle)
    void onSubTitile(){
        ChooseLocationActivity_.intent(mContext).start();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        finish();
    }

    /**
     * 请求高德地图搜索关键字接口
     */
    AMapResponseListener searchPlaceListener = new AMapResponseListener(mContext) {

        @Override
        public void onAMapSucess(JSONObject resultJson) {
            ESLogUtil.d(mContext, "resultJson:"+resultJson);
            AMapLocationListResultBean aMapLocationListResult = (AMapLocationListResultBean) ESJsonUtil.fromJson(resultJson.toString(), AMapLocationListResultBean.class);
            if(aMapLocationListResult != null){
                aroundList.clear();
                aroundList.addAll(aMapLocationListResult.getPois());
                placeAdapter.notifyDataSetChanged();
            }else{
                rlProgress.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
                tvNoData.setText("请求失败，点击可以重试！");
                tvNoData.setClickable(true);
            }
        }

        @Override
        public void onAMapNoData() {
            tvNoData.setText("未获取到数据");
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setClickable(false);
            listView.setVisibility(View.GONE);
        }

        @Override
        public void onAMapNotify(String notify) {
            ESToastUtil.showToast(mContext, notify);

        }

        @Override
        public void onStart() {
            rlProgress.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
        }

        @Override
        public void onFinish() {
            rlProgress.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            rlProgress.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText("请求失败，点击可以重试！");
            tvNoData.setClickable(true);
        }
    };
}
