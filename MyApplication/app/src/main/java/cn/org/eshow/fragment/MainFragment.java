package cn.org.eshow.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.ecloud.pulltozoomview.PullToZoomListViewEx;
import cn.org.eshow.R;
import cn.org.eshow.adapter.FunctionAdapter;
import cn.org.eshow.bean.Enum_FunctionsInMain;
import cn.org.eshow_structure.fragment.ESFragment;
import cn.org.eshow_structure.util.ESViewUtil;

/**
 * 首页
 * Created by daikting on 16/2/19.
 */
public class MainFragment extends ESFragment {
    private Context mContext;
    private PullToZoomListViewEx listView;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_main, null);
        ESViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.llParent));

        listView = (PullToZoomListViewEx) view.findViewById(R.id.listview);

        List<Enum_FunctionsInMain> functionBeanList = new ArrayList<Enum_FunctionsInMain>();
        for(Enum_FunctionsInMain function : Enum_FunctionsInMain.values()){
            functionBeanList.add(function);
        }
        FunctionAdapter functionAdapter = new FunctionAdapter(mContext, functionBeanList);
        listView.setAdapter(functionAdapter);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        listView.ZOOM_ANIMATION_DURING = 100l;
        int height = ESViewUtil.scaleValue(mContext, 300.0f);
        listView.setHeaderViewSize(mScreenWidth, (int) height);
        return view;
    }

    public void onSlindingClose(int xPoint) {
        if (listView != null) {
            listView.startJazzyItemAnimate(xPoint);
        }
    }
}
