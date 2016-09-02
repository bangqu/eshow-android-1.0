package com.bangqu.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 设计院 on 2016/6/30.
 * 通用信息
 */
public class Contact {

    public final static String USERINFO="userinfo";

    /**
     * 将图片联合路径处理成单个
     *
     * @param imgs
     * @return
     */
    public static List<String> getPhotos(String imgs) {
        List<String> listphotos = new ArrayList<String>();
        int index = imgs.indexOf(",");
        String imgurl;
        while (index > -1) {
            imgurl = imgs.substring(0, index);
            listphotos.add(imgurl);
            Log.e("imgUrl==>", imgurl);
            imgs = imgs.substring(index + 1);
            index = imgs.indexOf(",");
        }
        Log.e("imgs==>", imgs);
        listphotos.add(imgs);
        return listphotos;
    }


}
