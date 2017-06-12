package cn.org.eshow.demo.music;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.bean.Song;
import cn.org.eshow.demo.common.SharedPrefUtil;

/**
 * Created by Administrator on 2016/4/20.
 */
@EActivity(R.layout.activity_musicdown)
public class MusicDownActivity extends Activity implements View.OnClickListener {
    public static MusicDownActivity instants;
    private Context context = MusicDownActivity.this;
    private String tag = "MusicDownActivity";
    private String sdPath = Environment.getExternalStorageDirectory().getPath() + "/EShouMusic/";//SD卡的路径
    private String filePath = "";//下载后的路径
    public static List<Song> downList = new ArrayList<>();
    public static List<Song> downSuecessList = new ArrayList<>();
    private MusicDowningAdapter musicDowningAdapter;
    private MusicDownedAdapter musicDownedAdapter;
    private int max = 0;//文件大小/进度条最大值
    private int downSize = 0;//下载大小值
    public Handler handler = new UIHandler();
    private static Boolean isExit = false;
    public static Boolean isDowning = false;
    static Boolean isOnDown = true;//判断是正在下载，还是已下载。
    static int pasePosition = 0;//记录刚才点击的position

    @ViewById(R.id.lvMusicDownList)
    ListView lvMusicDownList;
    @ViewById(R.id.rlBack)
    RelativeLayout mRlMenu;
    @ViewById(R.id.material_back_button)
    MaterialMenuView mMaterialBackButton;
    @ViewById(R.id.tvTitle)
    TextView mTvTitle;
    @ViewById(R.id.btDowned)
    Button btDowned;
    @ViewById(R.id.btDowning)
    Button btDowning;

