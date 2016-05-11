package cn.org.eshow.demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 高德地图返回位置信息
 * Created by daikting on 16/3/23.
 */
public class AMapLocationListResultBean implements Serializable{
    List<AMapLocationBean> pois;

    public List<AMapLocationBean> getPois() {
        return pois;
    }

    public void setPois(List<AMapLocationBean> pois) {
        this.pois = pois;
    }
}
