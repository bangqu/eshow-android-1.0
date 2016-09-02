package com.bangqu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangqu.adapter.PhotoAdapter;
import com.bangqu.lib.R;
import com.bangqu.listener.OnClickFinishListener;
import com.bangqu.utils.ImageUtils;
import com.bangqu.view.SilderViewPager;
import com.longtu.base.util.StringUtils;
import com.longtu.base.util.ToastUtils;

import java.io.File;
import java.util.List;

/***
 * @项目名:Mps
 * @类名:PhotosFullActivity.java
 * @创建人:shibaotong
 * @类描述:图片预览
 * @date:2015年12月4日
 * @Version:1.0 ****************************************
 */

public class PhotosFullActivity extends Activity implements OnPageChangeListener,View.OnClickListener,OnClickFinishListener {
    private SilderViewPager vp_photo;
    private Button btn_cancel, btn_del;
    
    private PhotoAdapter photoAdapter;
    private List<String> listurls;
    private int position;
    
    private boolean del=false;
    private String type;

    private TextView tvCur;
    private ImageView ivDown;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case  1:
                    listurls.remove(position);
                    if (listurls.size()<=position&&position>0){
                        position--;
                    }
                    if(listurls!=null) {
                        photoAdapter = new PhotoAdapter(listurls, PhotosFullActivity.this, PhotosFullActivity.this);
                        vp_photo.setAdapter(photoAdapter);

                        if (position != -1) {
                            vp_photo.setCurrentItem(position);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initViews();
        initDatas();
        setDatas();
        setListener();
    }

    public void initDatas() {
        listurls=getIntent().getStringArrayListExtra("urls");
        position=getIntent().getIntExtra("position", -1);
        del=getIntent().getBooleanExtra("del", false);
        type=getIntent().getStringExtra("type");
        
        if(del){
            btn_del.setVisibility(View.VISIBLE);
        }else{
            btn_del.setVisibility(View.GONE);
        }
        
        if(listurls!=null){
        photoAdapter=new PhotoAdapter(listurls, this,this);
        vp_photo.setAdapter(photoAdapter);
        
        if(position!=-1){
            vp_photo.setCurrentItem(position);
        }
        }
        
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try{
            super.onTouchEvent(event);
        } catch(IllegalArgumentException ex) {
        }
        return false;
    }

    public void initViews() {
        vp_photo=(SilderViewPager)findViewById(R.id.vp_photo);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_del=(Button)findViewById(R.id.btn_del);
        tvCur=(TextView)findViewById(R.id.tvCur);
        ivDown=(ImageView)findViewById(R.id.ivDown);
    }

    public void setContentView() {
        setContentView(R.layout.activity_photo_browe);

    }

    public void setDatas() { 

    }

    public void setListener() { 
        btn_cancel.setOnClickListener(this);
        vp_photo.setOnPageChangeListener(this);
        btn_del.setOnClickListener(this);
        ivDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (!StringUtils.isEmpty(listurls.get(position))) {
                                Bitmap bitmap = ImageUtils.getImage(listurls.get(position));

                                ImageUtils.saveFile(bitmap,
                                        listurls.get(position).substring(
                                                listurls.get(position)
                                                        .lastIndexOf("/") + 1),PhotosFullActivity.this);
                                Log.e("load==>", "保存成功2！");

                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        ToastUtils.show(PhotosFullActivity.this,
                                                "已经保存到手机");
                                        sdScan();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.btn_cancel){
            Intent intent=new Intent();
            intent.putExtra("position",position);
            setResult(41,intent);
            finish();
            overridePendingTransition(R.anim.zoom_out,R.anim.zoom_finish_in);
        }else  if (v.getId()==R.id.btn_del){
            if (listurls.size()>1){
                handler.sendEmptyMessage(1);
            }else {
                finish();
            }
        }else if (v.getId()==R.id.ivDown){

        }

    }
    /***
     * 保存图片通知手机扫描
     */
    public void sdScan() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/chakeshe/"));
        intent.setData(uri);
        sendBroadcast(intent);
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {   
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {  
        position=arg0;
        tvCur.setText((position+1)+"/"+listurls.size());
        if (listurls.get(position).indexOf("http://")>-1){
            ivDown.setVisibility(View.VISIBLE);
        }else {
            ivDown.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageSelected(int arg0) {  
    }

    @Override
    public void onFinish() {
        Intent intent=new Intent();
        intent.putExtra("position",position);
        setResult(41,intent);
        finish();
        overridePendingTransition(R.anim.zoom_out,R.anim.zoom_finish_in);
    }
}
