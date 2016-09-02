package com.bangqu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.base.activity.BangquApplication;
import com.bangqu.bean.AlbumBean;
import com.bangqu.lib.R;
import com.longtu.base.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by 唯图 on 2016/8/31.
 */
public class AlbumAdapter extends BaseAdapter {

    private List<AlbumBean.DataBean> listalbums;
    private Context context;

    public AlbumAdapter( List<AlbumBean.DataBean> listalbums,Context context){
        this.listalbums=listalbums;
        this.context=context;
    }

    @Override
    public int getCount() {
        return listalbums.size()%2>0?listalbums.size()/2+1:listalbums.size()/2;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Album_item album_item;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.album_item,null);
            album_item=new Album_item();
            album_item.flLeft=(FrameLayout)convertView.findViewById(R.id.flLeft);
            album_item.flRight=(FrameLayout)convertView.findViewById(R.id.flRight);
            album_item.ivLeft=(ImageView) convertView.findViewById(R.id.ivLeft);
            album_item.ivRight=(ImageView) convertView.findViewById(R.id.ivRight);
            album_item.tvLeft=(TextView) convertView.findViewById(R.id.tvLeft);
            album_item.tvRight=(TextView) convertView.findViewById(R.id.tvRight);
            convertView.setTag(album_item);
        }else {
            album_item= (Album_item) convertView.getTag();
        }
        ViewGroup.LayoutParams params=album_item.ivLeft.getLayoutParams();
        params.width= BangquApplication.width/2-BangquApplication.width/22;
        params.height=BangquApplication.width/2-BangquApplication.width/12;

        album_item.ivRight.setLayoutParams(params);
        album_item.flRight.setVisibility(View.VISIBLE);

        if (!StringUtils.isEmpty(listalbums.get(position*2).getImageurl())){
            if (listalbums.get(position*2).getImageurl().indexOf("http://")>-1) {
                ImageLoader.getInstance().displayImage(listalbums.get(position * 2).getImageurl(), album_item.ivLeft);
            }else {
                ImageLoader.getInstance().displayImage("file://"+listalbums.get(position * 2).getImageurl(), album_item.ivLeft);
            }
        }

        if (position*2+1<listalbums.size()&&!StringUtils.isEmpty(listalbums.get(position*2+1).getImageurl())){
            if (listalbums.get(position*2+1).getImageurl().indexOf("http://")>-1) {
                ImageLoader.getInstance().displayImage(listalbums.get(position * 2 + 1).getImageurl(), album_item.ivRight);
            }else {
                ImageLoader.getInstance().displayImage("file://"+listalbums.get(position * 2 + 1).getImageurl(), album_item.ivRight);
            }

        }else {
            album_item.flRight.setVisibility(View.INVISIBLE);
        }

        if (!StringUtils.isEmpty(listalbums.get(position*2).getTitle())){
          album_item.tvLeft.setText(listalbums.get(position*2).getTitle());
        }

        if (position*2+1<listalbums.size()&&!StringUtils.isEmpty(listalbums.get(position*2+1).getTitle())){
            album_item.tvRight.setText(listalbums.get(position*2+1).getTitle());
        }

        return convertView;
    }

    class Album_item{
        FrameLayout flLeft, flRight;
        ImageView ivLeft, ivRight;
        TextView tvLeft, tvRight;
    }
}
