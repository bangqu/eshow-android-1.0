package cn.org.eshow.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;

import cn.org.eshow.R;
import cn.org.eshow.bean.Song;

@EActivity(R.layout.activity_musicplay)
public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    public static PlayActivity instants;
    private String tag = "PlayActivity";
    @ViewById(R.id.tvsTile)
    TextView tvsTile;//播放音乐名字显示
    @ViewById(R.id.tvMusicStartTime)
    TextView tvMusicStartTime;
    @ViewById(R.id.tvMusicEndTime)
    TextView tvMusicEndTime;
    @ViewById(R.id.llMusicPlayBack)
    LinearLayout llMusicPlayBack;
    @ViewById(R.id.llPlayBackGround)
    LinearLayout llPlayBackGround;
    @ViewById(R.id.llMusicDown)
    LinearLayout llMusicDown;
    @ViewById(R.id.bottom_btn_previous)
    ImageView bottom_btn_previous;
    @ViewById(R.id.bottom_btn_play)
    ImageView bottom_btn_play;
    @ViewById(R.id.bottom_btn_next)
    ImageView bottom_btn_next;
    @ViewById(R.id.seekbar)
    SeekBar seekbar;
    @ViewById(R.id.ivSingerPhoto)
    ImageView ivSingerPhoto;
    @ViewById(R.id.ivSinger)
    ImageView ivSinger;
    @ViewById(R.id.tvsPlaySinger)
    TextView tvsPlaySinger;
    private boolean isFirstPlay = true;//还没有播放
    private int musicTime;//正在播放音乐总时长
    private String musicName;//正在播放音乐名字
    private String singer;
    private int times = 0;//进度条进度
    private Animation operatingAnim;
    private boolean isPlay = true;
    private int playingPosition;

    public NotificationManager mNotificationManager;
    /**
     * Notification 的ID
     */
    int notifyId = 101;
