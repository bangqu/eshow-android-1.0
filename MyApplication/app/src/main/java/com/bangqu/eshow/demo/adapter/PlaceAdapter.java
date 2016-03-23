package com.bangqu.eshow.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.bean.AMapLocationBean;
import com.bangqu.eshow.util.ESViewUtil;

import java.util.List;

/**
 * 高德地图获取到的周围信息
 * Created by daikting on 16/2/18.
 */
public class PlaceAdapter extends BaseAdapter{
    private Context mContext;
    private List<AMapLocationBean> aroundList;
    public PlaceAdapter(Context context, List<AMapLocationBean> aroundList){
        this.mContext = context;
        this.aroundList = aroundList;
    }
    @Override
    public int getCount() {
        return aroundList.size();
    }

    @Override
    public Object getItem(int position) {
        return aroundList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;
        if(convertView == null){
            viewHold = new ViewHold();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_place,null);
            ESViewUtil.scaleContentView((LinearLayout)convertView.findViewById(R.id.llParent));
            viewHold.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHold.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            convertView.setTag(viewHold);
        }else{
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.tvName.setText(aroundList.get(position).getName());
        viewHold.tvAddress.setText(aroundList.get(position).getAddress());
        return convertView;
    }

    class ViewHold{
        TextView tvName;
        TextView tvAddress;
    }
}
