package com.bangqu.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.bangqu.lib.R;
import com.bangqu.listener.OnClickFinishListener;
import com.longtu.base.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/***
 * 
 * @项目名:Mps  
 * 
 * @类名:PhotoAdapter.java  
 * 
 * @创建人:shibaotong 
 *
 * @类描述:大图预览适配器
 * 
 * @date:2015年12月4日
 * 
 * @Version:1.0 
 *
 *****************************************
 */
public class PhotoAdapter extends PagerAdapter {
    private List<String> listurls;
    private Context context;
    
    private PhotoView iv_photo;
    private OnClickFinishListener listener;
    
    public PhotoAdapter(List<String> listurls, Context context, OnClickFinishListener listener) {
        this.listurls=listurls;
        this.context=context;
        this.listener=listener;
    }

    @Override
    public int getCount() { 
        return listurls.size();
    }


    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == (View) arg1;
    }

    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    public Object instantiateItem(View container, final int position) {
        View view=LayoutInflater.from(context).inflate(R.layout.photo_item, null);
        iv_photo=(PhotoView)view.findViewById(R.id.iv_photo);

        if(!StringUtils.isEmpty(listurls.get(position))){
            if(listurls.get(position).indexOf("http://")>-1){
                ImageLoader.getInstance().displayImage(listurls.get(position), iv_photo);
            }else{
                ImageLoader.getInstance().displayImage("file://"+listurls.get(position), iv_photo);
            }
        }

        iv_photo.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                listener.onFinish();
            }
        });
        
        ((ViewPager) container).addView(view, 0);
        return view;
    }

}
