package com.bangqu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bangqu.bean.SelectPhotoBean;
import com.bangqu.lib.R;
import com.longtu.base.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;

public class SelectPhotoAdapter extends BaseAdapter {
    
    private List<List<SelectPhotoBean>> listpaths;
    private Context context;
    private List<File> listfiles;
    private List<String> listps;
    
    public SelectPhotoAdapter(List<List<SelectPhotoBean>> listpaths, List<File> listfiles,List<String> listps, Context context) {
        this.listpaths=listpaths;
        this.listfiles=listfiles;
        this.listps=listps;
        this.context=context;
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
            convertView=LayoutInflater.from(context).inflate(R.layout.select_item, null);
            select_item=new Select_item();
            select_item.iv_photo=(ImageView)convertView.findViewById(R.id.iv_photo);
            select_item.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
            select_item.tv_cout=(TextView)convertView.findViewById(R.id.tv_cout);
            select_item.tvSelectNum=(TextView)convertView.findViewById(R.id.tvSelectNum);
            convertView.setTag(select_item);
        }else{
            select_item=(Select_item) convertView.getTag();
        }

        if(listpaths.get(position)!=null){
            select_item.tv_cout.setText(listpaths.get(position).size()+"");

            if (getNum(position)>0) {
                select_item.tvSelectNum.setText(getNum(position) + "");
                select_item.tvSelectNum.setVisibility(View.VISIBLE);
            }else {
                select_item.tvSelectNum.setVisibility(View.GONE);
            }
            
            if(listpaths.get(position).size()>0){
           if(!StringUtils.isEmpty(listpaths.get(position).get(0).path)){
               /*        if(new File(listfiles.get(position).getAbsolutePath()+"/"+listpaths.get(position).get(0).path).exists()){
                        Bitmap bitmap=BitmapFactory.decodeFile(listfiles.get(position).getAbsolutePath()+"/"+listpaths.get(position).get(0).path);
                        select_item.iv_photo.setImageBitmap(bitmap);
                        } */
                       ImageLoader.getInstance().displayImage("file://"+listfiles.get(position).getAbsolutePath()+"/"+listpaths.get(position).get(0).path,
                                select_item.iv_photo);
                        
                        select_item.tv_name.setText(listfiles.get(position).getAbsolutePath().substring(
                                listfiles.get(position).getAbsolutePath().lastIndexOf("/")+1));
                        
                }
            }
        }
        
        
        return convertView;
    }

    private int getNum(int position){
        int len=listpaths.get(position).size();
        int count=0;
        for (int i=0;i<len;i++){
            for (int j=0;j<listps.size();j++){
                if (listps.get(j).indexOf(listpaths.get(position).get(i).path)>-1){
                    count++;
                }
            }
        }
        return count;
    }
    
    class Select_item{
        ImageView iv_photo ;
        TextView tv_name, tv_cout,tvSelectNum;
    }

}
