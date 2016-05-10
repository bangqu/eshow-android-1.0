package cn.org.eshow.music;

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.org.eshow.R;
import cn.org.eshow.bean.Song;

public class ReadXmlUtil {
    /**
     * 解析网络音乐列表xml
     */
    public static List<Song> parseWebSongList(Context context) {
        List<Song> data = null;
        cn.org.eshow.bean.Song song = null;
        XmlResourceParser parser = context.getResources().getXml(R.xml.web_list);
        try {
            int eventType = parser.getEventType();
            //直到读取文档结束
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                switch (eventType) {
                    //开始读取文档
                    case XmlResourceParser.START_DOCUMENT:
                        data = new ArrayList<Song>();
                        break;
                    //开始读取文档标签
                    case XmlResourceParser.START_TAG:
                        if (parser.getName().equals("song")) {
                            song = new cn.org.eshow.bean.Song();
                            song.setId(parser.getAttributeIntValue(0, 0));
                        } else if (parser.getName().equals("name")) {
                            song.setName(parser.nextText());
                        } else if (parser.getName().equals("artist")) {
                            song.setArtist(new cn.org.eshow.bean.Artist(0, parser.nextText(), null));
                        } else if (parser.getName().equals("album")) {
//							song.setAlbum(new Album(0, parser.nextText(), null));
                        } else if (parser.getName().equals("displayName")) {
                            song.setDisplayName(parser.nextText());
                        } else if (parser.getName().equals("mimeType")) {
                            song.setMimeType(parser.nextText());
                        } else if (parser.getName().equals("netUrl")) {
                            song.setNetUrl(parser.nextText());
                        } else if (parser.getName().equals("durationTime")) {
                            song.setDurationTime(Integer.valueOf(parser.nextText()));
                        } else if (parser.getName().equals("size")) {
                            song.setSize(Integer.valueOf(parser.nextText()));
                        }
                        break;
                    //读取文档标签结束
                    case XmlResourceParser.END_TAG:
                        if (parser.getName().equals("song")) {
                            data.add(song);
                            song = null;
                        }
                        break;
                    default:
                        break;
                }
                //读取下个元素
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
