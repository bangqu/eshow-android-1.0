package com.bangqu.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.adapter.AlbumAdapter;
import com.bangqu.base.activity.BaseActivity;
import com.bangqu.bean.AlbumBean;
import com.bangqu.lib.R;
import com.longtu.base.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailsActivity extends BaseActivity {

    private ListView lvAlbums;

    private RelativeLayout rlBack;
    private MaterialMenuView material_back_button;
    private TextView tvTitle;
    private String title;
    private AlbumBean albumBean;

    private Message msg;
    private AlbumAdapter albumAdapter;

    private View FooterView;
    private ImageView ivLoading ;
    private TextView tvLoading;
    private List<String> liststrs;

    private ImageView ivRight;
    private int position;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:

                    albumBean= JSONObject.parseObject(msg.obj.toString(),AlbumBean.class);
                    if (albumBean!=null&&albumBean.getData()!=null){
                        albumAdapter=new AlbumAdapter(albumBean.getData(),AlbumDetailsActivity.this);
                        lvAlbums.setAdapter(albumAdapter);
                        setAlbums();

                    }

                    break;
            }
        }
    };

    private void setAlbums(){
        liststrs.clear();
        for (int i=0;i<albumBean.getData().size();i++){
            liststrs.add(albumBean.getData().get(i).getImageurl());
        }
    }


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_album_details);
    }

    @Override
    public void initViews() {
        lvAlbums=(ListView)findViewById(R.id.lvAlbums);
        rlBack=(RelativeLayout)findViewById(R.id.rlBack);
        material_back_button = (MaterialMenuView) findViewById(R.id.material_back_button);
        material_back_button.setState(MaterialMenuDrawable.IconState.ARROW);
        tvTitle=(TextView) findViewById(R.id.tvTitle);
        title=getIntent().getStringExtra("title");

        if (!StringUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    @Override
    public void initDatas() {
        get("http://www.image.tunqu.net/albumload.json");
        liststrs=new ArrayList<>();
    }

    @Override
    public void setDatas() {

    }

    @Override
    public void setListener() {
        lvAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intent = new Intent(AlbumDetailsActivity.this, PhotosFullActivity.class);
                intent.putExtra("position", 2*(position));
                intent.putStringArrayListExtra("urls", (ArrayList<String>) liststrs);
                Jump(intent,SEE_IMAGE);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });
        material_back_button.setOnClickListener(this);
    }

    @Override
    public void ResumeDatas() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.material_back_button){
            finish();
        }
    }

    @Override
    public void OnReceive(String requestname, String response) {
        msg=new Message();
        if (requestname.equals("http://www.image.tunqu.net/albumload.json")){
            msg.what=1;
        }
        msg.obj=response;
        handler.sendMessage(msg);
    }
}
