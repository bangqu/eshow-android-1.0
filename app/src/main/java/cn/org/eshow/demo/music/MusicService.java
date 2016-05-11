package cn.org.eshow.demo.music;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import org.androidannotations.annotations.EService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import cn.org.eshow.demo.bean.Song;
import cn.org.eshow.demo.common.SharedPrefUtil;

/**
 * 音乐疗法播放音乐的Service
 * Created by daikting on 15/12/24.
 */
@EService
public class MusicService extends Service {
    private static final String tag = "PlayActivity-MS";
    private Context mContext = MusicService.this;
    public static List<Song> playMusicList = new ArrayList<>(); //播放列表；
    public static MediaPlayer mediaPlayer;
    private int pausePosition = 0;//记录暂停的位置
    private int musicTimes = 0;//音乐时长
    private boolean isStart = false;
    private boolean isStop = false;
    public static int playPosition;//播放的索引
    //    public static int amationPosition;//用于列表清除动画  在开始播放时赋值  播放结束时
    public static Handler musicHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
        sendMessageToUi();//发送消息通知界面读取进度
        musicHandler = new Handler() {//接收消息，做处理
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.i(tag, "接受到的消息：" + msg.what);
                switch (msg.what) {
                    case 0://
                        sendMessageToUi();
//                        PlayActivity.instants.myHandler.sendEmptyMessage(1);
                        break;
                    case 1://还没有播放
                        break;
                    case 2://暂停
                        pauseMusic();
                        break;
                    case 3://恢复
                        resumeMusic();
                        break;
                    case 4://开始播放某个歌曲
                        Log.i(tag, "接受到的歌曲名：" + MusicListActivity.songList.get(Integer.parseInt(msg.obj.toString())).getName());
                        if (SharedPrefUtil.getSeltetposition(mContext) != null) {
                            cantralAmation(Integer.parseInt(SharedPrefUtil.getSeltetposition(mContext)), 1, MusicListActivity.instans.musicListHandler);//取消刚才的动画
                        }
                        playPosition = (int) msg.obj;
                        cantralAmation(playPosition, 0, MusicListActivity.instans.musicListHandler);
                        musicPlay((int) msg.obj);
                        Log.i(tag, "开始播放某个歌曲 SharedPrefUtil.getSelectPosition(mContext)：" + SharedPrefUtil.getSeltetposition(mContext));
                        isStart = true;
                        break;
                    case 5://同至获取歌曲信息
                        PlayActivity.instants.myHandler.sendEmptyMessage(1);
                        break;
                    case 6://播放上一曲
                        if (SharedPrefUtil.getSeltetposition(mContext) != null) {
                            cantralAmation(Integer.parseInt(SharedPrefUtil.getSeltetposition(mContext)), 1, MusicListActivity.instans.musicListHandler);//取消刚才的动画
                        }
                        if (playPosition == 0) {
                            playPosition = MusicListActivity.songList.size() - 1;
                        } else {
                            playPosition = playPosition - 1;
                        }
                        cantralAmation(playPosition, 0, MusicListActivity.instans.musicListHandler);//显示播放的动画
                        Log.i(tag, "播放上一首歌曲");
                        musicPlay(playPosition);//当前歌曲已播放完毕，需要播放下一首歌曲
                        break;
                    case 7://播放下一曲
                        if (SharedPrefUtil.getSeltetposition(mContext) != null) {
                            cantralAmation(Integer.parseInt(SharedPrefUtil.getSeltetposition(mContext)), 1, MusicListActivity.instans.musicListHandler);//取消刚才的动画
                        }
                        if (playPosition + 1 >= MusicListActivity.songList.size()) {
                            playPosition = 0;
                        } else {
                            playPosition = playPosition + 1;
                        }
                        cantralAmation(playPosition, 0, MusicListActivity.instans.musicListHandler);//显示播放的动画
                        if (MusicDownActivity.instants != null) {
                            MusicDownActivity.instants.handler.sendEmptyMessage(0);
                        }
                        Log.i(tag, "播放下一首歌曲");
                        musicPlay(playPosition);
                        break;
                    case 8://播放音乐
//                        musicPlay(Integer.parseInt(msg.obj.toString()));
                        break;
                }
            }
        };
    }

    public static void cantralAmation(int cancralPosition, int what, Handler handler) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = cancralPosition + "";
        handler.sendMessage(msg);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(tag, "onStart");
        takeWakeLock();
        takeWifiLock();
    }

    @Override
    public void onDestroy() {
        Log.d(tag, "onDestroy");
        mediaIsPlay = false;
        isStart = false;//关闭发往界面消息
        isStop = true;//停止线程
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        releaseWakeLock();
        releaseWifiLock();
        super.onDestroy();
    }

    // 实例化自定义的Binder类
    private final IBinder mBinder = new MusicBind();

    /**
     * 自定义的Binder类，这个是一个内部类，所以可以知道其外围类的对象，通过这个类，让Activity知道其Service的对象
     */
    public class MusicBind extends Binder {
        public MusicService getService() {
            // 返回Activity所关联的Service对象，这样在Activity里，就可调用Service里的一些公用方法和公用属性
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    boolean manControlIsPlay = false;//当前人工控制正在播放，初始值 设置为 false

    /**
     * 正式播放治疗音乐
     */
    boolean mediaIsPlay = false;//mediaPlayer是否开始播放歌曲，初始值为 false
    private String nowPlayName = "";//当前播放歌曲名，初始值 为 “”

    public void musicPlay(int position) {
//        Log.i(tag, "正式播放音乐");
        if (MusicListActivity.songList != null) {
//            Log.i(tag, " 调理音乐列表大小：MusicListActivity.songList.size()＝" + MusicListActivity.songList.size());
        }
        if (MusicListActivity.songList != null && MusicListActivity.songList.size() > 0) {//&& playMusicList != null && playMusicList.size() > 0
            String path = "";
            Song song = MusicListActivity.songList.get(position);    //取得点击的歌曲
            if (song.getFilePath() != null) {
                path = song.getFilePath();//当前歌曲的  存放路径
            } else {
                path = song.getNetUrl();//当前歌曲的  网络路径
            }
            nowPlayName = song.getName();//重置  当前播放歌曲名
//            Log.i(tag, "当前播放音乐名：" + nowPlayName);
            try {
                Log.i(tag, " 当前播放音乐路径：" + path);
                mediaPlayer.reset();//播放器重置，后 ，播放当前歌曲
                if (path != null) {//路径zhengque，直接去播放该音乐
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.setOnCompletionListener(treatMusicCompletionListener);
                    mediaPlayer.prepare();
//                    Log.i(tag, "当前音乐：" + nowPlayName + "--" + "时长:" + musicTimes);
                    mediaPlayer.start();
                    SharedPrefUtil.setSelectPosition(mContext, position + "");
                    Log.i(tag, "播放歌曲 SharedPrefUtil.getSelectPosition(mContext)：" + SharedPrefUtil.getSeltetposition(mContext));
                    mediaIsPlay = true;//mediaplayer 从调理列表 开始播放音乐了，置 true
                } else {
//                    Log.i(tag, "当前要播放的歌曲不存在，从播放列表中删除该歌曲信息");
                    musicHandler.sendEmptyMessage(7);//通知播放下一曲
                    mediaIsPlay = false;//音乐文件不存在，mediaplayer 没有播放音乐，置 false
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i(tag, "FileNotFoundException,未找到当前要播放的歌曲");
                mediaIsPlay = false;//。。。，mediaplayer 没有播放音乐，置 false

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Log.i(tag, "IllegalArgumentException异常情况");
                mediaIsPlay = false;

            } catch (SecurityException e) {
                e.printStackTrace();
                Log.i(tag, "SecurityException异常情况");
                mediaIsPlay = false;

            } catch (IllegalStateException e) {
                e.printStackTrace();
                Log.i(tag, "IllegalStateException异常情况");
                mediaPlayer = null;
                mediaPlayer = new MediaPlayer();
                mediaIsPlay = false;

            } catch (IOException e) {
                e.printStackTrace();
                Log.i(tag, "IOException异常情况");
                mediaIsPlay = false;
            }
//            catch (Exception e) {
//                e.printStackTrace();//????????????????? 重新播放，播放下一曲
//                Log.i(tag, "Exception异常情况");
//                mediaIsPlay = false;
//            }
            Log.i(tag, "mediaIsPlay is " + String.valueOf(mediaIsPlay));
            if (!mediaIsPlay) {
                mediaIsPlay = false;//mediaplayer 没有播放音乐，置 false
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                } else {
                    mediaPlayer.release();
                }
            }
        } else {
            Log.i(tag, "没有可播放的音乐，重新开始");
            try {
                mediaPlayer.release();
                mediaPlayer.reset();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(tag, "没有可播放的音乐，Exception");
                mediaPlayer = null;
                mediaPlayer = new MediaPlayer();
            }
//            praperTreatMusic();//重新  准备开始治疗音乐
        }
    }

    //更新播放进度条
    private void sendMessageToUi() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isStart) {
                        if (PlayActivity.instants.myHandler != null) {
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = playPosition + "";
                            PlayActivity.instants.myHandler.sendMessage(msg);
                        } else {
                            Log.i(tag, "PlayActivity.instants.myHandler = null");
                        }
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    /*
     * 监听音乐是否在播放。
     * */
    public static boolean musicIsPlaying() {
        if (mediaPlayer == null) {
            return false;
        } else {
            try {
                if (mediaPlayer.isPlaying()) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(tag, "判断musicIsPlaying时 异常");
                return false;
            }
        }
    }

    /**
     * 暂停音乐,暂停播放当前歌曲
     */
    public void pauseMusic() {
        manControlIsPlay = false;//人为点击  暂停播放  音乐

        Log.d(tag, "pauseMusic（）");
        try {
            if (mediaPlayer.isPlaying()) {
                pausePosition = mediaPlayer.getCurrentPosition();
                Log.d(tag, "pausePosition:" + pausePosition);
                mediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复播放被暂停的歌曲
     */
    public void resumeMusic() {
        manControlIsPlay = true;//人为点击 继续播放  音乐
        Log.d(tag, "resumeMusic()");
        try {
            if (!mediaPlayer.isPlaying()) {
                Log.d(tag, "resumeMusic:" + pausePosition);
                mediaPlayer.start();
                mediaPlayer.seekTo(pausePosition);
                pausePosition = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
        }

    }


    /**
     * 音乐播放完成
     */
    MediaPlayer.OnCompletionListener treatMusicCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Log.i(tag, "treatMusicCompletionListener音乐播放完成");
            if (onTreatMusicEndListener == null) {
                musicHandler.sendEmptyMessage(7);//通知播放下一曲
            } else {
                mediaPlayer.release();
            }
        }
    };

    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;

    /**
     * 保持cpu不休眠
     */
    private void takeWakeLock() {
        //创建PowerManager对象
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //保持cpu一直运行，不管屏幕是否黑屏
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "com.wunyin.treatMusic");
        wakeLock.acquire();

    }

    OnTreatMusicEndListener onTreatMusicEndListener;

    /**
     * 设置理疗音乐结束的回调
     * 设置回调后会在当前播放的音乐结束后播放理疗结束音乐再进行回调
     *
     * @param onTreatMusicEndListener
     */
    public void setOnTreatMusicEndListener(OnTreatMusicEndListener onTreatMusicEndListener) {
        this.onTreatMusicEndListener = onTreatMusicEndListener;
    }

    public interface OnTreatMusicEndListener {
        void onTreatMusicEndListener();
    }

    /**
     * 释放wifi休眠
     */
    private void releaseWifiLock() {
//        Log.d(tag, "Releasing wifi lock");
        if (wifiLock != null) {
            wifiLock.release();
            wifiLock = null;
        }
    }

    WifiManager.WifiLock wifiLock;

    /**
     * 保持wifi不休眠
     */
    private void takeWifiLock() {
//        Log.d(tag, "Taking wifi lock");
        if (wifiLock == null) {
            WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            wifiLock = manager.createWifiLock("com.wuyin.treatMusic");
            wifiLock.setReferenceCounted(false);
        }
        wifiLock.acquire();
    }

    /**
     * 释放cpu休眠
     */
    private void releaseWakeLock() {
        if (wakeLock != null) {
            wakeLock.release();
        }
    }
}