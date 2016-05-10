package cn.org.eshow.third;

import android.app.Activity;
import android.content.Context;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * 友盟分享封装
 * Created by daikting on 16/3/28.
 */
public class UMShared {

    public void openShare(Context context, String title,String text,String imgUrl,String showUrl,UMShareListener umShareListener){
        ShareAction shareAction = new ShareAction((Activity)context);
        shareAction.setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
        shareAction.withTitle(title);
        shareAction.withText(text);
        if(imgUrl != null){
            UMImage umImage = new UMImage(context,imgUrl);
            shareAction.withMedia(umImage);
        }
        shareAction.withTargetUrl(showUrl);
        shareAction.setCallback(umShareListener);
        shareAction.open();
    }
    /**
     * 分享到qq
     * @param context
     * @param title
     * @param text
     * @param imgUrl
     * @param umShareListener
     */
    public void onQQShare(Context context, String title,String text,String imgUrl,String showUrl,UMShareListener umShareListener){
        ShareAction shareAction = new ShareAction((Activity)context);
        shareAction.setPlatform(SHARE_MEDIA.QQ);
        shareAction.withTitle(title);
        shareAction.withText(text);
        if(imgUrl != null){
            UMImage umImage = new UMImage(context,imgUrl);
            shareAction.withMedia(umImage);
        }
        shareAction.withTargetUrl(showUrl);
        shareAction.setCallback(umShareListener);
        shareAction.share();
    }
    /**
     * 分享到qq空间
     * @param context
     * @param title
     * @param text
     * @param imgUrl
     * @param umShareListener
     */
    public void onQZoneShare(Context context, String title,String text,String imgUrl,String showUrl,UMShareListener umShareListener){
        ShareAction shareAction = new ShareAction((Activity)context);
        shareAction.setPlatform(SHARE_MEDIA.QZONE);
        shareAction.withTitle(title);
        shareAction.withText(text);
        if(imgUrl != null){
            UMImage umImage = new UMImage(context,imgUrl);
            shareAction.withMedia(umImage);
        }
        shareAction.withTargetUrl(showUrl);
        shareAction.setCallback(umShareListener);
        shareAction.share();
    }

    /**
     * 分享到微信
     * @param context
     * @param title
     * @param text
     * @param imgUrl
     * @param umShareListener
     */
    public void onWeiXinShare(Context context, String title,String text,String imgUrl,String showUrl,UMShareListener umShareListener){
        ShareAction shareAction = new ShareAction((Activity)context);
        shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
        shareAction.withTitle(title);
        shareAction.withText(text);
        if(imgUrl != null){
            UMImage umImage = new UMImage(context,imgUrl);
            shareAction.withMedia(umImage);
        }
        shareAction.withTargetUrl(showUrl);
        shareAction.setCallback(umShareListener);
        shareAction.share();
    }

    /**
     * 分享到微信朋友圈
     * @param context
     * @param title
     * @param text
     * @param imgUrl
     * @param umShareListener
     */
    public void onWeiXinCircleShare(Context context, String title,String text,String imgUrl,String showUrl,UMShareListener umShareListener){
        ShareAction shareAction = new ShareAction((Activity)context);
        shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
        shareAction.withTitle(title);
        shareAction.withText(text);
        if(imgUrl != null){
            UMImage umImage = new UMImage(context,imgUrl);
            shareAction.withMedia(umImage);
        }
        shareAction.withTargetUrl(showUrl);
        shareAction.setCallback(umShareListener);
        shareAction.share();
    }

    /**
     * 分享到新浪微博
     * @param context
     * @param text
     * @param umShareListener
     */
    public void onSinaShare(Context context,String text,String showUrl,UMShareListener umShareListener){
        ShareAction shareAction = new ShareAction((Activity)context);
        shareAction.setPlatform(SHARE_MEDIA.SINA);
        shareAction.withText(text);
        shareAction.withTargetUrl(showUrl);
        shareAction.setCallback(umShareListener);
        shareAction.share();
    }
}
