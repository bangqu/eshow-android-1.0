package com.bangqu.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageUtils {
    

    
    
    /***
     * 获取网络图
     * @param url
     * @return
     */
    public static InputStream getInputStream(Object url) {
        URL myFileUrl = null;
        InputStream is=null;
        try {
            myFileUrl = new URL(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            is = conn.getInputStream();
            is.close();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }
    
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    
    
    /***
     * 获取网络图
     * @param url
     * @return
     */
    public static Bitmap getImage(Object url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    
    /**
     * 保存图片
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static void saveFile(Bitmap bm, String fileName, Context context) throws IOException {
        File dirFile = new File(Environment.getExternalStorageDirectory()+"/chakeshe/");
        if(!dirFile.exists()){   
            dirFile.mkdir();   
        }   
        File myCaptureFile = new File(dirFile,fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        Log.e("bitmap==>", fileName);
        bm.compress(CompressFormat.JPEG, 80, bos);
        bos.flush();   
        bos.close();  
        
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(myCaptureFile);
        intent.setData(uri);
        context.sendBroadcast(intent);      
        }
    
    /***
     * 保存网络图片
     * @param imgurl
     */
    public static void loadfile(final String imgurl){
        new Thread(){
            public void run() {
                try {
                    URL url = new URL(imgurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(6*1000);  // 注意要设置超时，设置时间不要超过10秒，避免被android系统回收
                    if (conn.getResponseCode() != 200) throw new RuntimeException("请求url失败");
                    InputStream inSream = conn.getInputStream();
                    //把图片保存到项目的根目录
                    readAsFile(inSream, new File(Environment.getExternalStorageDirectory()+"/"+"test.jpg"));
                 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
}
    public static void readAsFile(InputStream inSream, File file) throws Exception {
        FileOutputStream outStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len = -1;
        while( (len = inSream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inSream.close();
    }
    
    /***
     * 图片处理
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = null;
             output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            
            Canvas canvas = new Canvas(output);
     
            final int color = 0xff424242;  
     
            final Paint paint = new Paint();
     
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
     
            final RectF rectF = new RectF(rect);
     
            final float roundPx = pixels;  
     
            paint.setAntiAlias(true);  
     
            canvas.drawARGB(0, 0, 0, 0);  
     
            paint.setColor(color);  
     
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
     
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
     
            canvas.drawBitmap(bitmap, rect, rect, paint);  
           
        
        return output;  
     
        }


}
