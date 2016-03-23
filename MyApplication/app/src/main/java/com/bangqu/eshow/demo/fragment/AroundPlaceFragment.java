package com.bangqu.eshow.demo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.adapter.PlaceAdapter;
import com.bangqu.eshow.demo.bean.AMapLocationBean;
import com.bangqu.eshow.demo.bean.AMapLocationListResultBean;
import com.bangqu.eshow.demo.common.Global;
import com.bangqu.eshow.demo.network.AMapResponseListener;
import com.bangqu.eshow.demo.network.NetworkInterface;
import com.bangqu.eshow.fragment.ESProgressDialogFragment;
import com.bangqu.eshow.util.ESDialogUtil;
import com.bangqu.eshow.util.ESJsonUtil;
import com.bangqu.eshow.util.ESLogUtil;
import com.bangqu.eshow.util.ESToastUtil;
import com.bangqu.eshow.util.ESViewUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jazzylistview.JazzyListView;

/**
 * 地图周边分页
 * Created by daikting on 16/3/23.
 */
public class AroundPlaceFragment extends Fragment {
    private Context mContext;
    private String type;
    private String location;
    private JazzyListView listView;
    private TextView tvNoData;
    private List<AMapLocationBean> aroundList;
    private PlaceAdapter placeAdapter;
    ESProgressDialogFragment progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {

        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_chooseplace, null);
        ESViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.llParent));
        tvNoData = (TextView) view.findViewById(R.id.tvNoData);
        tvNoData.setVisibility(View.GONE);
        listView = (JazzyListView) view.findViewById(R.id.listview);
        NetworkInterface.placeAround(mContext, location, type, placeAroundListener);
        aroundList = new ArrayList<AMapLocationBean>();
        placeAdapter = new PlaceAdapter(mContext,aroundList);
        listView.setAdapter(placeAdapter);
        return view;
    }

    /**
     * 设置请求地址
     * @param type
     * @param location
     */
    public void setAroundInfo(String type,String location){
        this.type = type;
        this.location = location;
    }

    /**
     * 请求高德地图获取周边信息的接口回调
     */
    AMapResponseListener placeAroundListener = new AMapResponseListener(mContext) {

        @Override
        public void onAMapSucess(JSONObject resultJson) {
            ESLogUtil.d(mContext, "resultJson");
            AMapLocationListResultBean aMapLocationListResult = (AMapLocationListResultBean) ESJsonUtil.fromJson(resultJson.toString(), AMapLocationListResultBean.class);
            aroundList.clear();
            aroundList.addAll(aMapLocationListResult.getPois());
            placeAdapter.notifyDataSetChanged();
        }

        @Override
        public void onAMapNoData() {
            tvNoData.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        @Override
        public void onAMapNotify(String notify) {
            ESToastUtil.showToast(mContext, notify);

        }

        @Override
        public void onStart() {
            progressDialog = ESDialogUtil.showProgressDialog(mContext, Global.LOADING_PROGRESSBAR_ID, "请求数据中...");

        }

        @Override
        public void onFinish() {
            progressDialog.dismiss();

        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            progressDialog.dismiss();
            ESToastUtil.showToast(mContext, "请求失败，错误码：" + statusCode);

        }
    };
}
