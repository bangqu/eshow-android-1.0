package com.bangqu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 唯图 on 2016/8/31.
 */
public class AlbumBean implements Serializable {

    /**
     * title : this is title
     * imageurl : http://www.image.tunqu.net/1331D3212F30CC972420499EF7B6FB68.jpg
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String title;
        private String imageurl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }
    }
}
