package com.bangqu.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.adapter.PhotoRAdapter;
import com.bangqu.base.activity.BaseActivity;
import com.bangqu.lib.R;
import com.bangqu.listener.DeleteListener;
import com.longtu.base.util.FileUtils;
import com.longtu.base.util.StringUtils;
import com.longtu.base.util.ToastUtils;
import com.longtu.base.view.ScrollGridView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/***
 * 添加相册
 */
public class ChooseAlbumActivity extends BaseActivity implements DeleteListener,AdapterView.OnItemClickListener{

    private ScrollGridView gvChoose;

    private TextView tvSubTitle, tvTitle;
    private MaterialMenuView material_back_button;
    private RelativeLayout rlBack;

    private List<String>  listurls;
    private PhotoRAdapter photoRAdapter;
    private PopupWindow choosePhotoWindow;
    private EditText etTitle;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_choose_album);
    }

    @Override
    public void initViews() {
        tvSubTitle =(TextView)findViewById(R.id.tvSubTitle);
        tvTitle  =(TextView)findViewById(R.id.tvTitle);
        material_back_button  =(MaterialMenuView) findViewById(R.id.material_back_button);
        rlBack =(RelativeLayout) findViewById(R.id.rlBack);
        gvChoose=(ScrollGridView)findViewById(R.id.gvChoose);
        etTitle=(EditText)findViewById(R.id.etTitle);
    }

    @Override
    public void initDatas() {

        tvTitle.setText("添加相册");
        tvSubTitle.setText("完成");
        tvSubTitle.setVisibility(View.VISIBLE);
        material_back_button.setState(MaterialMenuDrawable.IconState.ARROW);
        listurls=new ArrayList<>();
        initChooseWindow();
        photoRAdapter=new PhotoRAdapter(listurls,this,this,0);
        gvChoose.setAdapter(photoRAdapter);
    }

    @Override
    public void setDatas() {

    }

    @Override
    public void setListener() {
        gvChoose.setOnItemClickListener(this);
        btnCancel.setOnClickListener(this);
        btnCam .setOnClickListener(this);
        btnCh.setOnClickListener(this);
        tvSubTitle.setOnClickListener(this);
        rlBack.setOnClickListener(this);
    }

    @Override
    public void ResumeDatas() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnCancel) {
            if (choosePhotoWindow != null)
                choosePhotoWindow.dismiss();
        }
        else if (v.getId()==R.id.btnCam) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_C_IMAGE);
            if (choosePhotoWindow != null)
                choosePhotoWindow.dismiss();
        }
        else if (v.getId()==R.id.btnCh) {
            intent=new Intent(this,SelectPhotoActivity.class);
            intent.putStringArrayListExtra("ps", (ArrayList<String>) listurls);
            Jump(intent,PHOTO);
            if (choosePhotoWindow != null)
                choosePhotoWindow.dismiss();
        }else if (v.getId()==R.id.tvSubTitle){
            if (StringUtils.isEmpty(etTitle.getText().toString())){
                ToastUtils.show(this,"请输入相册标题");
                return;
            }
            intent=new Intent();
            intent.putStringArrayListExtra("ps", (ArrayList<String>) listurls);
            setResult(ADDPHOTO,intent);
            finish();
        }else if (v.getId()==R.id.rlBack){
            finish();
        }
    }

    @Override
    public void ondel(int position) {
        listurls.remove(position);
        photoRAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listurls.size()<9){
            if (listurls.size()==position){
                choosePhotoWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                return;
            }
        }

        intent = new Intent(this, PhotosFullActivity.class);
        intent.putExtra("position",position);
        intent.putStringArrayListExtra("urls", (ArrayList<String>) listurls);
        Jump(intent,SEE_IMAGE);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    private Button btnCam, btnCh, btnCancel;
    private void initChooseWindow() {
        View moreView = getLayoutInflater().inflate(R.layout.choose_photo, null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        choosePhotoWindow = new PopupWindow(moreView);
        choosePhotoWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        choosePhotoWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        choosePhotoWindow.setFocusable(true);

        moreView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                choosePhotoWindow.dismiss();
                return false;
            }
        });
        btnCam = (Button) moreView.findViewById(R.id.btnCam);
        btnCh = (Button) moreView.findViewById(R.id.btnCh);
        btnCancel = (Button) moreView.findViewById(R.id.btnCancel);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (data == null)
                return;

            if (data != null) {
                Uri uri = data.getData();
                    Cursor cursor = getContentResolver().query(uri, null, null, null,null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        listurls.add(path);
                        photoRAdapter=new PhotoRAdapter(listurls,this,this,0);
                        gvChoose.setAdapter(photoRAdapter);
                }
            }
        } else if (requestCode==REQUEST_C_IMAGE){
            Log.e("requestCode==>",requestCode+"---");
            if (data == null)
                return;
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            String name = (new DateFormat()).format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
            FileUtils.writeFile(Environment.getExternalStorageDirectory() + "/chakeshe/" + name,bitmap);
                listurls.add(Environment.getExternalStorageDirectory() + "/chakeshe/" + name);
                photoRAdapter=new PhotoRAdapter(listurls,this,this,0);
                gvChoose.setAdapter(photoRAdapter);
        }else if (requestCode==PHOTO&&resultCode==SET_PHOTO){
            listurls=data.getStringArrayListExtra("ps");
            photoRAdapter=new PhotoRAdapter(listurls,this,this,0);
            gvChoose.setAdapter(photoRAdapter);
        }
    }
}
