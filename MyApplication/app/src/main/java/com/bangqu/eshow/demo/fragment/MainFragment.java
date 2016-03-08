package com.bangqu.eshow.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.adapter.FunctionAdapter;
import com.bangqu.eshow.demo.bean.FunctionBean;
import com.bangqu.eshow.fragment.ESFragment;
import com.bangqu.eshow.util.ESViewUtil;
import com.ecloud.pulltozoomview.PullToZoomListViewEx;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * Created by daikting on 16/2/19.
 */
public class MainFragment extends ESFragment implements View.OnClickListener {
    private Context mContext;
    private PullToZoomListViewEx listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_main, null);
        ESViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.llParent));
        listView = (PullToZoomListViewEx) view.findViewById(R.id.listview);

        String[] functionNames = getResources().getStringArray(R.array.functions);
        int[] funcitonIcons = {R.drawable.main_function_1,
                R.drawable.main_function_2,
                R.drawable.main_function_3,
                R.drawable.main_function_4,
                R.drawable.main_function_5,
                R.drawable.main_function_6,
                R.drawable.main_function_7,
                R.drawable.main_function_8,
                R.drawable.main_function_9};
        List<FunctionBean> functionBeanList = new ArrayList<FunctionBean>();
        for(int i = 0; i< functionNames.length; i++){
            FunctionBean functionBean = new FunctionBean();
            functionBean.setName(functionNames[i]);
            functionBean.setIconId(funcitonIcons[i]);
            functionBeanList.add(functionBean);
        }
        FunctionAdapter functionAdapter = new FunctionAdapter(mContext,functionBeanList);
        listView.setAdapter(functionAdapter);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        listView.ZOOM_ANIMATION_DURING = 100l;
        listView.setHeaderViewSize(mScreenWidth,300);
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
