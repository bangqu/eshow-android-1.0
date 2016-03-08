package com.bangqu.eshow.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.eshow.demo.R;
import com.bangqu.eshow.demo.bean.FunctionBean;

import java.util.List;

/**
 * 主页功能列表适配器
 * Created by daikting on 16/2/18.
 */
public class FunctionAdapter extends BaseAdapter{
    private Context mContext;
    private List<FunctionBean> functions;
    public FunctionAdapter(Context context,List<FunctionBean> functions){
        this.mContext = context;
        this.functions = functions;
    }
    @Override
    public int getCount() {
        return functions.size();
    }

    @Override
    public Object getItem(int position) {
        return functions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;
        if(convertView == null){
            viewHold = new ViewHold();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_function,null);
            viewHold.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHold.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            convertView.setTag(viewHold);
        }else{
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.tvName.setText(functions.get(position).getName());
        viewHold.ivIcon.setImageDrawable(mContext.getResources().getDrawable(functions.get(position).getIconId()));
        return convertView;
    }

    class ViewHold{
        TextView tvName;
        ImageView ivIcon;
    }
}
