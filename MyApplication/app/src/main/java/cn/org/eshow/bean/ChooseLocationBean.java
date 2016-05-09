package cn.org.eshow.bean;

/**
 * 地图上选择地方所需数据的封装
 * Created by daikting on 16/3/18.
 */
public class ChooseLocationBean {
    private double lon;
    private double lat;
    private String address;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
