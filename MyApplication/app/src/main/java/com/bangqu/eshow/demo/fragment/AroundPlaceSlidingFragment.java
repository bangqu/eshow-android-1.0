package com.bangqu.eshow.demo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.view.sliding.ESSlidingTabView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daikting on 16/3/23.
 */
public class AroundPlaceSlidingFragment extends Fragment {

    private Context mContext = null;
    ESSlidingTabView mSlidingTabView;
    private String location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_aroundplacesliding, null);
        mSlidingTabView = (ESSlidingTabView) view.findViewById(R.id.mAbSlidingTabView);
        initPlaceSliding();
        return view;
    }

    /**
     * 设置请求地址
     * @param location
     */
    public void setLocation(String location){
        this.location = location;
    }
    /**
     * 初始化地图周边的分页
     */
    private void initPlaceSliding() {
        //缓存数量
        mSlidingTabView.getViewPager().setOffscreenPageLimit(4);
        mSlidingTabView.setSlidingEnabled(true);
        mSlidingTabView.setTabTextSize(26);
        String type1 = "写字楼";
        String type2 = "小区";
        String type3 = "学校";
        String allType = "产业园区|"+type1 + "|" + type2 + "|" + type3;
        AroundPlaceFragment allPlaceFragment = new AroundPlaceFragment();
        allPlaceFragment.setAroundInfo(allType, location);
        AroundPlaceFragment type1PlaceFragment = new AroundPlaceFragment();
        type1PlaceFragment.setAroundInfo(type1, location);

        AroundPlaceFragment type2PlaceFragment = new AroundPlaceFragment();
        type2PlaceFragment.setAroundInfo(type2, location);

        AroundPlaceFragment type3PlaceFragment = new AroundPlaceFragment();
        type3PlaceFragment.setAroundInfo(type3, location);

        List<Fragment> mFragments = new ArrayList<Fragment>();
        mFragments.add(allPlaceFragment);
        mFragments.add(type1PlaceFragment);
        mFragments.add(type2PlaceFragment);
        mFragments.add(type3PlaceFragment);

        List<String> tabTexts = new ArrayList<String>();
        tabTexts.add("全部");
        tabTexts.add(type1);
        tabTexts.add(type2);
        tabTexts.add(type3);
        //设置样式
        mSlidingTabView.setTabTextColor(getResources().getColor(R.color.normal_text));
        mSlidingTabView.setTabSelectColor(getResources().getColor(R.color.colorAccent));
        mSlidingTabView.setTabBackgroundResource(R.drawable.tab_bg);
        mSlidingTabView.setTabLayoutBackgroundResource(R.drawable.slide_top);
        mSlidingTabView.addItemViews(tabTexts, mFragments);
        mSlidingTabView.setTabPadding(20, 20, 20, 20);
        mSlidingTabView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
