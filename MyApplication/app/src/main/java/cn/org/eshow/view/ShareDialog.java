package cn.org.eshow.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.umeng.socialize.Config;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import cn.org.eshow.R;
import cn.org.eshow.third.UMShared;
import cn.org.eshow_structure.util.ESLogUtil;
import cn.org.eshow_structure.util.ESToastUtil;
import cn.org.eshow_structure.util.ESViewUtil;


/**
 * 分享对话框
 *
 * 请在调用本对话框的页面重写onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    UMShareAPI.get(this).onActivityResult( requestCode, resultCode, data);
    }
 */
public class ShareDialog extends Dialog {
    LinearLayout llWeiXin;
    LinearLayout llWeiXinCircle;
    LinearLayout llQZone;
    LinearLayout llQQ;
    LinearLayout llSina;
    LinearLayout llCopy;
    Context context;

    String shareTitle = "EShow开源框架";
    String shareText = "简单、快捷、易用。集合文件下载，地图定位，音乐播放，用户聊天等功能框架。";
    String imgUrl ="http://77wdb6.com2.z0.glb.qiniucdn.com/icon.png";
    String showUrl = "http://www.eshow.org.cn";
    ShareCallBackListener shareCallBackListener;
    private UMShareListener umShareListener;

    public ShareDialog(Context context) {
        super(context, R.style.confirm_dialog);
        this.context = context;
    }

    public ShareDialog(Context context,UMShareListener umShareListener) {
        super(context, R.style.confirm_dialog);
        ESLogUtil.i(context, "ShareDialog 构造器");
        this.context = context;
        this.umShareListener=umShareListener;
    }
    /**
     * 指定分享内容的构造
     * @param context
     * @param shareTitle 分享出去的标题
     * @param shareText  分享出去的文字
     * @param imgUrl 分享出去显示图标的地址
     * @param showUrl 分享出去点击跳转的页面
     */
    public ShareDialog(Context context,String shareTitle,String shareText,String imgUrl,String showUrl) {
        super(context, R.style.confirm_dialog);
        this.context = context;
        this.shareTitle = shareTitle;
        this.shareText = shareText;
        this.imgUrl = imgUrl;
        this.showUrl = showUrl;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        ESViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        getWindow().setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        getWindow().setWindowAnimations(R.style.ShowShareDialogAni);  //添加动画
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        llWeiXin = (LinearLayout) findViewById(R.id.llWeiXin);
        llWeiXin.setOnClickListener(clickListener);
        llWeiXinCircle = (LinearLayout) findViewById(R.id.llWeiXinCircle);
        llWeiXinCircle.setOnClickListener(clickListener);
        llQZone = (LinearLayout) findViewById(R.id.llQZone);
        llQZone.setOnClickListener(clickListener);
        llQQ = (LinearLayout) findViewById(R.id.llQQ);
        llQQ.setOnClickListener(clickListener);
        llSina = (LinearLayout) findViewById(R.id.llSina);
        llSina.setOnClickListener(clickListener);
        llCopy = (LinearLayout) findViewById(R.id.llCopy);
        llCopy.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            UMShared umShared = new UMShared();
//            UMShareListener umShareListener = new UMShareListener() {
//                @Override
//                public void onResult(SHARE_MEDIA share_media) {
//                    if(shareCallBackListener != null){
//                        shareCallBackListener.onResult(share_media);
//                    }
//                }
//
//                @Override
//                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//                    if(shareCallBackListener != null){
//                        shareCallBackListener.onError(share_media, throwable);
//                    }
//                }
//
//                @Override
//                public void onCancel(SHARE_MEDIA share_media) {
//                    if(shareCallBackListener != null){
//                        shareCallBackListener.onCancel(share_media);
//                    }
//                }
//            };
            switch (v.getId()){
                case R.id.llWeiXin:
                    Config.dialog = ProgressDialog.show(context, "提示", "正在请求跳转....");
                    umShared.onWeiXinShare(context, shareTitle, shareText, imgUrl, showUrl, umShareListener);
                    break;
                case R.id.llWeiXinCircle:
                    Config.dialog = ProgressDialog.show(context, "提示", "正在请求跳转....");
                    umShared.onWeiXinCircleShare(context, shareTitle, shareText, imgUrl, showUrl, umShareListener);

                    break;
                case R.id.llQQ:
                    Config.dialog = ProgressDialog.show(context, "提示", "正在请求跳转....");
                    umShared.onQQShare(context, shareTitle, shareText, imgUrl, showUrl, umShareListener);

                    break;
                case R.id.llQZone:
                    Config.dialog = ProgressDialog.show(context, "提示", "正在请求跳转....");
                    umShared.onQZoneShare(context, shareTitle, shareText, imgUrl, showUrl, umShareListener);

                    break;
                case R.id.llSina:
                    ESToastUtil.showToast(context,"新浪分享正在开发中 ...");
                    //Config.dialog = ProgressDialog.show(context, "提示", "正在请求跳转....");
                    //umShared.onSinaShare(context, shareText, showUrl, umShareListener);

                    break;
                case R.id.llCopy:
                    ClipboardManager myClipboard = (ClipboardManager)context.getSystemService(Activity.CLIPBOARD_SERVICE);
                    ClipData myClip = ClipData.newPlainText("text", showUrl);
                    myClipboard.setPrimaryClip(myClip);

                    ESToastUtil.showToast(context,"复制链接成功！");
                    break;
            }
            ShareDialog.this.dismiss();
        }
    };

    /**
     * 设置分享回调监听
     * @param shareCallBackListener
     */
    public void setShareCallBackListener(ShareCallBackListener shareCallBackListener){
        this.shareCallBackListener = shareCallBackListener;
    }

    interface ShareCallBackListener{
        void onResult(SHARE_MEDIA var1);

        void onError(SHARE_MEDIA var1, Throwable var2);

        void onCancel(SHARE_MEDIA var1);
    }
}