//    /** 是否在播放*/
//    public boolean isPlay = false;
    /**
     * 通知栏按钮广播
     */
    public ButtonBroadcastReceiver bReceiver;
    /**
     * 通知栏按钮点击事件对应的ACTION
     */
    public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instants = this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initButtonReceiver();
        MusicService.musicHandler.sendEmptyMessage(0);
    }

    public Handler myHandler = new Handler() {//接收消息，做处理
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (MusicService.musicIsPlaying()) {
                        if (isPlay) {
                            isPlay = false;
                            startRotate(ivSinger);
                            startRotate(ivSingerPhoto);
                            Log.i(tag, "开始旋转");
                        }
                        playingPosition = Integer.parseInt(msg.obj.toString());
                        musicName = MusicListActivity.songList.get(playingPosition).getName();
                        musicTime = MusicService.mediaPlayer.getDuration();
                        singer = MusicListActivity.songList.get(playingPosition).getArtist().getName();
                        if (tvsTile.getText().equals(musicName)) {

                        } else {
                            bottom_btn_play.setBackgroundResource(R.drawable.ic_music_pause);
                            clearAllNotify();
                            showButtonNotify(singer, musicName);
                            seekbar.setMax(MusicService.mediaPlayer.getDuration());//设置进度条
                            tvsTile.setText(musicName);
                            tvsPlaySinger.setText(singer);
                            tvMusicEndTime.setText(getTimes(musicTime, false));
                        }
                        seekbar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
                        tvMusicStartTime.setText(getTimes(MusicService.mediaPlayer.getCurrentPosition(), false));
                    }
                    break;
            }
        }
    };

    @AfterViews
    void init() {
        tvsTile.setFocusable(true);
        tvsTile.requestFocus();
        seekbar.incrementProgressBy(-1);
        seekbar.incrementSecondaryProgressBy(1);
        seekbar.setOnSeekBarChangeListener(new MySeekbar());
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.musicplay_miptip);
        llPlayBackGround.setBackgroundResource(R.drawable.liudehua);
        ivSinger.setBackgroundResource(R.drawable.liudehua);
        Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.liudehua);
        llPlayBackGround.setBackgroundDrawable(BlurImageview.BlurImages(rawBitmap, PlayActivity.this));
    }

    @Click({R.id.bottom_btn_previous, R.id.bottom_btn_play, R.id.bottom_btn_next, R.id.llMusicDown, R.id.llMusicPlayBack})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_btn_previous://上一曲
                bottom_btn_play.setBackgroundResource(R.drawable.ic_music_pause);
                MusicService.musicHandler.sendEmptyMessage(6);
                break;
            case R.id.bottom_btn_play:
                if (!MusicService.musicIsPlaying() && isFirstPlay) {//如果音乐没有播放，点击就从第一个播放
                    isFirstPlay = false;
                    Log.i(tag, "如果音乐没有播放，点击就从第一个播放" + seekbar.getProgress());
                    bottom_btn_play.setBackgroundResource(R.drawable.ic_music_pause);
                } else {
                    if (isFirstPlay) {
                        isFirstPlay = false;//已经开始播放
                    }
                    if (!MusicService.musicIsPlaying() && !isFirstPlay) {//恢复播放
                        bottom_btn_play.setBackgroundResource(R.drawable.ic_music_pause);
                        MusicService.musicHandler.sendEmptyMessage(3);
                        Log.i(tag, "resumeMusic()" + seekbar.getProgress());
                    } else if (MusicService.musicIsPlaying() && !isFirstPlay) {//暂停播放
                        bottom_btn_play.setBackgroundResource(R.drawable.ic_music_play);
                        MusicService.musicHandler.sendEmptyMessage(2);
                        Log.i(tag, "pauseMusic()" + seekbar.getProgress());
                    }
                }
                break;
            case R.id.bottom_btn_next://下一曲
                MusicService.musicHandler.sendEmptyMessage(7);
                bottom_btn_play.setBackgroundResource(R.drawable.ic_music_pause);
                break;
            case R.id.llMusicPlayBack://返回列表
                finish();
                break;
            case R.id.llMusicDown://下载本歌曲

                if (MusicListActivity.instans.songList.get(playingPosition).isDownFinish()) {//是否下载过
                    Toast.makeText(this, "歌曲已经下载过", Toast.LENGTH_SHORT).show();
                    Intent intent = MusicDownActivity_.intent(this).get();
                    startActivity(intent);
                } else {
                    if (MusicDownActivity.downList!=null){
                        for (Song song:MusicDownActivity.downList){
                            if (song.getName().equals(MusicListActivity.instans.songList.get(playingPosition).getName())){
                                Toast.makeText(this, "歌曲已经在下载列表中", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (MusicListActivity.instans.isNetworkAvailable(PlayActivity.this)) {//是否有网络
                        Intent intent = MusicDownActivity_.intent(this).get();
                        intent.putExtra("downPosition", playingPosition + "");
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }

    }

    //图片旋转
    private void mipTip(ImageView iv) {
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {

            iv.startAnimation(operatingAnim);
        }
    }

    //给musicservice发消息
    private void sendToMusicService(Song song, int msg) {
        Message musicMsg = new Message();
        Bundle downBundle = new Bundle();
        downBundle.putSerializable("musicName", (Serializable) song);
        musicMsg.what = msg;
        musicMsg.setData(downBundle);
        MusicService.musicHandler.sendMessage(musicMsg);
    }

    //获取音乐名字在namelist的索引
    public int getIndex(Song song) {
        for (int index = 0; index < MusicListActivity.songList.size(); index++) {
            if (song.equals(MusicListActivity.songList.get(index))) {
                Log.i(tag, "index：" + index);
                return index;
            }
        }
        return 0;
    }

    //进度条处理
    class MySeekbar implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            times = seekbar.getProgress();
            tvMusicStartTime.setText(getTimes(times, false));
            Log.i(tag, "seekbar.getProgress():" + seekbar.getProgress());
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            times = seekbar.getProgress();
            tvMusicStartTime.setText(getTimes(times, false));
            MusicService.mediaPlayer.seekTo(seekbar.getProgress());
            Log.i(tag, "seekbar.getProgress():" + seekbar.getProgress());
        }

    }

    public String getTimes(int time, boolean isSecond) {//毫秒需/1000
        if (!isSecond) {
            time /= 1000;
        }
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    @Override
    protected void onDestroy() {
        Log.i(tag, "onDestroy()");
        isFirstPlay = true;
        isPlay = true;
        clearAllNotify();
        if (operatingAnim != null) {
            ivSingerPhoto.clearAnimation();
        }
        super.onDestroy();
    }

    protected void startRotate(ImageView imageView) {
        LinearInterpolator lin = new LinearInterpolator();
        RotateAnimation ra = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(15000);
        ra.setRepeatCount(-1);
        ra.setInterpolator(lin);
        imageView.startAnimation(ra);
    }

    /**
     * 音乐播放广播
     */
    public void initButtonReceiver() {
        bReceiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        registerReceiver(bReceiver, intentFilter);
    }

    public final static String INTENT_BUTTONID_TAG = "ButtonId";
    /**
     * 上一首 按钮点击 ID
     */
    public final static int BUTTON_PREV_ID = 1;
    /**
     * 播放/暂停 按钮点击 ID
     */
    public final static int BUTTON_PALY_ID = 2;
    /**
     * 下一首 按钮点击 ID
     */
    public final static int BUTTON_NEXT_ID = 3;

    /**
     * 广播监听按钮点击时间
     */
    public class ButtonBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(ACTION_BUTTON)) {
                //通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                switch (buttonId) {
                    case BUTTON_PREV_ID://上一曲
                        bottom_btn_play.setBackgroundResource(R.drawable.ic_music_pause);
                        MusicService.musicHandler.sendEmptyMessage(6);
                        Log.i(tag, "广播点击上一曲");
                        break;
                    case BUTTON_PALY_ID://暂停播放
                            if (!MusicService.musicIsPlaying()) {//恢复播放
                                bottom_btn_play.setBackgroundResource(R.drawable.ic_music_pause);
                                Log.i(tag, "广播点击暂停");
                                MusicService.musicHandler.sendEmptyMessage(3);
                            } else if (MusicService.musicIsPlaying()) {//暂停播放
                                bottom_btn_play.setBackgroundResource(R.drawable.ic_music_play);
                                Log.i(tag, "广播点击恢复");
                                MusicService.musicHandler.sendEmptyMessage(2);
                            }
                        break;
                    case BUTTON_NEXT_ID://下一曲
                        Log.i(tag, "广播点击下一曲");
                        MusicService.musicHandler.sendEmptyMessage(7);
                        bottom_btn_play.setBackgroundResource(R.drawable.ic_music_pause);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 清除当前创建的通知栏
     */
    public void clearNotify(int notifyId) {
        mNotificationManager.cancel(notifyId);//删除一个特定的通知ID对应的通知
    }

    /**
     * 清除所有通知栏
     */
    public void clearAllNotify() {
        mNotificationManager.cancelAll();// 删除你发的所有通知
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    /**
     * 带按钮的通知栏
     */
    public void showButtonNotify(String singer, String songName) {
        boolean isNotify = true;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.view_music_nofications);
        mRemoteViews.setImageViewResource(R.id.custom_song_icon, R.drawable.liudehua);
        mRemoteViews.setTextViewText(R.id.tv_custom_song_singer, singer);
        mRemoteViews.setTextViewText(R.id.tv_custom_song_name, songName);
        mRemoteViews.setViewVisibility(R.id.ll_custom_button, View.VISIBLE);
        if (isNotify){
            isNotify = false;
            mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.drawable.ic_music_pause);
        }else{
            isNotify = true;
            mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.drawable.ic_music_play);
        }

        //点击的事件处理
        Intent buttonIntent = new Intent(ACTION_BUTTON);
        /* 上一首按钮 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PREV_ID);
        //这里加了广播，所及INTENT的必须用getBroadcast方法
        PendingIntent intent_prev = PendingIntent.getBroadcast(this, 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_prev, intent_prev);
        /* 播放/暂停  按钮 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
        PendingIntent intent_paly = PendingIntent.getBroadcast(this, 2, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_play, intent_paly);
		/* 下一首 按钮  */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_NEXT_ID);
        PendingIntent intent_next = PendingIntent.getBroadcast(this, 3, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_next, intent_next);
        mBuilder.setContent(mRemoteViews)
                .setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("正在播放")
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(true)
                .setSmallIcon(R.drawable.liudehua);
        Notification notify = mBuilder.build();
        notify.flags = Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(200, notify);
    }

}
