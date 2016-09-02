package com.bangqu.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bangqu.adapter.SelectGridPhotoAdapter;
import com.bangqu.adapter.SelectPhotoAdapter;
import com.bangqu.base.activity.BaseActivity;
import com.bangqu.bean.SelectPhotoBean;
import com.bangqu.lib.R;
import com.longtu.base.util.ToastUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectPhotoActivity extends Activity implements OnItemClickListener,View.OnClickListener{

    /***
     * 临时存储路径
     */
    private List<String> listpaths;
    private List<File> listfiles;
    
    private List<List<SelectPhotoBean>> lists;
    private SelectPhotoBean selectPhotoBean;
    private List<SelectPhotoBean> listselects;
    
    private ImageButton ibtn_left, ibtn_right;
    private TextView tv_title;
    private ListView lv_select;
    private GridView gv_select;
    
    
    private SelectPhotoAdapter selectPhotoAdapter;
    private int pos;
    
    private SelectGridPhotoAdapter selectGridPhotoAdapter;
    private boolean select=false;

    private List<String> listps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initViews();
        initDatas();
        setDatas();
        setListener();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case 1:

                if (listfiles != null) {
                    for (int i = 0; i < listfiles.size(); i++) {
                        listpaths = Arrays
                                .asList(listfiles.get(i).list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename.endsWith(".jpg")||filename.endsWith(".png")||filename.endsWith(".gif")||filename.endsWith(".bmp"))
                                    return true;
                                return false;
                            }
                        }));
                        listselects=new ArrayList<SelectPhotoBean>();
                        for(int j=0;j<listpaths.size();j++){
                            selectPhotoBean=new SelectPhotoBean();
                            selectPhotoBean.path=listpaths.get(j);
                            listselects.add(selectPhotoBean);
                        }
                            lists.add(listselects); 
                        listpaths=null;
                    }
                }
                
                selectPhotoAdapter.notifyDataSetChanged();

                break;

            default:
                break;
            }
        }
    };

    public void initDatas() {
        ibtn_left.setImageResource(R.mipmap.back_w);
        tv_title.setText("照片");
        ibtn_right.setImageResource(R.mipmap.ok_btn);
        ibtn_right.setVisibility(View.INVISIBLE);
        gv_select.setVisibility(View.GONE);
        
        listpaths = new ArrayList<String>();
        listfiles = new ArrayList<File>();
        lists=new ArrayList<List<SelectPhotoBean>>();
        listps=getIntent().getStringArrayListExtra("ps");
        
        getImages(); 
        selectPhotoAdapter=new SelectPhotoAdapter(lists,listfiles,listps, this);
        lv_select.setAdapter(selectPhotoAdapter);
      
    }

    public void initViews() {
        ibtn_left = (ImageButton) findViewById(R.id.ibtn_mune);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ibtn_right = (ImageButton) findViewById(R.id.ibtn_search);
        lv_select=(ListView)findViewById(R.id.lv_select);
        gv_select=(GridView)findViewById(R.id.gv_select);
    }

    public void setContentView() {
        setContentView(R.layout.activity_select);
    }

    public void setDatas() {

    }

    public void setListener() { 
        ibtn_left.setOnClickListener(this);
        lv_select.setOnItemClickListener(this);
        gv_select.setOnItemClickListener(this);
        ibtn_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.ibtn_mune) {
            if (!select) {
                finish();
            } else {
                lv_select.setVisibility(View.VISIBLE);
                gv_select.setVisibility(View.GONE);
                ibtn_right.setVisibility(View.INVISIBLE);
                for (int i = 0; i < listselects.size(); i++) {
                    listselects.get(i).select = false;
                }
                tv_title.setText("照片");
                select = false;
            }
        }
        if (v.getId()==R.id.ibtn_search) {
            Intent intent=new Intent();
            intent.putStringArrayListExtra("ps", (ArrayList<String>) listps);
            setResult(3,intent);
/*//            Constants.listpaths.clear();
            for (int i = 0; i < listselects.size(); i++) {
                if (listselects.get(i).select) {
                    if (listfiles != null && listfiles.size() > 0) {
//                        Constants.listpaths.add(listfiles.get(pos).getAbsolutePath() + "/" + listselects.get(i).path);
                    } else {
//                        Constants.listpaths.add(listselects.get(i).path);
                    }
                }
            }*/
            finish();
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!select){
                finish();
            }else{
                lv_select.setVisibility(View.VISIBLE);
                gv_select.setVisibility(View.GONE);
                ibtn_right.setVisibility(View.INVISIBLE); 
                for(int i=0;i<listselects.size();i++){
                    listselects.get(i).select=false;
                }
                tv_title.setText("照片");
                select=false;
            }
            
            
        }
        return false;
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = SelectPhotoActivity.this
                        .getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                         +MediaStore.Images.Media.MIME_TYPE + "=? or "
                           +MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] {"image/bmp","image/gif", "image/jpeg", "image/png" },
                        MediaStore.Images.Media.DATE_MODIFIED);

                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    String dirPath = parentFile.getAbsolutePath();

                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (listpaths.contains(dirPath)) {
                        continue;
                    } else {
                        listfiles.add(parentFile);
                        listpaths.add(dirPath);
                    }

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg"))
                                return true;
                            return false;
                        }
                    }).length;
                     
                }
                mCursor.close();
                // 扫描完成，辅助的HashSet也就可以释放内存了
                /*
                 * // 通知Handler扫描图片完成 mHandler.sendEmptyMessage(0x110);
                 */
                for (int i = 0; i < listpaths.size(); i++) {
                    Log.e("path==>", listpaths.get(i));
                }
                listpaths = null;
                handler.sendEmptyMessage(1);
            }
        }).start();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) { 
        Log.e("parent==>", parent.getId()+"");
        if (parent.getId()==R.id.lv_select) {
            pos = position;
            listselects = null;
            listselects = lists.get(position);
            tv_title.setText(listfiles.get(position).getAbsolutePath().substring(
                    listfiles.get(position).getAbsolutePath().lastIndexOf("/") + 1));
            setSt();
            selectGridPhotoAdapter = new SelectGridPhotoAdapter(listselects, listfiles.get(position).getAbsolutePath(), this);
            gv_select.setAdapter(selectGridPhotoAdapter);
            lv_select.setVisibility(View.GONE);
            gv_select.setVisibility(View.VISIBLE);
            select = true;
        }
       else if (parent.getId()==R.id.gv_select) {
            listselects.get(position).select = !listselects.get(position).select;
            if(listselects.get(position).select){
                if (!listps.contains(listfiles.get(pos).getAbsolutePath() + "/" +listselects.get(position).path)){
                    if (listps.size()<9) {
                        listps.add(listfiles.get(pos).getAbsolutePath() + "/" + listselects.get(position).path);
                    }else {
                        ToastUtils.show(this,"最多选择9张照片");
                        listselects.get(position).select = !listselects.get(position).select;
                    }
                }
            }else {
                if (listps.contains(listfiles.get(pos).getAbsolutePath() + "/" +listselects.get(position).path)){
                    listps.remove(listfiles.get(pos).getAbsolutePath() + "/" +listselects.get(position).path);
                }
            }
            setselect();
            selectPhotoAdapter.notifyDataSetChanged();
            selectGridPhotoAdapter.notifyDataSetChanged();
        }
    }

    private void setSt(){
        for (int i=0;i<listps.size();i++){
            for (int j=0;j<listselects.size();j++){
                if (listps.get(i).indexOf(listselects.get(j).path)>-1){
                    listselects.get(j).select=true;
                }
            }
        }
    }
    
    private void setselect(){
        int i;
        for(i=0;i<listselects.size();i++){
            if(listselects.get(i).select){
                ibtn_right.setVisibility(View.VISIBLE);
                return;
            }
        }
        if(i==listselects.size()){
            ibtn_right.setVisibility(View.INVISIBLE);
        }
    }
   
}
