package com.bangqu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bangqu.base.activity.BangquApplication;
import com.bangqu.bean.SelectPhotoBean;
import com.bangqu.lib.R;
import com.longtu.base.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class SelectGridPhotoAdapter extends BaseAdapter {
    
    private List<SelectPhotoBean> listpaths;
    private Context context;
    private String path;

    
    public SelectGridPhotoAdapter(List<SelectPhotoBean> listpaths, String path, Context convertView) {
        this.listpaths=listpaths;
        this.context=convertView;
        this.path=path;
    }

    @Override
    public int getCount() { 
        return listpaths.size();
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
        Select_item select_item;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.select_grid, null);
            select_item=new Select_item();
            select_item.iv_photo=(ImageView)convertView.findViewById(R.id.iv_photo);
            select_item.btn_select=(ImageView)convertView.findViewById(R.id.btn_select);
            convertView.setTag(select_item);        
        }else{
            select_item=(Select_item) convertView.getTag();
        }
        
        LayoutParams params=select_item.iv_photo.getLayoutParams();
        params.width= BangquApplication.width/4-15;
        params.height=params.width;
        if(!StringUtils.isEmpty(path)){
        if(!StringUtils.isEmpty(listpaths.get(position).path)){
            ImageLoader.getInstance().displayImage("file://"+path+"/"+listpaths.get(position).path,
                    select_item.iv_photo);
        }
        }else{
            if(!StringUtils.isEmpty(listpaths.get(position).url)){
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(listpaths.get(position).url, select_item.iv_photo);
            }
        }
        
        if(listpaths.get(position).select){
            select_item.btn_select.setBackgroundResource(R.mipmap.pictures_selected);
        }else{
            select_item.btn_select.setBackgroundResource(R.mipmap.picture_unselected);
        }
        
        return convertView;
    }
    
    class Select_item{
        ImageView iv_photo;
        ImageView btn_select;
    }

}