    public final class UIHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: // 更新进度
                    if (downSize == max) { // 已经下载等于最大值
                        MusicListActivity.songList.get(getIndex(downList.get(0), MusicListActivity.songList)).setSize(max);//给下载好的音乐设置名字大小路径
                        MusicListActivity.songList.get(getIndex(downList.get(0), MusicListActivity.songList)).setDownFinish(true);
                        MusicListActivity.songList.get(getIndex(downList.get(0), MusicListActivity.songList)).setFilePath(filePath);
                        Log.i(tag, "max:" + max + "  filePath:" + filePath);
                        String saveMusic = getIndex(downList.get(0), MusicListActivity.songList) + "," + max + "," + filePath;
                        Log.i(tag, "saveMusic:" +saveMusic);
                        String musiclist = "";
                        if (SharedPrefUtil.getDownedmusic(MusicDownActivity.this) != null) {
                            musiclist = SharedPrefUtil.getDownedmusic(MusicDownActivity.this);
                            Log.i(tag, "musiclist:" +musiclist);
                            SharedPrefUtil.setDowmedMusic(MusicDownActivity.this, musiclist + "@123@" + saveMusic);
                        } else {
                            SharedPrefUtil.setDowmedMusic(MusicDownActivity.this, saveMusic);
                        }
                        downSuecessList.add(downList.get(0));
                        Log.i(tag, "downSuecessList.size:" + downSuecessList.size());
                        Log.i(tag, "getDownedmusic:" + SharedPrefUtil.getDownedmusic(MusicDownActivity.this));
                        Message msgs = new Message();
                        msgs.what = 2;
                        msgs.obj = getIndex(downList.get(0), MusicListActivity.songList);
                        MusicListActivity.instans.musicListHandler.sendMessage(msgs);
                        downList.remove(0);
                        if (downList != null && downList.size() > 0) {// 还有没有下载的 继续下载
                            musicDown();
                        } else {
                            isDowning = false;
                        }
                    } else {
//                        Log.i(tag, "downSize:" + formatParameters(downSize) + "/" + formatParameters(max));
                    }
                    btDowning.setText("下载中（" + downList.size() + ")");
                    btDowned.setText("已下载（" + downSuecessList.size() + ")");
                    updateProgressPartly(0, downSize / 1024, max / 1024, formatParameters(downSize), formatParameters(max));
                    break;
                case 2: // 下载失败
                    Log.i(tag, downList.get(0).getName() + "下载失败");
                    break;
                case 3: // 删除正在下载
                    int position = msg.getData().getInt("position");
                    if (position == 0) {
                        exit();
                    }
                    Log.i(tag, "downList.size():" + downList.size());
                    break;
                case 0: // 显示正在播放歌曲
//                    int positin = (int)msg.obj;
                    if (MusicService.playPosition < downSuecessList.size()) {
                        showOlaying(pasePosition, true);
                        showOlaying(MusicService.playPosition, false);
                        pasePosition = MusicService.playPosition;
                    } else {
                        showOlaying(pasePosition, true);
                        Log.i(tag, "播放歌曲未下载");
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instants = this;
        if (getIntent().getStringExtra("downPosition") != null) {
            Song song = MusicListActivity.songList.get(Integer.parseInt(getIntent().getStringExtra("downPosition")));
            downList.add(song);
            if (MusicListActivity.instans.isNetworkAvailable(MusicDownActivity.this)) {
                musicDown();
            } else {
                Toast.makeText(MusicDownActivity.this, "网络不可用！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //根据长度转成kb(千字节)和mb(兆字节)
    public static String formatParameters(int parameters) {
        BigDecimal filesize = new BigDecimal(parameters);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "M");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "K");
    }

    @AfterViews
    void find() {
        mTvTitle.setText("下载列表");
        mMaterialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);
        mMaterialBackButton.setVisibility(View.VISIBLE);
        musicDowningAdapter = new MusicDowningAdapter(context, downList);
//        btDowning.setEnabled(false);
//        btDowned.setEnabled(true);
        lvMusicDownList.setAdapter(musicDowningAdapter);
        lvMusicDownList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isOnDown) {
                    if (!isExit) {
                        isExit = true;
                        exit();
                        Log.i(tag, "暂停下载的歌曲:" + downList.get(position).getName());
                    } else {
                        isExit = false;
                        musicDown();
                        Log.i(tag, "恢复下载的歌曲:" + downList.get(position).getName());
                    }
                } else {
                    for (int index = 0; index < MusicListActivity.songList.size(); index++) {
                        if (MusicListActivity.songList.get(index).getName().equals(downSuecessList.get(position).getName())) {
                            showOlaying(pasePosition, true);//隐藏刚才点击示的
                            showOlaying(position, false);//显示现在刚才点击示的
                            pasePosition = position;
                            Message msg = new Message();
                            msg.what = 4;
                            msg.obj = index;
                            MusicService.musicHandler.sendMessage(msg);
                            finish();
                        }
                    }
                }
            }
        });
    }

    @Click({R.id.btDowned, R.id.btDowning})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btDowned:
                isOnDown = false;
//                btDowning.setEnabled(true);
//                btDowned.setEnabled(false);
                btDowned.setTextColor(getResources().getColor(R.color.textColorPrimary));
                btDowning.setTextColor(getResources().getColor(R.color.normal_text));
                if (downSuecessList != null) {
                    downSuecessList.clear();
                }
                for (Song song : MusicListActivity.songList) {
                    if (song.isDownFinish()) {
                        Log.i(tag, "song.name:" + song.getName() + "  singer:" + song.getArtist().getName()
                                + "  filePath:" + song.getFilePath() + "  size:" + song.getSize());
                        downSuecessList.add(song);
                    }
                }
                musicDownedAdapter = new MusicDownedAdapter(context, downSuecessList);
                lvMusicDownList.setAdapter(musicDownedAdapter);
                break;
            case R.id.btDowning:
                isOnDown = true;
