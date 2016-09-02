package com.bangqu.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends BaseActivity implements AdapterView.OnItemClickListener,AbsListView.OnScrollListener{

    private RelativeLayout  rlBack;
    private MaterialMenuView material_back_button;
    private TextView tvTitle;

    private PullToRefreshListView plAlbums;
    private ListView lvAlbums;

    private int index=1;

    private List<AlbumBean.DataBean> listalbums;
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
                    if (index==1){
                        listalbums.clear();
                    }
                    albumBean= JSONObject.parseObject(msg.obj.toString(),AlbumBean.class);
                    if (albumBean!=null&&albumBean.getData()!=null){
                        listalbums.addAll(albumBean.getData());
                        setAlbums();

                    }

                    albumAdapter.notifyDataSetChanged();

                    break;
            }
        }
    };

    private void setAlbums(){
        liststrs.clear();
        for (int i=0;i<listalbums.size();i++){
            liststrs.add(listalbums.get(i).getImageurl());
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_album);
    }

    @Override
    public void initViews() {
        rlBack=(RelativeLayout)findViewById(R.id.rlBack);
        material_back_button=(MaterialMenuView) findViewById(R.id.material_back_button);
        tvTitle=(TextView) findViewById(R.id.tvTitle);
        ivRight=(ImageView) findViewById(R.id.ivRight);
        ivRight.setImageResource(R.mipmap.ic_menu_add);
        ivRight.setVisibility(View.VISIBLE);
        plAlbums=(PullToRefreshListView)findViewById(R.id.plAlbums);
        lvAlbums=plAlbums.getRefreshableView();
        material_back_button.setState(MaterialMenuDrawable.IconState.ARROW);
        tvTitle.setText("相册");

        FooterView= LayoutInflater.from(this).inflate(R.layout.list_footer,null);
        ivLoading =(ImageView) FooterView.findViewById(R.id.ivLoading);
        tvLoading=(TextView)FooterView.findViewById(R.id.tvLoading);
        lvAlbums.addFooterView(FooterView);
        loading(ivLoading);


    }

    @Override
    public void initDatas() {
        listalbums=new ArrayList<>();
        albumAdapter=new AlbumAdapter(listalbums,this);
        lvAlbums.setAdapter(albumAdapter);
        liststrs=new ArrayList<>();
        getAlbum();
    }

    private void getAlbum(){
        get("http://www.image.tunqu.net/albumload.json");
    }

    @Override
    public void setDatas() {

    }

    @Override
    public void setListener() {
        plAlbums.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(AlbumActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        plAlbums.onRefreshComplete();
                        index=1;
                        getAlbum();
                    }
                },500);

            }
        });
        lvAlbums.setOnScrollListener(this);

        lvAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0||position==listalbums.size()+1){
                    return;
                }
                intent = new Intent(AlbumActivity.this, AlbumDetailsActivity.class);
                intent.putExtra("title",listalbums.get(2*(position-1)).getTitle());
                Jump(intent);
                /*intent.putExtra("position", 2*(position-1));
                intent.putStringArrayListExtra("urls", (ArrayList<String>) liststrs);
                Jump(intent,SEE_IMAGE);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);*/
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
        }else if (v.getId()==R.id.ivRight){
            intent=new Intent(this,ChooseAlbumActivity.class);
            Jump(intent,ADDPHOTO);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1&&ivLoading.getVisibility()==View.VISIBLE) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       index++;
                       getAlbum();
                    }
                }, 500);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==SEE_IMAGE&&resultCode==SELECT_IMAGE){
            position=data.getIntExtra("position",0);
            lvAlbums.setSelection(position/2);
        }else if (requestCode==ADDPHOTO&&resultCode==ADDPHOTO){
            List<String> list=data.getStringArrayListExtra("ps");
            for (int i=0;i<list.size();i++){
                AlbumBean.DataBean dataBean=new AlbumBean.DataBean();
                dataBean.setTitle("this is title");
                dataBean.setImageurl(list.get(i));
                listalbums.add(dataBean);
            }
            albumAdapter.notifyDataSetChanged();
        }
    }
}
