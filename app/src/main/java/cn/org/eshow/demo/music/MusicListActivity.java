package cn.org.eshow.demo.music;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.bean.Song;
import cn.org.eshow.demo.common.SharedPrefUtil;

@EActivity(R.layout.activity_musiclist)
public class MusicListActivity extends AppCompatActivity {
    public static MusicListActivity instans;
    private Context mContext = MusicListActivity.this;
    private String tag = "MusicListActivity";
    private String sdPath = Environment.getExternalStorageDirectory().getPath() + "/EShouMusic/";//SD卡的路径
    private LinearLayout llMusicPlayBack;
    private ImageView ivIsplaying;
    private MusicAdapter adapter;
    private AnimationDrawable animationDrawable;
    public static List<Song> songList = new ArrayList<>();
    public static int pasePositin = 0;//记录上次的position
    private boolean isFistStart = true;

    @ViewById(R.id.lvMusicList)
    ListView lvMusicList;
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;
    public Handler musicListHandler = new Handler() {//接收消息，做处理
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(tag, "接受到的消息：" + msg.what);
            switch (msg.what) {
                case 0://现在播放的歌曲，设置动画
                    int positionShow = 0;
                    if (msg.obj != null) {
                        positionShow = Integer.parseInt(msg.obj.toString());
                    }
                    ivIsplaying = (ImageView) getViewByPosition(positionShow, lvMusicList).findViewById(R.id.ivIsplaying);
                    showPlaying(ivIsplaying);
                    break;
                case 1://清除刚才歌曲条目的动画
                    if (msg.obj != null) {
                        pasePositin = Integer.parseInt(msg.obj.toString());
                        Log.i(tag, "msg.obj.toString()：" + msg.obj.toString());
                    }
                    Log.i(tag, "清除刚才歌曲条目的动画：" + pasePositin + "--"
                            + songList.get(pasePositin).getName());
                    ivIsplaying = (ImageView) getViewByPosition(pasePositin, lvMusicList).findViewById(R.id.ivIsplaying);
                    if (animationDrawable != null) {
                        animationDrawable.stop();
                        ivIsplaying.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    adapter = new MusicAdapter(mContext, songList);
                    lvMusicList.setAdapter(adapter);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instans = this;
        MusicService_.intent(getApplication()).start();
        if (songList != null) {
            songList.clear();
        }
        songList = ReadXmlUtil.parseWebSongList(this);
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @AfterViews
    void findView() {
        mTvTitle.setText("音乐列表");
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);
        mMaterialBackButton.setVisibility(View.VISIBLE);
        redDownedMusic();
        adapter = new MusicAdapter(mContext, songList);
        lvMusicList.setAdapter(adapter);
        lvMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFistStart) {
                    isFistStart = false;
                }
                Message msg = new Message();
                msg.what = 4;
                msg.obj = position;
                if (songList.get(position).isDownFinish()) {//是本地就直接播放
                    MusicService.musicHandler.sendMessage(msg);
                    PlayActivity_.intent(mContext).start();
                } else {
                    if (isNetworkAvailable(MusicListActivity.this)) {//不是本地先判断音乐
                        MusicService.musicHandler.sendMessage(msg);
                        PlayActivity_.intent(mContext).start();
                    } else {
                        Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }

    //当前播放设置音符动画
    private void showPlaying(ImageView id) {
        id.setVisibility(View.VISIBLE);
        id.setImageResource(R.drawable.animation_musicplying);
        animationDrawable = (AnimationDrawable) id.getDrawable();
        animationDrawable.start();
    }

    //设置已经下载的音乐信息
    private void redDownedMusic() {
        if (SharedPrefUtil.getDownedmusic(MusicListActivity.this) != null) {
            String musiclist = SharedPrefUtil.getDownedmusic(MusicListActivity.this);
            Log.i(tag, "int position：" + musiclist);
            String[] mlist = musiclist.split("@123@");
            if (mlist.length > 0) {
                for (String songMessage : mlist) {
                    Log.i(tag, "int position：" + songMessage.split(",").length);
                    int position = Integer.parseInt(songMessage.split(",")[0]);
                    Log.i(tag, "int position：" + position);
                    Log.i(tag, "int Siz：" + Integer.parseInt(songMessage.split(",")[1]));
                    Log.i(tag, "int FilePath：" + songMessage.split(",")[2]);
                    MusicListActivity.songList.get(position).setSize(Integer.parseInt(songMessage.split(",")[1]));
                    MusicListActivity.songList.get(position).setFilePath(songMessage.split(",")[2]);
                    MusicListActivity.songList.get(position).setDownFinish(true);
                }
            } else {
                Log.i(tag, "没有下载的音乐");
            }
        } else {
            Log.i(tag, "没有下载的音乐");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(tag, "onResume");
        lvMusicList.setSelection(MusicService.playPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (songList != null) {
            songList.clear();
        }
        isFistStart = true;
        Log.i(tag, "onDestroy");
        MusicService_.intent(getApplication()).stop();
    }

    //更新下载后状态
    private void updateState(int position) {
        int firstVisiblePosition = lvMusicList.getFirstVisiblePosition();
        int lastVisiblePosition = lvMusicList.getLastVisiblePosition();
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            View view = lvMusicList.getChildAt(position - firstVisiblePosition);
            if (view.getTag() instanceof MusicAdapter.ViewHolder) {
                MusicAdapter.ViewHolder vh = (MusicAdapter.ViewHolder) view.getTag();
                vh.ivMusicDowned.setVisibility(View.VISIBLE);
                vh.tvSize.setVisibility(View.VISIBLE);
                vh.tvSize.setText(songList.get(position).getSize());
            }
        }
    }

    /**
     * 检查当前网络是否可用
     *
     * @return
     */

    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
