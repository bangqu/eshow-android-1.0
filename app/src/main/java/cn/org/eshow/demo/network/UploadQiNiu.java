package cn.org.eshow.demo.network;

import android.content.Context;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import cn.org.eshow.demo.common.SharedPrefUtil;
import cn.org.eshow.framwork.util.AbLogUtil;

/**
 * 上传文件到七牛上
 * Created by daikting on 15/10/19.
 */
public class UploadQiNiu {
    /**
     * 七牛空间上传地址
     */
    private static final String QINIU_URL = "http://77wdb6.com2.z0.glb.qiniucdn.com/";
    private Context mContext;
    private String fileUrl;
    private UploadListener uploadListener;
    /**
     *
     * @param context
     * @param fileUrl 本地文件存放路径
     */
    public UploadQiNiu(Context context, String fileUrl,UploadListener uploadListener) {
        this.mContext = context;
        this.fileUrl = fileUrl;
        this.uploadListener = uploadListener;

        NetworkInterface.getQiniuToken(context,getTokenResonseListener);
    }


    /**
     * 获取七牛token回调
     */
    ESResponseListener getTokenResonseListener = new ESResponseListener(mContext) {
        @Override
        public void onBQSucess( String bqMsg, JSONObject resultJson) {
            AbLogUtil.d(mContext,"七牛token:"+bqMsg);
            SharedPrefUtil.setQiniuToken(mContext, bqMsg);
            NetworkInterface.getQiniuKey(mContext,getKeyResonseListener);
        }

        @Override
        public void onBQNoData() {

        }

        @Override
        public void onBQNotify(String bqMsg) {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            AbLogUtil.d(mContext,"Token获取失败");

            if(uploadListener != null){
                uploadListener.onFailed("Token获取失败！");
            }
        }
    };


    /**
     * 获取七牛key的回调
     */
    ESResponseListener getKeyResonseListener = new ESResponseListener(mContext) {
        @Override
        public void onBQSucess(String bqMsg, JSONObject resultJson) {
            SharedPrefUtil.setQiniuKey(mContext,bqMsg);
            String token = SharedPrefUtil.getQiniuToken(mContext);
            String key = SharedPrefUtil.getQiniuKey(mContext);
            AbLogUtil.d(mContext, "Qiniu token:" + token);
            AbLogUtil.d(mContext, "Qiniu key:" + key);
            uploadImg(token,key,fileUrl);
        }

        @Override
        public void onBQNoData() {

        }

        @Override
        public void onBQNotify(String bqMsg) {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onFailure(int statusCode, String content, Throwable error) {
            if(uploadListener != null){
                uploadListener.onFailed("Key获取失败！");
            }
        }
    };

    /**
     * 上传图片到七牛
     * @param token 获得七牛上传凭证uploadToken
     * @param key 七牛时间凭证
     * @param fileUrl 手机SD卡文件存放路径
     */
    private void uploadImg(final String token, final String key, final String fileUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (token != null) {
                    AbLogUtil.d(mContext, "Qiniu token:" + fileUrl);
                    UploadManager uploadManager = new UploadManager();
                    uploadManager.put(fileUrl, key, token,
                            new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    String url = QINIU_URL + key;
                                    if(uploadListener != null){
                                        uploadListener.onSucess(url);
                                    }

                                }
                            }, null);
                } else {
                    if(uploadListener != null){
                        uploadListener.onFailed("未知原因上传失败！");
                    }
                }
            }
        }).start();
    }

    public interface UploadListener{
        void onSucess(String url);

        void onFailed(String msg);
    }
}
