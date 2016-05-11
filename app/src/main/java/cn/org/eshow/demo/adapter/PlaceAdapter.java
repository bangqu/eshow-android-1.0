package cn.org.eshow.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.bean.AMapLocationBean;
import cn.org.eshow.demo.common.Global;
import cn.org.eshow.framwork.util.AbLogUtil;
import cn.org.eshow.framwork.util.AbViewUtil;

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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHold viewHold;
        if(convertView == null){
            viewHold = new ViewHold();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_place,null);
            AbViewUtil.scaleContentView((LinearLayout)convertView.findViewById(R.id.llParent));
            viewHold.rlItem = (RelativeLayout) convertView.findViewById(R.id.rlItem);
            viewHold.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHold.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            convertView.setTag(viewHold);
        }else{
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.tvName.setText(aroundList.get(position).getName());
        viewHold.tvAddress.setText(aroundList.get(position).getAddress());
        viewHold.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbLogUtil.d("AroudMap", "SendBrocast");
                AMapLocationBean aMapLocationBean = aroundList.get(position);
                Intent intent = new Intent(Global.EShow_Broadcast_Action.ACTION_LOCATION_GOT);
                intent.putExtra("location", aMapLocationBean);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                Activity parentActivity = (Activity) mContext;
                parentActivity.finish();
            }
        });
        return convertView;
    }

    class ViewHold{
        TextView tvName;
        TextView tvAddress;
        RelativeLayout rlItem;
    }
}