//                btDowning.setEnabled(false);
//                btDowned.setEnabled(true);
                btDowning.setTextColor(getResources().getColor(R.color.textColorPrimary));
                btDowned.setTextColor(getResources().getColor(R.color.normal_text));
                musicDowningAdapter = new MusicDowningAdapter(context, downList);
                lvMusicDownList.setAdapter(musicDowningAdapter);
                break;
            default:
                break;
        }
    }

    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }


    //下载歌曲
    private void musicDown() {
        isDowning = true;
        for (Song songs : downList) {
            Log.i(tag, "song.name:" + songs.getName());
        }
        String path = downList.get(0).getNetUrl();
        String filename = path.substring(path.lastIndexOf('/') + 1);
        try {
            // URL编码（这里是为了将中文进行URL编码）
            filename = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        path = path.substring(0, path.lastIndexOf("/") + 1) + filename;
        filePath = sdPath + filename;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File savDir = new File(sdPath);
            download(path, savDir);
        } else {
            Toast.makeText(getApplicationContext(),
                    "没有sd卡", Toast.LENGTH_LONG).show();
        }
    }

    private DownloadTask task;

    private void exit() {
        if (task != null)
            task.exit();
    }

    private void download(String path, File savDir) {
        task = new DownloadTask(path, savDir);
        new Thread(task).start();
    }

    /**
     * UI控件画面的重绘(更新)是由主线程负责处理的，如果在子线程中更新UI控件的值，更新后的值不会重绘到屏幕上
     * 一定要在主线程里更新UI控件的值，这样才能在屏幕上显示出来，不能在子线程中更新UI控件的值
     */
    private final class DownloadTask implements Runnable {
        private String path;
        private File saveDir;
        private FileDownloader loader;

        public DownloadTask(String path, File saveDir) {
            this.path = path;
            this.saveDir = saveDir;
        }

        /**
         * 退出下载
         */
        public void exit() {
            if (loader != null)
                loader.exit();
        }

        DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
            @Override
            public void onDownloadSize(int size) {
                Message msg = new Message();
                msg.what = 1;
                msg.getData().putInt("size", size);
                handler.sendMessage(msg);
                downSize = size;
            }
        };

        public void run() {
            try {
                // 实例化一个文件下载器
                loader = new FileDownloader(getApplicationContext(), path,
                        saveDir, 3);
                // 设置进度条最大值
                max = loader.getFileSize();
                loader.download(downloadProgressListener);
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendMessage(handler.obtainMessage(2)); // 发送一条空消息对象
            }
        }
    }

    //更新进度条
    private void updateProgressPartly(int position, int progress, int max, String tvDowning, String tvDownSize) {
        int firstVisiblePosition = lvMusicDownList.getFirstVisiblePosition();
        int lastVisiblePosition = lvMusicDownList.getLastVisiblePosition();
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            View view = lvMusicDownList.getChildAt(position - firstVisiblePosition);
            if (view.getTag() instanceof MusicDowningAdapter.ViewHolder) {
                MusicDowningAdapter.ViewHolder vh = (MusicDowningAdapter.ViewHolder) view.getTag();
                vh.tvMusicState.setVisibility(View.GONE);
                vh.llDowning.setVisibility(View.VISIBLE);
                vh.sbDowning.setProgress(progress);
                vh.sbDowning.setMax(max);
                vh.tvDowning.setText(tvDowning);
                vh.tvDowmSize.setText(tvDownSize);
            }
        }
    }

    //显示播放中
    private void showOlaying(int position, boolean isShow) {
        int firstVisiblePosition = lvMusicDownList.getFirstVisiblePosition();
        int lastVisiblePosition = lvMusicDownList.getLastVisiblePosition();
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            View view = lvMusicDownList.getChildAt(position - firstVisiblePosition);
            if (view.getTag() instanceof MusicDownedAdapter.ViewHolder) {
                MusicDownedAdapter.ViewHolder vh = (MusicDownedAdapter.ViewHolder) view.getTag();
                if (isShow) {
                    vh.llPlaying.setVisibility(View.GONE);
                } else {
                    vh.llPlaying.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    //获取音乐在list的索引
    private int getIndex(Song song, List<Song> list) {
        for (int index = 0; index < list.size(); index++) {
            if (list.get(index).getName().equals(song.getName())) {
                return index;
            }
        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOnDown = true;
    }
}
